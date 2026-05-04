package uniquindio.edu.co.plataformaproptech.estruturas;


public class ColaPrioridad<T> {

    private NodoColaPrioridad<T> frente;
    private int tamanio;

    public ColaPrioridad() {
        this.frente = null;
        this.tamanio = 0;
    }

    // Insertar según prioridad (número menor = mayor prioridad)
    public void encolar(T dato, int prioridad) {
        NodoColaPrioridad<T> nuevo = new NodoColaPrioridad<>(dato, prioridad);
        if (estaVacia() || prioridad < frente.prioridad) {
            nuevo.siguiente = frente;
            frente = nuevo;
        } else {
            NodoColaPrioridad<T> actual = frente;
            while (actual.siguiente != null && actual.siguiente.prioridad <= prioridad) {
                actual = actual.siguiente;
            }
            nuevo.siguiente = actual.siguiente;
            actual.siguiente = nuevo;
        }
        tamanio++;
    }

    // Eliminar y retornar el de mayor prioridad
    public T desencolar() {
        if (estaVacia()) return null;
        T dato = frente.dato;
        frente = frente.siguiente;
        tamanio--;
        return dato;
    }

    // Ver el de mayor prioridad sin eliminarlo
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
        NodoColaPrioridad<T> actual = frente;
        while (actual != null) {
            sb.append("(").append(actual.dato).append(", P").append(actual.prioridad).append(")");
            if (actual.siguiente != null) sb.append(" -> ");
            actual = actual.siguiente;
        }
        sb.append("]");
        return sb.toString();
    }
}