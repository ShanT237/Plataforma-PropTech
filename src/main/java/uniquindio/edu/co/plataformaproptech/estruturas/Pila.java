package uniquindio.edu.co.plataformaproptech.estruturas;


public class Pila<T> {

    private Nodo<T> tope;
    private int tamanio;

    public Pila() {
        this.tope = null;
        this.tamanio = 0;
    }


    public void apilar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.siguiente = tope;
        tope = nuevo;
        tamanio++;
    }

    public T desapilar() {
        if (estaVacia()) return null;
        T dato = tope.dato;
        tope = tope.siguiente;
        tamanio--;
        return dato;
    }

    public T verTope() {
        if (estaVacia()) return null;
        return tope.dato;
    }

    public boolean estaVacia() {
        return tope == null;
    }

    public int getTamanio() {
        return tamanio;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Tope -> [");
        Nodo<T> actual = tope;
        while (actual != null) {
            sb.append(actual.dato);
            if (actual.siguiente != null) sb.append(" -> ");
            actual = actual.siguiente;
        }
        sb.append("]");
        return sb.toString();
    }
}