package uniquindio.edu.co.plataformaproptech.repositorios;

import uniquindio.edu.co.plataformaproptech.estructuras.ListaEnlazada;
import uniquindio.edu.co.plataformaproptech.estructuras.TablaHash;
import uniquindio.edu.co.plataformaproptech.modelos.Operacion;
import uniquindio.edu.co.plataformaproptech.modelos.enums.TipoOperacion;

import java.util.ArrayList;
import java.util.List;

public class RepositorioOperaciones {

    private TablaHash<String, Operacion> tablaPorId;
    private ListaEnlazada<Operacion> todasLasOperaciones;

    public RepositorioOperaciones() {
        this.tablaPorId = new TablaHash<>(100);
        this.todasLasOperaciones = new ListaEnlazada<>();
    }

    public void agregar(Operacion operacion) {
        if (tablaPorId.contiene(operacion.getId())) return;
        tablaPorId.insertar(operacion.getId(), operacion);
        todasLasOperaciones.agregar(operacion);
    }

    public Operacion buscarPorId(String id) {
        return tablaPorId.buscar(id);
    }

    public boolean eliminar(String id) {
        Operacion operacion = tablaPorId.buscar(id);
        if (operacion == null) return false;
        tablaPorId.eliminar(id);
        for (int i = 0; i < todasLasOperaciones.getTamanio(); i++) {
            if (todasLasOperaciones.obtener(i).getId().equals(id)) {
                todasLasOperaciones.eliminar(i);
                break;
            }
        }
        return true;
    }

    public boolean actualizar(Operacion operacion) {
        Operacion existente = tablaPorId.buscar(operacion.getId());
        if (existente == null) return false;
        tablaPorId.insertar(operacion.getId(), operacion);
        for (int i = 0; i < todasLasOperaciones.getTamanio(); i++) {
            if (todasLasOperaciones.obtener(i).getId().equals(operacion.getId())) {
                todasLasOperaciones.eliminar(i);
                todasLasOperaciones.agregar(operacion);
                break;
            }
        }
        return true;
    }

    public List<Operacion> obtenerTodas() {
        List<Operacion> resultado = new ArrayList<>();
        for (int i = 0; i < todasLasOperaciones.getTamanio(); i++) {
            resultado.add(todasLasOperaciones.obtener(i));
        }
        return resultado;
    }

    public List<Operacion> filtrarPorTipo(TipoOperacion tipo) {
        List<Operacion> resultado = new ArrayList<>();
        for (int i = 0; i < todasLasOperaciones.getTamanio(); i++) {
            Operacion operacion = todasLasOperaciones.obtener(i);
            if (operacion.getTipoOperacion() == tipo) resultado.add(operacion);
        }
        return resultado;
    }

    public List<Operacion> filtrarPorCliente(String idCliente) {
        List<Operacion> resultado = new ArrayList<>();
        for (int i = 0; i < todasLasOperaciones.getTamanio(); i++) {
            Operacion operacion = todasLasOperaciones.obtener(i);
            if (operacion.getIdCliente().equals(idCliente)) resultado.add(operacion);
        }
        return resultado;
    }

    public List<Operacion> filtrarPorInmueble(String codigoInmueble) {
        List<Operacion> resultado = new ArrayList<>();
        for (int i = 0; i < todasLasOperaciones.getTamanio(); i++) {
            Operacion operacion = todasLasOperaciones.obtener(i);
            if (operacion.getCodigoInmueble().equals(codigoInmueble)) resultado.add(operacion);
        }
        return resultado;
    }

    public List<Operacion> filtrarPorAsesor(String idAsesor) {
        List<Operacion> resultado = new ArrayList<>();
        for (int i = 0; i < todasLasOperaciones.getTamanio(); i++) {
            Operacion operacion = todasLasOperaciones.obtener(i);
            if (operacion.getIdAsesor().equals(idAsesor)) resultado.add(operacion);
        }
        return resultado;
    }

    public double calcularTotalVentas() {
        double total = 0;
        for (int i = 0; i < todasLasOperaciones.getTamanio(); i++) {
            Operacion operacion = todasLasOperaciones.obtener(i);
            if (operacion.getTipoOperacion() == TipoOperacion.VENTA) {
                total += operacion.getValorAcordado();
            }
        }
        return total;
    }

    public double calcularTotalArriendos() {
        double total = 0;
        for (int i = 0; i < todasLasOperaciones.getTamanio(); i++) {
            Operacion operacion = todasLasOperaciones.obtener(i);
            if (operacion.getTipoOperacion() == TipoOperacion.ARRIENDO) {
                total += operacion.getValorAcordado();
            }
        }
        return total;
    }

    public boolean existe(String id) {
        return tablaPorId.contiene(id);
    }
}