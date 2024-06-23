module example.adivina_el_codigo {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;

    opens example.adivina_el_codigo to javafx.fxml;
    exports example.adivina_el_codigo;
}
