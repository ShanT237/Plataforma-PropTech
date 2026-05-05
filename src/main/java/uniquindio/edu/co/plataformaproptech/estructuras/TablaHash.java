package uniquindio.edu.co.plataformaproptech.estructuras;

public class TablaHash<K, V> {

    private EntradaHash<K, V>[] tabla;
    private int capacidad;
    private int tamanio;

    @SuppressWarnings("unchecked")
    public TablaHash(int capacidad) {
        this.capacidad = capacidad;
        this.tabla = new EntradaHash[capacidad];
        this.tamanio = 0;
    }


    private int hash(K clave) {
        return Math.abs(clave.hashCode() % capacidad);
    }

    public void insertar(K clave, V valor) {
        int indice = hash(clave);
        EntradaHash<K, V> actual = tabla[indice];

        while (actual != null) {
            if (actual.clave.equals(clave)) {
                actual.valor = valor;
                return;
            }
            actual = actual.siguiente;
        }

        EntradaHash<K, V> nuevo = new EntradaHash<>(clave, valor);
        nuevo.siguiente = tabla[indice];
        tabla[indice] = nuevo;
        tamanio++;
    }

    public V buscar(K clave) {
        int indice = hash(clave);
        EntradaHash<K, V> actual = tabla[indice];

        while (actual != null) {
            if (actual.clave.equals(clave)) {
                return actual.valor;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    public boolean eliminar(K clave) {
        int indice = hash(clave);
        EntradaHash<K, V> actual = tabla[indice];
        EntradaHash<K, V> anterior = null;

        while (actual != null) {
            if (actual.clave.equals(clave)) {
                if (anterior == null) {
                    tabla[indice] = actual.siguiente;
                } else {
                    anterior.siguiente = actual.siguiente;
                }
                tamanio--;
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
        return false;
    }

    public boolean contiene(K clave) {
        return buscar(clave) != null;
    }

    public int getTamanio() {
        return tamanio;
    }

    public boolean estaVacia() {
        return tamanio == 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < capacidad; i++) {
            if (tabla[i] != null) {
                sb.append("[").append(i).append("] ");
                EntradaHash<K, V> actual = tabla[i];
                while (actual != null) {
                    sb.append("(").append(actual.clave).append(" -> ").append(actual.valor).append(")");
                    if (actual.siguiente != null) sb.append(" | ");
                    actual = actual.siguiente;
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}