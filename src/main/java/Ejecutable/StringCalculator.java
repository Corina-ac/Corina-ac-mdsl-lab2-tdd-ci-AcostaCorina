package Ejecutable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StringCalculator {
    
    /**
     * Clase auxiliar para retornar la configuración del delimitador y la cadena de números restante.
     */
    private static class DelimiterConfig {
        final String regex;
        final String numbers;

        public DelimiterConfig(String regex, String numbers) {
            this.regex = regex;
            this.numbers = numbers;
        }
    }
    
    // ==========================================================
    // MÉTODO PRINCIPAL (ORQUESTADOR)
    // ==========================================================
    
    public int add(String numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return 0;
        }
        
        // 1. CONFIGURACIÓN: Obtiene el delimitador (incluyendo personalizado, coma, o salto de línea).
        DelimiterConfig config = getDelimiterConfig(numbers);
        String[] nums = splitNumbers(config.numbers, config.regex);
        
        // 2. PROCESAMIENTO: Recorre y suma los números, registrando negativos.
        StringBuilder negativeNumbers = new StringBuilder();
        int sum = 0;
        
        for (String num : nums) {
            sum += processNumber(num, negativeNumbers);
        }
        
        // 3. VALIDACIÓN FINAL: Lanza excepción si se encontraron negativos.
        if (negativeNumbers.length() > 0) {
            throw new IllegalArgumentException("negativos no permitidos: " + negativeNumbers.toString());
        }
        
        return sum;
    }

    // ==========================================================
    // MÉTODOS AUXILIARES
    // ==========================================================
    
    /**
     * Extrae el patrón de delimitador (regex) y la parte de la cadena que contiene los números.
     * Soporta: por defecto (,|\\n), custom simple (//;\n), custom múltiple (//[d1][d2]\n)
     */
    private DelimiterConfig getDelimiterConfig(String numbers) {
        // Detección de delimitador personalizado (Iteración 6)
        if (numbers.startsWith("//")) {
            int delimiterEndIndex = numbers.indexOf("\n");
            
            if (delimiterEndIndex != -1) {
                String delimiterSection = numbers.substring(2, delimiterEndIndex);
                
                // Lógica para manejar delimitadores MÚLTIPLES o simples entre corchetes (del iteración avanzada)
                if (delimiterSection.startsWith("[")) {
                    List<String> delimiters = new ArrayList<>();
                    // Patrón para encontrar todos los [delimitadores]
                    Pattern p = Pattern.compile("\\[(.*?)\\]");
                    Matcher m = p.matcher(delimiterSection);
                    
                    while (m.find()) {
                        delimiters.add(m.group(1));
                    }
                    
                    if (!delimiters.isEmpty()) {
                        // Construir el regex final: Delim1|Delim2|Delim3
                        String regex = delimiters.stream()
                            .map(Pattern::quote) // Escapar caracteres especiales de regex
                            .collect(Collectors.joining("|")); // Unir con OR lógico
                        
                        String remainingNumbers = numbers.substring(delimiterEndIndex + 1);
                        return new DelimiterConfig(regex, remainingNumbers);
                    }
                } else {
                    // Lógica para un solo delimitador sin corchetes (ej: //;\n1;2)
                    String custom = delimiterSection;
                    String regex = Pattern.quote(custom); // Usar Pattern.quote para escapar caracteres
                    String remainingNumbers = numbers.substring(delimiterEndIndex + 1);
                    return new DelimiterConfig(regex, remainingNumbers);
                }
            }
        }
        
        // Configuración por defecto (Iteración 5: Coma O Salto de línea)
        return new DelimiterConfig(",|\\n", numbers);
    }
    
    /**
     * Procesa un solo número: lo suma si es <= 1000 y registra si es negativo (Iteración 7).
     * @param num La subcadena del número a procesar.
     * @param negativeNumbers Builder para registrar los números negativos encontrados.
     * @return El valor del número a sumar (o 0 si es inválido o > 1000).
     */
    private int processNumber(String num, StringBuilder negativeNumbers) {
        if (num == null || num.trim().isEmpty()) {
            return 0;
        }
        
        int currentNumber;
        try {
            currentNumber = Integer.parseInt(num.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
        
        // Registrar Negativos (Iteración 7)
        if (currentNumber < 0) {
            if (negativeNumbers.length() > 0) {
                negativeNumbers.append(", ");
            }
            negativeNumbers.append(currentNumber);
        }
        
        // Regla de ignorar > 1000 (Iteración EXTRA)
        if (currentNumber <= 1000) {
            return currentNumber;
        }
        
        return 0;
    }

    /**
     * Wrapper simple para el método split.
     */
    private String[] splitNumbers(String numbers, String regex) {
        if (numbers == null) {
            return new String[0];
        }
        return numbers.split(regex);
    }
}