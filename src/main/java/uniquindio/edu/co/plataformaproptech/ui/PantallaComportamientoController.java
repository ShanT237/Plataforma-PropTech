package uniquindio.edu.co.plataformaproptech.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import uniquindio.edu.co.plataformaproptech.GestorProptech;
import uniquindio.edu.co.plataformaproptech.modelos.Alerta;
import uniquindio.edu.co.plataformaproptech.modelos.enums.TipoAlerta;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PantallaComportamientoController implements Initializable {

    @FXML private TableView<Alerta> tablaAlertas;
    @FXML private TableColumn<Alerta, String> colId;
    @FXML private TableColumn<Alerta, String> colMensaje;
    @FXML private TableColumn<Alerta, String> colFecha;
    @FXML private TableColumn<Alerta, String> colNivel;
    @FXML private TableColumn<Alerta, String> colEntidad;
    @FXML private TableColumn<Alerta, Boolean> colRevisada;
    @FXML private Label lblTotal;
    @FXML private Label lblEstado;

    private final GestorProptech gestor = GestorProptech.getInstancia();
    private ObservableList<Alerta> listaObservable = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarColumnas();
        cargarAlertasComportamiento();
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMensaje.setCellValueFactory(new PropertyValueFactory<>("mensaje"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colNivel.setCellValueFactory(new PropertyValueFactory<>("nivelAlerta"));
        colEntidad.setCellValueFactory(new PropertyValueFactory<>("idEntidadRelacionada"));
        colRevisada.setCellValueFactory(new PropertyValueFactory<>("revisada"));
        tablaAlertas.setItems(listaObservable);
    }

    private void cargarAlertasComportamiento() {
        List<Alerta> todas = gestor.getServicioAlertas().obtenerTodas();
        List<Alerta> comportamiento = todas.stream()
                .filter(a -> a.getTipoAlerta() == TipoAlerta.COMPORTAMIENTO_INUSUAL)
                .toList();
        listaObservable.setAll(comportamiento);
        lblTotal.setText(comportamiento.size() + " alertas detectadas");
    }

    @FXML
    private void ejecutarTodas() {
        gestor.getDetectorComportamiento().ejecutarTodasLasDetecciones();
        cargarAlertasComportamiento();
        lblEstado.setText("Todas las detecciones ejecutadas correctamente.");
        mostrarInfo("Detecciones completadas. Se analizaron inmuebles, " +
                "clientes, asesores y zonas.");
    }

    @FXML
    private void detectarVisitasSinCierre() {
        gestor.getDetectorComportamiento().detectarInmueblesConMuchasVisitasSinCierre();
        cargarAlertasComportamiento();
        lblEstado.setText("Detección de inmuebles con visitas sin cierre completada.");
    }

    @FXML
    private void detectarClientesMultiples() {
        gestor.getDetectorComportamiento().detectarClientesConMultiplesVisitas();
        cargarAlertasComportamiento();
        lblEstado.setText("Detección de clientes con múltiples visitas completada.");
    }

    @FXML
    private void detectarSobrecarga() {
        gestor.getDetectorComportamiento().detectarAsesoresConSobrecarga();
        cargarAlertasComportamiento();
        lblEstado.setText("Detección de asesores con sobrecarga completada.");
    }

    @FXML
    private void detectarConcentracion() {
        gestor.getDetectorComportamiento().detectarConcentracionDeInteresPorZona();
        cargarAlertasComportamiento();
        lblEstado.setText("Detección de concentración por zona completada.");
    }

    @FXML
    private void volverAlInicio() {
        Navegador.navegarAInicio();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detecciones completadas");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}