package uniquindio.edu.co.plataformaproptech.estructuras;

public class NodoColaPrioridad<T> {
    T dato;
    int prioridad;
    NodoColaPrioridad<T> siguiente;

    public NodoColaPrioridad(T dato, int prioridad) {
        this.dato = dato;
        this.prioridad = prioridad;
        this.siguiente = null;
    }
}