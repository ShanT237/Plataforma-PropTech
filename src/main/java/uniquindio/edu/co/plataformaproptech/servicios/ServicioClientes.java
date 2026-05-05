package uniquindio.edu.co.plataformaproptech.servicios;

import uniquindio.edu.co.plataformaproptech.modelos.Cliente;
import uniquindio.edu.co.plataformaproptech.modelos.enums.EstadoBusqueda;
import uniquindio.edu.co.plataformaproptech.modelos.enums.TipoCliente;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioClientes;

import java.util.ArrayList;
import java.util.List;

public class ServicioClientes {

    private RepositorioClientes repositorio;

    public ServicioClientes(RepositorioClientes repositorio) {
        this.repositorio = repositorio;
    }

    public boolean registrar(Cliente cliente) {
        if (cliente == null) return false;
        if (repositorio.existe(cliente.getId())) return false;
        repositorio.agregar(cliente);
        return true;
    }

    public boolean actualizar(Cliente cliente) {
        if (cliente == null) return false;
        if (!repositorio.existe(cliente.getId())) return false;
        return repositorio.actualizar(cliente);
    }

    public boolean eliminar(String id) {
        if (id == null || id.isEmpty()) return false;
        if (!repositorio.existe(id)) return false;
        return repositorio.eliminar(id);
    }

    public Cliente buscarPorId(String id) {
        if (id == null || id.isEmpty()) return null;
        return repositorio.buscarPorId(id);
    }

    public List<Cliente> obtenerTodos() {
        return repositorio.obtenerTodos();
    }

    public List<Cliente> filtrarPorTipo(TipoCliente tipo) {
        if (tipo == null) return new ArrayList<>();
        return repositorio.filtrarPorTipo(tipo);
    }

    public List<Cliente> filtrarPorEstado(EstadoBusqueda estado) {
        if (estado == null) return new ArrayList<>();
        return repositorio.filtrarPorEstado(estado);
    }

    public List<Cliente> filtrarPorZona(String zona) {
        if (zona == null || zona.isEmpty()) return new ArrayList<>();
        return repositorio.filtrarPorZona(zona);
    }

    public List<Cliente> filtrarPorRangoPresupuesto(double minimo, double maximo) {
        if (minimo < 0 || maximo < minimo) return new ArrayList<>();
        List<Cliente> resultado = new ArrayList<>();
        List<Cliente> todos = repositorio.obtenerTodos();
        for (Cliente cliente : todos) {
            if (cliente.getPresupuesto() >= minimo && cliente.getPresupuesto() <= maximo) {
                resultado.add(cliente);
            }
        }
        return resultado;
    }

    public boolean actualizarEstado(String id, EstadoBusqueda nuevoEstado) {
        Cliente cliente = repositorio.buscarPorId(id);
        if (cliente == null) return false;
        cliente.setEstadoBusqueda(nuevoEstado);
        return repositorio.actualizar(cliente);
    }

    public List<Cliente> clientesActivosEnZona(String zona) {
        if (zona == null || zona.isEmpty()) return new ArrayList<>();
        List<Cliente> resultado = new ArrayList<>();
        List<Cliente> porZona = repositorio.filtrarPorZona(zona);
        for (Cliente cliente : porZona) {
            if (cliente.getEstadoBusqueda() == EstadoBusqueda.ACTIVO) {
                resultado.add(cliente);
            }
        }
        return resultado;
    }

    public List<Cliente> clientesConAltaProbabilidadCierre() {
        List<Cliente> resultado = new ArrayList<>();
        List<Cliente> todos = repositorio.obtenerTodos();
        for (Cliente cliente : todos) {
            if (cliente.getEstadoBusqueda() == EstadoBusqueda.EN_NEGOCIACION) {
                resultado.add(cliente);
            }
        }
        return resultado;
    }
}