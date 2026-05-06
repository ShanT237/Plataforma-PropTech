package uniquindio.edu.co.plataformaproptech.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import uniquindio.edu.co.plataformaproptech.GestorProptech;
import uniquindio.edu.co.plataformaproptech.modelos.Inmueble;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PantallaRecomendacionesController implements Initializable {

    @FXML private TableView<Inmueble> tablaResultados;
    @FXML private TableColumn<Inmueble, String> colCodigo;
    @FXML private TableColumn<Inmueble, String> colTipo;
    @FXML private TableColumn<Inmueble, String> colDireccion;
    @FXML private TableColumn<Inmueble, String> colCiudad;
    @FXML private TableColumn<Inmueble, String> colFinalidad;
    @FXML private TableColumn<Inmueble, Double> colPrecio;
    @FXML private TableColumn<Inmueble, Integer> colHabitaciones;
    @FXML private TableColumn<Inmueble, String> colEstado;
    @FXML private TextField txtIdCliente;
    @FXML private TextField txtCodigoInmueble;
    @FXML private Label lblTotal;
    @FXML private Label lblTipoRecomendacion;

    private final GestorProptech gestor = GestorProptech.getInstancia();
    private ObservableList<Inmueble> listaObservable = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarColumnas();
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
        tablaResultados.setItems(listaObservable);
    }

    @FXML
    private void recomendarPorPerfil() {
        String idCliente = txtIdCliente.getText().trim();
        if (idCliente.isEmpty()) {
            mostrarAlerta("Ingresa el ID del cliente.");
            return;
        }
        List<Inmueble> resultado = gestor.getServicioRecomendacion().recomendar(idCliente);
        mostrarResultados(resultado, "Recomendaciones por perfil del cliente");
    }

    @FXML
    private void recomendarPorHistorial() {
        String idCliente = txtIdCliente.getText().trim();
        if (idCliente.isEmpty()) {
            mostrarAlerta("Ingresa el ID del cliente.");
            return;
        }
        List<Inmueble> resultado = gestor.getServicioRecomendacion().recomendarPorHistorial(idCliente);
        mostrarResultados(resultado, "Recomendaciones por historial de visitas");
    }

    @FXML
    private void recomendarSimilares() {
        String codigo = txtCodigoInmueble.getText().trim();
        if (codigo.isEmpty()) {
            mostrarAlerta("Ingresa el código del inmueble.");
            return;
        }
        List<Inmueble> resultado = gestor.getServicioRecomendacion().recomendarSimilares(codigo);
        mostrarResultados(resultado, "Inmuebles similares a " + codigo);
    }

    @FXML
    private void limpiar() {
        txtIdCliente.clear();
        txtCodigoInmueble.clear();
        listaObservable.clear();
        lblTotal.setText("0 inmuebles encontrados");
        lblTipoRecomendacion.setText("Resultados");
    }

    private void mostrarResultados(List<Inmueble> resultado, String titulo) {
        if (resultado.isEmpty()) {
            mostrarAlerta("No se encontraron recomendaciones.");
            listaObservable.clear();
        } else {
            listaObservable.setAll(resultado);
        }
        lblTipoRecomendacion.setText(titulo);
        lblTotal.setText(resultado.size() + " inmuebles encontrados");
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
}