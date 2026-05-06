package uniquindio.edu.co.plataformaproptech.ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import uniquindio.edu.co.plataformaproptech.GestorProptech;
import uniquindio.edu.co.plataformaproptech.modelos.Visita;
import uniquindio.edu.co.plataformaproptech.modelos.enums.EstadoVisita;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class FormularioVisitaController implements Initializable {

    @FXML private TextField txtId;
    @FXML private TextField txtIdCliente;
    @FXML private TextField txtCodigoInmueble;
    @FXML private TextField txtIdAsesor;
    @FXML private TextField txtHora;
    @FXML private TextArea txtObservaciones;
    @FXML private ComboBox<EstadoVisita> cmbEstado;
    @FXML private DatePicker dateFecha;
    @FXML private CheckBox chkUrgente;

    private final GestorProptech gestor = GestorProptech.getInstancia();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbEstado.setItems(FXCollections.observableArrayList(EstadoVisita.values()));
        cmbEstado.setValue(EstadoVisita.PENDIENTE);
        dateFecha.setValue(LocalDate.now());
    }

    @FXML
    private void guardar() {
        if (!validar()) return;

        LocalTime hora;
        try {
            hora = LocalTime.parse(txtHora.getText().trim());
        } catch (DateTimeParseException e) {
            mostrarAlerta("El formato de hora es inválido. Usa HH:MM. Ej: 10:30");
            return;
        }

        Visita visita = new Visita(
                txtId.getText().trim(),
                txtIdCliente.getText().trim(),
                txtCodigoInmueble.getText().trim(),
                dateFecha.getValue(),
                hora,
                txtIdAsesor.getText().trim(),
                cmbEstado.getValue(),
                txtObservaciones.getText().trim()
        );

        boolean exito;
        if (chkUrgente.isSelected()) {
            exito = gestor.getServicioVisitas().programarUrgente(visita, 1);
        } else {
            exito = gestor.getServicioVisitas().programar(visita);
        }

        if (exito) {
            cerrarVentana();
        } else {
            mostrarAlerta("No se pudo programar la visita. Verifica que el cliente, inmueble y asesor existan.");
        }
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private boolean validar() {
        if (txtId.getText().trim().isEmpty()) {
            mostrarAlerta("El ID de la visita es obligatorio.");
            return false;
        }
        if (txtIdCliente.getText().trim().isEmpty()) {
            mostrarAlerta("El ID del cliente es obligatorio.");
            return false;
        }
        if (txtCodigoInmueble.getText().trim().isEmpty()) {
            mostrarAlerta("El código del inmueble es obligatorio.");
            return false;
        }
        if (txtIdAsesor.getText().trim().isEmpty()) {
            mostrarAlerta("El ID del asesor es obligatorio.");
            return false;
        }
        if (dateFecha.getValue() == null) {
            mostrarAlerta("La fecha es obligatoria.");
            return false;
        }
        if (txtHora.getText().trim().isEmpty()) {
            mostrarAlerta("La hora es obligatoria.");
            return false;
        }
        if (cmbEstado.getValue() == null) {
            mostrarAlerta("Selecciona el estado de la visita.");
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