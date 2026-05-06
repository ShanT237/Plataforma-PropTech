module uniquindio.edu.co.plataformaproptech {
    requires javafx.controls;
    requires javafx.fxml;

    opens uniquindio.edu.co.plataformaproptech to javafx.fxml;
    opens uniquindio.edu.co.plataformaproptech.ui to javafx.fxml;
    opens uniquindio.edu.co.plataformaproptech.modelos to javafx.base, javafx.fxml;
    opens uniquindio.edu.co.plataformaproptech.modelos.enums to javafx.base, javafx.fxml;
    opens uniquindio.edu.co.plataformaproptech.estructuras to javafx.base;
    opens uniquindio.edu.co.plataformaproptech.repositorios to javafx.base;
    opens uniquindio.edu.co.plataformaproptech.servicios to javafx.base;

    exports uniquindio.edu.co.plataformaproptech;
}