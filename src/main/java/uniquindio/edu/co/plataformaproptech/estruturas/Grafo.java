package uniquindio.edu.co.plataformaproptech.estruturas;

import java.util.ArrayList;
import java.util.List;

public class Grafo<T> {

    private TablaHash<T, ListaEnlazada<T>> listaAdyacencia;
    private int totalVertices;
    private int totalAristas;
    private boolean dirigido;

    public Grafo(boolean dirigido) {
        this.listaAdyacencia = new TablaHash<>(100);
        this.totalVertices = 0;
        this.totalAristas = 0;
        this.dirigido = dirigido;
    }

    public void agregarVertice(T vertice) {
        if (!listaAdyacencia.contiene(vertice)) {
            listaAdyacencia.insertar(vertice, new ListaEnlazada<>());
            totalVertices++;
        }
    }

    public void agregarArista(T origen, T destino) {
        agregarVertice(origen);
        agregarVertice(destino);

        listaAdyacencia.buscar(origen).agregar(destino);

        if (!dirigido) {
            listaAdyacencia.buscar(destino).agregar(origen);
        }
        totalAristas++;
    }

    public boolean existeArista(T origen, T destino) {
        ListaEnlazada<T> vecinos = listaAdyacencia.buscar(origen);
        if (vecinos == null) return false;
        for (int i = 0; i < vecinos.getTamanio(); i++) {
            if (vecinos.obtener(i).equals(destino)) return true;
        }
        return false;
    }


    public List<T> obtenerVecinos(T vertice) {
        List<T> vecinos = new ArrayList<>();
        ListaEnlazada<T> lista = listaAdyacencia.buscar(vertice);
        if (lista == null) return vecinos;
        for (int i = 0; i < lista.getTamanio(); i++) {
            vecinos.add(lista.obtener(i));
        }
        return vecinos;
    }

    public boolean existeVertice(T vertice) {
        return listaAdyacencia.contiene(vertice);
    }

    public int getTotalVertices() {
        return totalVertices;
    }

    public int getTotalAristas() {
        return totalAristas;
    }

    public boolean esDirigido() {
        return dirigido;
    }
}
