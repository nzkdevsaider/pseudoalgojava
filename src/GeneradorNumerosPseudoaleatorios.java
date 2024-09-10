import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

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
        int n = numeros.size();
        int k = (int) Math.sqrt(n);
        int[] observados = new int[k];
        double esperados = (double) n / k;

        for (Double numero : numeros) {
            int intervalo = (int) (numero * k);
            if (intervalo == k)
                intervalo--;
            observados[intervalo]++;
        }

        double chiCuadrado = 0;
        for (int i = 0; i < k; i++) {
            chiCuadrado += Math.pow(observados[i] - esperados, 2) / esperados;
        }

        double valorCritico = obtenerValorCriticoChi(k - 1);

        System.out.println("Estadistico y la frecuencia observada: " + chiCuadrado);
        System.out.println("Valor crítico: " + valorCritico);

        return chiCuadrado <= valorCritico;
    }

    // Chi-cuadrado
    private static double obtenerValorCriticoChi(int gradosLibertad) {
        double[] valoresCriticos = { 3.841, 5.991, 7.815, 9.488, 11.070, 12.592, 14.067, 15.507, 16.919 };
        if (gradosLibertad > 0 && gradosLibertad <= valoresCriticos.length) {
            return valoresCriticos[gradosLibertad - 1];
        } else {
            return Math.sqrt(2 * gradosLibertad) + 1.645;
        }
    }

    private static boolean pruebaIndependencia(List<Double> numeros) {
        int n = numeros.size();
        if (n < 10) {
            System.out.println("Se necesitan al menos 10 números para realizar la prueba de independencia.");
            return false;
        }

        List<Double> sortedNumbers = new ArrayList<>(numeros);
        Collections.sort(sortedNumbers);
        double mediana = (n % 2 == 0) ? (sortedNumbers.get(n / 2 - 1) + sortedNumbers.get(n / 2)) / 2.0
                : sortedNumbers.get(n / 2);

        int rachas = 1;
        boolean encimaDeLaMediana = numeros.get(0) >= mediana;
        for (int i = 1; i < n; i++) {
            boolean actualEncimaDeLaMediana = numeros.get(i) >= mediana;
            if (actualEncimaDeLaMediana != encimaDeLaMediana) {
                rachas++;
                encimaDeLaMediana = actualEncimaDeLaMediana;
            }
        }

        int n1 = 0, n2 = 0;
        for (Double numero : numeros) {
            if (numero >= mediana)
                n1++;
            else
                n2++;
        }

        double mediaRachas = ((2.0 * n1 * n2) / n) + 1;
        double varianzaRachas = (2.0 * n1 * n2 * (2.0 * n1 * n2 - n)) / (n * n * (n - 1));

        double z = (rachas - mediaRachas) / Math.sqrt(varianzaRachas);

        double valorCritico = 1.96;

        System.out.println("Número de rachas: " + rachas);
        System.out.println("Media de rachas: " + mediaRachas);
        System.out.println("Estadístico Z: " + z);
        System.out.println("Valor crítico: ±" + valorCritico);

        return Math.abs(z) <= valorCritico;
    }
}