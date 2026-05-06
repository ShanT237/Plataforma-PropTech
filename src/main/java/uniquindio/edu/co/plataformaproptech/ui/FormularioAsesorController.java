package uniquindio.edu.co.plataformaproptech.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import uniquindio.edu.co.plataformaproptech.GestorProptech;
import uniquindio.edu.co.plataformaproptech.modelos.Asesor;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FormularioAsesorController implements Initializable {

    @FXML private Label lblTitulo;
    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEspecialidad;
    @FXML private TextField txtZona;
    @FXML private TextField txtCierres;

    private final GestorProptech gestor = GestorProptech.getInstancia();
    private Asesor asesorEditar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtCierres.setText("0");
    }

    public void setAsesor(Asesor asesor) {
        this.asesorEditar = asesor;
        if (asesor != null) {
            lblTitulo.setText("Editar Asesor");
            txtId.setText(asesor.getId());
            txtId.setDisable(true);
            txtNombre.setText(asesor.getNombre());
            txtCorreo.setText(asesor.getCorreo());
            txtTelefono.setText(asesor.getTelefono());
            txtEspecialidad.setText(asesor.getEspecialidad());
            txtZona.setText(asesor.getZonaAsignada());
            txtCierres.setText(String.valueOf(asesor.getCierresRealizados()));
        }
    }

    @FXML
    private void guardar() {
        if (!validar()) return;

        Asesor asesor = new Asesor(
                txtId.getText().trim(),
                txtNombre.getText().trim(),
                txtCorreo.getText().trim(),
                txtTelefono.getText().trim(),
                txtEspecialidad.getText().trim(),
                txtZona.getText().trim(),
                new ArrayList<>(),
                new ArrayList<>(),
                Integer.parseInt(txtCierres.getText().trim())
        );

        boolean exito;
        if (asesorEditar == null) {
            exito = gestor.getServicioAsesores().registrar(asesor);
        } else {
            exito = gestor.getServicioAsesores().actualizar(asesor);
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
        if (txtEspecialidad.getText().trim().isEmpty()) {
            mostrarAlerta("La especialidad es obligatoria.");
            return false;
        }
        if (txtZona.getText().trim().isEmpty()) {
            mostrarAlerta("La zona asignada es obligatoria.");
            return false;
        }
        try {
            Integer.parseInt(txtCierres.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta("Los cierres deben ser un número válido.");
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