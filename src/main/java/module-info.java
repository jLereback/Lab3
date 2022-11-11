module se.iths.labb {
    requires javafx.controls;
    requires javafx.fxml;


    opens se.iths.labb to javafx.fxml;
    exports se.iths.labb;

    opens se.iths.labb.shapes to javafx.fxml;
    exports se.iths.labb.shapes;

    opens se.iths.labb.tools;
    exports se.iths.labb.tools;
    exports se.iths.labb.shapes.shape;
    opens se.iths.labb.shapes.shape to javafx.fxml;
}