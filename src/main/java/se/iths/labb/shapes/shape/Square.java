package se.iths.labb.shapes.shape;

import javafx.scene.canvas.GraphicsContext;
import se.iths.labb.shapes.ShapeParameter;
import se.iths.labb.shapes.ShapeType;

import static se.iths.labb.shapes.ShapeType.*;

public final class Square extends Shape {
    double halfSideSize = getSize()/2;

    String colorAsString = getColor().toString().substring(2, 10);
    public Square(ShapeParameter parameter) {
        super(parameter);
    }

    @Override
    public Shape getShapeDuplicate() {
        return new Square(getDuplicate());
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setFill(getColor());
        context.fillRect(getX() - halfSideSize, getY() - halfSideSize, getSize(), getSize());
    }

    @Override
    public Boolean isInside(double posX, double posY) {
        double distanceToX = Math.abs(posX - getX());
        double distanceToY = Math.abs(posY - getY());

        return distanceToX <= halfSideSize && distanceToY <= halfSideSize;
    }
    @Override
    public ShapeType getType() {
        return SQUARE;
    }

    @Override
    public String toString() {
        return "<rect x=\"" + (getX() - halfSideSize) + "\" " +
                "y=\"" + (getY() - halfSideSize) + "\" " +
                "width=\"" + getSize() + "\" " +
                "height=\"" + getSize() + "\" " +
                "fill=\"#" + colorAsString + "\" />";
    }
}