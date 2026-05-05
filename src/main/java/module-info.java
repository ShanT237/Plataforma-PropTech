module uniquindio.edu.co.plataformaproptech {
    requires javafx.controls;
    requires javafx.fxml;


    opens uniquindio.edu.co.plataformaproptech to javafx.fxml;
    exports uniquindio.edu.co.plataformaproptech;
    exports uniquindio.edu.co.plataformaproptech.repositorios;
    opens uniquindio.edu.co.plataformaproptech.repositorios to javafx.fxml;
    exports uniquindio.edu.co.plataformaproptech.servicios;
    opens uniquindio.edu.co.plataformaproptech.servicios to javafx.fxml;
}