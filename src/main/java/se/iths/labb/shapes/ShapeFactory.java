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

    public Shape convertStringToShape(String line) {
        try {
            Pattern pattern = Pattern.compile("=");

            String[] parameterArray = pattern.split(line);
            if (line.contains("circle")) {
                return getShape(CIRCLE, new ShapeParameter(
                        Double.parseDouble(parameterArray[1].substring(1, 5)),
                        Double.parseDouble(parameterArray[2].substring(1, 5)),
                        (Double.parseDouble(parameterArray[3].substring(1, 5)) * 2),
                        Color.valueOf(parameterArray[4].substring(1, 10))));
            } else if (line.contains("rect")) {
                return getShape(SQUARE, new ShapeParameter(
                        Double.parseDouble(parameterArray[1].substring(1, 5)),
                        Double.parseDouble(parameterArray[2].substring(1, 5)),
                        Double.parseDouble(parameterArray[3].substring(1, 5)),
                        Color.valueOf(parameterArray[4].substring(1, 10))));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException();
        }
        return null;
    }
}