package top.dontplay.network;

import java.io.IOException;

public class WebSocketManager implements Runnable {
    private static int port;

    public WebSocketManager(int port) {
        WebSocketManager.port = port;
    }

    public static void startSocketServer() throws IOException, InterruptedException {
        //        int port = 8887; // 843 flash policy port
        SocketServer s = new SocketServer(port);
        s.start();
        System.out.println("ChatServer started on port: " + s.getPort());
    }

    @Override
    public void run() {
        try {
            startSocketServer();
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to start WebSocket Server: " + e.getMessage());
        }
    }
}
