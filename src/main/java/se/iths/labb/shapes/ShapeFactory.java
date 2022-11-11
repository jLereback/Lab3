package se.iths.labb.shapes;

import javafx.scene.paint.Color;
import se.iths.labb.shapes.shape.Circle;
import se.iths.labb.shapes.shape.Shape;
import se.iths.labb.shapes.shape.Square;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static se.iths.labb.shapes.ShapeType.*;


public class ShapeFactory {

    static Pattern color = Pattern.compile("fill=.{10}");
    static Pattern X = Pattern.compile("x=.\\d+");
    static Pattern Y = Pattern.compile("y=.\\d+");
    static Pattern radius = Pattern.compile("r=.\\d+");
    static Pattern size = Pattern.compile("width=.\\d+");


    public Shape getShape(ShapeType shapeType, ShapeParameter parameter) {
        return switch (shapeType) {
            case CIRCLE -> new Circle(parameter);
            case SQUARE -> new Square(parameter);
        };
    }

    public static String findSVGValue(String line, Pattern pattern) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            String found = matcher.group(0);
            List<String> foundStrings = Arrays.stream(found.split("=\"")).toList();
            return foundStrings.get(1);
        }
        return null;
    }

    public Shape convertStringToShape(String line) {
        try {
            if (line.contains("circle"))
                return getShape(CIRCLE,
                        new ShapeParameter(
                                getX(line),
                                getY(line),
                                getRadius(line) * 2,
                                getColor(line)));
            else if (line.contains("rect"))
                return getShape(SQUARE,
                        new ShapeParameter(
                                getX(line),
                                getY(line),
                                getSize(line),
                                getColor(line)));
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException();
        }
        return null;
    }

    private static Color getColor(String line) {
        return Color.valueOf(Objects.requireNonNull(findSVGValue(line, color)));
    }

    private static double getSize(String line) {
        return Double.parseDouble(Objects.requireNonNull(findSVGValue(line, size)));
    }

    private static double getY(String line) {
        return Double.parseDouble(Objects.requireNonNull(findSVGValue(line, Y)));
    }

    private static double getX(String line) {
        return Double.parseDouble(Objects.requireNonNull(findSVGValue(line, X)));
    }
    private static double getRadius(String line) {
        return Double.parseDouble(Objects.requireNonNull(findSVGValue(line, radius)));
    }

}