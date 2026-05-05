package uniquindio.edu.co.plataformaproptech.servicios;

import uniquindio.edu.co.plataformaproptech.estructuras.Grafo;
import uniquindio.edu.co.plataformaproptech.modelos.Visita;
import uniquindio.edu.co.plataformaproptech.modelos.Inmueble;
import uniquindio.edu.co.plataformaproptech.modelos.Cliente;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioClientes;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioInmuebles;
import uniquindio.edu.co.plataformaproptech.repositorios.RepositorioVisitas;

import java.util.ArrayList;
import java.util.List;

public class ServicioGrafo {

    private Grafo<String> grafoClientesInmuebles;
    private Grafo<String> grafoInmueblesRelacionados;
    private RepositorioVisitas repositorioVisitas;
    private RepositorioClientes repositorioClientes;
    private RepositorioInmuebles repositorioInmuebles;

    public ServicioGrafo(RepositorioVisitas repositorioVisitas,
                         RepositorioClientes repositorioClientes,
                         RepositorioInmuebles repositorioInmuebles) {
        this.repositorioVisitas = repositorioVisitas;
        this.repositorioClientes = repositorioClientes;
        this.repositorioInmuebles = repositorioInmuebles;
        this.grafoClientesInmuebles = new Grafo<>(true);
        this.grafoInmueblesRelacionados = new Grafo<>(false);
    }

    public void construirGrafoClientesInmuebles() {
        List<Cliente> clientes = repositorioClientes.obtenerTodos();
        for (Cliente cliente : clientes) {
            grafoClientesInmuebles.agregarVertice(cliente.getId());
            List<Visita> visitas = repositorioVisitas.filtrarPorCliente(cliente.getId());
            for (Visita visita : visitas) {
                grafoClientesInmuebles.agregarVertice(visita.getCodigoInmueble());
                grafoClientesInmuebles.agregarArista(cliente.getId(), visita.getCodigoInmueble());
            }
        }
    }

    public void construirGrafoInmueblesRelacionados() {
        List<Inmueble> inmuebles = repositorioInmuebles.obtenerTodos();
        for (int i = 0; i < inmuebles.size(); i++) {
            for (int j = i + 1; j < inmuebles.size(); j++) {
                Inmueble a = inmuebles.get(i);
                Inmueble b = inmuebles.get(j);
                if (sonInmueblesRelacionados(a, b)) {
                    grafoInmueblesRelacionados.agregarArista(a.getCodigo(), b.getCodigo());
                }
            }
        }
    }

    private boolean sonInmueblesRelacionados(Inmueble a, Inmueble b) {
        boolean mismoTipo = a.getTipo() == b.getTipo();
        boolean mismaZona = a.getBarrio().equalsIgnoreCase(b.getBarrio());
        boolean precioSimilar = Math.abs(a.getPrecio() - b.getPrecio()) <= a.getPrecio() * 0.2;
        return mismoTipo && mismaZona && precioSimilar;
    }

    public List<String> obtenerInmueblesVisitadosPorCliente(String idCliente) {
        if (!grafoClientesInmuebles.existeVertice(idCliente)) return new ArrayList<>();
        return grafoClientesInmuebles.obtenerVecinos(idCliente);
    }

    public List<String> obtenerClientesQueVisitaronInmueble(String codigoInmueble) {
        List<String> resultado = new ArrayList<>();
        List<Cliente> clientes = repositorioClientes.obtenerTodos();
        for (Cliente cliente : clientes) {
            if (grafoClientesInmuebles.existeArista(cliente.getId(), codigoInmueble)) {
                resultado.add(cliente.getId());
            }
        }
        return resultado;
    }

    public List<String> obtenerInmueblesRelacionados(String codigoInmueble) {
        if (!grafoInmueblesRelacionados.existeVertice(codigoInmueble)) return new ArrayList<>();
        return grafoInmueblesRelacionados.obtenerVecinos(codigoInmueble);
    }

    public List<String> obtenerClientesConInteresesSimilares(String idCliente) {
        List<String> inmueblesCliente = obtenerInmueblesVisitadosPorCliente(idCliente);
        List<String> clientesSimilares = new ArrayList<>();
        List<Cliente> todos = repositorioClientes.obtenerTodos();
        for (Cliente otroCliente : todos) {
            if (otroCliente.getId().equals(idCliente)) continue;
            List<String> inmueblesOtro = obtenerInmueblesVisitadosPorCliente(otroCliente.getId());
            for (String codigo : inmueblesCliente) {
                if (inmueblesOtro.contains(codigo) && !clientesSimilares.contains(otroCliente.getId())) {
                    clientesSimilares.add(otroCliente.getId());
                }
            }
        }
        return clientesSimilares;
    }

    public List<String> rankingZonasPorActividad() {
        List<Inmueble> todos = repositorioInmuebles.obtenerTodos();
        List<String> zonas = new ArrayList<>();
        List<Integer> conteos = new ArrayList<>();
        for (Inmueble inmueble : todos) {
            String zona = inmueble.getBarrio();
            int visitas = repositorioVisitas.filtrarPorInmueble(inmueble.getCodigo()).size();
            int indice = zonas.indexOf(zona);
            if (indice == -1) {
                zonas.add(zona);
                conteos.add(visitas);
            } else {
                conteos.set(indice, conteos.get(indice) + visitas);
            }
        }
        return ordenarZonasPorActividad(zonas, conteos);
    }

    private List<String> ordenarZonasPorActividad(List<String> zonas, List<Integer> conteos) {
        for (int i = 0; i < zonas.size() - 1; i++) {
            for (int j = 0; j < zonas.size() - i - 1; j++) {
                if (conteos.get(j) < conteos.get(j + 1)) {
                    String tempZona = zonas.get(j);
                    zonas.set(j, zonas.get(j + 1));
                    zonas.set(j + 1, tempZona);
                    int tempConteo = conteos.get(j);
                    conteos.set(j, conteos.get(j + 1));
                    conteos.set(j + 1, tempConteo);
                }
            }
        }
        return zonas;
    }

    public void reconstruirGrafos() {
        grafoClientesInmuebles = new Grafo<>(true);
        grafoInmueblesRelacionados = new Grafo<>(false);
        construirGrafoClientesInmuebles();
        construirGrafoInmueblesRelacionados();
    }
}