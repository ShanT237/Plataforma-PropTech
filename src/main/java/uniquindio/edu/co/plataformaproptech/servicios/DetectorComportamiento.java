package uniquindio.edu.co.plataformaproptech.servicios;

import uniquindio.edu.co.plataformaproptech.modelos.Asesor;
import uniquindio.edu.co.plataformaproptech.modelos.Inmueble;
import uniquindio.edu.co.plataformaproptech.modelos.Cliente;
import uniquindio.edu.co.plataformaproptech.modelos.Visita;
import uniquindio.edu.co.plataformaproptech.modelos.enums.EstadoVisita;
import uniquindio.edu.co.plataformaproptech.modelos.enums.NivelAlerta;
import uniquindio.edu.co.plataformaproptech.modelos.enums.TipoAlerta;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioAsesores;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioClientes;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioInmuebles;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioVisitas;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;
import java.util.List;

public class DetectorComportamiento {

    private RepositorioVisitas repositorioVisitas;
    private RepositorioInmuebles repositorioInmuebles;
    private RepositorioClientes repositorioClientes;
    private RepositorioAsesores repositorioAsesores;
    private ServicioAlertas servicioAlertas;

    private static final int LIMITE_VISITAS_SIN_CIERRE = 10;
    private static final int LIMITE_VISITAS_CLIENTE = 5;
    private static final int LIMITE_VISITAS_ASESOR = 20;
    private static final int DIAS_VENTANA = 7;

    public DetectorComportamiento(RepositorioVisitas repositorioVisitas,
                                  RepositorioInmuebles repositorioInmuebles,
                                  RepositorioClientes repositorioClientes,
                                  RepositorioAsesores repositorioAsesores,
                                  ServicioAlertas servicioAlertas) {
        this.repositorioVisitas = repositorioVisitas;
        this.repositorioInmuebles = repositorioInmuebles;
        this.repositorioClientes = repositorioClientes;
        this.repositorioAsesores = repositorioAsesores;
        this.servicioAlertas = servicioAlertas;
    }

    public void detectarInmueblesConMuchasVisitasSinCierre() {
        List<Inmueble> todos = repositorioInmuebles.obtenerTodos();
        for (Inmueble inmueble : todos) {
            List<Visita> visitas = repositorioVisitas.filtrarPorInmueble(inmueble.getCodigo());
            long sinCierre = visitas.stream()
                    .filter(v -> v.getEstado() != EstadoVisita.REALIZADA)
                    .count();
            if (sinCierre >= LIMITE_VISITAS_SIN_CIERRE) {
                servicioAlertas.generarAlerta(
                        "Inmueble " + inmueble.getCodigo() + " tiene " + sinCierre + " visitas sin cierre",
                        NivelAlerta.ALTO,
                        TipoAlerta.COMPORTAMIENTO_INUSUAL,
                        inmueble.getCodigo()
                );
            }
        }
    }

    public void detectarClientesConMultiplesVisitas() {
        List<Cliente> todos = repositorioClientes.obtenerTodos();
        for (Cliente cliente : todos) {
            List<Visita> visitas = repositorioVisitas.filtrarPorCliente(cliente.getId());
            long visitasRecientes = visitas.stream()
                    .filter(v -> v.getFecha().isAfter(LocalDate.now().minusDays(DIAS_VENTANA)))
                    .count();
            if (visitasRecientes >= LIMITE_VISITAS_CLIENTE) {
                servicioAlertas.generarAlerta(
                        "Cliente " + cliente.getNombre() + " agendó " + visitasRecientes + " visitas en " + DIAS_VENTANA + " días",
                        NivelAlerta.MEDIO,
                        TipoAlerta.COMPORTAMIENTO_INUSUAL,
                        cliente.getId()
                );
            }
        }
    }

    public void detectarAsesoresConSobrecarga() {
        List<Asesor> todos = repositorioAsesores.obtenerTodos();
        for (Asesor asesor : todos) {
            List<Visita> visitas = repositorioVisitas.filtrarPorAsesor(asesor.getId());
            long visitasActivas = visitas.stream()
                    .filter(v -> v.getEstado() == EstadoVisita.PENDIENTE
                            || v.getEstado() == EstadoVisita.CONFIRMADA)
                    .count();
            if (visitasActivas >= LIMITE_VISITAS_ASESOR) {
                servicioAlertas.generarAlerta(
                        "Asesor " + asesor.getNombre() + " tiene sobrecarga con " + visitasActivas + " visitas activas",
                        NivelAlerta.ALTO,
                        TipoAlerta.COMPORTAMIENTO_INUSUAL,
                        asesor.getId()
                );
            }
        }
    }

    public void detectarConcentracionDeInteresPorZona() {
        List<Inmueble> todos = repositorioInmuebles.obtenerTodos();
        List<String> zonas = new ArrayList<>();
        List<Integer> conteos = new ArrayList<>();
        for (Inmueble inmueble : todos) {
            List<Visita> visitas = repositorioVisitas.filtrarPorInmueble(inmueble.getCodigo());
            long visitasRecientes = visitas.stream()
                    .filter(v -> v.getFecha().isAfter(LocalDate.now().minusDays(DIAS_VENTANA)))
                    .count();
            String zona = inmueble.getBarrio();
            int indice = zonas.indexOf(zona);
            if (indice == -1) {
                zonas.add(zona);
                conteos.add((int) visitasRecientes);
            } else {
                conteos.set(indice, conteos.get(indice) + (int) visitasRecientes);
            }
        }
        for (int i = 0; i < zonas.size(); i++) {
            if (conteos.get(i) >= LIMITE_VISITAS_SIN_CIERRE) {
                servicioAlertas.generarAlerta(
                        "Concentración inusual de interés en zona " + zonas.get(i) + " con " + conteos.get(i) + " visitas recientes",
                        NivelAlerta.MEDIO,
                        TipoAlerta.COMPORTAMIENTO_INUSUAL,
                        zonas.get(i)
                );
            }
        }
    }

    public void ejecutarTodasLasDetecciones() {
        detectarInmueblesConMuchasVisitasSinCierre();
        detectarClientesConMultiplesVisitas();
        detectarAsesoresConSobrecarga();
        detectarConcentracionDeInteresPorZona();
    }
}