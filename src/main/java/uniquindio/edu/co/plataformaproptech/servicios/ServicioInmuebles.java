package uniquindio.edu.co.plataformaproptech.servicios;

import uniquindio.edu.co.plataformaproptech.modelos.Inmueble;
import uniquindio.edu.co.plataformaproptech.modelos.enums.EstadoInmueble;
import uniquindio.edu.co.plataformaproptech.modelos.enums.FinalidadInmueble;
import uniquindio.edu.co.plataformaproptech.modelos.enums.TipoInmueble;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioInmuebles;

import java.util.ArrayList;
import java.util.List;

public class ServicioInmuebles {

    private RepositorioInmuebles repositorio;

    public ServicioInmuebles(RepositorioInmuebles repositorio) {
        this.repositorio = repositorio;
    }

    public boolean registrar(Inmueble inmueble) {
        if (inmueble == null) return false;
        if (repositorio.existe(inmueble.getCodigo())) return false;
        repositorio.agregar(inmueble);
        return true;
    }

    public boolean actualizar(Inmueble inmueble) {
        if (inmueble == null) return false;
        if (!repositorio.existe(inmueble.getCodigo())) return false;
        return repositorio.actualizar(inmueble);
    }

    public boolean eliminar(String codigo) {
        if (codigo == null || codigo.isEmpty()) return false;
        if (!repositorio.existe(codigo)) return false;
        return repositorio.eliminar(codigo);
    }

    public Inmueble buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.isEmpty()) return null;
        return repositorio.buscarPorCodigo(codigo);
    }

    public List<Inmueble> obtenerTodos() {
        return repositorio.obtenerTodos();
    }

    public List<Inmueble> filtrarPorTipo(TipoInmueble tipo) {
        if (tipo == null) return new ArrayList<>();
        return repositorio.filtrarPorTipo(tipo);
    }

    public List<Inmueble> filtrarPorEstado(EstadoInmueble estado) {
        if (estado == null) return new ArrayList<>();
        return repositorio.filtrarPorEstado(estado);
    }

    public List<Inmueble> filtrarPorFinalidad(FinalidadInmueble finalidad) {
        if (finalidad == null) return new ArrayList<>();
        return repositorio.filtrarPorFinalidad(finalidad);
    }

    public List<Inmueble> filtrarPorRangoPrecio(double minimo, double maximo) {
        if (minimo < 0 || maximo < minimo) return new ArrayList<>();
        List<Inmueble> resultado = new ArrayList<>();
        List<Inmueble> todos = repositorio.obtenerTodos();
        for (Inmueble inmueble : todos) {
            if (inmueble.getPrecio() >= minimo && inmueble.getPrecio() <= maximo) {
                resultado.add(inmueble);
            }
        }
        return resultado;
    }

    public List<Inmueble> filtrarPorCiudad(String ciudad) {
        if (ciudad == null || ciudad.isEmpty()) return new ArrayList<>();
        List<Inmueble> resultado = new ArrayList<>();
        List<Inmueble> todos = repositorio.obtenerTodos();
        for (Inmueble inmueble : todos) {
            if (inmueble.getCiudad().equalsIgnoreCase(ciudad)) resultado.add(inmueble);
        }
        return resultado;
    }

    public List<Inmueble> filtrarPorZona(String zona) {
        if (zona == null || zona.isEmpty()) return new ArrayList<>();
        List<Inmueble> resultado = new ArrayList<>();
        List<Inmueble> todos = repositorio.obtenerTodos();
        for (Inmueble inmueble : todos) {
            if (inmueble.getBarrio().equalsIgnoreCase(zona)) resultado.add(inmueble);
        }
        return resultado;
    }

    public List<Inmueble> filtrarCombinado(TipoInmueble tipo, FinalidadInmueble finalidad,
                                           double precioMin, double precioMax,
                                           int habitacionesMin, String ciudad) {
        List<Inmueble> resultado = new ArrayList<>();
        List<Inmueble> todos = repositorio.obtenerTodos();
        for (Inmueble inmueble : todos) {
            boolean cumpleTipo = tipo == null || inmueble.getTipo() == tipo;
            boolean cumpleFinalidad = finalidad == null || inmueble.getFinalidad() == finalidad;
            boolean cumplePrecio = inmueble.getPrecio() >= precioMin && inmueble.getPrecio() <= precioMax;
            boolean cumpleHabitaciones = inmueble.getHabitaciones() >= habitacionesMin;
            boolean cumpleCiudad = ciudad == null || inmueble.getCiudad().equalsIgnoreCase(ciudad);
            if (cumpleTipo && cumpleFinalidad && cumplePrecio && cumpleHabitaciones && cumpleCiudad) {
                resultado.add(inmueble);
            }
        }
        return resultado;
    }

    public Inmueble deshacerUltimoCambio() {
        return repositorio.deshacerUltimoCambio();
    }
}