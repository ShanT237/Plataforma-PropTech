package uniquindio.edu.co.plataformaproptech;

import javafx.application.Application;
import javafx.stage.Stage;
import uniquindio.edu.co.plataformaproptech.ui.Navegador;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setMaximized(true);
        Navegador.setStage(stage);
        Navegador.navegarAInicio();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}