package se.iths.labb.svg;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import se.iths.labb.Model;
import se.iths.labb.shapes.*;
import se.iths.labb.shapes.ShapeFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private Model model;
    private final ShapeFactory shapeFactory;
    private final ThreadLocal<Socket> socket = new ThreadLocal<>();
    private PrintWriter writer;
    private BufferedReader reader;
    private final BooleanProperty connected = new SimpleBooleanProperty(false);
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static final Server server = new Server();

    public static Server getServer() {
        return server;
    }

    public Server() {
        this.shapeFactory = new ShapeFactory();
    }

    public void connect(Model model) {
        this.model = model;
        connectToServer();
    }

    public void disconnect() {
        try {
            terminateServer();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private void terminateServer() throws InterruptedException {
        socket.remove();
        model.setServerConnected(false);
        System.out.print("Disconnected from Server");
    }

    public void connectToServer() {
        try {
            initServer();
            System.out.print("Connected to Server");
            executorService.submit(this::clientHandler);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void initServer() throws IOException {

        connected.bindBidirectional(model.serverConnectedProperty());
        socket.set(new Socket("127.0.0.1", 8000));
        writer = new PrintWriter(socket.get().getOutputStream(), true);
        reader = new BufferedReader(new InputStreamReader(socket.get().getInputStream()));

        model.setServerConnected(true);
    }

    private void clientHandler() {
        try {
            while (model.isServerConnected()) {
                readFromServer();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void readFromServer() throws IOException {
        String line = reader.readLine();
        if (line == null || line.contains("joined") || line.contains("left"))
            return;
        Platform.runLater(() -> model.addShapeToList(shapeFactory.convertSVGToShape(line)));
    }

    public void addShapeToServer(Shape shape) {
        if (model.isServerConnected())
            try {
                writer.println(shape.toString());
            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
            }
        else
            model.addShapeToList(shape);
    }
}