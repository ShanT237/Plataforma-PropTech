package uniquindio.edu.co.plataformaproptech.repositorios;

import uniquindio.edu.co.plataformaproptech.estructuras.ArbolBST;
import uniquindio.edu.co.plataformaproptech.estructuras.ListaEnlazada;
import uniquindio.edu.co.plataformaproptech.estructuras.Pila;
import uniquindio.edu.co.plataformaproptech.estructuras.TablaHash;
import uniquindio.edu.co.plataformaproptech.modelos.Inmueble;
import uniquindio.edu.co.plataformaproptech.modelos.enums.EstadoInmueble;
import uniquindio.edu.co.plataformaproptech.modelos.enums.FinalidadInmueble;
import uniquindio.edu.co.plataformaproptech.modelos.enums.TipoInmueble;

import java.util.ArrayList;
import java.util.List;

public class RepositorioInmuebles {

    private TablaHash<String, Inmueble> tablaPorCodigo;
    private ArbolBST<Double> arbolPrecios;
    // ✅ CORREGIDO: Pila en lugar de ListaEnlazada — permite undo/redo en O(1)
    private Pila<Inmueble> historialCambios;
    private ListaEnlazada<Inmueble> todosLosInmuebles;

    public RepositorioInmuebles() {
        this.tablaPorCodigo = new TablaHash<>(100);
        this.arbolPrecios = new ArbolBST<>();
        this.historialCambios = new Pila<>();
        this.todosLosInmuebles = new ListaEnlazada<>();
    }

    public void agregar(Inmueble inmueble) {
        if (tablaPorCodigo.contiene(inmueble.getCodigo())) return;
        tablaPorCodigo.insertar(inmueble.getCodigo(), inmueble);
        arbolPrecios.insertar(inmueble.getPrecio());
        todosLosInmuebles.agregar(inmueble);
    }

    public Inmueble buscarPorCodigo(String codigo) {
        return tablaPorCodigo.buscar(codigo);
    }

    public boolean eliminar(String codigo) {
        Inmueble inmueble = tablaPorCodigo.buscar(codigo);
        if (inmueble == null) return false;
        arbolPrecios.eliminar(inmueble.getPrecio());
        tablaPorCodigo.eliminar(codigo);
        for (int i = 0; i < todosLosInmuebles.getTamanio(); i++) {
            if (todosLosInmuebles.obtener(i).getCodigo().equals(codigo)) {
                todosLosInmuebles.eliminar(i);
                break;
            }
        }
        return true;
    }

    public boolean actualizar(Inmueble inmueble) {
        Inmueble existente = tablaPorCodigo.buscar(inmueble.getCodigo());
        if (existente == null) return false;
        // ✅ apilar() en lugar de agregar() — el estado anterior queda en el tope
        historialCambios.apilar(existente);
        arbolPrecios.eliminar(existente.getPrecio());
        tablaPorCodigo.insertar(inmueble.getCodigo(), inmueble);
        arbolPrecios.insertar(inmueble.getPrecio());
        for (int i = 0; i < todosLosInmuebles.getTamanio(); i++) {
            if (todosLosInmuebles.obtener(i).getCodigo().equals(inmueble.getCodigo())) {
                todosLosInmuebles.eliminar(i);
                todosLosInmuebles.agregar(inmueble);
                break;
            }
        }
        return true;
    }

    public List<Inmueble> obtenerTodos() {
        List<Inmueble> resultado = new ArrayList<>();
        for (int i = 0; i < todosLosInmuebles.getTamanio(); i++) {
            resultado.add(todosLosInmuebles.obtener(i));
        }
        return resultado;
    }

    public List<Double> buscarPorRangoPrecio(double minimo, double maximo) {
        return arbolPrecios.buscarPorRango(minimo, maximo);
    }

    public List<Inmueble> filtrarPorTipo(TipoInmueble tipo) {
        List<Inmueble> resultado = new ArrayList<>();
        for (int i = 0; i < todosLosInmuebles.getTamanio(); i++) {
            Inmueble inmueble = todosLosInmuebles.obtener(i);
            if (inmueble.getTipo() == tipo) resultado.add(inmueble);
        }
        return resultado;
    }

    public List<Inmueble> filtrarPorEstado(EstadoInmueble estado) {
        List<Inmueble> resultado = new ArrayList<>();
        for (int i = 0; i < todosLosInmuebles.getTamanio(); i++) {
            Inmueble inmueble = todosLosInmuebles.obtener(i);
            if (inmueble.getEstado() == estado) resultado.add(inmueble);
        }
        return resultado;
    }

    public List<Inmueble> filtrarPorFinalidad(FinalidadInmueble finalidad) {
        List<Inmueble> resultado = new ArrayList<>();
        for (int i = 0; i < todosLosInmuebles.getTamanio(); i++) {
            Inmueble inmueble = todosLosInmuebles.obtener(i);
            if (inmueble.getFinalidad() == finalidad) resultado.add(inmueble);
        }
        return resultado;
    }

    /**
     * Deshace el último cambio restaurando el estado anterior desde la cima de la pila.
     * Ahora es O(1) gracias a la Pila.
     */
    public Inmueble deshacerUltimoCambio() {
        if (historialCambios.estaVacia()) return null;
        // ✅ desapilar() extrae el último estado en O(1)
        Inmueble anterior = historialCambios.desapilar();
        arbolPrecios.eliminar(tablaPorCodigo.buscar(anterior.getCodigo()).getPrecio());
        tablaPorCodigo.insertar(anterior.getCodigo(), anterior);
        arbolPrecios.insertar(anterior.getPrecio());
        for (int i = 0; i < todosLosInmuebles.getTamanio(); i++) {
            if (todosLosInmuebles.obtener(i).getCodigo().equals(anterior.getCodigo())) {
                todosLosInmuebles.eliminar(i);
                todosLosInmuebles.agregar(anterior);
                break;
            }
        }
        return anterior;
    }

    public boolean existe(String codigo) {
        return tablaPorCodigo.contiene(codigo);
    }
}