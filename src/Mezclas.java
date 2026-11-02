public final class Mezclas {
    private Mezclas() { }

    private static int contar(int[][] arreglos) {
        if (arreglos.length < 1) throw new IllegalArgumentException("No hay arreglos");
        long total = 0;
        for (int[] a : arreglos) {
            if (a == null || a.length == 0) throw new IllegalArgumentException("Arreglo vacio");
            total += a.length;
        }
        if (total > Integer.MAX_VALUE) throw new IllegalArgumentException("Demasiados elementos");
        return (int) total;
    }

    public static int[] kMergeHeap(int[][] arreglos) {
        int[] salida = new int[contar(arreglos)];
        MinHeap heap = new MinHeap(arreglos.length);
        for (int i = 0; i < arreglos.length; i++)
            heap.insertar(new Entrada(arreglos[i][0], i, 0));
        for (int j = 0; j < salida.length; j++) {
            Entrada e = heap.extraerMin();
            salida[j] = e.valor;
            int posicion = e.posicion + 1;
            if (posicion < arreglos[e.arreglo].length)
                heap.insertar(new Entrada(arreglos[e.arreglo][posicion], e.arreglo, posicion));
        }
        return salida;
    }

    public static int[] kMergeCB(int[][] arreglos) {
        int[] salida = new int[contar(arreglos)];
        ColaBinomial cola = new ColaBinomial();
        for (int i = 0; i < arreglos.length; i++)
            cola.insertar(new Entrada(arreglos[i][0], i, 0));
        for (int j = 0; j < salida.length; j++) {
            Entrada e = cola.extraerMin();
            salida[j] = e.valor;
            int posicion = e.posicion + 1;
            if (posicion < arreglos[e.arreglo].length)
                cola.insertar(new Entrada(arreglos[e.arreglo][posicion], e.arreglo, posicion));
        }
        return salida;
    }
}
