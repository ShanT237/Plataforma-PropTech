package uniquindio.edu.co.plataformaproptech.estructuras;

import java.util.ArrayList;
import java.util.List;

public class ArbolBST<T extends Comparable<T>> {

    private NodoArbol<T> raiz;

    public ArbolBST() {
        this.raiz = null;
    }

    // Insertar
    public void insertar(T dato) {
        raiz = insertarRecursivo(raiz, dato);
    }

    private NodoArbol<T> insertarRecursivo(NodoArbol<T> nodo, T dato) {
        if (nodo == null) return new NodoArbol<>(dato);
        if (dato.compareTo(nodo.dato) < 0) {
            nodo.izquierdo = insertarRecursivo(nodo.izquierdo, dato);
        } else if (dato.compareTo(nodo.dato) > 0) {
            nodo.derecho = insertarRecursivo(nodo.derecho, dato);
        }
        return nodo;
    }

    // Buscar
    public boolean buscar(T dato) {
        return buscarRecursivo(raiz, dato);
    }

    private boolean buscarRecursivo(NodoArbol<T> nodo, T dato) {
        if (nodo == null) return false;
        if (dato.compareTo(nodo.dato) == 0) return true;
        if (dato.compareTo(nodo.dato) < 0) return buscarRecursivo(nodo.izquierdo, dato);
        return buscarRecursivo(nodo.derecho, dato);
    }

    // Eliminar
    public void eliminar(T dato) {
        raiz = eliminarRecursivo(raiz, dato);
    }

    private NodoArbol<T> eliminarRecursivo(NodoArbol<T> nodo, T dato) {
        if (nodo == null) return null;
        if (dato.compareTo(nodo.dato) < 0) {
            nodo.izquierdo = eliminarRecursivo(nodo.izquierdo, dato);
        } else if (dato.compareTo(nodo.dato) > 0) {
            nodo.derecho = eliminarRecursivo(nodo.derecho, dato);
        } else {
            if (nodo.izquierdo == null) return nodo.derecho;
            if (nodo.derecho == null) return nodo.izquierdo;
            NodoArbol<T> sucesor = minimoNodo(nodo.derecho);
            nodo.dato = sucesor.dato;
            nodo.derecho = eliminarRecursivo(nodo.derecho, sucesor.dato);
        }
        return nodo;
    }

    private NodoArbol<T> minimoNodo(NodoArbol<T> nodo) {
        while (nodo.izquierdo != null) nodo = nodo.izquierdo;
        return nodo;
    }

    // Recorrido inorden (devuelve elementos ordenados)
    public List<T> inorden() {
        List<T> resultado = new ArrayList<>();
        inordenRecursivo(raiz, resultado);
        return resultado;
    }

    private void inordenRecursivo(NodoArbol<T> nodo, List<T> resultado) {
        if (nodo == null) return;
        inordenRecursivo(nodo.izquierdo, resultado);
        resultado.add(nodo.dato);
        inordenRecursivo(nodo.derecho, resultado);
    }

    // Buscar por rango
    public List<T> buscarPorRango(T minimo, T maximo) {
        List<T> resultado = new ArrayList<>();
        buscarRangoRecursivo(raiz, minimo, maximo, resultado);
        return resultado;
    }

    private void buscarRangoRecursivo(NodoArbol<T> nodo, T minimo, T maximo, List<T> resultado) {
        if (nodo == null) return;
        if (nodo.dato.compareTo(minimo) > 0) {
            buscarRangoRecursivo(nodo.izquierdo, minimo, maximo, resultado);
        }
        if (nodo.dato.compareTo(minimo) >= 0 && nodo.dato.compareTo(maximo) <= 0) {
            resultado.add(nodo.dato);
        }
        if (nodo.dato.compareTo(maximo) < 0) {
            buscarRangoRecursivo(nodo.derecho, minimo, maximo, resultado);
        }
    }

    public boolean estaVacio() {
        return raiz == null;
    }

    @Override
    public String toString() {
        return inorden().toString();
    }
}