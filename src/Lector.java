import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public final class Lector {
    private static final class EnterosRapidos implements AutoCloseable {
        final InputStream in;
        final byte[] buffer = new byte[1 << 16];
        int cursor, limite;
        EnterosRapidos(Path ruta) throws IOException { in = new BufferedInputStream(Files.newInputStream(ruta)); }
        int leerByte() throws IOException {
            if (cursor >= limite) {
                limite = in.read(buffer);
                cursor = 0;
                if (limite < 0) return -1;
            }
            return buffer[cursor++] & 255;
        }
        int siguiente() throws IOException {
            int c;
            do { c = leerByte(); } while (c >= 0 && c <= 32);
            if (c < 0) throw new IOException("Fin de archivo inesperado");
            int signo = 1;
            if (c == '-') { signo = -1; c = leerByte(); }
            if (c < '0' || c > '9') throw new IOException("Entero invalido");
            int v = 0;
            while (c >= '0' && c <= '9') {
                v = v * 10 + c - '0';
                c = leerByte();
            }
            if (c > 32) throw new IOException("Separador invalido");
            return v * signo;
        }
        boolean hayMas() throws IOException {
            int c;
            do { c = leerByte(); } while (c >= 0 && c <= 32);
            return c >= 0;
        }
        @Override public void close() throws IOException { in.close(); }
    }

    public static int[][] leer(Path archivo) throws IOException {
        try (EnterosRapidos lector = new EnterosRapidos(archivo)) {
            int n = lector.siguiente();
            int k = lector.siguiente();
            if (k < 5 || n < k) throw new IOException("Cabecera invalida: se requiere n >= k >= 5");
            int[][] datos = new int[k][];
            int total = 0;
            for (int i = 0; i < k; i++) {
                int[] temporal = new int[16];
                int cantidad = 0;
                int anterior = 0;
                while (true) {
                    int valor = lector.siguiente();
                    if (valor == -1) break;
                    if (valor <= 0 || valor < anterior) throw new IOException("Datos invalidos en arreglo " + (i + 1));
                    if (cantidad == temporal.length) temporal = Arrays.copyOf(temporal, temporal.length * 2);
                    temporal[cantidad++] = valor;
                    anterior = valor;
                    if (++total > n) throw new IOException("Mas de n elementos");
                }
                if (cantidad == 0) throw new IOException("Arreglo vacio");
                datos[i] = Arrays.copyOf(temporal, cantidad);
            }
            if (total != n || lector.hayMas()) throw new IOException("Cantidad de elementos o fin de archivo incorrectos");
            return datos;
        }
    }
}
