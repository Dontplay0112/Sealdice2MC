package top.dontplay;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.ChatEvent;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import net.minecraft.server.MinecraftServer;
import top.dontplay.command.SealDiceCommand;
import top.dontplay.command.SealPortCommand;
import top.dontplay.event.ChatEventListener;
import top.dontplay.network.SocketServer;
import top.dontplay.network.WebSocketManager;

public final class SealDice2MC {
    public static final String MOD_ID = "sealdice2mc";
    public static MinecraftServer SERVER_INSTANCE;

    public static void init() {
        // Start WebSocket server when Minecraft server starts, and stop it when Minecraft server stops
        LifecycleEvent.SERVER_STARTED.register(server -> {
            SERVER_INSTANCE = server;
            new Thread(new WebSocketManager(8887)).start();
        });

        LifecycleEvent.SERVER_STOPPING.register(server -> {
            if (SocketServer.instance != null) {
                try {
                    SocketServer.instance.stop();
                } catch (InterruptedException e) {
                    System.err.println("Error stopping WebSocket Server: " + e.getMessage());
                }
            }
        });

        // Register commands
        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
            SealDiceCommand.register(dispatcher);
            SealPortCommand.register(dispatcher);
        });

        // Listen for chat events and forward them to the WebSocket clients
        ChatEvent.RECEIVED.register((player, component) -> {
            assert player != null;
            ChatEventListener.onPlayerChat(player, component.getString());
            return EventResult.pass();
        });
    }
}
