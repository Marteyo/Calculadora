package com.calculadora.usuario.calculadora;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CalculatorTest {

    private Calculator c;

    @Before
    public void setUp() {
        c = new Calculator();
    }

    /** Pulsa teclas: dígitos, '.', + - * /, '=', 'C', '<' (borrar), 'n' (±), '%'. */
    private String press(String keys) {
        for (char k : keys.toCharArray()) {
            if (Character.isDigit(k)) c.digit(k);
            else if (k == '.') c.decimal();
            else if (k == '=') c.equals();
            else if (k == 'C') c.clear();
            else if (k == '<') c.backspace();
            else if (k == 'n') c.negate();
            else if (k == '%') c.percent();
            else c.operator(k);
        }
        return c.getDisplay();
    }

    @Test public void suma() { assertEquals("7", press("3+4=")); }
    @Test public void restaNoInvierteElPrimerNumero() { assertEquals("2", press("5-3=")); }
    @Test public void restaConResultadoNegativo() { assertEquals("-2", press("3-5=")); }
    @Test public void multiplicacion() { assertEquals("12", press("3*4=")); }
    @Test public void division() { assertEquals("2.5", press("5/2=")); }
    @Test public void decimales() { assertEquals("0.3", press("0.1+0.2=")); }

    @Test public void encadenaOperaciones() { assertEquals("20", press("2+3*4=")); }
    @Test public void encadenaRestas() { assertEquals("4", press("10-3-3=")); }
    @Test public void divisionTrasResultadoCero() { assertEquals("0", press("0/5/2=")); }

    @Test public void dividirEntreCeroDaError() { assertEquals(Calculator.ERROR, press("5/0=")); }
    @Test public void trasErrorSeReinicia() { press("5/0="); assertEquals("7", press("7")); }

    @Test public void clearReiniciaLaOperacionPendiente() {
        press("5+3");
        press("C");
        assertEquals("4", press("4="));
    }

    @Test public void seguirOperandoConElResultado() { assertEquals("17", press("3+4=*3-4=")); }
    @Test public void digitoTrasIgualEmpiezaNumeroNuevo() { assertEquals("9", press("3+4=9")); }
    @Test public void cambiarOperadorSinSegundoNumero() { assertEquals("10", press("5+*2-")); }
    @Test public void igualSinOperacionNoHaceNada() { assertEquals("5", press("5=")); }

    @Test public void soloUnPuntoDecimal() { assertEquals("1.5", press("1..5")); }
    @Test public void puntoAlPrincipio() { assertEquals("0.5", press(".5")); }
    @Test public void sinCerosAIzquierda() { assertEquals("7", press("007")); }
    @Test public void limiteDeDigitos() { assertEquals("123456789012", press("1234567890123456")); }

    @Test public void borrarUltimoDigito() { assertEquals("12", press("123<")); }
    @Test public void borrarHastaVacioMuestraCero() { assertEquals("0", press("5<")); }

    @Test public void cambioDeSigno() { assertEquals("-5", press("5n")); }
    @Test public void cambioDeSignoDosVeces() { assertEquals("5", press("5nn")); }
    @Test public void operarConNegativo() { assertEquals("-2", press("5n+3=")); }

    @Test public void porcentaje() { assertEquals("0.5", press("50%")); }

    @Test public void resultadoMuyGrandeUsaNotacionCientifica() {
        assertEquals("1E+14", press("10000000*10000000="));
    }
}
