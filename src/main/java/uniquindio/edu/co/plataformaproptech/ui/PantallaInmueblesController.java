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
import javafx.stage.Stage;
import uniquindio.edu.co.plataformaproptech.GestorProptech;
import uniquindio.edu.co.plataformaproptech.modelos.Inmueble;
import uniquindio.edu.co.plataformaproptech.modelos.enums.EstadoInmueble;
import uniquindio.edu.co.plataformaproptech.modelos.enums.FinalidadInmueble;
import uniquindio.edu.co.plataformaproptech.modelos.enums.TipoInmueble;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PantallaInmueblesController implements Initializable {

    @FXML private TableView<Inmueble> tablaInmuebles;
    @FXML private TableColumn<Inmueble, String> colCodigo;
    @FXML private TableColumn<Inmueble, String> colTipo;
    @FXML private TableColumn<Inmueble, String> colDireccion;
    @FXML private TableColumn<Inmueble, String> colCiudad;
    @FXML private TableColumn<Inmueble, String> colFinalidad;
    @FXML private TableColumn<Inmueble, Double> colPrecio;
    @FXML private TableColumn<Inmueble, Integer> colHabitaciones;
    @FXML private TableColumn<Inmueble, String> colEstado;
    @FXML private ComboBox<TipoInmueble> cmbTipo;
    @FXML private ComboBox<FinalidadInmueble> cmbFinalidad;
    @FXML private ComboBox<EstadoInmueble> cmbEstado;
    @FXML private TextField txtBuscar;
    @FXML private TextField txtPrecioMin;
    @FXML private TextField txtPrecioMax;
    @FXML private Label lblTotal;

    private final GestorProptech gestor = GestorProptech.getInstancia();
    private ObservableList<Inmueble> listaObservable = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarColumnas();
        configurarFiltros();
        cargarInmuebles();
    }

    private void configurarColumnas() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        colFinalidad.setCellValueFactory(new PropertyValueFactory<>("finalidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colHabitaciones.setCellValueFactory(new PropertyValueFactory<>("habitaciones"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tablaInmuebles.setItems(listaObservable);
    }

    private void configurarFiltros() {
        cmbTipo.setItems(FXCollections.observableArrayList(TipoInmueble.values()));
        cmbFinalidad.setItems(FXCollections.observableArrayList(FinalidadInmueble.values()));
        cmbEstado.setItems(FXCollections.observableArrayList(EstadoInmueble.values()));
    }

    private void cargarInmuebles() {
        List<Inmueble> todos = gestor.getServicioInmuebles().obtenerTodos();
        listaObservable.setAll(todos);
        lblTotal.setText("Total: " + todos.size() + " inmuebles");
    }

    @FXML
    private void abrirFormularioNuevo() {
        abrirFormulario(null);
    }

    @FXML
    private void editarInmueble() {
        Inmueble seleccionado = tablaInmuebles.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selecciona un inmueble para editar.");
            return;
        }
        abrirFormulario(seleccionado);
    }

    @FXML
    private void eliminarInmueble() {
        Inmueble seleccionado = tablaInmuebles.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selecciona un inmueble para eliminar.");
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Eliminar el inmueble " + seleccionado.getCodigo() + "?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");
        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                gestor.getServicioInmuebles().eliminar(seleccionado.getCodigo());
                cargarInmuebles();
            }
        });
    }

    @FXML
    private void deshacerCambio() {
        Inmueble anterior = gestor.getServicioInmuebles().deshacerUltimoCambio();
        if (anterior == null) {
            mostrarAlerta("No hay cambios para deshacer.");
            return;
        }
        cargarInmuebles();
        mostrarInfo("Cambio deshecho. Se restauró el inmueble: " + anterior.getCodigo());
    }

    @FXML
    private void buscarInmueble() {
        String codigo = txtBuscar.getText().trim();
        if (codigo.isEmpty()) {
            cargarInmuebles();
            return;
        }
        Inmueble encontrado = gestor.getServicioInmuebles().buscarPorCodigo(codigo);
        listaObservable.clear();
        if (encontrado != null) {
            listaObservable.add(encontrado);
            lblTotal.setText("Total: 1 inmueble encontrado");
        } else {
            mostrarAlerta("No se encontró ningún inmueble con el código: " + codigo);
        }
    }

    @FXML
    private void aplicarFiltros() {
        TipoInmueble tipo = cmbTipo.getValue();
        FinalidadInmueble finalidad = cmbFinalidad.getValue();
        EstadoInmueble estado = cmbEstado.getValue();
        double precioMin = parsearDouble(txtPrecioMin.getText(), 0);
        double precioMax = parsearDouble(txtPrecioMax.getText(), Double.MAX_VALUE);

        List<Inmueble> resultado = gestor.getServicioInmuebles()
                .filtrarCombinado(tipo, finalidad, precioMin, precioMax, 0, null);

        if (estado != null) {
            resultado = resultado.stream()
                    .filter(i -> i.getEstado() == estado)
                    .toList();
        }

        listaObservable.setAll(resultado);
        lblTotal.setText("Total: " + resultado.size() + " inmuebles");
    }

    @FXML
    private void limpiarFiltros() {
        cmbTipo.setValue(null);
        cmbFinalidad.setValue(null);
        cmbEstado.setValue(null);
        txtPrecioMin.clear();
        txtPrecioMax.clear();
        txtBuscar.clear();
        cargarInmuebles();
    }
    @FXML
    private void volverAlInicio() {
        Navegador.navegarAInicio();
    }

    private void abrirFormulario(Inmueble inmueble) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/FormularioInmueble.fxml")
            );
            Parent root = loader.load();
            FormularioInmuebleController controller = loader.getController();
            controller.setInmueble(inmueble);
            Stage stage = new Stage();
            stage.setTitle(inmueble == null ? "Nuevo Inmueble" : "Editar Inmueble");
            stage.setScene(new Scene(root));
            stage.setMinWidth(1024);
            stage.setMinHeight(800);
            stage.setWidth(1024);
            stage.setHeight(800);
            stage.initOwner(Navegador.getStage());
            stage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            stage.centerOnScreen();
            stage.setOnHidden(e -> cargarInmuebles());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double parsearDouble(String texto, double valorDefecto) {
        try {
            return texto.isEmpty() ? valorDefecto : Double.parseDouble(texto);
        } catch (NumberFormatException e) {
            return valorDefecto;
        }
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