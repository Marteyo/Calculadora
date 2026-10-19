package com.calculadora.usuario.calculadora;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String STATE_CALCULATOR = "calculator";

    private Calculator calculator = new Calculator();
    private TextView resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultado = (TextView) findViewById(R.id.resultado);

        if (savedInstanceState != null) {
            Calculator saved = (Calculator) savedInstanceState.getSerializable(STATE_CALCULATOR);
            if (saved != null) calculator = saved;
        }
        actualizarPantalla();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_CALCULATOR, calculator);
    }

    /** Botones de dígito: el dígito es el texto del botón. */
    public void onDigito(View view) {
        calculator.digit(((TextView) view).getText().charAt(0));
        actualizarPantalla();
    }

    /** Botones + − × ÷: la operación ('+', '-', '*', '/') va en el atributo tag. */
    public void onOperador(View view) {
        calculator.operator(((String) view.getTag()).charAt(0));
        actualizarPantalla();
    }

    public void onIgual(View view) {
        calculator.equals();
        actualizarPantalla();
    }

    public void onBorrarTodo(View view) {
        calculator.clear();
        actualizarPantalla();
    }

    public void onBorrarUltimo(View view) {
        calculator.backspace();
        actualizarPantalla();
    }

    public void onComa(View view) {
        calculator.decimal();
        actualizarPantalla();
    }

    public void onSigno(View view) {
        calculator.negate();
        actualizarPantalla();
    }

    public void onPorcentaje(View view) {
        calculator.percent();
        actualizarPantalla();
    }

    private void actualizarPantalla() {
        resultado.setText(calculator.getDisplay());
    }
}
