package uniquindio.edu.co.plataformaproptech.servicios;

import uniquindio.edu.co.plataformaproptech.modelos.Operacion;
import uniquindio.edu.co.plataformaproptech.modelos.enums.EstadoOperacion;
import uniquindio.edu.co.plataformaproptech.modelos.enums.TipoOperacion;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioOperaciones;

import java.util.ArrayList;
import java.util.List;

/**
 * Capa de servicio para Operaciones.
 * Antes las operaciones se gestionaban directamente desde el repositorio;
 * este servicio añade validación, lógica de negocio y un punto único de acceso.
 */
public class ServicioOperaciones {

    private final RepositorioOperaciones repositorio;

    public ServicioOperaciones(RepositorioOperaciones repositorio) {
        this.repositorio = repositorio;
    }

    // ── CRUD básico ──────────────────────────────────────────────────────────

    public boolean registrar(Operacion operacion) {
        if (operacion == null) return false;
        if (repositorio.existe(operacion.getId())) return false;
        if (operacion.getValorAcordado() <= 0) return false;
        repositorio.agregar(operacion);
        return true;
    }

    public boolean actualizar(Operacion operacion) {
        if (operacion == null) return false;
        if (!repositorio.existe(operacion.getId())) return false;
        return repositorio.actualizar(operacion);
    }

    public boolean cancelar(String id) {
        if (id == null || id.isEmpty()) return false;
        Operacion operacion = repositorio.buscarPorId(id);
        if (operacion == null) return false;
        operacion.setEstado(EstadoOperacion.CANCELADO);
        return repositorio.actualizar(operacion);
    }

    public boolean completar(String id) {
        if (id == null || id.isEmpty()) return false;
        Operacion operacion = repositorio.buscarPorId(id);
        if (operacion == null) return false;
        operacion.setEstado(EstadoOperacion.COMPLETADO);
        return repositorio.actualizar(operacion);
    }

    public boolean eliminar(String id) {
        if (id == null || id.isEmpty()) return false;
        if (!repositorio.existe(id)) return false;
        return repositorio.eliminar(id);
    }

    public Operacion buscarPorId(String id) {
        if (id == null || id.isEmpty()) return null;
        return repositorio.buscarPorId(id);
    }

    // ── Consultas ────────────────────────────────────────────────────────────

    public List<Operacion> obtenerTodas() {
        return repositorio.obtenerTodas();
    }

    public List<Operacion> filtrarPorTipo(TipoOperacion tipo) {
        if (tipo == null) return new ArrayList<>();
        return repositorio.filtrarPorTipo(tipo);
    }

    public List<Operacion> filtrarPorCliente(String idCliente) {
        if (idCliente == null || idCliente.isEmpty()) return new ArrayList<>();
        return repositorio.filtrarPorCliente(idCliente);
    }

    public List<Operacion> filtrarPorInmueble(String codigoInmueble) {
        if (codigoInmueble == null || codigoInmueble.isEmpty()) return new ArrayList<>();
        return repositorio.filtrarPorInmueble(codigoInmueble);
    }

    public List<Operacion> filtrarPorAsesor(String idAsesor) {
        if (idAsesor == null || idAsesor.isEmpty()) return new ArrayList<>();
        return repositorio.filtrarPorAsesor(idAsesor);
    }

    public List<Operacion> filtrarPorEstado(EstadoOperacion estado) {
        if (estado == null) return new ArrayList<>();
        List<Operacion> resultado = new ArrayList<>();
        for (Operacion op : repositorio.obtenerTodas()) {
            if (op.getEstado() == estado) resultado.add(op);
        }
        return resultado;
    }

    // ── Estadísticas ─────────────────────────────────────────────────────────

    public double calcularTotalVentas() {
        return repositorio.calcularTotalVentas();
    }

    public double calcularTotalArriendos() {
        return repositorio.calcularTotalArriendos();
    }

    public double calcularComisionesAsesor(String idAsesor) {
        if (idAsesor == null || idAsesor.isEmpty()) return 0;
        return repositorio.filtrarPorAsesor(idAsesor).stream()
                .filter(op -> op.getEstado() == EstadoOperacion.COMPLETADO)
                .mapToDouble(Operacion::getComision)
                .sum();
    }

    public boolean existe(String id) {
        return repositorio.existe(id);
    }
}