package top.dontplay.event;

import com.google.gson.Gson;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import top.dontplay.SealDice2MC;
import top.dontplay.network.SocketServer;
import top.dontplay.network.payload.ChatMessage;
import top.dontplay.network.payload.SendMessage;
import top.dontplay.network.payload.WsEvent;

import java.util.UUID;

public class ChatEventListener {
    private static final Gson GSON = new Gson();

    // MC -> WebSocket
    public static void onPlayerChat(ServerPlayer player, String content) {
        ChatMessage msg = new ChatMessage();
        msg.content = content;
        msg.uuid = player.getUUID().toString();
        msg.name = player.getName().getString();
        msg.isAdmin = player.hasPermissions(2);
        msg.messageType = "group";

        WsEvent eventb = new WsEvent();
        eventb.event = msg;
        eventb.type = "message";

        String jsonText = GSON.toJson(eventb);
        broadcastSocketClient(jsonText);
    }

    // WebSocket -> MC
    public static void onMessage(String message) {
        SendMessage msg = GSON.fromJson(message, SendMessage.class);
        if (SealDice2MC.SERVER_INSTANCE == null) return;

        if ("private".equals(msg.messageType)) {
            // send to specific player
            try {
                UUID uid = UUID.fromString(msg.uuid);
                ServerPlayer player = SealDice2MC.SERVER_INSTANCE.getPlayerList().getPlayer(uid);
                if (player != null) {
                    player.sendSystemMessage(Component.literal(msg.content));
                }
            } catch (IllegalArgumentException ignored) {}
        } else {
            // broadcast to all players
            SealDice2MC.SERVER_INSTANCE.getPlayerList().broadcastSystemMessage(
                    Component.literal(msg.content), false
            );
        }
    }

    public static void broadcastSocketClient(String s) {
        if (SocketServer.instance != null) {
            SocketServer.instance.broadcast(s);
        }
    }
}