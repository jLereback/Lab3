package se.iths.labb;

import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import se.iths.labb.shapes.Shape;
import se.iths.labb.shapes.ShapeFactory;
import se.iths.labb.shapes.ShapeParameter;
import se.iths.labb.shapes.ShapeType;

import java.util.Optional;

import static javafx.scene.input.KeyCombination.*;
import static se.iths.labb.svg.SVGWriter.getSVGWriter;

public class Controller {
    static final KeyCombination SAVE = new KeyCodeCombination(KeyCode.S, CONTROL_DOWN);
    static final KeyCombination UNDO = new KeyCodeCombination(KeyCode.Z, CONTROL_DOWN);
    static final KeyCombination REDO = new KeyCodeCombination(KeyCode.Y, CONTROL_DOWN);
    static final KeyCombination EXIT = new KeyCodeCombination(KeyCode.E, ALT_DOWN);

    static final Color BACKGROUND_COLOR = Color.web("#edece0");
    static final int MAX_WIDTH = 2000;
    static final int MAX_HEIGHT = 1000;
    public TitledPane chatArea;

    Model model = new Model();
    ShapeFactory shapeFactory = new ShapeFactory();
    private Stage stage;

    public GraphicsContext context;
    public ShapeParameter shapeParameter;
    public ChoiceBox<ShapeType> shapeType;
    public ListView<String> chatListview;
    public CheckMenuItem connectToServer;
    public Spinner<Double> sizeSpinner;
    public ColorPicker colorPicker;
    public CheckMenuItem viewRedo;
    public CheckMenuItem viewUndo;
    public Label connectedLabel;
    public TextField chatInput;
    public Canvas paintingArea;
    public ToggleButton brush;
    public Button sendButton;
    public Button undoButton;
    public Button redoButton;
    public MenuItem menuUndo;
    public MenuItem menuSave;
    public MenuItem menuRedo;
    public MenuItem menuExit;

    public void initialize() {
        context = paintingArea.getGraphicsContext2D();

        connectToServer.selectedProperty().bindBidirectional(model.serverConnectedProperty());
        connectedLabel.visibleProperty().bind(model.serverConnectedProperty());

        viewUndo.selectedProperty().bindBidirectional(model.undoVisibleProperty());
        undoButton.visibleProperty().bind(model.undoVisibleProperty());

        viewRedo.selectedProperty().bindBidirectional(model.redoVisibleProperty());
        redoButton.visibleProperty().bind(model.redoVisibleProperty());

        chatArea.visibleProperty().bind(model.serverConnectedProperty());

        brush.textProperty().bind(model.brushTextProperty());
        chatInput.textProperty().bindBidirectional(model.chatInputProperty());



        chatListview.setItems(model.getChatList());


        sendButton.disableProperty().bind(model.chatInputProperty().isEmpty());



        colorPicker.valueProperty().bindBidirectional(model.colorProperty());

        shapeType.valueProperty().bindBidirectional(model.shapeTypeProperty());
        shapeType.setItems(model.getChoiceBoxShapeList());

        sizeSpinner.getValueFactory().valueProperty().bindBidirectional(model.sizeProperty());

        model.getShapeList().addListener((ListChangeListener<Shape>) onChange -> draw());

        menuRedo.setAccelerator(REDO);
        menuUndo.setAccelerator(UNDO);
        menuSave.setAccelerator(SAVE);
        menuExit.setAccelerator(EXIT);

        preparePaintingArea();
    }

    public void canvasClicked(MouseEvent mouseEvent) {
        if (brush.isSelected()) {
            paintingArea.setOnMouseDragReleased(this::createNewShape);
        }
        if (mouseEvent.isControlDown() && mouseEvent.isShiftDown())
            updateShape(mouseEvent);
        else if (mouseEvent.isControlDown())
            updateColor(mouseEvent);
        else if (mouseEvent.isShiftDown())
            updateSize(mouseEvent);
        else
            createNewShape(mouseEvent);
        model.getRedoDeque().clear();
    }

    private void createNewShape(MouseEvent mouseEvent) {
        createNewShapeParameter(mouseEvent.getX(), mouseEvent.getY());

        model.addToUndoDeque();
        model.sendToList(shapeFactory.getShape(model.getShapeType(), shapeParameter));
    }

    private void createNewShapeParameter(double posX, double posY) {
        shapeParameter = new ShapeParameter(posX, posY, model.getSize(), model.getColor());
    }

    private void draw() {
        preparePaintingArea();
        model.getShapeList().forEach(shape -> shape.draw(context));
    }

    private void preparePaintingArea() {
        context.setFill(BACKGROUND_COLOR);
        context.fillRect(0, 0, MAX_WIDTH, MAX_HEIGHT);
    }

    public void undoClicked() {
        model.undo();
    }

    public void redoClicked() {
        model.redo();
    }

    public void updateShape(MouseEvent mouseEvent) {
        if (findShape(mouseEvent).isEmpty())
            return;
        model.addToUndoDeque();
        findShape(mouseEvent).ifPresent(shape -> shape.updateShape(model.getColor(), model.getSize()));
        model.updateShapeList();
    }

    private void updateColor(MouseEvent mouseEvent) {
        if (findShape(mouseEvent).isEmpty())
            return;
        model.addToUndoDeque();
        findShape(mouseEvent).ifPresent(shape -> shape.setColor(model.getColor()));
        model.updateShapeList();
    }

    private void updateSize(MouseEvent mouseEvent) {
        if (findShape(mouseEvent).isEmpty())
            return;
        model.addToUndoDeque();
        findShape(mouseEvent).ifPresent(shape -> shape.setSize(model.getSize()));
        model.updateShapeList();
    }

    private Optional<Shape> findShape(MouseEvent mouseEvent) {
        return model.getShapeList().stream()
                .filter(shape -> shape.isInside(mouseEvent.getX(), mouseEvent.getY()))
                .reduce((first, second) -> second);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void connectToServer() {
        if (connectToServer.isSelected())
            model.connectToServer();
        else
            model.disconnect();
    }

    public void save() {
        getSVGWriter().save(model, stage);
    }

    public void exit() {
        System.exit(0);
    }

    public void updateBrush() {
        if (brush.isSelected()) {
            model.setBrushText("Put down Brush");
            paintingArea.setOnMouseDragged(this::createNewShape);
        }
        else {
            model.setBrushText("Pick up Brush");
            paintingArea.setOnMouseDragged(null);
        }
    }

    public void sendMessage() {
        model.getServer().sendMessage(model.getChatInput());
    }
}

