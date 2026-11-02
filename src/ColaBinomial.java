public final class ColaBinomial {
    private static final class Nodo {
        Entrada dato;
        int grado;
        Nodo hijo;
        Nodo hermano;
        Nodo(Entrada dato) { this.dato = dato; }
    }

    private Nodo raices;

    public boolean estaVacia() { return raices == null; }

    private static Nodo unirArboles(Nodo a, Nodo b) {
        if (a.dato.valor > b.dato.valor) {
            Nodo temp = a; a = b; b = temp;
        }
        b.hermano = a.hijo;
        a.hijo = b;
        a.grado++;
        return a;
    }

    private static Nodo mezclarRaices(Nodo a, Nodo b) {
        Nodo cabeza = null;
        Nodo cola = null;
        while (a != null || b != null) {
            Nodo elegido;
            if (b == null || (a != null && a.grado <= b.grado)) {
                elegido = a;
                a = a.hermano;
            } else {
                elegido = b;
                b = b.hermano;
            }
            if (cabeza == null) cabeza = elegido;
            else cola.hermano = elegido;
            cola = elegido;
        }
        if (cola != null) cola.hermano = null;
        return cabeza;
    }

    private void unir(Nodo otrasRaices) {
        raices = mezclarRaices(raices, otrasRaices);
        if (raices == null) return;
        Nodo anterior = null;
        Nodo actual = raices;
        Nodo siguiente = actual.hermano;
        while (siguiente != null) {
            if (actual.grado != siguiente.grado ||
                (siguiente.hermano != null && siguiente.hermano.grado == actual.grado)) {
                anterior = actual;
                actual = siguiente;
            } else if (actual.dato.valor <= siguiente.dato.valor) {
                actual.hermano = siguiente.hermano;
                unirArboles(actual, siguiente);
            } else {
                if (anterior == null) raices = siguiente;
                else anterior.hermano = siguiente;
                unirArboles(siguiente, actual);
                actual = siguiente;
            }
            siguiente = actual.hermano;
        }
    }

    public void insertar(Entrada e) { unir(new Nodo(e)); }

    public Entrada extraerMin() {
        if (raices == null) throw new IllegalStateException("Cola vacia");
        Nodo minimo = raices;
        Nodo anteriorMinimo = null;
        Nodo anterior = null;
        for (Nodo p = raices; p != null; p = p.hermano) {
            if (p.dato.valor < minimo.dato.valor) {
                minimo = p;
                anteriorMinimo = anterior;
            }
            anterior = p;
        }
        if (anteriorMinimo == null) raices = minimo.hermano;
        else anteriorMinimo.hermano = minimo.hermano;
        Nodo hijosInvertidos = null;
        Nodo hijo = minimo.hijo;
        while (hijo != null) {
            Nodo proximo = hijo.hermano;
            hijo.hermano = hijosInvertidos;
            hijosInvertidos = hijo;
            hijo = proximo;
        }
        unir(hijosInvertidos);
        return minimo.dato;
    }
}
