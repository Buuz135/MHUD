package com.buuz135.mhud;

import com.hypixel.hytale.protocol.packets.interface_.CustomUICommand;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class MultipleCustomUIHud extends CustomUIHud {

    private static Method BUILD_METHOD;
    private static Field COMMANDS_FIELD;

    static {
        try {
            BUILD_METHOD = CustomUIHud.class.getDeclaredMethod("build", UICommandBuilder.class);
            BUILD_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            BUILD_METHOD = null;
            MultipleHUD.getInstance().getLogger().at(Level.SEVERE).log("Could not find method 'build' in CustomUIHud");
            MultipleHUD.getInstance().getLogger().at(Level.SEVERE).log(e.getMessage());
        }

        try {
            COMMANDS_FIELD = UICommandBuilder.class.getDeclaredField("commands");
            COMMANDS_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            COMMANDS_FIELD = null;
            MultipleHUD.getInstance().getLogger().at(Level.SEVERE).log("Could not find field 'commands' in UICommandBuilder");
            MultipleHUD.getInstance().getLogger().at(Level.SEVERE).log(e.getMessage());
        }
    }

    static void buildHud (@Nonnull UICommandBuilder uiCommandBuilder, String id, @Nonnull CustomUIHud hud) {
        try {
            if (BUILD_METHOD == null || COMMANDS_FIELD == null) return;

            UICommandBuilder singleHudBuilder = new UICommandBuilder();
            BUILD_METHOD.invoke(hud, singleHudBuilder);

            final List<CustomUICommand> commands = (List<CustomUICommand>) COMMANDS_FIELD.get(uiCommandBuilder);
            Arrays.stream(singleHudBuilder.getCommands()).forEach((CustomUICommand command) -> {
                if (command.selector == null) {
                    command.selector = '#' + id;
                } else {
                    command.selector = '#' + id + ' ' + command.selector;
                }
                commands.add(command);
            });
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    // key is the id as provided by mod, value is normalized id to be compatible with hud.
    private final HashMap<String, String> shownHuds = new HashMap<>();

    public MultipleCustomUIHud(@NonNullDecl PlayerRef playerRef) {
        super(playerRef);
    }

    @Override
    protected void build(@NonNullDecl UICommandBuilder uiCommandBuilder) {
        uiCommandBuilder.append("HUD/MultipleHUD.ui");
        // individual hud renders will be handled by the showHud method
    }

    public void add (String identifier, CustomUIHud hud) {
        UICommandBuilder commandBuilder = new UICommandBuilder();
        String normalizedId = shownHuds.get(identifier);
        boolean shownBefore = normalizedId != null;
        if (shownBefore) {
            commandBuilder.clear("#MultipleHUD #" + normalizedId);
        } else {
            normalizedId = identifier.replaceAll("[^a-zA-Z0-9]", "");
            shownHuds.put(identifier, normalizedId);
            commandBuilder.appendInline("#MultipleHUD", "Group #" + normalizedId + " {}");
        }
        buildHud(commandBuilder, normalizedId, hud);
        update(false, commandBuilder);
    }

    public void remove (String identifier) {
        String normalizedId = shownHuds.get(identifier);
        boolean shownBefore = normalizedId != null;
        if (!shownBefore) return;
        shownHuds.remove(identifier);
        UICommandBuilder commandBuilder = new UICommandBuilder();
        commandBuilder.remove("#MultipleHUD #" + normalizedId);
        update(false, commandBuilder);
    }
}
