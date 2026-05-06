package uniquindio.edu.co.plataformaproptech;

import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioAlertas;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioAsesores;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioClientes;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioInmuebles;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioOperaciones;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioVisitas;
import uniquindio.edu.co.plataformaproptech.servicios.DetectorComportamiento;
import uniquindio.edu.co.plataformaproptech.servicios.ServicioAlertas;
import uniquindio.edu.co.plataformaproptech.servicios.ServicioClientes;
import uniquindio.edu.co.plataformaproptech.servicios.ServicioGrafo;
import uniquindio.edu.co.plataformaproptech.servicios.ServicioInmuebles;
import uniquindio.edu.co.plataformaproptech.servicios.ServicioRecomendacion;
import uniquindio.edu.co.plataformaproptech.servicios.ServicioVisitas;
import uniquindio.edu.co.plataformaproptech.servicios.ServicioAsesores;

public class GestorProptech {

    private static GestorProptech instancia;

    private final RepositorioInmuebles repositorioInmuebles;
    private final RepositorioClientes repositorioClientes;
    private final RepositorioAsesores repositorioAsesores;
    private final RepositorioVisitas repositorioVisitas;
    private final RepositorioOperaciones repositorioOperaciones;
    private final RepositorioAlertas repositorioAlertas;

    private final ServicioInmuebles servicioInmuebles;
    private final ServicioClientes servicioClientes;
    private final ServicioVisitas servicioVisitas;
    private final ServicioRecomendacion servicioRecomendacion;
    private final ServicioAlertas servicioAlertas;
    private final ServicioGrafo servicioGrafo;
    private final DetectorComportamiento detectorComportamiento;
    private final ServicioAsesores servicioAsesores;

    private GestorProptech() {
        repositorioInmuebles = new RepositorioInmuebles();
        repositorioClientes = new RepositorioClientes();
        repositorioAsesores = new RepositorioAsesores();
        repositorioVisitas = new RepositorioVisitas();
        repositorioOperaciones = new RepositorioOperaciones();
        repositorioAlertas = new RepositorioAlertas();
        servicioAsesores = new ServicioAsesores(repositorioAsesores);

        servicioInmuebles = new ServicioInmuebles(repositorioInmuebles);
        servicioClientes = new ServicioClientes(repositorioClientes);
        servicioVisitas = new ServicioVisitas(
                repositorioVisitas,
                repositorioClientes,
                repositorioInmuebles,
                repositorioAsesores
        );
        servicioRecomendacion = new ServicioRecomendacion(
                repositorioInmuebles,
                repositorioClientes,
                repositorioVisitas
        );
        servicioAlertas = new ServicioAlertas(
                repositorioAlertas,
                repositorioInmuebles,
                repositorioVisitas,
                repositorioClientes
        );
        servicioGrafo = new ServicioGrafo(
                repositorioVisitas,
                repositorioClientes,
                repositorioInmuebles
        );
        detectorComportamiento = new DetectorComportamiento(
                repositorioVisitas,
                repositorioInmuebles,
                repositorioClientes,
                repositorioAsesores,
                servicioAlertas
        );
    }

    public static GestorProptech getInstancia() {
        if (instancia == null) {
            instancia = new GestorProptech();
        }
        return instancia;
    }

    public ServicioInmuebles getServicioInmuebles() { return servicioInmuebles; }
    public ServicioClientes getServicioClientes() { return servicioClientes; }
    public ServicioVisitas getServicioVisitas() { return servicioVisitas; }
    public ServicioRecomendacion getServicioRecomendacion() { return servicioRecomendacion; }
    public ServicioAlertas getServicioAlertas() { return servicioAlertas; }
    public ServicioGrafo getServicioGrafo() { return servicioGrafo; }
    public DetectorComportamiento getDetectorComportamiento() { return detectorComportamiento; }
    public RepositorioOperaciones getRepositorioOperaciones() { return repositorioOperaciones; }
    public ServicioAsesores getServicioAsesores() { return servicioAsesores; }
}