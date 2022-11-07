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
                return getShape(CIRCLE,
                        new ShapeParameter(
                        getX(parameterArray),
                        getY(parameterArray),
                        getSize(parameterArray) * 2,
                        getColor(parameterArray, 4)));
            } else if (line.contains("rect")) {
                return getShape(SQUARE,
                        new ShapeParameter(
                        getX(parameterArray),
                        getY(parameterArray),
                        getSize(parameterArray),
                        getColor(parameterArray, 5)));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException();
        }
        return null;
    }
    private static Color getColor(String[] parameterArray, int x) {
        return Color.valueOf(parameterArray[x].substring(1, 10));
    }
    private static double getSize(String[] parameterArray) {
        return Double.parseDouble(parameterArray[3].substring(1, 5));
    }
    private static double getY(String[] parameterArray) {
        return Double.parseDouble(parameterArray[2].substring(1, 5));
    }
    private static double getX(String[] parameterArray) {
        return Double.parseDouble(parameterArray[1].substring(1, 5));
    }
}