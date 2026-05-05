package uniquindio.edu.co.plataformaproptech.repositorios;

import uniquindio.edu.co.plataformaproptech.estructuras.Cola;
import uniquindio.edu.co.plataformaproptech.estructuras.ColaPrioridad;
import uniquindio.edu.co.plataformaproptech.estructuras.ListaEnlazada;
import uniquindio.edu.co.plataformaproptech.estructuras.TablaHash;
import uniquindio.edu.co.plataformaproptech.modelos.Visita;
import uniquindio.edu.co.plataformaproptech.modelos.enums.EstadoVisita;

import java.util.ArrayList;
import java.util.List;

public class RepositorioVisitas {

    private TablaHash<String, Visita> tablaPorId;
    private Cola<Visita> visitasPendientes;
    private ColaPrioridad<Visita> visitasUrgentes;
    private ListaEnlazada<Visita> historialVisitas;

    public RepositorioVisitas() {
        this.tablaPorId = new TablaHash<>(100);
        this.visitasPendientes = new Cola<>();
        this.visitasUrgentes = new ColaPrioridad<>();
        this.historialVisitas = new ListaEnlazada<>();
    }

    public void agregar(Visita visita) {
        if (tablaPorId.contiene(visita.getId())) return;
        tablaPorId.insertar(visita.getId(), visita);
        historialVisitas.agregar(visita);
        if (visita.getEstado() == EstadoVisita.PENDIENTE) {
            visitasPendientes.encolar(visita);
        }
    }

    public void agregarUrgente(Visita visita, int prioridad) {
        if (tablaPorId.contiene(visita.getId())) return;
        tablaPorId.insertar(visita.getId(), visita);
        historialVisitas.agregar(visita);
        visitasUrgentes.encolar(visita, prioridad);
    }

    public Visita procesarSiguientePendiente() {
        return visitasPendientes.desencolar();
    }

    public Visita procesarSiguienteUrgente() {
        return visitasUrgentes.desencolar();
    }

    public Visita buscarPorId(String id) {
        return tablaPorId.buscar(id);
    }

    public boolean actualizarEstado(String id, EstadoVisita nuevoEstado) {
        Visita visita = tablaPorId.buscar(id);
        if (visita == null) return false;
        visita.setEstado(nuevoEstado);
        return true;
    }

    public List<Visita> obtenerTodas() {
        List<Visita> resultado = new ArrayList<>();
        for (int i = 0; i < historialVisitas.getTamanio(); i++) {
            resultado.add(historialVisitas.obtener(i));
        }
        return resultado;
    }

    public List<Visita> filtrarPorEstado(EstadoVisita estado) {
        List<Visita> resultado = new ArrayList<>();
        for (int i = 0; i < historialVisitas.getTamanio(); i++) {
            Visita visita = historialVisitas.obtener(i);
            if (visita.getEstado() == estado) resultado.add(visita);
        }
        return resultado;
    }

    public List<Visita> filtrarPorCliente(String idCliente) {
        List<Visita> resultado = new ArrayList<>();
        for (int i = 0; i < historialVisitas.getTamanio(); i++) {
            Visita visita = historialVisitas.obtener(i);
            if (visita.getIdCliente().equals(idCliente)) resultado.add(visita);
        }
        return resultado;
    }

    public List<Visita> filtrarPorInmueble(String codigoInmueble) {
        List<Visita> resultado = new ArrayList<>();
        for (int i = 0; i < historialVisitas.getTamanio(); i++) {
            Visita visita = historialVisitas.obtener(i);
            if (visita.getCodigoInmueble().equals(codigoInmueble)) resultado.add(visita);
        }
        return resultado;
    }

    public List<Visita> filtrarPorAsesor(String idAsesor) {
        List<Visita> resultado = new ArrayList<>();
        for (int i = 0; i < historialVisitas.getTamanio(); i++) {
            Visita visita = historialVisitas.obtener(i);
            if (visita.getIdAsesor().equals(idAsesor)) resultado.add(visita);
        }
        return resultado;
    }

    public boolean existe(String id) {
        return tablaPorId.contiene(id);
    }

    public int totalPendientes() {
        return visitasPendientes.getTamanio();
    }

    public int totalUrgentes() {
        return visitasUrgentes.getTamanio();
    }
}