package uniquindio.edu.co.plataformaproptech.repositorios;

import uniquindio.edu.co.plataformaproptech.estructuras.ArbolBST;
import uniquindio.edu.co.plataformaproptech.estructuras.ListaEnlazada;
import uniquindio.edu.co.plataformaproptech.estructuras.TablaHash;
import uniquindio.edu.co.plataformaproptech.modelos.Asesor;

import java.util.ArrayList;
import java.util.List;

public class RepositorioAsesores {

    private TablaHash<String, Asesor> tablaPorId;
    private ArbolBST<Integer> arbolPorCierres;
    private ListaEnlazada<Asesor> todosLosAsesores;

    public RepositorioAsesores() {
        this.tablaPorId = new TablaHash<>(50);
        this.arbolPorCierres = new ArbolBST<>();
        this.todosLosAsesores = new ListaEnlazada<>();
    }

    public void agregar(Asesor asesor) {
        if (tablaPorId.contiene(asesor.getId())) return;
        tablaPorId.insertar(asesor.getId(), asesor);
        arbolPorCierres.insertar(asesor.getCierresRealizados());
        todosLosAsesores.agregar(asesor);
    }

    public Asesor buscarPorId(String id) {
        return tablaPorId.buscar(id);
    }

    public boolean eliminar(String id) {
        Asesor asesor = tablaPorId.buscar(id);
        if (asesor == null) return false;
        arbolPorCierres.eliminar(asesor.getCierresRealizados());
        tablaPorId.eliminar(id);
        for (int i = 0; i < todosLosAsesores.getTamanio(); i++) {
            if (todosLosAsesores.obtener(i).getId().equals(id)) {
                todosLosAsesores.eliminar(i);
                break;
            }
        }
        return true;
    }

    public boolean actualizar(Asesor asesor) {
        Asesor existente = tablaPorId.buscar(asesor.getId());
        if (existente == null) return false;
        arbolPorCierres.eliminar(existente.getCierresRealizados());
        tablaPorId.insertar(asesor.getId(), asesor);
        arbolPorCierres.insertar(asesor.getCierresRealizados());
        for (int i = 0; i < todosLosAsesores.getTamanio(); i++) {
            if (todosLosAsesores.obtener(i).getId().equals(asesor.getId())) {
                todosLosAsesores.eliminar(i);
                todosLosAsesores.agregar(asesor);
                break;
            }
        }
        return true;
    }

    public List<Asesor> obtenerTodos() {
        List<Asesor> resultado = new ArrayList<>();
        for (int i = 0; i < todosLosAsesores.getTamanio(); i++) {
            resultado.add(todosLosAsesores.obtener(i));
        }
        return resultado;
    }

    public List<Integer> rankingPorCierres() {
        return arbolPorCierres.inorden();
    }

    public List<Asesor> filtrarPorZona(String zona) {
        List<Asesor> resultado = new ArrayList<>();
        for (int i = 0; i < todosLosAsesores.getTamanio(); i++) {
            Asesor asesor = todosLosAsesores.obtener(i);
            if (asesor.getZonaAsignada().equalsIgnoreCase(zona)) resultado.add(asesor);
        }
        return resultado;
    }

    public List<Asesor> filtrarPorEspecialidad(String especialidad) {
        List<Asesor> resultado = new ArrayList<>();
        for (int i = 0; i < todosLosAsesores.getTamanio(); i++) {
            Asesor asesor = todosLosAsesores.obtener(i);
            if (asesor.getEspecialidad().equalsIgnoreCase(especialidad)) resultado.add(asesor);
        }
        return resultado;
    }

    public boolean existe(String id) {
        return tablaPorId.contiene(id);
    }
}
