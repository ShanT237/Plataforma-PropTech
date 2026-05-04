package uniquindio.edu.co.plataformaproptech.modelos;

import java.util.List;

public class Asesor {
    private String id;
    private String nombre;
    private String correo;
    private String telefono;
    private String especialidad;
    private String zonaAsignada;
    private List<String> inmueblesAsignados;
    private List<String> visitasAgendadas;
    private int cierresRealizados;

    public Asesor(String id, String nombre, String correo, String telefono, String especialidad, String zonaAsignada, List<String> inmueblesAsignados, List<String> visitasAgendadas, int cierresRealizados) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.especialidad = especialidad;
        this.zonaAsignada = zonaAsignada;
        this.inmueblesAsignados = inmueblesAsignados;
        this.visitasAgendadas = visitasAgendadas;
        this.cierresRealizados = cierresRealizados;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getZonaAsignada() {
        return zonaAsignada;
    }

    public void setZonaAsignada(String zonaAsignada) {
        this.zonaAsignada = zonaAsignada;
    }

    public List<String> getInmueblesAsignados() {
        return inmueblesAsignados;
    }

    public void setInmueblesAsignados(List<String> inmueblesAsignados) {
        this.inmueblesAsignados = inmueblesAsignados;
    }

    public List<String> getVisitasAgendadas() {
        return visitasAgendadas;
    }

    public void setVisitasAgendadas(List<String> visitasAgendadas) {
        this.visitasAgendadas = visitasAgendadas;
    }

    public int getCierresRealizados() {
        return cierresRealizados;
    }

    public void setCierresRealizados(int cierresRealizados) {
        this.cierresRealizados = cierresRealizados;
    }

    @Override
    public String toString() {
        return "Asesor{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", zonaAsignada='" + zonaAsignada + '\'' +
                ", inmueblesAsignados=" + inmueblesAsignados +
                ", vistasAgendadas=" + visitasAgendadas +
                ", cierresRealizados=" + cierresRealizados +
                '}';
    }
}
