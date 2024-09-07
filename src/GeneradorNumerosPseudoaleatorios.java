import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class GeneradorNumerosPseudoaleatorios {
    private static final int MAX_N = 50;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        List<Double> numeros;
        boolean numerosAceptados = false;

        while (!numerosAceptados) {
            mostrarMenu();
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    numeros = generarCuadradosMedios();
                    break;
                case 2:
                    numeros = generarLehmer();
                    break;
                case 3:
                    numeros = generarJava();
                    break;
                case 4:
                    numeros = introducirNumeros();
                    break;
                default:
                    System.out.println("Opción no válida.");
                    continue;
            }

            if (numeros != null) {
                guardarNumeros(numeros);
                numerosAceptados = realizarPruebas(numeros);
            }
        }

        System.out.println("Programa finalizado.");
        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\nSeleccione una opción:");
        System.out.println("1. Generar números usando Cuadrados Medios");
        System.out.println("2. Generar números usando Lehmer");
        System.out.println("3. Generar números usando Java Random");
        System.out.println("4. Introducir números manualmente");
    }

    private static List<Double> generarCuadradosMedios() {
        System.out.print("Ingrese la semilla (debe ser de 4 dígitos): ");
        int semilla = scanner.nextInt();

        if (semilla < 1000 || semilla > 9999) {
            System.out.println("La semilla debe ser un número de 4 dígitos.");
            return null;
        }

        System.out.print("Ingrese la cantidad de números a generar (máx " + MAX_N + "): ");
        int n = Math.min(scanner.nextInt(), MAX_N);

        if (n < 1 && n > MAX_N) {
            System.out.println("La cantidad de números a generar debe ser mayor a 0 y menor a " + MAX_N + ".");
            return null;
        }

        List<Double> numeros = new ArrayList<>();

        // Algoritmo de cuadrados medios
        for (int i = 0; i < n; i++) {
            int cuadrado = semilla * semilla;
            String cuadradoStr = String.format("%08d", cuadrado);
            semilla = Integer.parseInt(cuadradoStr.substring(2, 6));
            numeros.add(semilla / 10000.0);
        }
        return numeros;
    }

    private static List<Double> generarLehmer() {
        System.out.print("Ingrese la semilla: ");
        long semilla = scanner.nextLong();
        System.out.print("Ingrese la cantidad de números a generar (máx " + MAX_N + "): ");
        int n = Math.min(scanner.nextInt(), MAX_N);
        if (n < 1 && n > MAX_N) {
            System.out.println("La cantidad de números a generar debe ser mayor a 0 y menor a " + MAX_N + ".");
            return null;
        }

        List<Double> numeros = new ArrayList<>();

        // Algoritmo de Lehmer
        long m = (long) Math.pow(2, 31) - 1;
        long a = 16807;

        for (int i = 0; i < n; i++) {
            semilla = (a * semilla) % m;
            numeros.add(semilla / (double) m);
        }
        return numeros;
    }

    private static List<Double> generarJava() {
        System.out.print("Ingrese la semilla: ");
        long semilla = scanner.nextLong();
        System.out.print("Ingrese la cantidad de números a generar (máx " + MAX_N + "): ");
        int n = Math.min(scanner.nextInt(), MAX_N);

        Random random = new Random(semilla);
        List<Double> numeros = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            numeros.add(random.nextDouble());
        }
        return numeros;
    }

    private static List<Double> introducirNumeros() {
        System.out.println("Pegue los números (uno por línea). Ingrese una línea vacía para finalizar:");
        List<Double> numeros = new ArrayList<>();
        String linea;
        while (!(linea = scanner.nextLine()).isEmpty()) {
            try {
                numeros.add(Double.parseDouble(linea));
            } catch (NumberFormatException e) {
                System.out.println("Número no válido: " + linea);
            }
        }
        return numeros;
    }

    private static void guardarNumeros(List<Double> numeros) {
        System.out.print("¿Desea guardar los números en un archivo? (s/n): ");
        if (scanner.nextLine().toLowerCase().startsWith("s")) {
            try (FileWriter writer = new FileWriter("numeros_pseudoaleatorios.txt")) {
                for (Double numero : numeros) {
                    writer.write(numero + "\n");
                }
                System.out.println("Números guardados en 'numeros_pseudoaleatorios.txt'");
            } catch (IOException e) {
                System.out.println("Error al guardar el archivo: " + e.getMessage());
            }
        } else {
            System.out.println("Números generados:");
            for (Double numero : numeros) {
                System.out.println(numero);
            }
        }
    }

    private static boolean realizarPruebas(List<Double> numeros) {
        boolean pruebaUniformidad = pruebaUniformidad(numeros);
        boolean pruebaIndependencia = pruebaIndependencia(numeros);

        if (pruebaUniformidad && pruebaIndependencia) {
            System.out.println("Los números han sido aceptados por todas las pruebas.");
            return true;
        } else {
            if (!pruebaUniformidad) {
                System.out.println("Los números no pasaron la prueba de uniformidad.");
            }
            if (!pruebaIndependencia) {
                System.out.println("Los números no pasaron la prueba de independencia.");
            }
            return false;
        }
    }

    private static boolean pruebaUniformidad(List<Double> numeros) {
        // Implementar la prueba de uniformidad
        // Este es un placeholder, deberá ser reemplazado por la implementación real
        return true;
    }

    private static boolean pruebaIndependencia(List<Double> numeros) {
        // Implementar la prueba de independencia
        // Este es un placeholder, deberá ser reemplazado por la implementación real
        return true;
    }
}