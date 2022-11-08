package se.iths.labb;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import se.iths.labb.shapes.Shape;
import se.iths.labb.shapes.ShapeType;
import se.iths.labb.svg.Server;

import java.util.ArrayDeque;
import java.util.Deque;

import static se.iths.labb.shapes.ShapeType.*;

public class Model {
    Server server = new Server();

    private final BooleanProperty serverConnected;
    private final BooleanProperty undoVisible;
    private final BooleanProperty redoVisible;
    private final BooleanProperty brush;
    private final BooleanProperty eraser;
    private final ObservableList<ShapeType> choiceBoxShapeList;
    private final Deque<Deque<Shape>> undoDeque;
    private final Deque<Deque<Shape>> redoDeque;
    private final ObservableList<Shape> shapeList;
    private final ObjectProperty<Double> size;
    private final ObjectProperty<Color> color;
    private final ObjectProperty<ShapeType> shapeType;
    private final StringProperty chatInput;
    private final ObservableList<String> chatList;
    private final BooleanProperty chatExpanded;



    public Model() {
        this.chatExpanded = new SimpleBooleanProperty();
        this.chatList = FXCollections.observableArrayList();
        this.chatInput = new SimpleStringProperty();
        this.serverConnected = new SimpleBooleanProperty();
        this.undoVisible = new SimpleBooleanProperty(true);
        this.redoVisible = new SimpleBooleanProperty(true);
        this.eraser = new SimpleBooleanProperty(false);
        this.brush = new SimpleBooleanProperty(false);
        this.choiceBoxShapeList = FXCollections.observableArrayList(ShapeType.values());
        this.shapeList = FXCollections.observableArrayList();
        this.undoDeque = new ArrayDeque<>();
        this.redoDeque = new ArrayDeque<>();
        this.color = new SimpleObjectProperty<>(Color.web("#44966C"));
        this.size = new SimpleObjectProperty<>(50.0);
        this.shapeType = new SimpleObjectProperty<>(CIRCLE);
    }

    public boolean getChatExpanded() {
        return chatExpanded.get();
    }

    public boolean isChatExpanded() {
        return chatExpanded.get();
    }

    public BooleanProperty chatExpandedProperty() {
        return chatExpanded;
    }

    public void setChatExpanded(boolean chatExpanded) {
        this.chatExpanded.set(chatExpanded);
    }

    public ObservableList<String> getChatList() {
        return chatList;
    }

    public String getChatInput() {
        return chatInput.get();
    }

    public StringProperty chatInputProperty() {
        return chatInput;
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

    public BooleanProperty undoVisibleProperty() {
        return undoVisible;
    }

    public BooleanProperty redoVisibleProperty() {
        return redoVisible;
    }

    public boolean isEraser() {
        return eraser.get();
    }

    public BooleanProperty eraserProperty() {
        return eraser;
    }

    public boolean isBrush() {
        return brush.get();
    }

    public BooleanProperty brushProperty() {
        return brush;
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
    public Server getServer() {
        return server;
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
            server.addShapeToServer(shape);
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
        server.connect(this);
    }

    public void disconnect() {
        server.disconnect();
    }
}