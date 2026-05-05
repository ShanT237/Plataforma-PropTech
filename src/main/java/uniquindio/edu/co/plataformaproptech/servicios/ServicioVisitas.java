package uniquindio.edu.co.plataformaproptech.servicios;

import uniquindio.edu.co.plataformaproptech.modelos.Visita;
import uniquindio.edu.co.plataformaproptech.modelos.enums.EstadoVisita;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioAsesores;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioInmuebles;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioClientes;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioVisitas;

import java.util.ArrayList;
import java.util.List;

public class ServicioVisitas {

    private RepositorioVisitas repositorioVisitas;
    private RepositorioClientes repositorioClientes;
    private RepositorioInmuebles repositorioInmuebles;
    private RepositorioAsesores repositorioAsesores;

    public ServicioVisitas(RepositorioVisitas repositorioVisitas,
                           RepositorioClientes repositorioClientes,
                           RepositorioInmuebles repositorioInmuebles,
                           RepositorioAsesores repositorioAsesores) {
        this.repositorioVisitas = repositorioVisitas;
        this.repositorioClientes = repositorioClientes;
        this.repositorioInmuebles = repositorioInmuebles;
        this.repositorioAsesores = repositorioAsesores;
    }

    public boolean programar(Visita visita) {
        if (visita == null) return false;
        if (repositorioVisitas.existe(visita.getId())) return false;
        if (!repositorioClientes.existe(visita.getIdCliente())) return false;
        if (!repositorioInmuebles.existe(visita.getCodigoInmueble())) return false;
        if (!repositorioAsesores.existe(visita.getIdAsesor())) return false;
        repositorioVisitas.agregar(visita);
        return true;
    }

    public boolean programarUrgente(Visita visita, int prioridad) {
        if (visita == null) return false;
        if (repositorioVisitas.existe(visita.getId())) return false;
        if (!repositorioClientes.existe(visita.getIdCliente())) return false;
        if (!repositorioInmuebles.existe(visita.getCodigoInmueble())) return false;
        if (!repositorioAsesores.existe(visita.getIdAsesor())) return false;
        repositorioVisitas.agregarUrgente(visita, prioridad);
        return true;
    }

    public boolean cancelar(String id) {
        if (id == null || id.isEmpty()) return false;
        return repositorioVisitas.actualizarEstado(id, EstadoVisita.CANCELADA);
    }

    public boolean confirmar(String id) {
        if (id == null || id.isEmpty()) return false;
        return repositorioVisitas.actualizarEstado(id, EstadoVisita.CONFIRMADA);
    }

    public boolean reprogramar(String id, Visita nuevaVisita) {
        if (id == null || nuevaVisita == null) return false;
        if (!repositorioVisitas.existe(id)) return false;
        repositorioVisitas.actualizarEstado(id, EstadoVisita.REPROGRAMADA);
        repositorioVisitas.agregar(nuevaVisita);
        return true;
    }

    public boolean marcarRealizada(String id) {
        if (id == null || id.isEmpty()) return false;
        return repositorioVisitas.actualizarEstado(id, EstadoVisita.REALIZADA);
    }

    public Visita buscarPorId(String id) {
        if (id == null || id.isEmpty()) return null;
        return repositorioVisitas.buscarPorId(id);
    }

    public List<Visita> obtenerTodas() {
        return repositorioVisitas.obtenerTodas();
    }

    public List<Visita> filtrarPorEstado(EstadoVisita estado) {
        if (estado == null) return new ArrayList<>();
        return repositorioVisitas.filtrarPorEstado(estado);
    }

    public List<Visita> filtrarPorCliente(String idCliente) {
        if (idCliente == null || idCliente.isEmpty()) return new ArrayList<>();
        return repositorioVisitas.filtrarPorCliente(idCliente);
    }

    public List<Visita> filtrarPorInmueble(String codigoInmueble) {
        if (codigoInmueble == null || codigoInmueble.isEmpty()) return new ArrayList<>();
        return repositorioVisitas.filtrarPorInmueble(codigoInmueble);
    }

    public List<Visita> filtrarPorAsesor(String idAsesor) {
        if (idAsesor == null || idAsesor.isEmpty()) return new ArrayList<>();
        return repositorioVisitas.filtrarPorAsesor(idAsesor);
    }

    public Visita procesarSiguientePendiente() {
        return repositorioVisitas.procesarSiguientePendiente();
    }

    public Visita procesarSiguienteUrgente() {
        return repositorioVisitas.procesarSiguienteUrgente();
    }

    public int totalPendientes() {
        return repositorioVisitas.totalPendientes();
    }

    public int totalUrgentes() {
        return repositorioVisitas.totalUrgentes();
    }
}