package uniquindio.edu.co.plataformaproptech.modelos;

import uniquindio.edu.co.plataformaproptech.modelos.enums.EstadoOperacion;
import uniquindio.edu.co.plataformaproptech.modelos.enums.TipoOperacion;

import java.time.LocalDate;

public class Operacion {
    private String id;
    private String codigoInmueble;
    private String idCliente;
    private String idAsesor;
    private LocalDate fecha;
    private TipoOperacion tipoOperacion;
    private double valorAcordado;
    private double comision;
    private EstadoOperacion estado;


    public Operacion(String id, String codigoInmueble, String idCliente, String idAsesor, LocalDate fecha, TipoOperacion tipoOperacion, double valorAcordado, double comision, EstadoOperacion estado) {
        this.id = id;
        this.codigoInmueble = codigoInmueble;
        this.idCliente = idCliente;
        this.idAsesor = idAsesor;
        this.fecha = fecha;
        this.tipoOperacion = tipoOperacion;
        this.valorAcordado = valorAcordado;
        this.comision = comision;
        this.estado = estado;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigoInmueble() {
        return codigoInmueble;
    }

    public void setCodigoInmueble(String codigoInmueble) {
        this.codigoInmueble = codigoInmueble;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdAsesor() {
        return idAsesor;
    }

    public void setIdAsesor(String idAsesor) {
        this.idAsesor = idAsesor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public TipoOperacion getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(TipoOperacion tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public double getValorAcordado() {
        return valorAcordado;
    }

    public void setValorAcordado(double valorAcordado) {
        this.valorAcordado = valorAcordado;
    }

    public double getComision() {
        return comision;
    }

    public void setComision(double comision) {
        this.comision = comision;
    }

    public EstadoOperacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoOperacion estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Operacion{" +
                "id='" + id + '\'' +
                ", codigoInmueble='" + codigoInmueble + '\'' +
                ", valorAcordado=" + valorAcordado +
                ", estado='" + estado + '\'' +
                '}';
    }
}
