import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Function;

public final class Experimentos {
    private static final int CALENTAMIENTOS = 3;
    private static final int REPETICIONES = 10;

    private static double medir(int[][] a, Function<int[][], int[]> mezcla, int[] referencia) {
        for (int i = 0; i < CALENTAMIENTOS; i++) {
            int[] obtenido = mezcla.apply(a);
            if (!Arrays.equals(referencia, obtenido)) throw new AssertionError("Error en calentamiento");
        }
        long totalNs = 0;
        for (int i = 0; i < REPETICIONES; i++) {
            long inicio = System.nanoTime();
            int[] obtenido = mezcla.apply(a);
            long fin = System.nanoTime();
            totalNs += fin - inicio;
            // Validacion EXCLUIDA del cronometro.
            if (!Arrays.equals(referencia, obtenido)) throw new AssertionError("Error en repeticion " + i);
        }
        return totalNs / (double) REPETICIONES / 1_000_000.0;
    }

    public static double[] ejecutar(Path archivo) throws IOException {
        int[][] a = Lector.leer(archivo); // Lectura EXCLUIDA del cronometro.
        int n = 0;
        for (int[] arreglo : a) n += arreglo.length;
        int[] referencia = Mezclas.kMergeHeap(a);
        Pruebas.verificar(a, referencia); // Verificacion completa EXCLUIDA.
        double heapMs = medir(a, Mezclas::kMergeHeap, referencia);
        double cbMs = medir(a, Mezclas::kMergeCB, referencia);
        System.out.printf(Locale.US, "n=%d k=%d Heap=%.3f ms ColaBinomial=%.3f ms%n",
                n, a.length, heapMs, cbMs);
        return new double[] {heapMs, cbMs};
    }

    private static void todos(Path carpeta, Path csv) throws IOException {
        Files.createDirectories(carpeta);
        Path parent = csv.toAbsolutePath().getParent();
        if (parent != null) Files.createDirectories(parent);
        int[][] configuraciones = {
            {100_000, 10}, {200_000, 10}, {500_000, 10}, {1_000_000, 10},
            {5_000_000, 5}, {5_000_000, 10}, {5_000_000, 15},
            {5_000_000, 20}, {5_000_000, 25}, {5_000_000, 30}
        };
        try (BufferedWriter out = Files.newBufferedWriter(csv)) {
            out.write("n,k,semilla,rango_min,rango_max,heap_ms,binomial_ms\n");
            for (int[] c : configuraciones) {
                int n = c[0], k = c[1];
                long semilla = 2026L + n + k;
                Path archivo = carpeta.resolve("datos_n" + n + "_k" + k + ".txt");
                System.out.println("Generando " + archivo + " semilla=" + semilla);
                Generador.generar(n, k, archivo, semilla, Generador.MAX_VALOR);
                double[] t = ejecutar(archivo);
                out.write(String.format(Locale.US, "%d,%d,%d,1,%d,%.6f,%.6f%n",
                        n, k, semilla, Generador.MAX_VALOR, t[0], t[1]));
                out.flush();
                Files.deleteIfExists(archivo);
            }
        }
        System.out.println("CSV generado: " + csv);
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 1) {
            ejecutar(Path.of(args[0]));
        } else if (args.length == 3 && args[0].equals("todos")) {
            todos(Path.of(args[1]), Path.of(args[2]));
        } else {
            System.out.println("Uso: java Experimentos archivo.txt");
            System.out.println("  o: java Experimentos todos carpeta_temporal resultados.csv");
        }
    }
}
