package se.iths.labb.shapes;

import javafx.scene.canvas.GraphicsContext;

import static se.iths.labb.shapes.ShapeType.*;

public final class Square extends Shape {
    double halfSideSize = getSize() >> 1;
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

    private double getHalfSideSize() {
        return getSize() >> 1;
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
    public String drawToSVGAsString() {
        String convertColor = getColor().toString().substring(2, 10);
        return "<rect x=\"" + (getX() - halfSideSize) + "\" " +
                "y=\"" + (getY() - halfSideSize) + "\" " +
                "width=\"" + getSize() + "\" " +
                "height=\"" + getSize() + "\" " +
                "fill=\"#" + convertColor + "\" />";
    }
}