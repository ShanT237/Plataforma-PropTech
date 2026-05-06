package uniquindio.edu.co.plataformaproptech.servicios;

import uniquindio.edu.co.plataformaproptech.modelos.Asesor;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioAsesores;

import java.util.ArrayList;
import java.util.List;

public class ServicioAsesores {

    private RepositorioAsesores repositorio;

    public ServicioAsesores(RepositorioAsesores repositorio) {
        this.repositorio = repositorio;
    }

    public boolean registrar(Asesor asesor) {
        if (asesor == null) return false;
        if (repositorio.existe(asesor.getId())) return false;
        repositorio.agregar(asesor);
        return true;
    }

    public boolean actualizar(Asesor asesor) {
        if (asesor == null) return false;
        if (!repositorio.existe(asesor.getId())) return false;
        return repositorio.actualizar(asesor);
    }

    public boolean eliminar(String id) {
        if (id == null || id.isEmpty()) return false;
        if (!repositorio.existe(id)) return false;
        return repositorio.eliminar(id);
    }

    public Asesor buscarPorId(String id) {
        if (id == null || id.isEmpty()) return null;
        return repositorio.buscarPorId(id);
    }

    public List<Asesor> obtenerTodos() {
        return repositorio.obtenerTodos();
    }

    public List<Asesor> filtrarPorZona(String zona) {
        if (zona == null || zona.isEmpty()) return new ArrayList<>();
        return repositorio.filtrarPorZona(zona);
    }

    public List<Asesor> filtrarPorEspecialidad(String especialidad) {
        if (especialidad == null || especialidad.isEmpty()) return new ArrayList<>();
        return repositorio.filtrarPorEspecialidad(especialidad);
    }

    public List<Integer> rankingPorCierres() {
        return repositorio.rankingPorCierres();
    }

    public boolean existe(String id) {
        return repositorio.existe(id);
    }
}