package uniquindio.edu.co.plataformaproptech.servicios;

import uniquindio.edu.co.plataformaproptech.modelos.Cliente;
import uniquindio.edu.co.plataformaproptech.modelos.Inmueble;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioClientes;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioInmuebles;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioVisitas;

import java.util.ArrayList;
import java.util.List;

public class ServicioRecomendacion {

    private RepositorioInmuebles repositorioInmuebles;
    private RepositorioClientes repositorioClientes;
    private RepositorioVisitas repositorioVisitas;

    public ServicioRecomendacion(RepositorioInmuebles repositorioInmuebles,
                                 RepositorioClientes repositorioClientes,
                                 RepositorioVisitas repositorioVisitas) {
        this.repositorioInmuebles = repositorioInmuebles;
        this.repositorioClientes = repositorioClientes;
        this.repositorioVisitas = repositorioVisitas;
    }

    public List<Inmueble> recomendar(String idCliente) {
        Cliente cliente = repositorioClientes.buscarPorId(idCliente);
        if (cliente == null) return new ArrayList<>();
        List<Inmueble> todos = repositorioInmuebles.obtenerTodos();
        List<Inmueble> recomendados = new ArrayList<>();
        for (Inmueble inmueble : todos) {
            if (cumplePerfilCliente(inmueble, cliente)) {
                recomendados.add(inmueble);
            }
        }
        return recomendados;
    }

    private boolean cumplePerfilCliente(Inmueble inmueble, Cliente cliente) {
        boolean cumplePrecio = inmueble.getPrecio() <= cliente.getPresupuesto();
        boolean cumpleTipo = inmueble.getTipo() == cliente.getTipoInmuebleDeseado();
        boolean cumpleHabitaciones = inmueble.getHabitaciones() >= cliente.getHabitacionesMinimas();
        boolean cumpleZona = cliente.getZonaDeInteres().contains(inmueble.getBarrio())
                || cliente.getZonaDeInteres().contains(inmueble.getCiudad());
        boolean estaDisponible = inmueble.isDisponible();
        return cumplePrecio && cumpleTipo && cumpleHabitaciones && cumpleZona && estaDisponible;
    }

    public List<Inmueble> recomendarSimilares(String codigoInmueble) {
        Inmueble referencia = repositorioInmuebles.buscarPorCodigo(codigoInmueble);
        if (referencia == null) return new ArrayList<>();
        List<Inmueble> todos = repositorioInmuebles.obtenerTodos();
        List<Inmueble> similares = new ArrayList<>();
        for (Inmueble inmueble : todos) {
            if (inmueble.getCodigo().equals(codigoInmueble)) continue;
            if (esSimilar(inmueble, referencia)) {
                similares.add(inmueble);
            }
        }
        return similares;
    }

    private boolean esSimilar(Inmueble inmueble, Inmueble referencia) {
        boolean mismoTipo = inmueble.getTipo() == referencia.getTipo();
        boolean mismaFinalidad = inmueble.getFinalidad() == referencia.getFinalidad();
        boolean precioSimilar = Math.abs(inmueble.getPrecio() - referencia.getPrecio())
                <= referencia.getPrecio() * 0.2;
        boolean zonaSimilar = inmueble.getCiudad().equalsIgnoreCase(referencia.getCiudad());
        boolean habitacionesSimilares = Math.abs(inmueble.getHabitaciones()
                - referencia.getHabitaciones()) <= 1;
        return mismoTipo && mismaFinalidad && precioSimilar && zonaSimilar && habitacionesSimilares;
    }

    public List<Inmueble> recomendarPorHistorial(String idCliente) {
        List<Inmueble> resultado = new ArrayList<>();
        List<Inmueble> visitados = obtenerInmueblesVisitadosPorCliente(idCliente);
        if (visitados.isEmpty()) return recomendar(idCliente);
        for (Inmueble visitado : visitados) {
            List<Inmueble> similares = recomendarSimilares(visitado.getCodigo());
            for (Inmueble similar : similares) {
                if (!resultado.contains(similar) && !visitados.contains(similar)) {
                    resultado.add(similar);
                }
            }
        }
        return resultado;
    }

    private List<Inmueble> obtenerInmueblesVisitadosPorCliente(String idCliente) {
        List<Inmueble> resultado = new ArrayList<>();
        repositorioVisitas.filtrarPorCliente(idCliente).forEach(visita -> {
            Inmueble inmueble = repositorioInmuebles.buscarPorCodigo(visita.getCodigoInmueble());
            if (inmueble != null && !resultado.contains(inmueble)) {
                resultado.add(inmueble);
            }
        });
        return resultado;
    }
}