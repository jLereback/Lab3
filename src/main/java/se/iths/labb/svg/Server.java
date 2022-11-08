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
import java.util.concurrent.TimeUnit;

public class Server {
    private Model model;
    private final ShapeFactory shapeFactory;
    private final ThreadLocal<Socket> socket = new ThreadLocal<>();
    private PrintWriter writer;
    private BufferedReader reader;
    private final BooleanProperty connected = new SimpleBooleanProperty(false);
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public Server() {
        this.shapeFactory = new ShapeFactory();
    }

    public void connect(Model model) {
        this.model = model;
        if (!model.isServerConnected())
            return;
        connectToServer();
    }

    public void disconnect() {
        try {
            terminateServer();
        } catch (InterruptedException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void terminateServer() throws InterruptedException, IOException {
        if (executorService.awaitTermination(10, TimeUnit.MILLISECONDS))
            executorService.shutdown();
        writer.close();
        model.setServerConnected(false);
        System.out.println("Disconnected from Server");
    }

    public void connectToServer() {
        try {
            initServer();
            System.out.println("Connected to Server");
            executorService.submit(this::handleClient);
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

    private void handleClient() {
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
        if (line.endsWith("/>"))
            Platform.runLater(() -> model.addShapeToList(shapeFactory.convertStringToShape(line)));
        else
            Platform.runLater(() -> model.getChatList().add(line));
    }

    public void addShapeToServer(Shape shape) {
        try {
            writer.println(shape.toString());
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }
    public void sendMessage(String string) {
        writer.println("Julia: " + string);
    }
}
