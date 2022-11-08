package se.iths.labb.shapes;

import javafx.scene.canvas.Canvas;

public class ResizablePaintingArea extends Canvas {

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double minWidth(double height) {
        return 350;
    }

    @Override
    public double minHeight(double width) {
        return 240;
    }

    @Override
    public double prefWidth(double height) {
        return 550;
    }

    @Override
    public double prefHeight(double width) {
        return 390;
    }

    @Override
    public double maxWidth(double height) {
        return Double.MAX_VALUE;
    }


    @Override
    public double maxHeight(double width) {
        return Double.MAX_VALUE;
    }

    @Override
    public void resize(double width, double height) {
        this.setWidth(width);
        this.setHeight(height);
    }
}
