package uniquindio.edu.co.plataformaproptech.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Navegador {

    private static Stage stagePrincipal;

    public static void setStage(Stage stage) {
        stagePrincipal = stage;
    }

    public static void navegarA(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Navegador.class.getResource("/fxml/" + fxml)
            );
            Parent root = loader.load();

            Scene escenaActual = stagePrincipal.getScene();

            if (escenaActual == null) {
                stagePrincipal.setScene(new Scene(root));
            } else {
                escenaActual.setRoot(root);
            }

            stagePrincipal.setTitle("PropTech - " + titulo);

            if (!stagePrincipal.isMaximized()) {
                stagePrincipal.setMaximized(true);
            }

        } catch (Exception e) {
            System.err.println("Error al navegar a: " + fxml);
            e.printStackTrace();
        }
    }

    public static void navegarAInicio() {
        navegarA("PantallaInicio.fxml", "Panel de Control");
    }

    public static Stage getStage() {
        return stagePrincipal;
    }
}