package uniquindio.edu.co.plataformaproptech.ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import uniquindio.edu.co.plataformaproptech.GestorProptech;
import uniquindio.edu.co.plataformaproptech.modelos.Inmueble;
import uniquindio.edu.co.plataformaproptech.modelos.enums.EstadoInmueble;
import uniquindio.edu.co.plataformaproptech.modelos.enums.FinalidadInmueble;
import uniquindio.edu.co.plataformaproptech.modelos.enums.TipoInmueble;

import java.net.URL;
import java.util.ResourceBundle;

public class FormularioInmuebleController implements Initializable {

    @FXML private Label lblTitulo;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtCiudad;
    @FXML private TextField txtBarrio;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtArea;
    @FXML private TextField txtHabitaciones;
    @FXML private TextField txtBanos;
    @FXML private TextField txtCodigoAsesor;
    @FXML private ComboBox<TipoInmueble> cmbTipo;
    @FXML private ComboBox<FinalidadInmueble> cmbFinalidad;
    @FXML private ComboBox<EstadoInmueble> cmbEstado;
    @FXML private CheckBox chkDisponible;

    private final GestorProptech gestor = GestorProptech.getInstancia();
    private Inmueble inmuebleEditar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbTipo.setItems(FXCollections.observableArrayList(TipoInmueble.values()));
        cmbFinalidad.setItems(FXCollections.observableArrayList(FinalidadInmueble.values()));
        cmbEstado.setItems(FXCollections.observableArrayList(EstadoInmueble.values()));
    }

    public void setInmueble(Inmueble inmueble) {
        this.inmuebleEditar = inmueble;
        if (inmueble != null) {
            lblTitulo.setText("Editar Inmueble");
            txtCodigo.setText(inmueble.getCodigo());
            txtCodigo.setDisable(true);
            txtDireccion.setText(inmueble.getDireccion());
            txtCiudad.setText(inmueble.getCiudad());
            txtBarrio.setText(inmueble.getBarrio());
            txtPrecio.setText(String.valueOf(inmueble.getPrecio()));
            txtArea.setText(String.valueOf(inmueble.getArea()));
            txtHabitaciones.setText(String.valueOf(inmueble.getHabitaciones()));
            txtBanos.setText(String.valueOf(inmueble.getBanos()));
            txtCodigoAsesor.setText(inmueble.getCodigoAsesor());
            cmbTipo.setValue(inmueble.getTipo());
            cmbFinalidad.setValue(inmueble.getFinalidad());
            cmbEstado.setValue(inmueble.getEstado());
            chkDisponible.setSelected(inmueble.isDisponible());
        }
    }

    @FXML
    private void guardar() {
        if (!validar()) return;
        Inmueble inmueble = new Inmueble(
                txtCodigo.getText().trim(),
                txtDireccion.getText().trim(),
                txtCiudad.getText().trim(),
                txtBarrio.getText().trim(),
                cmbTipo.getValue(),
                cmbFinalidad.getValue(),
                Double.parseDouble(txtPrecio.getText().trim()),
                Double.parseDouble(txtArea.getText().trim()),
                Integer.parseInt(txtHabitaciones.getText().trim()),
                Integer.parseInt(txtBanos.getText().trim()),
                cmbEstado.getValue(),
                chkDisponible.isSelected(),
                txtCodigoAsesor.getText().trim()
        );
        boolean exito;
        if (inmuebleEditar == null) {
            exito = gestor.getServicioInmuebles().registrar(inmueble);
        } else {
            exito = gestor.getServicioInmuebles().actualizar(inmueble);
        }
        if (exito) {
            cerrarVentana();
        } else {
            mostrarAlerta("No se pudo guardar. Verifica que el código no esté duplicado.");
        }
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private boolean validar() {
        if (txtCodigo.getText().trim().isEmpty()) {
            mostrarAlerta("El código es obligatorio.");
            return false;
        }
        if (txtDireccion.getText().trim().isEmpty()) {
            mostrarAlerta("La dirección es obligatoria.");
            return false;
        }
        if (txtCiudad.getText().trim().isEmpty()) {
            mostrarAlerta("La ciudad es obligatoria.");
            return false;
        }
        if (cmbTipo.getValue() == null) {
            mostrarAlerta("Selecciona el tipo de inmueble.");
            return false;
        }
        if (cmbFinalidad.getValue() == null) {
            mostrarAlerta("Selecciona la finalidad.");
            return false;
        }
        if (cmbEstado.getValue() == null) {
            mostrarAlerta("Selecciona el estado.");
            return false;
        }
        try {
            Double.parseDouble(txtPrecio.getText().trim());
            Double.parseDouble(txtArea.getText().trim());
            Integer.parseInt(txtHabitaciones.getText().trim());
            Integer.parseInt(txtBanos.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta("Precio, área, habitaciones y baños deben ser números válidos.");
            return false;
        }
        return true;
    }

    private void cerrarVentana() {
        ((Stage) txtCodigo.getScene().getWindow()).close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}