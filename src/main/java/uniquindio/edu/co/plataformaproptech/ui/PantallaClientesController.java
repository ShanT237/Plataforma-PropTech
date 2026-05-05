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
import uniquindio.edu.co.plataformaproptech.modelos.Cliente;
import uniquindio.edu.co.plataformaproptech.modelos.enums.EstadoBusqueda;
import uniquindio.edu.co.plataformaproptech.modelos.enums.TipoCliente;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PantallaClientesController implements Initializable {

    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, String> colId;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colCorreo;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colTipo;
    @FXML private TableColumn<Cliente, Double> colPresupuesto;
    @FXML private TableColumn<Cliente, String> colEstado;
    @FXML private ComboBox<TipoCliente> cmbTipo;
    @FXML private ComboBox<EstadoBusqueda> cmbEstado;
    @FXML private TextField txtBuscar;
    @FXML private TextField txtZona;
    @FXML private Label lblTotal;

    private final GestorProptech gestor = GestorProptech.getInstancia();
    private ObservableList<Cliente> listaObservable = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarColumnas();
        configurarFiltros();
        cargarClientes();
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoCliente"));
        colPresupuesto.setCellValueFactory(new PropertyValueFactory<>("presupuesto"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoBusqueda"));
        tablaClientes.setItems(listaObservable);
    }

    private void configurarFiltros() {
        cmbTipo.setItems(FXCollections.observableArrayList(TipoCliente.values()));
        cmbEstado.setItems(FXCollections.observableArrayList(EstadoBusqueda.values()));
    }

    private void cargarClientes() {
        List<Cliente> todos = gestor.getServicioClientes().obtenerTodos();
        listaObservable.setAll(todos);
        lblTotal.setText("Total: " + todos.size() + " clientes");
    }

    @FXML
    private void abrirFormularioNuevo() {
        abrirFormulario(null);
    }

    @FXML
    private void editarCliente() {
        Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selecciona un cliente para editar.");
            return;
        }
        abrirFormulario(seleccionado);
    }

    @FXML
    private void eliminarCliente() {
        Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selecciona un cliente para eliminar.");
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Eliminar al cliente " + seleccionado.getNombre() + "?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");
        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                gestor.getServicioClientes().eliminar(seleccionado.getId());
                cargarClientes();
            }
        });
    }

    @FXML
    private void buscarCliente() {
        String id = txtBuscar.getText().trim();
        if (id.isEmpty()) {
            cargarClientes();
            return;
        }
        Cliente encontrado = gestor.getServicioClientes().buscarPorId(id);
        listaObservable.clear();
        if (encontrado != null) {
            listaObservable.add(encontrado);
            lblTotal.setText("Total: 1 cliente encontrado");
        } else {
            mostrarAlerta("No se encontró ningún cliente con el ID: " + id);
        }
    }

    @FXML
    private void aplicarFiltros() {
        List<Cliente> resultado = gestor.getServicioClientes().obtenerTodos();
        if (cmbTipo.getValue() != null) {
            resultado = resultado.stream()
                    .filter(c -> c.getTipoCliente() == cmbTipo.getValue())
                    .toList();
        }
        if (cmbEstado.getValue() != null) {
            resultado = resultado.stream()
                    .filter(c -> c.getEstadoBusqueda() == cmbEstado.getValue())
                    .toList();
        }
        if (!txtZona.getText().trim().isEmpty()) {
            String zona = txtZona.getText().trim();
            resultado = resultado.stream()
                    .filter(c -> c.getZonaDeInteres().contains(zona))
                    .toList();
        }
        listaObservable.setAll(resultado);
        lblTotal.setText("Total: " + resultado.size() + " clientes");
    }

    @FXML
    private void limpiarFiltros() {
        cmbTipo.setValue(null);
        cmbEstado.setValue(null);
        txtZona.clear();
        txtBuscar.clear();
        cargarClientes();
    }

    @FXML
    private void volverAlInicio() {
        Navegador.navegarAInicio();
    }

    private void abrirFormulario(Cliente cliente) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/FormularioCliente.fxml")
            );
            Parent root = loader.load();
            FormularioClienteController controller = loader.getController();
            controller.setCliente(cliente);
            Stage stage = new Stage();
            stage.setTitle(cliente == null ? "Nuevo Cliente" : "Editar Cliente");
            stage.setScene(new Scene(root));
            stage.setMinWidth(660);
            stage.setMinHeight(700);
            stage.setWidth(700);
            stage.setHeight(750);
            stage.initOwner(Navegador.getStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.centerOnScreen();
            stage.setOnHidden(e -> cargarClientes());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}