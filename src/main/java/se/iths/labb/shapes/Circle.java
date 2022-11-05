package se.iths.labb.shapes;

import javafx.scene.canvas.GraphicsContext;

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

        double distToCenter = (Math.pow(distX + distY,2));

        return distToCenter <= radiusSq;
    }

    @Override
    public ShapeType getType() {
        return CIRCLE;
    }

    @Override
    public String drawToSVGAsString() {
        return "<circle cx=\"" + getX() + "\" " +
                "cy=\"" + getY() + "\" " +
                "r=\"" + radius + "\" " +
                "fill=\"#" + colorAsString + "\" />";
    }
}