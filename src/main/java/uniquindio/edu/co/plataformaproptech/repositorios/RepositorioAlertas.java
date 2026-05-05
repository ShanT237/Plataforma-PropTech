package uniquindio.edu.co.plataformaproptech.repositorios;

import uniquindio.edu.co.plataformaproptech.estructuras.Cola;
import uniquindio.edu.co.plataformaproptech.estructuras.ColaPrioridad;
import uniquindio.edu.co.plataformaproptech.estructuras.ListaEnlazada;
import uniquindio.edu.co.plataformaproptech.estructuras.TablaHash;
import uniquindio.edu.co.plataformaproptech.modelos.Alerta;
import uniquindio.edu.co.plataformaproptech.modelos.enums.NivelAlerta;
import uniquindio.edu.co.plataformaproptech.modelos.enums.TipoAlerta;

import java.util.ArrayList;
import java.util.List;

public class RepositorioAlertas {

    private TablaHash<String, Alerta> tablaPorId;
    private ColaPrioridad<Alerta> alertasPriorizadas;
    private Cola<Alerta> alertasPendientes;
    private ListaEnlazada<Alerta> todasLasAlertas;

    public RepositorioAlertas() {
        this.tablaPorId = new TablaHash<>(100);
        this.alertasPriorizadas = new ColaPrioridad<>();
        this.alertasPendientes = new Cola<>();
        this.todasLasAlertas = new ListaEnlazada<>();
    }

    public void agregar(Alerta alerta) {
        if (tablaPorId.contiene(alerta.getId())) return;
        tablaPorId.insertar(alerta.getId(), alerta);
        todasLasAlertas.agregar(alerta);
        int prioridad = convertirPrioridad(alerta.getNivelAlerta());
        alertasPriorizadas.encolar(alerta, prioridad);
        if (!alerta.isRevisada()) {
            alertasPendientes.encolar(alerta);
        }
    }

    private int convertirPrioridad(NivelAlerta nivel) {
        switch (nivel) {
            case ALTO: return 1;
            case MEDIO: return 2;
            case BAJO: return 3;
            default: return 3;
        }
    }

    public Alerta procesarSiguienteAlerta() {
        return alertasPriorizadas.desencolar();
    }

    public Alerta procesarSiguientePendiente() {
        return alertasPendientes.desencolar();
    }

    public Alerta buscarPorId(String id) {
        return tablaPorId.buscar(id);
    }

    public boolean marcarComoRevisada(String id) {
        Alerta alerta = tablaPorId.buscar(id);
        if (alerta == null) return false;
        alerta.setRevisada(true);
        return true;
    }

    public List<Alerta> obtenerTodas() {
        List<Alerta> resultado = new ArrayList<>();
        for (int i = 0; i < todasLasAlertas.getTamanio(); i++) {
            resultado.add(todasLasAlertas.obtener(i));
        }
        return resultado;
    }

    public List<Alerta> filtrarPorNivel(NivelAlerta nivel) {
        List<Alerta> resultado = new ArrayList<>();
        for (int i = 0; i < todasLasAlertas.getTamanio(); i++) {
            Alerta alerta = todasLasAlertas.obtener(i);
            if (alerta.getNivelAlerta() == nivel) resultado.add(alerta);
        }
        return resultado;
    }

    public List<Alerta> filtrarPorTipo(TipoAlerta tipo) {
        List<Alerta> resultado = new ArrayList<>();
        for (int i = 0; i < todasLasAlertas.getTamanio(); i++) {
            Alerta alerta = todasLasAlertas.obtener(i);
            if (alerta.getTipoAlerta() == tipo) resultado.add(alerta);
        }
        return resultado;
    }

    public List<Alerta> filtrarPorRevisada(boolean revisada) {
        List<Alerta> resultado = new ArrayList<>();
        for (int i = 0; i < todasLasAlertas.getTamanio(); i++) {
            Alerta alerta = todasLasAlertas.obtener(i);
            if (alerta.isRevisada() == revisada) resultado.add(alerta);
        }
        return resultado;
    }

    public boolean existe(String id) {
        return tablaPorId.contiene(id);
    }

    public int totalPendientes() {
        return alertasPendientes.getTamanio();
    }
}