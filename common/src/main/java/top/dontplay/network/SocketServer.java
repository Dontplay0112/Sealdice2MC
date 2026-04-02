package top.dontplay.network;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import top.dontplay.event.ChatEventListener;

import java.net.InetSocketAddress;

public class SocketServer extends WebSocketServer {

    public static SocketServer instance;
    public SocketServer(int port) {
        super(new InetSocketAddress(port));
        if (instance != null) {
            try {
                instance.stop();
            } catch (Exception ignored) {}
        }
        instance = this;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println(conn + " has left the room!");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        ChatEventListener.onMessage(message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("An error occurred on connection " + conn + ":" + ex);
    }

    @Override
    public void onStart() {
        System.out.println("Server started!");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);

    }
}