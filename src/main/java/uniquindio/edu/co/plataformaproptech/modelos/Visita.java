package uniquindio.edu.co.plataformaproptech.modelos;

import uniquindio.edu.co.plataformaproptech.modelos.enums.EstadoVisita;

import java.time.LocalDate;
import java.time.LocalTime;

public class Visita {
    private String id;
    private String idCliente;
    private String codigoInmueble;
    private LocalDate fecha;
    private LocalTime hora;
    private String idAsesor;
    private EstadoVisita estado;
    private String observaciones;

    public Visita(String id, String idCliente, String codigoInmueble, LocalDate fecha, LocalTime hora, String idAsesor, EstadoVisita estado, String observaciones) {
        this.id = id;
        this.idCliente = idCliente;
        this.codigoInmueble = codigoInmueble;
        this.fecha = fecha;
        this.hora = hora;
        this.idAsesor = idAsesor;
        this.estado = estado;
        this.observaciones = observaciones;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getCodigoInmueble() {
        return codigoInmueble;
    }

    public void setCodigoInmueble(String codigoInmueble) {
        this.codigoInmueble = codigoInmueble;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getIdAsesor() {
        return idAsesor;
    }

    public void setIdAsesor(String idAsesor) {
        this.idAsesor = idAsesor;
    }

    public EstadoVisita getEstado() {
        return estado;
    }

    public void setEstado(EstadoVisita estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return "Visita{" +
                "id='" + id + '\'' +
                ", codigoInmueble='" + codigoInmueble + '\'' +
                ", fecha=" + fecha +
                ", estada=" + estado +
                '}';
    }
}
