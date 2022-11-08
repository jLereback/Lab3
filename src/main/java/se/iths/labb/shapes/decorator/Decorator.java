package se.iths.labb.shapes.decorator;

import javafx.scene.canvas.GraphicsContext;
import se.iths.labb.shapes.Shape;
import se.iths.labb.shapes.ShapeParameter;
import se.iths.labb.shapes.ShapeType;

public class Decorator extends Shape {
    public Decorator(ShapeParameter parameter) {
        super(parameter);
    }

    @Override
    public void draw(GraphicsContext context) {
        return;
    }

    @Override
    public Shape getShapeDuplicate() {
        return null;
    }

    @Override
    public Boolean isInside(double posX, double posY) {
        return null;
    }

    @Override
    public ShapeType getType() {
        return null;
    }

    @Override
    public String toString() {
        return null;
    }
}
