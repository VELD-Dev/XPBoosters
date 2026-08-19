package com.velddev.xpalchemy.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.velddev.xpalchemy.data.PlayerDebtData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.Locale;

// Debug-only commands: /xpalchemy debt reset|add|set
public class XPAlchemyCommand {

    private static final SimpleCommandExceptionType INVALID_DEBT_TYPE =
            new SimpleCommandExceptionType(Component.literal("Invalid debt type - use hp, strength, or food"));

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_TYPES = (context, builder) ->
            SharedSuggestionProvider.suggest(new String[]{"hp", "strength", "food"}, builder);

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("xpalchemy")
                .then(Commands.literal("debt")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("reset")
                                .executes(XPAlchemyCommand::resetDebt))
                        .then(Commands.literal("add")
                                .then(Commands.argument("type", StringArgumentType.word())
                                        .suggests(SUGGEST_TYPES)
                                        .then(Commands.argument("amount", FloatArgumentType.floatArg())
                                                .executes(XPAlchemyCommand::addDebt))))
                        .then(Commands.literal("set")
                                .then(Commands.argument("type", StringArgumentType.word())
                                        .suggests(SUGGEST_TYPES)
                                        .then(Commands.argument("amount", FloatArgumentType.floatArg(0.0F, 100.0F))
                                                .executes(XPAlchemyCommand::setDebt))))));
    }

    private static PlayerDebtData.DebtType parseType(String raw) throws CommandSyntaxException {
        return switch (raw.toLowerCase(Locale.ROOT)) {
            case "hp", "health" -> PlayerDebtData.DebtType.HP;
            case "strength" -> PlayerDebtData.DebtType.STRENGTH;
            case "food", "hunger" -> PlayerDebtData.DebtType.FOOD;
            default -> throw INVALID_DEBT_TYPE.create();
        };
    }

    private static int resetDebt(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Player player = ctx.getSource().getPlayerOrException();
        PlayerDebtData.clearAllDebts(player);
        ctx.getSource().sendSuccess(() -> Component.literal("Reset all debt for " + player.getName().getString()), true);
        return 1;
    }

    private static int addDebt(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Player player = ctx.getSource().getPlayerOrException();
        PlayerDebtData.DebtType type = parseType(StringArgumentType.getString(ctx, "type"));
        float amount = FloatArgumentType.getFloat(ctx, "amount");

        float delta = amount >= 0.0F
                ? PlayerDebtData.addDebt(player, type, amount)
                : -PlayerDebtData.reduceDebt(player, type, -amount);

        float newValue = PlayerDebtData.getDebt(player, type);
        ctx.getSource().sendSuccess(() -> Component.literal(String.format(Locale.ROOT,
                "%s debt %+.1f -> %.1f for %s", type, delta, newValue, player.getName().getString())), true);
        return 1;
    }

    private static int setDebt(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Player player = ctx.getSource().getPlayerOrException();
        PlayerDebtData.DebtType type = parseType(StringArgumentType.getString(ctx, "type"));
        float amount = FloatArgumentType.getFloat(ctx, "amount");

        PlayerDebtData.setDebt(player, type, amount);
        ctx.getSource().sendSuccess(() -> Component.literal(String.format(Locale.ROOT,
                "%s debt set to %.1f for %s", type, amount, player.getName().getString())), true);
        return 1;
    }
}
