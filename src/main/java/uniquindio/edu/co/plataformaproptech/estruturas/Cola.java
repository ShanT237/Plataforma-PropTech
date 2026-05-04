package uniquindio.edu.co.plataformaproptech.estruturas;

public class Cola<T> {

    private Nodo<T> frente;
    private Nodo<T> fin;
    private int tamanio;

    public Cola() {
        this.frente = null;
        this.fin = null;
        this.tamanio = 0;
    }

    // Agregar al final de la cola
    public void encolar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (estaVacia()) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            fin = nuevo;
        }
        tamanio++;
    }

    // Eliminar y retornar el elemento del frente
    public T desencolar() {
        if (estaVacia()) return null;
        T dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) fin = null;
        tamanio--;
        return dato;
    }

    // Ver el frente sin eliminarlo
    public T verFrente() {
        if (estaVacia()) return null;
        return frente.dato;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int getTamanio() {
        return tamanio;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Frente -> [");
        Nodo<T> actual = frente;
        while (actual != null) {
            sb.append(actual.dato);
            if (actual.siguiente != null) sb.append(" -> ");
            actual = actual.siguiente;
        }
        sb.append("]");
        return sb.toString();
    }
}