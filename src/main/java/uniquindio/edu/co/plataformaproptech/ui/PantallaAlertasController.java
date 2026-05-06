package uniquindio.edu.co.plataformaproptech.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import uniquindio.edu.co.plataformaproptech.GestorProptech;
import uniquindio.edu.co.plataformaproptech.modelos.Alerta;
import uniquindio.edu.co.plataformaproptech.modelos.enums.NivelAlerta;
import uniquindio.edu.co.plataformaproptech.modelos.enums.TipoAlerta;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PantallaAlertasController implements Initializable {

    @FXML private TableView<Alerta> tablaAlertas;
    @FXML private TableColumn<Alerta, String> colId;
    @FXML private TableColumn<Alerta, String> colMensaje;
    @FXML private TableColumn<Alerta, String> colFecha;
    @FXML private TableColumn<Alerta, String> colNivel;
    @FXML private TableColumn<Alerta, String> colTipo;
    @FXML private TableColumn<Alerta, String> colEntidad;
    @FXML private TableColumn<Alerta, Boolean> colRevisada;
    @FXML private ComboBox<NivelAlerta> cmbNivel;
    @FXML private ComboBox<TipoAlerta> cmbTipo;
    @FXML private ComboBox<String> cmbRevisada;
    @FXML private Label lblTotal;
    @FXML private Label lblPendientes;

    private final GestorProptech gestor = GestorProptech.getInstancia();
    private ObservableList<Alerta> listaObservable = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarColumnas();
        configurarFiltros();
        cargarAlertas();
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMensaje.setCellValueFactory(new PropertyValueFactory<>("mensaje"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colNivel.setCellValueFactory(new PropertyValueFactory<>("nivelAlerta"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoAlerta"));
        colEntidad.setCellValueFactory(new PropertyValueFactory<>("idEntidadRelacionada"));
        colRevisada.setCellValueFactory(new PropertyValueFactory<>("revisada"));
        tablaAlertas.setItems(listaObservable);
    }

    private void configurarFiltros() {
        cmbNivel.setItems(FXCollections.observableArrayList(NivelAlerta.values()));
        cmbTipo.setItems(FXCollections.observableArrayList(TipoAlerta.values()));
        cmbRevisada.setItems(FXCollections.observableArrayList("Revisadas", "Sin revisar"));
    }

    private void cargarAlertas() {
        List<Alerta> todas = gestor.getServicioAlertas().obtenerTodas();
        listaObservable.setAll(todas);
        lblTotal.setText("Total: " + todas.size() + " alertas");
        lblPendientes.setText("Sin revisar: " + gestor.getServicioAlertas().obtenerNoRevisadas().size());
    }

    @FXML
    private void ejecutarDetecciones() {
        gestor.getServicioAlertas().ejecutarTodasLasDetecciones();
        gestor.getDetectorComportamiento().ejecutarTodasLasDetecciones();
        cargarAlertas();
        mostrarInfo("Detecciones ejecutadas. Se generaron " +
                gestor.getServicioAlertas().obtenerTodas().size() + " alertas en total.");
    }

    @FXML
    private void marcarRevisada() {
        Alerta seleccionada = tablaAlertas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Selecciona una alerta para marcar como revisada.");
            return;
        }
        boolean exito = gestor.getServicioAlertas().marcarComoRevisada(seleccionada.getId());
        if (exito) {
            cargarAlertas();
            mostrarInfo("Alerta marcada como revisada.");
        }
    }

    @FXML
    private void procesarSiguiente() {
        Alerta siguiente = gestor.getServicioAlertas().procesarSiguienteAlerta();
        if (siguiente == null) {
            mostrarAlerta("No hay alertas pendientes en la cola.");
            return;
        }
        cargarAlertas();
        mostrarInfo("Alerta procesada:\n" +
                "Nivel: " + siguiente.getNivelAlerta() + "\n" +
                "Tipo: " + siguiente.getTipoAlerta() + "\n" +
                "Mensaje: " + siguiente.getMensaje());
    }

    @FXML
    private void aplicarFiltros() {
        List<Alerta> resultado = gestor.getServicioAlertas().obtenerTodas();
        if (cmbNivel.getValue() != null) {
            resultado = resultado.stream()
                    .filter(a -> a.getNivelAlerta() == cmbNivel.getValue())
                    .toList();
        }
        if (cmbTipo.getValue() != null) {
            resultado = resultado.stream()
                    .filter(a -> a.getTipoAlerta() == cmbTipo.getValue())
                    .toList();
        }
        if (cmbRevisada.getValue() != null) {
            boolean revisada = cmbRevisada.getValue().equals("Revisadas");
            resultado = resultado.stream()
                    .filter(a -> a.isRevisada() == revisada)
                    .toList();
        }
        listaObservable.setAll(resultado);
        lblTotal.setText("Total: " + resultado.size() + " alertas");
    }

    @FXML
    private void limpiarFiltros() {
        cmbNivel.setValue(null);
        cmbTipo.setValue(null);
        cmbRevisada.setValue(null);
        cargarAlertas();
    }

    @FXML
    private void volverAlInicio() {
        Navegador.navegarAInicio();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}