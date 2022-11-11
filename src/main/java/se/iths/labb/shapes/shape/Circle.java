package se.iths.labb.shapes.shape;

import javafx.scene.canvas.GraphicsContext;
import se.iths.labb.shapes.ShapeParameter;
import se.iths.labb.shapes.ShapeType;

import java.util.Locale;

import static se.iths.labb.shapes.ShapeType.*;

public final class Circle extends Shape {
    double radius = getSize()/2;
    double radiusSq = radius * radius;
    String colorAsString = getColor().toString().substring(2, 10);
    public Circle(ShapeParameter parameter) {
        super(parameter);
    }

    @Override
    public Shape getShapeDuplicate() {
        return new Circle(getDuplicate());
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setFill(getColor());
        context.fillOval(getX() - radius, getY() - radius, getSize(), getSize());
    }

    @Override
    public Boolean isInside(double posX, double posY) {
        double distX = posX - getX();
        double distY = posY - getY();

        double distToCenter = (distX * distX + distY * distY);

        return distToCenter <= radiusSq;
    }
    @Override
    public ShapeType getType() {
        return CIRCLE;
    }

    @Override
    public String toString() {
        return "<circle cx=\"" + String.format(Locale.UK,"%.2f",getX()) + "\" " +
                "cy=\"" + String.format(Locale.UK,"%.2f",getY()) + "\" " +
                "r=\"" + radius + "\" " +
                "fill=\"#" + colorAsString + "\" />";
    }
}