package top.dontplay.command;

import com.google.gson.Gson;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import top.dontplay.event.ChatEventListener;
import top.dontplay.network.payload.ChatMessage;
import top.dontplay.network.payload.WsEvent;

public class SealDiceCommand {
    private static final Gson GSON = new Gson();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("sealdice")
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            String content = StringArgumentType.getString(context, "message");

                            ChatMessage msg = new ChatMessage();
                            msg.messageType = "private";
                            msg.isAdmin = source.hasPermission(2);
                            msg.content = content;

                            if (source.getEntity() instanceof ServerPlayer player) {
                                msg.name = player.getName().getString();
                                msg.uuid = player.getUUID().toString();
                            } else {
                                msg.name = "Server";
                                msg.messageType = "group";
                            }

                            WsEvent eventb = new WsEvent();
                            eventb.event = msg;
                            eventb.type = "message";

                            ChatEventListener.broadcastSocketClient(GSON.toJson(eventb));
                            return 1; // Means the command executed successfully
                        })
                )
        );
    }
}