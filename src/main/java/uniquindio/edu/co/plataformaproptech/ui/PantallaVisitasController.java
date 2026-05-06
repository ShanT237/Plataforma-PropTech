package uniquindio.edu.co.plataformaproptech.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import uniquindio.edu.co.plataformaproptech.GestorProptech;
import uniquindio.edu.co.plataformaproptech.modelos.Visita;
import uniquindio.edu.co.plataformaproptech.modelos.enums.EstadoVisita;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PantallaVisitasController implements Initializable {

    @FXML private TableView<Visita> tablaVisitas;
    @FXML private TableColumn<Visita, String> colId;
    @FXML private TableColumn<Visita, String> colCliente;
    @FXML private TableColumn<Visita, String> colInmueble;
    @FXML private TableColumn<Visita, String> colFecha;
    @FXML private TableColumn<Visita, String> colHora;
    @FXML private TableColumn<Visita, String> colAsesor;
    @FXML private TableColumn<Visita, String> colEstado;
    @FXML private TableColumn<Visita, String> colObservaciones;
    @FXML private ComboBox<EstadoVisita> cmbEstado;
    @FXML private TextField txtBuscar;
    @FXML private TextField txtIdCliente;
    @FXML private TextField txtIdInmueble;
    @FXML private Label lblTotal;
    @FXML private Label lblPendientes;

    private final GestorProptech gestor = GestorProptech.getInstancia();
    private ObservableList<Visita> listaObservable = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarColumnas();
        configurarFiltros();
        cargarVisitas();
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        colInmueble.setCellValueFactory(new PropertyValueFactory<>("codigoInmueble"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colAsesor.setCellValueFactory(new PropertyValueFactory<>("idAsesor"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colObservaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
        tablaVisitas.setItems(listaObservable);
    }

    private void configurarFiltros() {
        cmbEstado.setItems(FXCollections.observableArrayList(EstadoVisita.values()));
    }

    private void cargarVisitas() {
        List<Visita> todas = gestor.getServicioVisitas().obtenerTodas();
        listaObservable.setAll(todas);
        lblTotal.setText("Total: " + todas.size() + " visitas");
        lblPendientes.setText("Pendientes: " + gestor.getServicioVisitas().totalPendientes());
    }

    @FXML
    private void abrirFormularioNuevo() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/FormularioVisita.fxml")
            );
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Nueva Visita");
            stage.setScene(new Scene(root));
            stage.setMinWidth(1224);
            stage.setMinHeight(720);
            stage.setWidth(1270);
            stage.setHeight(800);
            stage.initOwner(Navegador.getStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.centerOnScreen();
            stage.setOnHidden(e -> cargarVisitas());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void confirmarVisita() {
        Visita seleccionada = tablaVisitas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Selecciona una visita para confirmar.");
            return;
        }
        boolean exito = gestor.getServicioVisitas().confirmar(seleccionada.getId());
        if (exito) {
            cargarVisitas();
            mostrarInfo("Visita confirmada exitosamente.");
        } else {
            mostrarAlerta("No se pudo confirmar la visita.");
        }
    }

    @FXML
    private void cancelarVisita() {
        Visita seleccionada = tablaVisitas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Selecciona una visita para cancelar.");
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar cancelación");
        confirmacion.setHeaderText("¿Cancelar la visita " + seleccionada.getId() + "?");
        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                gestor.getServicioVisitas().cancelar(seleccionada.getId());
                cargarVisitas();
            }
        });
    }

    @FXML
    private void marcarRealizada() {
        Visita seleccionada = tablaVisitas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Selecciona una visita para marcar como realizada.");
            return;
        }
        boolean exito = gestor.getServicioVisitas().marcarRealizada(seleccionada.getId());
        if (exito) {
            cargarVisitas();
            mostrarInfo("Visita marcada como realizada.");
        } else {
            mostrarAlerta("No se pudo actualizar la visita.");
        }
    }

    @FXML
    private void buscarVisita() {
        String id = txtBuscar.getText().trim();
        if (id.isEmpty()) {
            cargarVisitas();
            return;
        }
        Visita encontrada = gestor.getServicioVisitas().buscarPorId(id);
        listaObservable.clear();
        if (encontrada != null) {
            listaObservable.add(encontrada);
            lblTotal.setText("Total: 1 visita encontrada");
        } else {
            mostrarAlerta("No se encontró ninguna visita con el ID: " + id);
        }
    }

    @FXML
    private void aplicarFiltros() {
        List<Visita> resultado = gestor.getServicioVisitas().obtenerTodas();
        if (cmbEstado.getValue() != null) {
            resultado = resultado.stream()
                    .filter(v -> v.getEstado() == cmbEstado.getValue())
                    .toList();
        }
        if (!txtIdCliente.getText().trim().isEmpty()) {
            String idCliente = txtIdCliente.getText().trim();
            resultado = resultado.stream()
                    .filter(v -> v.getIdCliente().equals(idCliente))
                    .toList();
        }
        if (!txtIdInmueble.getText().trim().isEmpty()) {
            String idInmueble = txtIdInmueble.getText().trim();
            resultado = resultado.stream()
                    .filter(v -> v.getCodigoInmueble().equals(idInmueble))
                    .toList();
        }
        listaObservable.setAll(resultado);
        lblTotal.setText("Total: " + resultado.size() + " visitas");
    }

    @FXML
    private void limpiarFiltros() {
        cmbEstado.setValue(null);
        txtIdCliente.clear();
        txtIdInmueble.clear();
        txtBuscar.clear();
        cargarVisitas();
    }

    @FXML
    private void procesarSiguiente() {
        Visita siguiente = gestor.getServicioVisitas().procesarSiguientePendiente();
        if (siguiente == null) {
            mostrarAlerta("No hay visitas pendientes en la cola.");
            return;
        }
        cargarVisitas();
        mostrarInfo("Visita procesada: " + siguiente.getId() +
                "\nCliente: " + siguiente.getIdCliente() +
                "\nInmueble: " + siguiente.getCodigoInmueble());
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