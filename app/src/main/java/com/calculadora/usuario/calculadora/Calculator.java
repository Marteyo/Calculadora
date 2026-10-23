package com.calculadora.usuario.calculadora;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Lógica de la calculadora, sin dependencias de Android para poder probarla con JUnit.
 *
 * Funciona como una calculadora de bolsillo: guarda el primer operando y la operación
 * pendiente, y la resuelve al pulsar la siguiente operación o "=".
 */
public class Calculator implements Serializable {

    public static final String ERROR = "Error";
    private static final int MAX_DIGITS = 12;

    private String entry = "0";   // texto mostrado en pantalla
    private double accumulator;   // primer operando
    private char pendingOp = 0;   // '+', '-', '*', '/' o 0 si no hay ninguna
    private boolean fresh = true; // el siguiente dígito empieza un número nuevo
    private boolean hasOperand;   // el número en pantalla ya es el segundo operando
    private boolean error;

    public String getDisplay() {
        return error ? ERROR : entry;
    }

    public void digit(char d) {
        if (error) clear();
        if (fresh || entry.equals("0")) {
            entry = String.valueOf(d);
        } else if (digitCount() < MAX_DIGITS) {
            entry += d;
        }
        fresh = false;
        hasOperand = true;
    }

    public void decimal() {
        if (error) clear();
        if (fresh) {
            entry = "0.";
            fresh = false;
        } else if (!entry.contains(".")) {
            entry += ".";
        }
        hasOperand = true;
    }

    public void operator(char op) {
        if (error) return;
        double value = parse();
        if (pendingOp != 0 && hasOperand) {
            if (!resolve(value)) return;
        } else if (pendingOp == 0) {
            accumulator = value;
        }
        pendingOp = op;
        fresh = true;
        hasOperand = false;
    }

    public void equals() {
        if (error || pendingOp == 0) return;
        if (!resolve(parse())) return;
        pendingOp = 0;
        fresh = true;
        hasOperand = false;
    }

    public void clear() {
        entry = "0";
        accumulator = 0;
        pendingOp = 0;
        fresh = true;
        hasOperand = false;
        error = false;
    }

    public void backspace() {
        if (error) {
            clear();
            return;
        }
        if (fresh) return;
        entry = entry.substring(0, entry.length() - 1);
        if (entry.isEmpty() || entry.equals("-")) entry = "0";
    }

    public void negate() {
        if (error || parse() == 0 && !entry.contains(".")) return;
        entry = entry.startsWith("-") ? entry.substring(1) : "-" + entry;
        hasOperand = true;
    }

    public void percent() {
        if (error) return;
        entry = format(parse() / 100);
        hasOperand = true;
    }

    /** Aplica la operación pendiente a (acumulador, value) y deja el resultado en pantalla. */
    private boolean resolve(double value) {
        double result;
        switch (pendingOp) {
            case '+': result = accumulator + value; break;
            case '-': result = accumulator - value; break;
            case '*': result = accumulator * value; break;
            default:  result = accumulator / value; break;
        }
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            clear();
            error = true;
            return false;
        }
        accumulator = result;
        entry = format(result);
        return true;
    }

    private double parse() {
        return Double.parseDouble(entry);
    }

    private int digitCount() {
        int n = 0;
        for (int i = 0; i < entry.length(); i++) {
            if (Character.isDigit(entry.charAt(i))) n++;
        }
        return n;
    }

    /** 12 cifras significativas, sin ceros sobrantes: 7.0 -> "7", 0.1+0.2 -> "0.3". */
    static String format(double v) {
        BigDecimal bd = new BigDecimal(v).round(new MathContext(MAX_DIGITS)).stripTrailingZeros();
        if (bd.signum() == 0) return "0";
        double abs = Math.abs(v);
        return (abs >= 1e12 || abs < 1e-9) ? bd.toString() : bd.toPlainString();
    }
}
