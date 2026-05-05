package uniquindio.edu.co.plataformaproptech.servicios;

import uniquindio.edu.co.plataformaproptech.modelos.Alerta;
import uniquindio.edu.co.plataformaproptech.modelos.Inmueble;
import uniquindio.edu.co.plataformaproptech.modelos.Visita;
import uniquindio.edu.co.plataformaproptech.modelos.Cliente;
import uniquindio.edu.co.plataformaproptech.modelos.enums.EstadoVisita;
import uniquindio.edu.co.plataformaproptech.modelos.enums.NivelAlerta;
import uniquindio.edu.co.plataformaproptech.modelos.enums.TipoAlerta;
import uniquindio.edu.co.plataformaproptech.modelos.enums.EstadoInmueble;
import uniquindio.edu.co.plataformaproptech.modelos.enums.EstadoBusqueda;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioAlertas;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioClientes;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioInmuebles;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioVisitas;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ServicioAlertas {

    private RepositorioAlertas repositorioAlertas;
    private RepositorioInmuebles repositorioInmuebles;
    private RepositorioVisitas repositorioVisitas;
    private RepositorioClientes repositorioClientes;

    public ServicioAlertas(RepositorioAlertas repositorioAlertas,
                           RepositorioInmuebles repositorioInmuebles,
                           RepositorioVisitas repositorioVisitas,
                           RepositorioClientes repositorioClientes) {
        this.repositorioAlertas = repositorioAlertas;
        this.repositorioInmuebles = repositorioInmuebles;
        this.repositorioVisitas = repositorioVisitas;
        this.repositorioClientes = repositorioClientes;
    }

    public void generarAlerta(String mensaje, NivelAlerta nivel,
                              TipoAlerta tipo, String idEntidad) {
        String id = UUID.randomUUID().toString();
        Alerta alerta = new Alerta(id, mensaje, LocalDate.now(), nivel, tipo, idEntidad, false);
        repositorioAlertas.agregar(alerta);
    }

    public void detectarInmueblesSinVisitas(int diasLimite) {
        List<Inmueble> todos = repositorioInmuebles.obtenerTodos();
        for (Inmueble inmueble : todos) {
            List<Visita> visitas = repositorioVisitas.filtrarPorInmueble(inmueble.getCodigo());
            if (visitas.isEmpty() && inmueble.isDisponible()) {
                generarAlerta(
                        "Inmueble " + inmueble.getCodigo() + " sin visitas registradas",
                        NivelAlerta.MEDIO,
                        TipoAlerta.INMUEBLE_SIN_VISITAS,
                        inmueble.getCodigo()
                );
            }
        }
    }

    public void detectarVisitasPendientes() {
        List<Visita> pendientes = repositorioVisitas.filtrarPorEstado(EstadoVisita.PENDIENTE);
        for (Visita visita : pendientes) {
            if (visita.getFecha().isBefore(LocalDate.now())) {
                generarAlerta(
                        "Visita " + visita.getId() + " pendiente sin confirmar",
                        NivelAlerta.ALTO,
                        TipoAlerta.VISITA_PENDIENTE,
                        visita.getId()
                );
            }
        }
    }

    public void detectarInmueblesReservadosSinCierre(int diasLimite) {
        List<Inmueble> todos = repositorioInmuebles.obtenerTodos();
        for (Inmueble inmueble : todos) {
            if (inmueble.getEstado() == EstadoInmueble.RESERVADO) {
                List<Visita> visitas = repositorioVisitas.filtrarPorInmueble(inmueble.getCodigo());
                boolean tieneCierre = visitas.stream()
                        .anyMatch(v -> v.getEstado() == EstadoVisita.REALIZADA);
                if (!tieneCierre) {
                    generarAlerta(
                            "Inmueble " + inmueble.getCodigo() + " reservado sin cierre",
                            NivelAlerta.ALTO,
                            TipoAlerta.INMUEBLE_RESERVADO_SIN_CIERRE,
                            inmueble.getCodigo()
                    );
                }
            }
        }
    }

    public void detectarClientesSinSeguimiento() {
        List<Cliente> todos = repositorioClientes.obtenerTodos();
        for (Cliente cliente : todos) {
            if (cliente.getEstadoBusqueda() == EstadoBusqueda.ACTIVO) {
                List<Visita> visitas = repositorioVisitas.filtrarPorCliente(cliente.getId());
                if (visitas.isEmpty()) {
                    generarAlerta(
                            "Cliente " + cliente.getNombre() + " sin seguimiento reciente",
                            NivelAlerta.MEDIO,
                            TipoAlerta.CLIENTE_SIN_SEGUIMIENTO,
                            cliente.getId()
                    );
                }
            }
        }
    }

    public void detectarAltaDemanda(int minimoVisitas) {
        List<Inmueble> todos = repositorioInmuebles.obtenerTodos();
        for (Inmueble inmueble : todos) {
            List<Visita> visitas = repositorioVisitas.filtrarPorInmueble(inmueble.getCodigo());
            if (visitas.size() >= minimoVisitas) {
                generarAlerta(
                        "Inmueble " + inmueble.getCodigo() + " con alta demanda: " + visitas.size() + " visitas",
                        NivelAlerta.BAJO,
                        TipoAlerta.ALTA_DEMANDA,
                        inmueble.getCodigo()
                );
            }
        }
    }

    public void ejecutarTodasLasDetecciones() {
        detectarInmueblesSinVisitas(30);
        detectarVisitasPendientes();
        detectarInmueblesReservadosSinCierre(15);
        detectarClientesSinSeguimiento();
        detectarAltaDemanda(5);
    }

    public Alerta procesarSiguienteAlerta() {
        return repositorioAlertas.procesarSiguienteAlerta();
    }

    public boolean marcarComoRevisada(String id) {
        return repositorioAlertas.marcarComoRevisada(id);
    }

    public List<Alerta> obtenerTodas() {
        return repositorioAlertas.obtenerTodas();
    }

    public List<Alerta> obtenerNoRevisadas() {
        return repositorioAlertas.filtrarPorRevisada(false);
    }

    public int totalPendientes() {
        return repositorioAlertas.totalPendientes();
    }
}