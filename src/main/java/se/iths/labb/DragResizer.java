package se.iths.labb;

import javafx.geometry.Point2D;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

/**
 * Adapted from <a href="https://gist.github.com/andytill/4369729">...</a>
 * Adapted from <a href="https://github.com/grubbcc/anagrams/blob/browser/client/java/client/DragResizer.java">...</a>
 *
 * @author jlereback (modified from the original DragResizer created by AndyTill)
 */

@SuppressWarnings("UnnecessaryReturnStatement")
public class DragResizer {
    private static final int RESIZE_MARGIN = 6;
    private final Region region;
    private boolean draggingEast;
    private boolean draggingSouth;


    private final ObjectProperty<Point2D> mouseLocation = new SimpleObjectProperty<>();
    private DragResizer(Region aRegion) {
        region = aRegion;
    }
    public static void makeResizable(Region region) {
        DragResizer resizer = new DragResizer(region);
        region.setOnMousePressed(resizer::mousePressed);
        region.setOnMouseDragged(resizer::mouseDragged);
        region.setOnMouseMoved(resizer::mouseOver);
        region.setOnMouseReleased(resizer::mouseReleased);
    }

    public static void makeSizeStatic(Region region) {
        region.setOnMousePressed(null);
        region.setOnMouseDragged(null);
        region.setOnMouseMoved(null);
        region.setOnMouseReleased(null);
        region.setPrefHeight(5);
    }
    protected void mouseOver(MouseEvent event) {
        if (isInDraggableZoneS(event) || draggingSouth) {
            if (isInDraggableZoneE(event) || draggingEast)
                region.setCursor(Cursor.SE_RESIZE);
            else
                region.setCursor(Cursor.S_RESIZE);
        } else if (isInDraggableZoneE(event) || draggingEast)
            region.setCursor(Cursor.E_RESIZE);
        else
            region.setCursor(Cursor.DEFAULT);
    }
    private void mousePressed(MouseEvent event) {

        event.consume();

        //should bring clicked gameWindow to front if multiple gameWindows are open

        draggingEast = isInDraggableZoneE(event);
        draggingSouth = isInDraggableZoneS(event);

        mouseLocation.set(new Point2D((float) event.getScreenX(), (float) event.getScreenY()));
    }
    private boolean isInDraggableZoneS(MouseEvent event) {
        return event.getY() > (region.getHeight() - RESIZE_MARGIN);
    }
    private boolean isInDraggableZoneE(MouseEvent event) {
        return event.getX() > (region.getWidth() - RESIZE_MARGIN);
    }
    private void mouseDragged(MouseEvent event) {

        event.consume();

        if (draggingSouth)
            resizeSouth(event);
        if (draggingEast)
            resizeEast(event);
        if (draggingSouth || draggingEast)
            return;
    }
    private void resizeEast(MouseEvent event) {
        region.setMinWidth(Math.min(event.getX(), 500));
    }
    private void resizeSouth(MouseEvent event) {
        region.setMinHeight(Math.min(event.getY(), 600));
    }
    protected void mouseReleased(MouseEvent event) {
        draggingEast = false;
        draggingSouth = false;
        region.setCursor(Cursor.DEFAULT);
        mouseLocation.set(null);
    }
}