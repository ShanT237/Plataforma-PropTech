package uniquindio.edu.co.plataformaproptech.repositorios;

import uniquindio.edu.co.plataformaproptech.estructuras.ArbolBST;
import uniquindio.edu.co.plataformaproptech.estructuras.ListaEnlazada;
import uniquindio.edu.co.plataformaproptech.estructuras.Pila;
import uniquindio.edu.co.plataformaproptech.estructuras.TablaHash;
import uniquindio.edu.co.plataformaproptech.modelos.Cliente;
import uniquindio.edu.co.plataformaproptech.modelos.enums.EstadoBusqueda;
import uniquindio.edu.co.plataformaproptech.modelos.enums.TipoCliente;

import java.util.ArrayList;
import java.util.List;

public class RepositorioClientes {

    private TablaHash<String, Cliente> tablaPorId;
    private ArbolBST<Double> arbolPresupuestos;
    // ✅ CORREGIDO: Pila en lugar de ListaEnlazada — permite undo/redo en O(1)
    private Pila<Cliente> historialCambios;
    private ListaEnlazada<Cliente> todosLosClientes;

    public RepositorioClientes() {
        this.tablaPorId = new TablaHash<>(100);
        this.arbolPresupuestos = new ArbolBST<>();
        this.historialCambios = new Pila<>();
        this.todosLosClientes = new ListaEnlazada<>();
    }

    public void agregar(Cliente cliente) {
        if (tablaPorId.contiene(cliente.getId())) return;
        tablaPorId.insertar(cliente.getId(), cliente);
        arbolPresupuestos.insertar(cliente.getPresupuesto());
        todosLosClientes.agregar(cliente);
    }

    public Cliente buscarPorId(String id) {
        return tablaPorId.buscar(id);
    }

    public boolean eliminar(String id) {
        Cliente cliente = tablaPorId.buscar(id);
        if (cliente == null) return false;
        arbolPresupuestos.eliminar(cliente.getPresupuesto());
        tablaPorId.eliminar(id);
        for (int i = 0; i < todosLosClientes.getTamanio(); i++) {
            if (todosLosClientes.obtener(i).getId().equals(id)) {
                todosLosClientes.eliminar(i);
                break;
            }
        }
        return true;
    }

    public boolean actualizar(Cliente cliente) {
        Cliente existente = tablaPorId.buscar(cliente.getId());
        if (existente == null) return false;
        // ✅ apilar() en lugar de agregar() — el estado anterior queda en el tope
        historialCambios.apilar(existente);
        arbolPresupuestos.eliminar(existente.getPresupuesto());
        tablaPorId.insertar(cliente.getId(), cliente);
        arbolPresupuestos.insertar(cliente.getPresupuesto());
        for (int i = 0; i < todosLosClientes.getTamanio(); i++) {
            if (todosLosClientes.obtener(i).getId().equals(cliente.getId())) {
                todosLosClientes.eliminar(i);
                todosLosClientes.agregar(cliente);
                break;
            }
        }
        return true;
    }

    public List<Cliente> obtenerTodos() {
        List<Cliente> resultado = new ArrayList<>();
        for (int i = 0; i < todosLosClientes.getTamanio(); i++) {
            resultado.add(todosLosClientes.obtener(i));
        }
        return resultado;
    }

    public List<Cliente> filtrarPorTipo(TipoCliente tipo) {
        List<Cliente> resultado = new ArrayList<>();
        for (int i = 0; i < todosLosClientes.getTamanio(); i++) {
            Cliente cliente = todosLosClientes.obtener(i);
            if (cliente.getTipoCliente() == tipo) resultado.add(cliente);
        }
        return resultado;
    }

    public List<Cliente> filtrarPorEstado(EstadoBusqueda estado) {
        List<Cliente> resultado = new ArrayList<>();
        for (int i = 0; i < todosLosClientes.getTamanio(); i++) {
            Cliente cliente = todosLosClientes.obtener(i);
            if (cliente.getEstadoBusqueda() == estado) resultado.add(cliente);
        }
        return resultado;
    }

    public List<Double> buscarPorRangoPresupuesto(double minimo, double maximo) {
        return arbolPresupuestos.buscarPorRango(minimo, maximo);
    }

    public List<Cliente> filtrarPorZona(String zona) {
        List<Cliente> resultado = new ArrayList<>();
        for (int i = 0; i < todosLosClientes.getTamanio(); i++) {
            Cliente cliente = todosLosClientes.obtener(i);
            if (cliente.getZonaDeInteres().contains(zona)) resultado.add(cliente);
        }
        return resultado;
    }

    /**
     * Deshace el último cambio restaurando el estado anterior desde la cima de la pila.
     * Ahora es O(1) gracias a la Pila.
     */
    public Cliente deshacerUltimoCambio() {
        if (historialCambios.estaVacia()) return null;
        // ✅ desapilar() extrae el último estado en O(1)
        Cliente anterior = historialCambios.desapilar();
        arbolPresupuestos.eliminar(tablaPorId.buscar(anterior.getId()).getPresupuesto());
        tablaPorId.insertar(anterior.getId(), anterior);
        arbolPresupuestos.insertar(anterior.getPresupuesto());
        for (int i = 0; i < todosLosClientes.getTamanio(); i++) {
            if (todosLosClientes.obtener(i).getId().equals(anterior.getId())) {
                todosLosClientes.eliminar(i);
                todosLosClientes.agregar(anterior);
                break;
            }
        }
        return anterior;
    }

    public boolean existe(String id) {
        return tablaPorId.contiene(id);
    }
}