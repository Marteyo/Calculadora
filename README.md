# Calculadora

Calculadora Android con las cuatro operaciones básicas.

- Dígitos, punto decimal, `+ − × ÷` y `=`.
- `C` (borrar todo), `⌫` (borrar último dígito), `±` (cambiar signo) y `%`.
- Las operaciones se resuelven en el orden en que se pulsan, como en una calculadora de bolsillo (`2 + 3 × 4 = 20`).
- Se puede seguir operando con el resultado; dividir entre 0 muestra `Error`.
- Conserva el estado al rotar la pantalla.

## Estructura

- `Calculator.java`: lógica pura (sin Android), probada con JUnit.
- `MainActivity.java`: conecta los botones con la lógica.

## Pruebas

```
./gradlew test
```
