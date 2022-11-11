package se.iths.labb;

import javafx.collections.ListChangeListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import se.iths.labb.shapes.*;

import java.util.Optional;

import static javafx.scene.input.KeyCombination.*;
import static se.iths.labb.DragResizer.*;
import static se.iths.labb.svg.SVGWriter.getSVGWriter;

public class Controller {
    static final KeyCombination SAVE = new KeyCodeCombination(KeyCode.S, CONTROL_DOWN);
    static final KeyCombination UNDO = new KeyCodeCombination(KeyCode.Z, CONTROL_DOWN);
    static final KeyCombination REDO = new KeyCodeCombination(KeyCode.Y, CONTROL_DOWN);
    static final KeyCombination EXIT = new KeyCodeCombination(KeyCode.E, ALT_DOWN);
    static final Color BACKGROUND_COLOR = Color.web("#edece0");
    Model model = new Model();

    ShapeFactory shapeFactory = new ShapeFactory();
    private Stage stage;

    public GraphicsContext context;
    public ShapeParameter shapeParameter;
    public ChoiceBox<ShapeType> shapeType;
    public CheckMenuItem connectToServer;
    public Spinner<Double> sizeSpinner;
    public ListView<String> chatWindow;
    public TitledPane chatApplication;
    public TextField chatInputField;
    public ColorPicker colorPicker;
    public CheckMenuItem viewRedo;
    public CheckMenuItem viewUndo;
    public ToggleGroup equipment;
    public Button chatSendButton;
    public Label connectedLabel;
    public Canvas paintingArea;
    public ToggleButton eraser;
    public ToggleButton brush;
    public Button undoButton;
    public Button redoButton;
    public MenuItem menuUndo;
    public MenuItem menuSave;
    public MenuItem menuRedo;
    public MenuItem menuExit;

    public void initialize() {
        initPaintingArea();
        initShape();
        initButtons();
        initServer();
        initMenu();
        initChat();

        preparePaintingArea();
    }

    private void initPaintingArea() {
        context = paintingArea.getGraphicsContext2D();

        paintingArea.widthProperty().bindBidirectional(model.canvasWidthProperty());
        paintingArea.heightProperty().bindBidirectional(model.canvasHeightProperty());

        paintingArea.widthProperty().addListener(observable -> draw());
        paintingArea.heightProperty().addListener(observable -> draw());
    }

    private void initShape() {
        colorPicker.valueProperty().bindBidirectional(model.colorProperty());

        shapeType.valueProperty().bindBidirectional(model.shapeTypeProperty());
        shapeType.setItems(model.getChoiceBoxShapeList());

        sizeSpinner.getValueFactory().valueProperty().bindBidirectional(model.sizeProperty());

        model.getShapeList().addListener((ListChangeListener<Shape>) onChange -> draw());
    }

    private void initButtons() {
        viewUndo.selectedProperty().bindBidirectional(model.undoVisibleProperty());
        undoButton.visibleProperty().bind(model.undoVisibleProperty());

        viewRedo.selectedProperty().bindBidirectional(model.redoVisibleProperty());
        redoButton.visibleProperty().bind(model.redoVisibleProperty());

        brush.selectedProperty().bindBidirectional(model.brushProperty());
        eraser.selectedProperty().bindBidirectional(model.eraserProperty());
    }

    private void initServer() {
        connectToServer.selectedProperty().bindBidirectional(model.serverConnectProperty());
        connectedLabel.visibleProperty().bind(model.serverConnectProperty());
    }

    private void initMenu() {
        menuRedo.setAccelerator(REDO);
        menuUndo.setAccelerator(UNDO);
        menuSave.setAccelerator(SAVE);
        menuExit.setAccelerator(EXIT);
    }

    private void initChat() {
        DragResizer.makeResizable(chatApplication);
        chatApplication.expandedProperty().bindBidirectional(model.chatExpandedProperty());

        chatApplication.expandedProperty().addListener(observable -> openCloseChat());
        chatApplication.visibleProperty().bind(model.serverConnectProperty());

        chatSendButton.disableProperty().bindBidirectional(model.chatButtonProperty());
        chatInputField.textProperty().bindBidirectional(model.chatInputProperty());
        model.chatInputProperty().addListener(observable -> model.setChatButton(model.getChatInput().isBlank()));

        chatWindow.setItems(model.getChatList());

    }

    public void canvasClicked(MouseEvent mouseEvent) {
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

    private void erase(MouseEvent mouseEvent) {
        if (findShape(mouseEvent).isEmpty())
            return;
        model.addToUndoDeque();
        findShape(mouseEvent).ifPresent(shape -> model.getShapeList().remove(shape));
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
        context.fillRect(0, 0, paintingArea.getWidth(), paintingArea.getWidth());
    }

    public void undoClicked() {
        model.undo();
    }

    public void redoClicked() {
        model.redo();
    }

    public void resetClicked() {
        model.getRedoDeque().clear();
        model.addToUndoDeque();
        preparePaintingArea();
        model.getShapeList().clear();

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

    public void toggleBrush() {
        if (model.isBrush())
            paintingArea.setOnMouseDragged(this::createNewShape);
        else
            paintingArea.setOnMouseDragged(null);
    }

    public void toggleEraser() {
        if (model.isEraser()) {
            paintingArea.setOnMouseDragged(this::erase);
            paintingArea.setOnMouseClicked(this::erase);
        }
        else {
            paintingArea.setOnMouseDragged(null);
            paintingArea.setOnMouseClicked(this::canvasClicked);
        }
    }
    public void sendMessage() {
        if (chatInputField.getText() == null || chatInputField.getText().isBlank())
            return;
        else
            model.getServer().sendMessage(model.getChatInput());
        chatInputField.clear();
    }

    public void openCloseChat() {
        if (model.isChatExpanded()) {
            DragResizer.makeResizable(chatApplication);
        } else {
            makeSizeStatic(chatApplication);
        }
    }
}