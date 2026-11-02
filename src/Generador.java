import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Random;

public final class Generador {
    public static final int MAX_VALOR = 1_000_000;

    public static void generar(int n, int k, Path archivo, long semilla, int maxValor) throws IOException {
        if (k < 5 || n < k || maxValor < 1)
            throw new IllegalArgumentException("Se requiere n >= k >= 5 y maxValor >= 1");
        Random azar = new Random(semilla);
        int[] tamanos = new int[k];
        Arrays.fill(tamanos, 1);
        for (int i = k; i < n; i++) tamanos[azar.nextInt(k)]++;
        Path padre = archivo.toAbsolutePath().getParent();
        if (padre != null) Files.createDirectories(padre);
        try (BufferedWriter w = Files.newBufferedWriter(archivo)) {
            w.write(n + " " + k);
            w.newLine();
            for (int tamano : tamanos) {
                int[] a = new int[tamano];
                for (int i = 0; i < tamano; i++) a[i] = azar.nextInt(maxValor) + 1;
                Arrays.sort(a);
                for (int v : a) { w.write(Integer.toString(v)); w.write(' '); }
                w.write("-1");
                w.newLine();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 4 || args.length > 5) {
            System.out.println("Uso: java Generador n k archivo semilla [maxValor]");
            return;
        }
        int n = Integer.parseInt(args[0]);
        int k = Integer.parseInt(args[1]);
        int max = args.length == 5 ? Integer.parseInt(args[4]) : MAX_VALOR;
        generar(n, k, Path.of(args[2]), Long.parseLong(args[3]), max);
        System.out.println("Generado: " + args[2]);
    }
}
