package uniquindio.edu.co.plataformaproptech.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import uniquindio.edu.co.plataformaproptech.GestorProptech;

import java.net.URL;
import java.util.ResourceBundle;

public class PantallaInicioController implements Initializable {

    @FXML private Label lblTotalInmuebles;
    @FXML private Label lblTotalClientes;
    @FXML private Label lblTotalVisitas;
    @FXML private Label lblTotalAlertas;

    private final GestorProptech gestor = GestorProptech.getInstancia();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        actualizarContadores();
    }

    private void actualizarContadores() {
        lblTotalInmuebles.setText(String.valueOf(gestor.getServicioInmuebles().obtenerTodos().size()));
        lblTotalClientes.setText(String.valueOf(gestor.getServicioClientes().obtenerTodos().size()));
        lblTotalVisitas.setText(String.valueOf(gestor.getServicioVisitas().totalPendientes()));
        lblTotalAlertas.setText(String.valueOf(gestor.getServicioAlertas().totalPendientes()));
    }

    @FXML private void abrirInmuebles() { Navegador.navegarA("PantallaInmuebles.fxml", "Inmuebles"); }
    @FXML private void abrirClientes() { Navegador.navegarA("PantallaClientes.fxml", "Clientes"); }
    @FXML private void abrirAsesores() { Navegador.navegarA("PantallaAsesores.fxml", "Asesores"); }
    @FXML private void abrirVisitas() { Navegador.navegarA("PantallaVisitas.fxml", "Visitas"); }
    @FXML private void abrirAlertas() { Navegador.navegarA("PantallaAlertas.fxml", "Alertas"); }
    @FXML private void abrirRecomendaciones() { Navegador.navegarA("PantallaRecomendaciones.fxml", "Recomendaciones"); }
    @FXML private void abrirReportes() { Navegador.navegarA("PantallaReportes.fxml", "Reportes"); }
    @FXML private void abrirComportamiento() { Navegador.navegarA("PantallaComportamiento.fxml", "Comportamiento"); }
}