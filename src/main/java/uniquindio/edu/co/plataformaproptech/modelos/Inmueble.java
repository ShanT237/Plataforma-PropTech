package uniquindio.edu.co.plataformaproptech.modelos;


import uniquindio.edu.co.plataformaproptech.modelos.enums.EstadoInmueble;
import uniquindio.edu.co.plataformaproptech.modelos.enums.FinalidadInmueble;
import uniquindio.edu.co.plataformaproptech.modelos.enums.TipoInmueble;

public class Inmueble {

    private String codigo;
    private String direccion;
    private String ciudad;
    private String barrio;
    private TipoInmueble tipo;
    private FinalidadInmueble finalidad;
    private double precio;
    private double area;
    private int habitaciones;
    private int banos;
    private EstadoInmueble estado;
    private boolean disponible;
    private String codigoAsesor;

    public Inmueble(String codigo, String direccion, String ciudad, String barrio,
                    TipoInmueble tipo, FinalidadInmueble finalidad, double precio, double area,
                    int habitaciones, int banos, EstadoInmueble estado, boolean disponible,
                    String codigoAsesor) {
        this.codigo = codigo;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.barrio = barrio;
        this.tipo = tipo;
        this.finalidad = finalidad;
        this.precio = precio;
        this.area = area;
        this.habitaciones = habitaciones;
        this.banos = banos;
        this.estado = estado;
        this.disponible = disponible;
        this.codigoAsesor = codigoAsesor;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getBarrio() { return barrio; }
    public void setBarrio(String barrio) { this.barrio = barrio; }

    public TipoInmueble getTipo() { return tipo; }
    public void setTipo(TipoInmueble tipo) { this.tipo = tipo; }

    public FinalidadInmueble getFinalidad() { return finalidad; }
    public void setFinalidad(FinalidadInmueble finalidad) { this.finalidad = finalidad; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public double getArea() { return area; }
    public void setArea(double area) { this.area = area; }

    public int getHabitaciones() { return habitaciones; }
    public void setHabitaciones(int habitaciones) { this.habitaciones = habitaciones; }

    public int getBanos() { return banos; }
    public void setBanos(int banos) { this.banos = banos; }

    public EstadoInmueble getEstado() { return estado; }
    public void setEstado(EstadoInmueble estado) { this.estado = estado; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public String getCodigoAsesor() { return codigoAsesor; }
    public void setCodigoAsesor(String codigoAsesor) { this.codigoAsesor = codigoAsesor; }

    @Override
    public String toString() {
        return "[" + codigo + "] " + tipo + " en " + direccion + ", " + ciudad +
                " | " + finalidad + " | $" + precio + " | " + estado;
    }
}