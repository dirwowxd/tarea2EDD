public final class MinHeap {
    private final Entrada[] datos;
    private int cantidad;

    public MinHeap(int capacidad) {
        datos = new Entrada[capacidad];
    }

    public boolean estaVacio() { return cantidad == 0; }

    public void insertar(Entrada e) {
        if (cantidad == datos.length) throw new IllegalStateException("Heap lleno");
        int i = cantidad++;
        while (i > 0) {
            int padre = (i - 1) / 2;
            if (datos[padre].valor <= e.valor) break;
            datos[i] = datos[padre];
            i = padre;
        }
        datos[i] = e;
    }

    public Entrada extraerMin() {
        if (cantidad == 0) throw new IllegalStateException("Heap vacio");
        Entrada minimo = datos[0];
        Entrada ultimo = datos[--cantidad];
        datos[cantidad] = null;
        if (cantidad > 0) {
            int i = 0;
            while (2 * i + 1 < cantidad) {
                int hijo = 2 * i + 1;
                if (hijo + 1 < cantidad && datos[hijo + 1].valor < datos[hijo].valor) hijo++;
                if (ultimo.valor <= datos[hijo].valor) break;
                datos[i] = datos[hijo];
                i = hijo;
            }
            datos[i] = ultimo;
        }
        return minimo;
    }
}
