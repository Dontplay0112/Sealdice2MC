package top.dontplay.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import top.dontplay.config.SealDiceConfig;
import top.dontplay.network.WebSocketManager;

public class SealPortCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("sealport")
                .requires(source -> source.hasPermission(2))

                .then(Commands.argument("port", IntegerArgumentType.integer(1, 65535))
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            int port = IntegerArgumentType.getInteger(context, "port");

                            try {
                                SealDiceConfig.saveWebSocketPort(port);
                                new Thread(new WebSocketManager(port)).start();
                                source.sendSuccess(
                                        () -> Component.literal(
                                                "WebSocket Server restarted on port " + port + " (saved to config)"),
                                        false);
                            } catch (Exception e) {
                                source.sendFailure(
                                        Component.literal("Failed to update WebSocket port: " + e.getMessage()));
                            }

                            return 1;
                        })));
    }
}