package se.iths.labb.shapes;

import javafx.scene.paint.Color;

import java.util.regex.Pattern;

import static se.iths.labb.shapes.ShapeType.*;


public class ShapeFactory {
    public Shape getShape(ShapeType shapeType, ShapeParameter parameter) {
        return switch (shapeType) {
            case CIRCLE -> new Circle(parameter);
            case SQUARE -> new Square(parameter);
        };
    }
}