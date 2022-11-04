package se.iths.labb.svg;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;
import se.iths.labb.Model;
import se.iths.labb.shapes.Shape;
import se.iths.labb.shapes.ShapeFactory;
import se.iths.labb.shapes.ShapeParameter;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import static se.iths.labb.shapes.ShapeType.*;

public class Server {
    private final Model model;
    private final ShapeFactory shapeFactory;
    private PrintWriter writer;
    private BufferedReader reader;
    private final BooleanProperty connected;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static final Server server = new Server();
    public static Server getServer() {
        return server;
    }

    public Server(){
        this.model = new Model();
        this.shapeFactory = new ShapeFactory();
        this.connected = new SimpleBooleanProperty();
    }

    public void connectToServer() {
        connected.bindBidirectional(model.serverConnectedProperty());
        try {
            InputStream input;
            try (Socket socket = new Socket("127.0.0.1", 8000)) {
                writer = new PrintWriter(socket.getOutputStream(), true);
                input = socket.getInputStream();
            }
            reader = new BufferedReader(new InputStreamReader(input));

            model.setServerConnected(true);
            System.out.println("Connected to Server...");

            executorService.submit(this::networkHandler);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void networkHandler() {
       try {
           do {
               String line = reader.readLine();
               System.out.println(line);
               Platform.runLater(() -> model.addShapeToList(concertToShape(line)));
           } while (model.isServerConnected());

           } catch (IOException e) {
           System.out.println("I/O error. Disconnected.");
           Platform.runLater(() -> model.setServerConnected(false));
       }
    }

        public Shape concertToShape(String line) {
            try {
                Pattern pattern = Pattern.compile("=");

                String[] parameterArray = pattern.split(line);
                if (line.contains("circle")) {
                    return shapeFactory.getShape(CIRCLE, new ShapeParameter(
                            Double.parseDouble(parameterArray[1].substring(1, 5)),
                            Double.parseDouble(parameterArray[2].substring(1, 5)),
                            (Integer.parseInt(parameterArray[3].substring(1, 5)) * 2),
                            Color.valueOf(parameterArray[4].substring(1, 10))));
                } else if (line.contains("rect")) {
                    return shapeFactory.getShape(SQUARE, new ShapeParameter(
                            Double.parseDouble(parameterArray[1].substring(1, 5)),
                            Double.parseDouble(parameterArray[2].substring(1, 5)),
                            Integer.parseInt(parameterArray[3].substring(1, 5)),
                            Color.valueOf(parameterArray[4].substring(1, 10))));
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new RuntimeException();
            }
            return null;
        }

        public void addShapeToServer (Shape shape){
            if (model.isServerConnected())
                writer.println(shape.drawToSVGAsString());
            else
                model.addShapeToList(shape);

        }
    }
