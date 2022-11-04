package se.iths.labb;

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
    final static KeyCombination SAVE = new KeyCodeCombination(KeyCode.S, CONTROL_DOWN);
    final static KeyCombination UNDO = new KeyCodeCombination(KeyCode.Z, CONTROL_DOWN);
    final static KeyCombination REDO = new KeyCodeCombination(KeyCode.Y, CONTROL_DOWN);

    public static final int MAX_WIDTH = 2000;
    public static final int MAX_HEIGHT = 1000;
    public static final Color BACKGROUND_COLOR = Color.web("#edece0");
    Model model = new Model();
    ShapeFactory shapeFactory = new ShapeFactory();
    public Label connectedLabel;
    public CheckMenuItem connectToServer;
    public Canvas paintingArea2;
    public ShapeParameter shapeParameter;
    public GraphicsContext context;
    public Spinner<Integer> sizeSpinner;
    public ChoiceBox<ShapeType> shapeType;
    public ColorPicker colorPicker;
    public Canvas paintingArea;
    public MenuItem menuUndo;
    public MenuItem menuSave;
    public MenuItem menuRedo;
    private Stage stage;


    public void initialize() {
        context = paintingArea.getGraphicsContext2D();

        connectToServer.selectedProperty().bindBidirectional(model.serverConnectedProperty());
        connectedLabel.visibleProperty().bind(model.serverConnectedProperty());

        colorPicker.valueProperty().bindBidirectional(model.colorProperty());

        shapeType.valueProperty().bindBidirectional(model.shapeTypeProperty());
        shapeType.setItems(model.getChoiceBoxShapeList());

        sizeSpinner.getValueFactory().valueProperty().bindBidirectional(model.sizeProperty());

        menuRedo.setAccelerator(REDO);
        menuUndo.setAccelerator(UNDO);
        menuSave.setAccelerator(SAVE);


        preparePaintingArea();
    }

    public void canvasClicked(MouseEvent mouseEvent) {
        if (mouseEvent.isControlDown() && mouseEvent.isShiftDown())
            updateShape(mouseEvent);
        else if (mouseEvent.isControlDown())
            updateColor(mouseEvent);
        else if (mouseEvent.isShiftDown())
            updateSize(mouseEvent);
        else {
            createNewShape(mouseEvent);
        }
        model.getRedoDeque().clear();
        draw();
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
        draw();
    }

    public void redoClicked() {
        model.redo();
        draw();
    }

    public void updateShape(MouseEvent mouseEvent) {
        if (findShape(mouseEvent).isEmpty())
            return;
        model.addToUndoDeque();
        findShape(mouseEvent).ifPresent(shape -> shape.updateShape(model.getColor(), model.getSize()));
    }

    private void updateColor(MouseEvent mouseEvent) {
        if (findShape(mouseEvent).isEmpty())
            return;
        model.addToUndoDeque();
        findShape(mouseEvent).ifPresent(shape -> shape.setColor(model.getColor()));
    }

    private void updateSize(MouseEvent mouseEvent) {
        if (findShape(mouseEvent).isEmpty())
            return;
        model.addToUndoDeque();
        findShape(mouseEvent).ifPresent(shape -> shape.setSize(model.getSize()));
    }

    private Optional<Shape> findShape(MouseEvent mouseEvent) {
        return model.getShapeList().stream()
                .filter(shape -> shape.isInside(mouseEvent.getX(), mouseEvent.getY()))
                .reduce((first, second) -> second);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void save() {
        getSVGWriter().save(model, stage);
    }
    public void connectToServer() {
        if (connectToServer.isSelected())
            model.connectToServer();
    }

    public void exit() {
        System.exit(0);
    }
}