package Ejecutable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StringCalculatorTest {
    
    private StringCalculator calculator;

    @BeforeEach
    void setUp() {
        // Inicializa la calculadora antes de cada prueba para un entorno limpio
        calculator = new StringCalculator();
    }
    
    // ==========================================================
    // CASOS BÁSICOS (Iteraciones 1-4)
    // ==========================================================

    // ITERACIÓN 1: Cadena vacía
    @Test
    public void testEmptyStringReturnsZero() {
        assertEquals(0, calculator.add(""));
    }
    
    // ITERACIÓN 2: Un número
    @Test
    public void testSingleNumberReturnsSameNumber() {
        assertEquals(1, calculator.add("1"));
    }
    
    // ITERACIÓN 3: Dos números
    @Test
    public void testTwoNumbersCommaSeparatedReturnsSum() {
        assertEquals(3, calculator.add("1,2"));
    }
    
    // ITERACIÓN 4: N números (Generalización)
    @Test
    public void testMultipleNumbersReturnsSum() {
        assertEquals(6, calculator.add("1,2,3"));
        assertEquals(15, calculator.add("1,2,3,4,5"));
    }
    
    // ==========================================================
    // DELIMITADORES Y NEGATIVOS (Iteraciones 5-7)
    // ==========================================================

    // ITERACIÓN 5: Salto de línea como separador
    @Test
    public void testNewLineAsSeparator() {
        assertEquals(6, calculator.add("1\n2,3"));
        assertEquals(10, calculator.add("1\n2\n3\n4"));
    }
    
    // ITERACIÓN 6: Delimitador personalizado simple (e.g., //;\n)
    @Test
    public void testCustomDelimiterSimple() {
        assertEquals(3, calculator.add("//;\n1;2"));
        assertEquals(6, calculator.add("//|\n1|2|3"));
    }
    
    // ITERACIÓN 7: Números negativos (Debe lanzar excepción y listar todos)
    @Test
    public void testNegativeNumbersThrowException() {
        // Múltiples negativos
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.add("2,-4,3,-5");
        });
        // Verifica el mensaje de la excepción 
        assertTrue(exception.getMessage().contains("negativos no permitidos: -4, -5"));
        
        // Un solo negativo
        exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.add("-1,2");
        });
        assertTrue(exception.getMessage().contains("negativos no permitidos: -1"));
    }
    
    // ==========================================================
    // REQUISITOS AVANZADOS (Delimitadores y Regla del 1000)
    // ==========================================================
    
    // EXTRA: Ignorar números mayores a 1000
    @Test
    public void testIgnoreNumbersGreaterThan1000() {
        assertEquals(2, calculator.add("2,1001"));
        assertEquals(1002, calculator.add("2,1000")); // 1000 SÍ se suma
        assertEquals(6, calculator.add("1001,2,4,2000")); // Solo suma 2 y 4
    }
    
    // EXTRA: Delimitador de longitud arbitraria con corchetes (e.g., //[*]\n)
    @Test
    public void testCustomDelimiterWithArbitraryLength() {
        assertEquals(6, calculator.add("//[***]\n1***2***3")); 
        assertEquals(10, calculator.add("//[sep]\n4sep3sep3"));
    }

    // EXTRA: Múltiples delimitadores (e.g., //[*][%]\n)
    @Test
    public void testMultipleCustomDelimiters() {
        // Delimitadores: * y %
        assertEquals(6, calculator.add("//[*][%]\n1*2%3")); 
        
        // Delimitadores de varios caracteres: [sep] y [||]
        assertEquals(10, calculator.add("//[sep][||]\n4sep3||3")); 
    }
    
    // EXTRA: Cadena nula (Caso de borde)
    @Test
    public void testNullInputReturnsZero() {
        assertEquals(0, calculator.add(null));
    }
}