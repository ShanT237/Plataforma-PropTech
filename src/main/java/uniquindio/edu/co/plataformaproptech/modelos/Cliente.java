package uniquindio.edu.co.plataformaproptech.modelos;

import uniquindio.edu.co.plataformaproptech.modelos.enums.EstadoBusqueda;
import uniquindio.edu.co.plataformaproptech.modelos.enums.TipoCliente;
import uniquindio.edu.co.plataformaproptech.modelos.enums.TipoInmueble;

import java.util.List;

public class Cliente {
    private String id;
    private String nombre;
    private String correo;
    private String telefono;
    private TipoCliente tipoCliente;
    private double presupuesto;
    private List<String> zonaDeInteres;
    private TipoInmueble tipoInmuebleDeseado;
    private int habitacionesMinimas;
    private EstadoBusqueda estadoBusqueda;


    public Cliente(String id, String nombre, String telefono, String correo, TipoCliente tipoCliente, double presupuesto, List<String> zonaDeInteres, int habitacionesMinimas, TipoInmueble tipoInmuebleDeseado, EstadoBusqueda estadoBusqueda) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.tipoCliente = tipoCliente;
        this.presupuesto = presupuesto;
        this.zonaDeInteres = zonaDeInteres;
        this.habitacionesMinimas = habitacionesMinimas;
        this.tipoInmuebleDeseado = tipoInmuebleDeseado;
        this.estadoBusqueda = estadoBusqueda;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public int getHabitacionesMinimas() {
        return habitacionesMinimas;
    }

    public void setHabitacionesMinimas(int habitacionesMinimas) {
        this.habitacionesMinimas = habitacionesMinimas;
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

    public EstadoBusqueda getEstadoBusqueda() {
        return estadoBusqueda;
    }

    public void setEstadoBusqueda(EstadoBusqueda estadoBusqueda) {
        this.estadoBusqueda = estadoBusqueda;
    }

    public List<String> getZonaDeInteres() {
        return zonaDeInteres;
    }

    public void setZonaDeInteres(List<String> zonaDeInteres) {
        this.zonaDeInteres = zonaDeInteres;
    }

    public double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public TipoInmueble getTipoInmuebleDeseado() {
        return tipoInmuebleDeseado;
    }

    public void setTipoInmuebleDeseado(TipoInmueble tipoInmuebleDeseado) {
        this.tipoInmuebleDeseado = tipoInmuebleDeseado;
    }


    @Override
    public String toString() {
        return "Cliente{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", tipoCliente=" + tipoCliente +
                ", presupuesto=" + presupuesto +
                ", zonaDeInteres=" + zonaDeInteres +
                ", tipoInmuebleDeseado=" + tipoInmuebleDeseado +
                ", habitacionesMinimas=" + habitacionesMinimas +
                ", estadoBusqueda=" + estadoBusqueda +
                '}';
    }
}
