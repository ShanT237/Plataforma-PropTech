package uniquindio.edu.co.plataformaproptech.modelos;

import uniquindio.edu.co.plataformaproptech.modelos.enums.NivelAlerta;
import uniquindio.edu.co.plataformaproptech.modelos.enums.TipoAlerta;

import java.time.LocalDate;

public class Alerta {
    private String id;
    private String mensaje;
    private LocalDate fecha;
    private NivelAlerta nivelAlerta;
    private TipoAlerta tipoAlerta;
    private String idEntidadRelacionada;
    private boolean revisada;

    public Alerta(String id, String mensaje, LocalDate fecha, NivelAlerta nivelAlerta, TipoAlerta tipoAlerta, String idEntidadRelacionada, boolean revisada) {
        this.id = id;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.nivelAlerta = nivelAlerta;
        this.tipoAlerta = tipoAlerta;
        this.idEntidadRelacionada = idEntidadRelacionada;
        this.revisada = revisada;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public NivelAlerta getNivelAlerta() {
        return nivelAlerta;
    }

    public void setNivelAlerta(NivelAlerta nivelAlerta) {
        this.nivelAlerta = nivelAlerta;
    }

    public TipoAlerta getTipoAlerta() {
        return tipoAlerta;
    }

    public void setTipoAlerta(TipoAlerta tipoAlerta) {
        this.tipoAlerta = tipoAlerta;
    }

    public String getIdEntidadRelacionada() {
        return idEntidadRelacionada;
    }

    public void setIdEntidadRelacionada(String idEntidadRelacionada) {
        this.idEntidadRelacionada = idEntidadRelacionada;
    }

    public boolean isRevisada() {
        return revisada;
    }

    public void setRevisada(boolean revisada) {
        this.revisada = revisada;
    }


    @Override
    public String toString() {
        return "Alerta{" +
                "id='" + id + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", fecha=" + fecha +
                ", nivelAlerta=" + nivelAlerta +
                ", tipoAlerta=" + tipoAlerta +
                ", idEntidadRelacionada='" + idEntidadRelacionada + '\'' +
                ", revisada=" + revisada +
                '}';
    }
}
