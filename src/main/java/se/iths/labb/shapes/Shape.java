package se.iths.labb.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Shape {
    private double posX;
    private double posY;
    private int size;
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        double diffSize = this.size-size;

        setPosX(getPosX() + diffSize/2);
        setPosY(getPosY() + diffSize/2);
        this.size = size;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public void updateShape(Color color, int size){
        setColor(color);
        setSize(size);
    }
    public abstract Boolean isInside(double posX, double posY);

    public abstract ShapeType getType();

    public abstract String drawToSVGAsString();
}