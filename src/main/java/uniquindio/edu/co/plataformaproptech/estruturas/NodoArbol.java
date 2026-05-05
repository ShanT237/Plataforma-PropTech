package uniquindio.edu.co.plataformaproptech.estruturas;

public class NodoArbol<T>{
    T dato;
    NodoArbol<T> izquierdo;
    NodoArbol<T> derecho;

    public NodoArbol(T dato) {
        this.dato = dato;
        this.izquierdo = null;
        this.derecho = null;
    }
}
