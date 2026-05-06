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
import uniquindio.edu.co.plataformaproptech.modelos.Asesor;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PantallaAsesoresController implements Initializable {

    @FXML private TableView<Asesor> tablaAsesores;
    @FXML private TableColumn<Asesor, String> colId;
    @FXML private TableColumn<Asesor, String> colNombre;
    @FXML private TableColumn<Asesor, String> colCorreo;
    @FXML private TableColumn<Asesor, String> colTelefono;
    @FXML private TableColumn<Asesor, String> colEspecialidad;
    @FXML private TableColumn<Asesor, String> colZona;
    @FXML private TableColumn<Asesor, Integer> colCierres;
    @FXML private TextField txtBuscar;
    @FXML private TextField txtZona;
    @FXML private TextField txtEspecialidad;
    @FXML private Label lblTotal;

    private final GestorProptech gestor = GestorProptech.getInstancia();
    private ObservableList<Asesor> listaObservable = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarColumnas();
        cargarAsesores();
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        colZona.setCellValueFactory(new PropertyValueFactory<>("zonaAsignada"));
        colCierres.setCellValueFactory(new PropertyValueFactory<>("cierresRealizados"));
        tablaAsesores.setItems(listaObservable);
    }

    private void cargarAsesores() {
        List<Asesor> todos = gestor.getServicioAsesores().obtenerTodos();
        listaObservable.setAll(todos);
        lblTotal.setText("Total: " + todos.size() + " asesores");
    }

    @FXML
    private void abrirFormularioNuevo() {
        abrirFormulario(null);
    }

    @FXML
    private void editarAsesor() {
        Asesor seleccionado = tablaAsesores.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selecciona un asesor para editar.");
            return;
        }
        abrirFormulario(seleccionado);
    }

    @FXML
    private void eliminarAsesor() {
        Asesor seleccionado = tablaAsesores.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selecciona un asesor para eliminar.");
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Eliminar al asesor " + seleccionado.getNombre() + "?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");
        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                gestor.getServicioAsesores().eliminar(seleccionado.getId());
                cargarAsesores();
            }
        });
    }

    @FXML
    private void buscarAsesor() {
        String id = txtBuscar.getText().trim();
        if (id.isEmpty()) {
            cargarAsesores();
            return;
        }
        Asesor encontrado = gestor.getServicioAsesores().buscarPorId(id);
        listaObservable.clear();
        if (encontrado != null) {
            listaObservable.add(encontrado);
            lblTotal.setText("Total: 1 asesor encontrado");
        } else {
            mostrarAlerta("No se encontró ningún asesor con el ID: " + id);
        }
    }

    @FXML
    private void aplicarFiltros() {
        List<Asesor> resultado = gestor.getServicioAsesores().obtenerTodos();
        if (!txtZona.getText().trim().isEmpty()) {
            resultado = resultado.stream()
                    .filter(a -> a.getZonaAsignada().equalsIgnoreCase(txtZona.getText().trim()))
                    .toList();
        }
        if (!txtEspecialidad.getText().trim().isEmpty()) {
            resultado = resultado.stream()
                    .filter(a -> a.getEspecialidad().equalsIgnoreCase(txtEspecialidad.getText().trim()))
                    .toList();
        }
        listaObservable.setAll(resultado);
        lblTotal.setText("Total: " + resultado.size() + " asesores");
    }

    @FXML
    private void limpiarFiltros() {
        txtZona.clear();
        txtEspecialidad.clear();
        txtBuscar.clear();
        cargarAsesores();
    }

    @FXML
    private void verRanking() {
        List<Integer> ranking = gestor.getServicioAsesores().rankingPorCierres();
        StringBuilder sb = new StringBuilder("Ranking por cierres:\n\n");
        for (int i = 0; i < ranking.size(); i++) {
            sb.append(i + 1).append(". ").append(ranking.get(i)).append(" cierres\n");
        }
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Ranking de Asesores");
        info.setHeaderText("Asesores ordenados por cierres realizados");
        info.setContentText(sb.toString());
        info.showAndWait();
    }

    @FXML
    private void volverAlInicio() {
        Navegador.navegarAInicio();
    }

    private void abrirFormulario(Asesor asesor) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/FormularioAsesor.fxml")
            );
            Parent root = loader.load();
            FormularioAsesorController controller = loader.getController();
            controller.setAsesor(asesor);
            Stage stage = new Stage();
            stage.setTitle(asesor == null ? "Nuevo Asesor" : "Editar Asesor");
            stage.setScene(new Scene(root));
            stage.setMinWidth(660);
            stage.setMinHeight(600);
            stage.setWidth(700);
            stage.setHeight(650);
            stage.initOwner(Navegador.getStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.centerOnScreen();
            stage.setOnHidden(e -> cargarAsesores());
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