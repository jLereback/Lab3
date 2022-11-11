package se.iths.labb.shapes.shape;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import se.iths.labb.shapes.ShapeParameter;
import se.iths.labb.shapes.ShapeType;

public abstract class Shape {
    private final double posX;
    private final double posY;
    double size;
    private Color color;

    public Shape(ShapeParameter parameter) {
        this.posX = parameter.posX();
        this.posY = parameter.posY();
        this.size = parameter.size();
        this.color = parameter.color();
    }

    public abstract void draw(GraphicsContext context);

    public abstract Shape getShapeDuplicate();

    public ShapeParameter getDuplicate() {
        return new ShapeParameter(getX(), getY(), getSize(), getColor());
    }

    public double getX() {
        return posX;
    }

    public double getY() {
        return posY;
    }

    public double getSize() {
        return size;
    }
    public void setSize(double size) {
        this.size = size;
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }

    public void updateShape(Color color, double size) {
        setColor(color);
        setSize(size);
    }

    public abstract Boolean isInside(double posX, double posY);

    public abstract ShapeType getType();

    public abstract String toString();
}