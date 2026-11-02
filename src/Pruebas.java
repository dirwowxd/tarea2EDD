import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class Pruebas {
    public static void verificar(int[][] entrada, int[] salida) {
        int total = 0;
        Map<Integer, Integer> frecuencias = new HashMap<>();
        for (int[] a : entrada) {
            total += a.length;
            for (int v : a) frecuencias.merge(v, 1, Integer::sum);
        }
        if (salida.length != total) throw new AssertionError("Longitud incorrecta");
        for (int i = 0; i < salida.length; i++) {
            if (i > 0 && salida[i - 1] > salida[i]) throw new AssertionError("Salida desordenada");
            int cantidad = frecuencias.getOrDefault(salida[i], 0);
            if (cantidad == 0) throw new AssertionError("Valor extra o repetido indebidamente");
            if (cantidad == 1) frecuencias.remove(salida[i]);
            else frecuencias.put(salida[i], cantidad - 1);
        }
        if (!frecuencias.isEmpty()) throw new AssertionError("Faltan valores");
    }

    private static void caso(int[][] a) {
        int[] h = Mezclas.kMergeHeap(a);
        int[] cb = Mezclas.kMergeCB(a);
        verificar(a, h);
        verificar(a, cb);
        if (!Arrays.equals(h, cb)) throw new AssertionError("Las mezclas difieren");
    }
    public static void main(String[] args) {
        caso(new int[][] {{1,4,9}, {2,4}, {3,7,10,12}, {1,6}, {5,8,9}});
        caso(new int[][] {{1}, {1}, {1}, {1}, {1}});
        caso(new int[][] {{2}, {1,2,2,8}, {3}, {1,3,5,7,9}, {4,4}});
        caso(new int[][] {{100}, {1}, {5,5,5}, {2,3}, {8,9,10}});
        Random random = new Random(2026);
        for (int prueba = 0; prueba < 1000; prueba++) {
            int k = 5 + random.nextInt(25);
            int[][] a = new int[k][];
            for (int i = 0; i < k; i++) {
                a[i] = new int[1 + random.nextInt(75)];
                for (int j = 0; j < a[i].length; j++) a[i][j] = 1 + random.nextInt(200);
                Arrays.sort(a[i]);
            }
            caso(a);
        }
        System.out.println("OK: 4 casos dirigidos y 1000 casos aleatorios. Ambos algoritmos correctos.");
    }
}
