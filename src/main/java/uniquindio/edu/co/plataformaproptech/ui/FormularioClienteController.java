package uniquindio.edu.co.plataformaproptech.ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import uniquindio.edu.co.plataformaproptech.GestorProptech;
import uniquindio.edu.co.plataformaproptech.modelos.Cliente;
import uniquindio.edu.co.plataformaproptech.modelos.enums.EstadoBusqueda;
import uniquindio.edu.co.plataformaproptech.modelos.enums.TipoCliente;
import uniquindio.edu.co.plataformaproptech.modelos.enums.TipoInmueble;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class FormularioClienteController implements Initializable {

    @FXML private Label lblTitulo;
    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtPresupuesto;
    @FXML private TextField txtHabitaciones;
    @FXML private TextField txtZonas;
    @FXML private ComboBox<TipoCliente> cmbTipoCliente;
    @FXML private ComboBox<EstadoBusqueda> cmbEstadoBusqueda;
    @FXML private ComboBox<TipoInmueble> cmbTipoInmueble;

    private final GestorProptech gestor = GestorProptech.getInstancia();
    private Cliente clienteEditar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbTipoCliente.setItems(FXCollections.observableArrayList(TipoCliente.values()));
        cmbEstadoBusqueda.setItems(FXCollections.observableArrayList(EstadoBusqueda.values()));
        cmbTipoInmueble.setItems(FXCollections.observableArrayList(TipoInmueble.values()));
    }

    public void setCliente(Cliente cliente) {
        this.clienteEditar = cliente;
        if (cliente != null) {
            lblTitulo.setText("Editar Cliente");
            txtId.setText(cliente.getId());
            txtId.setDisable(true);
            txtNombre.setText(cliente.getNombre());
            txtCorreo.setText(cliente.getCorreo());
            txtTelefono.setText(cliente.getTelefono());
            txtPresupuesto.setText(String.valueOf(cliente.getPresupuesto()));
            txtHabitaciones.setText(String.valueOf(cliente.getHabitacionesMinimas()));
            txtZonas.setText(String.join(", ", cliente.getZonaDeInteres()));
            cmbTipoCliente.setValue(cliente.getTipoCliente());
            cmbEstadoBusqueda.setValue(cliente.getEstadoBusqueda());
            cmbTipoInmueble.setValue(cliente.getTipoInmuebleDeseado());
        }
    }

    @FXML
    private void guardar() {
        if (!validar()) return;

        List<String> zonas = Arrays.stream(txtZonas.getText().split(","))
                .map(String::trim)
                .filter(z -> !z.isEmpty())
                .collect(Collectors.toList());

        Cliente cliente = new Cliente(
                txtId.getText().trim(),
                txtNombre.getText().trim(),
                txtTelefono.getText().trim(),
                txtCorreo.getText().trim(),
                cmbTipoCliente.getValue(),
                Double.parseDouble(txtPresupuesto.getText().trim()),
                zonas,
                Integer.parseInt(txtHabitaciones.getText().trim()),
                cmbTipoInmueble.getValue(),
                cmbEstadoBusqueda.getValue()
        );

        boolean exito;
        if (clienteEditar == null) {
            exito = gestor.getServicioClientes().registrar(cliente);
        } else {
            exito = gestor.getServicioClientes().actualizar(cliente);
        }

        if (exito) {
            cerrarVentana();
        } else {
            mostrarAlerta("No se pudo guardar. Verifica que el ID no esté duplicado.");
        }
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private boolean validar() {
        if (txtId.getText().trim().isEmpty()) {
            mostrarAlerta("El ID es obligatorio.");
            return false;
        }
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarAlerta("El nombre es obligatorio.");
            return false;
        }
        if (txtCorreo.getText().trim().isEmpty()) {
            mostrarAlerta("El correo es obligatorio.");
            return false;
        }
        if (txtTelefono.getText().trim().isEmpty()) {
            mostrarAlerta("El teléfono es obligatorio.");
            return false;
        }
        if (cmbTipoCliente.getValue() == null) {
            mostrarAlerta("Selecciona el tipo de cliente.");
            return false;
        }
        if (cmbEstadoBusqueda.getValue() == null) {
            mostrarAlerta("Selecciona el estado de búsqueda.");
            return false;
        }
        if (cmbTipoInmueble.getValue() == null) {
            mostrarAlerta("Selecciona el tipo de inmueble deseado.");
            return false;
        }
        try {
            Double.parseDouble(txtPresupuesto.getText().trim());
            Integer.parseInt(txtHabitaciones.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta("Presupuesto y habitaciones deben ser números válidos.");
            return false;
        }
        return true;
    }

    private void cerrarVentana() {
        ((Stage) txtId.getScene().getWindow()).close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}