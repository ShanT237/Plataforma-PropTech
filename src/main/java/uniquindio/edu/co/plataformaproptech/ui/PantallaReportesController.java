package uniquindio.edu.co.plataformaproptech.ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import uniquindio.edu.co.plataformaproptech.GestorProptech;
import uniquindio.edu.co.plataformaproptech.modelos.Cliente;
import uniquindio.edu.co.plataformaproptech.modelos.Asesor;
import uniquindio.edu.co.plataformaproptech.modelos.enums.EstadoBusqueda;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PantallaReportesController implements Initializable {

    @FXML private Label lblTotalInmuebles;
    @FXML private Label lblTotalClientes;
    @FXML private Label lblTotalVisitas;
    @FXML private Label lblTotalAsesores;
    @FXML private Label lblClientesActivos;
    @FXML private ListView<String> listaZonas;
    @FXML private ListView<String> listaAsesores;
    @FXML private ListView<String> listaCierres;

    private final GestorProptech gestor = GestorProptech.getInstancia();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        actualizarResumen();
        actualizarRankingZonas();
        actualizarRankingAsesores();
        actualizarCierres();
    }

    private void actualizarResumen() {
        lblTotalInmuebles.setText(String.valueOf(
                gestor.getServicioInmuebles().obtenerTodos().size()));
        lblTotalClientes.setText(String.valueOf(
                gestor.getServicioClientes().obtenerTodos().size()));
        lblTotalVisitas.setText(String.valueOf(
                gestor.getServicioVisitas().obtenerTodas().size()));
        lblTotalAsesores.setText(String.valueOf(
                gestor.getServicioAsesores().obtenerTodos().size()));

        long activos = gestor.getServicioClientes().obtenerTodos().stream()
                .filter(c -> c.getEstadoBusqueda() == EstadoBusqueda.ACTIVO)
                .count();
        lblClientesActivos.setText(String.valueOf(activos));
    }

    @FXML
    private void actualizarRankingZonas() {
        List<String> zonas = gestor.getServicioGrafo().rankingZonasPorActividad();
        gestor.getServicioGrafo().reconstruirGrafos();
        if (zonas.isEmpty()) {
            listaZonas.setItems(FXCollections.observableArrayList("Sin datos disponibles"));
            return;
        }
        java.util.List<String> items = new java.util.ArrayList<>();
        for (int i = 0; i < zonas.size(); i++) {
            items.add((i + 1) + ". " + zonas.get(i));
        }
        listaZonas.setItems(FXCollections.observableArrayList(items));
    }

    @FXML
    private void actualizarRankingAsesores() {
        List<Asesor> asesores = gestor.getServicioAsesores().obtenerTodos();
        if (asesores.isEmpty()) {
            listaAsesores.setItems(FXCollections.observableArrayList("Sin asesores registrados"));
            return;
        }
        List<Asesor> ordenados = asesores.stream()
                .sorted((a, b) -> b.getCierresRealizados() - a.getCierresRealizados())
                .toList();
        java.util.List<String> items = new java.util.ArrayList<>();
        for (int i = 0; i < ordenados.size(); i++) {
            Asesor a = ordenados.get(i);
            items.add((i + 1) + ". " + a.getNombre() +
                    " — " + a.getCierresRealizados() + " cierres");
        }
        listaAsesores.setItems(FXCollections.observableArrayList(items));
    }

    @FXML
    private void actualizarCierres() {
        List<Cliente> clientes = gestor.getServicioClientes()
                .clientesConAltaProbabilidadCierre();
        if (clientes.isEmpty()) {
            listaCierres.setItems(FXCollections.observableArrayList("Sin clientes en negociación"));
            return;
        }
        java.util.List<String> items = new java.util.ArrayList<>();
        for (Cliente c : clientes) {
            items.add(c.getNombre() + " — $" + c.getPresupuesto());
        }
        listaCierres.setItems(FXCollections.observableArrayList(items));
    }

    @FXML
    private void volverAlInicio() {
        Navegador.navegarAInicio();
    }
}