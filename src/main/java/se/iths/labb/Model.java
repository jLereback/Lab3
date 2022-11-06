package se.iths.labb;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import se.iths.labb.shapes.Shape;
import se.iths.labb.shapes.ShapeType;

import java.util.ArrayDeque;
import java.util.Deque;

import static se.iths.labb.shapes.ShapeType.*;
import static se.iths.labb.svg.Server.*;

public class Model {
    private final BooleanProperty serverConnected;
    private final BooleanProperty undoVisible;
    private final BooleanProperty redoVisible;
    private final BooleanProperty brush;
    private final ObservableList<ShapeType> choiceBoxShapeList;
    private final Deque<Deque<Shape>> undoDeque;
    private final Deque<Deque<Shape>> redoDeque;
    private final ObservableList<Shape> shapeList;
    private ObjectProperty<String> brushText;
    private final ObjectProperty<Double> size;
    private final ObjectProperty<Color> color;
    private final ObjectProperty<ShapeType> shapeType;


    public Model() {

        this.serverConnected = new SimpleBooleanProperty();
        this.undoVisible = new SimpleBooleanProperty(true);
        this.redoVisible = new SimpleBooleanProperty(true);
        this.brush = new SimpleBooleanProperty(false);
        this.choiceBoxShapeList = FXCollections.observableArrayList(ShapeType.values());
        this.shapeList = FXCollections.observableArrayList();
        this.undoDeque = new ArrayDeque<>();
        this.redoDeque = new ArrayDeque<>();
        this.brushText = new SimpleObjectProperty<>("Pick up Brush");
        this.color = new SimpleObjectProperty<>(Color.web("#44966C"));
        this.size = new SimpleObjectProperty<>(50.0);
        this.shapeType = new SimpleObjectProperty<>(CIRCLE);
    }

    public ObjectProperty<ShapeType> shapeTypeProperty() {
        return shapeType;
    }

    public ShapeType getShapeType() {
        return shapeType.get();
    }

    public void setShapeType(ShapeType shapeType) {
        this.shapeType.set(shapeType);
    }

    public Property<Double> sizeProperty() {
        return size;
    }

    public Double getSize() {
        return size.get();
    }

    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    public Color getColor() {
        return color.get();
    }

    public boolean isUndoVisible() {
        return undoVisible.get();
    }

    public BooleanProperty undoVisibleProperty() {
        return undoVisible;
    }

    public void setUndoVisible(boolean undoVisible) {
        this.undoVisible.set(undoVisible);
    }

    public boolean isRedoVisible() {
        return redoVisible.get();
    }

    public BooleanProperty redoVisibleProperty() {
        return redoVisible;
    }

    public void setRedoVisible(boolean redoVisible) {
        this.redoVisible.set(redoVisible);
    }

    public void setBrushText(String brushText) {
        this.brushText.set(brushText);
    }

    public String getBrushText() {
        return brushText.get();
    }

    public ObjectProperty<String> brushTextProperty() {
        return brushText;
    }

    public boolean isBrush() {
        return brush.get();
    }

    public BooleanProperty brushProperty() {
        return brush;
    }

    public void setBrush(boolean brush) {
        this.brush.set(brush);
    }

    public ObservableList<ShapeType> getChoiceBoxShapeList() {
        return choiceBoxShapeList;
    }

    public ObservableList<Shape> getShapeList() {
        return shapeList;
    }

    public Deque<Deque<Shape>> getUndoDeque() {
        return undoDeque;
    }

    public Deque<Deque<Shape>> getRedoDeque() {
        return redoDeque;
    }

    public void undo() {
        if (undoDeque.isEmpty())
            return;

        addToRedoDeque();
        shapeList.clear();
        shapeList.addAll(undoDeque.removeLast());
    }

    public void redo() {
        if (redoDeque.isEmpty())
            return;

        addToUndoDeque();
        shapeList.clear();
        shapeList.addAll(redoDeque.removeLast());
    }

    public ObservableList<Shape> getTempList() {
        ObservableList<Shape> tempList = FXCollections.observableArrayList();
        for (Shape shape : shapeList)
            tempList.add(shape.getShapeDuplicate());
        return tempList;
    }
    public Deque<Shape> getShapeListAsDeque() {
        Deque<Shape> tempList = new ArrayDeque<>();
        for (Shape shape : shapeList)
            tempList.add(shape.getShapeDuplicate());
        return tempList;
    }

    public void updateShapeList() {
        ObservableList<Shape> tempList = getTempList();
        shapeList.clear();
        shapeList.addAll(tempList);
    }

    public void addToUndoDeque() {
        undoDeque.addLast(getShapeListAsDeque());
    }

    public void addToRedoDeque() {
        redoDeque.addLast(getShapeListAsDeque());
    }

    public void addShapeToList(Shape shape) {
        shapeList.add(shape);
    }

    public void sendToList(Shape shape) {
        if (isServerConnected())
            getServer().addShapeToServer(shape);
        else
            addShapeToList(shape);
    }

    public BooleanProperty serverConnectedProperty() {
        return serverConnected;
    }

    public boolean isServerConnected() {
        return serverConnected.get();
    }

    public void setServerConnected(boolean serverConnected) {
        this.serverConnected.set(serverConnected);
    }

    public void connectToServer() {
        getServer().connect(this);
    }

    public void disconnect() {
        getServer().disconnect();
    }
}