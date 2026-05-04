package uniquindio.edu.co.plataformaproptech.estruturas;

public class ListaEnlazada<T>{

    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamanio;

    public ListaEnlazada() {
        this.cabeza = null;
        this.cola = null;
        this.tamanio = 0;
    }

    public void agregar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            cola.siguiente = nuevo;
            cola = nuevo;
        }
        tamanio++;
    }

    public boolean eliminar(int indice) {
        if (indice < 0 || indice >= tamanio) return false;
        if (indice == 0) {
            cabeza = cabeza.siguiente;
            if (cabeza == null) cola = null;
        } else {
            Nodo<T> actual = cabeza;
            for (int i = 0; i < indice - 1; i++) {
                actual = actual.siguiente;
            }
            if (actual.siguiente == cola) {
                cola = actual;
            }
            actual.siguiente = actual.siguiente.siguiente;
        }
        tamanio--;
        return true;
    }

    public T obtener(int indice) {
        if (indice < 0 || indice >= tamanio) return null;
        Nodo<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.siguiente;
        }
        return actual.dato;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int getTamanio() {
        return tamanio;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Nodo<T> actual = cabeza;
        while (actual != null) {
            sb.append(actual.dato);
            if (actual.siguiente != null) sb.append(" -> ");
            actual = actual.siguiente;
        }
        sb.append("]");
        return sb.toString();
    }


}
