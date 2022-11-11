package se.iths.labb;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import se.iths.labb.shapes.shape.Shape;
import se.iths.labb.shapes.ShapeType;
import se.iths.labb.tools.Server;

import java.util.ArrayDeque;
import java.util.Deque;

import static se.iths.labb.shapes.ShapeType.*;

public class Model {
    Server server = new Server();

    private final ObservableList<ShapeType> choiceBoxShapeList;
    private final ObjectProperty<ShapeType> shapeType;
    private final ObservableList<String> chatList;
    private final ObservableList<Shape> shapeList;
    private final Deque<Deque<Shape>> undoDeque;
    private final Deque<Deque<Shape>> redoDeque;
    private final BooleanProperty serverConnect;
    private final BooleanProperty chatExpanded;
    private final ObjectProperty<Double> size;
    private final ObjectProperty<Color> color;
    private final BooleanProperty undoVisible;
    private final BooleanProperty redoVisible;
    private final BooleanProperty chatButton;
    private final DoubleProperty canvasHeight;
    private final DoubleProperty canvasWidth;
    private final StringProperty chatInput;
    private final BooleanProperty eraser;
    private final BooleanProperty brush;



    public Model() {
        this.chatExpanded = new SimpleBooleanProperty(true);
        this.chatList = FXCollections.observableArrayList();
        this.chatInput = new SimpleStringProperty();
        this.chatButton = new SimpleBooleanProperty(true);
        this.serverConnect = new SimpleBooleanProperty();
        this.canvasHeight = new SimpleDoubleProperty();
        this.canvasWidth = new SimpleDoubleProperty();
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

    public BooleanProperty chatButtonProperty() {
        return chatButton;
    }

    public void setChatButton(boolean chatButton) {
        this.chatButton.set(chatButton);
    }

    public double getCanvasWidth() {
        return canvasWidth.get();
    }

    public DoubleProperty canvasWidthProperty() {
        return canvasWidth;
    }
    public double getCanvasHeight() {
        return canvasHeight.get();
    }
    public DoubleProperty canvasHeightProperty() {
        return canvasHeight;
    }

    public boolean isChatExpanded() {
        return chatExpanded.get();
    }

    public BooleanProperty chatExpandedProperty() {
        return chatExpanded;
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

    public Deque<Shape> getTempList() {
        Deque<Shape> tempList = new ArrayDeque<>();
        for (Shape shape : shapeList)
            tempList.add(shape.getShapeDuplicate());
        return tempList;
    }

    public void updateShapeList() {
        Deque<Shape> tempList = getTempList();
        shapeList.clear();
        shapeList.addAll(tempList);
    }

    public void addToUndoDeque() {
        undoDeque.addLast(getTempList());
    }

    public void addToRedoDeque() {
        redoDeque.addLast(getTempList());
    }

    public void addShapeToList(Shape shape) {
        shapeList.add(shape);
    }

    public void sendToList(Shape shape) {
        if (getServerConnect())
            server.addShapeToServer();
        else
            addShapeToList(shape);
    }

    public BooleanProperty serverConnectProperty() {
        return serverConnect;
    }

    public boolean getServerConnect() {
        return serverConnect.get();
    }

    public void setServerConnect(boolean serverConnect) {
        this.serverConnect.set(serverConnect);
    }

    public void connectToServer() {
        server.connect(this);
    }

    public void disconnect() {
        server.disconnect();
    }
}