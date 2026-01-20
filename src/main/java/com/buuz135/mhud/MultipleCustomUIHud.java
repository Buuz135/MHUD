package com.buuz135.mhud;


import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.logging.Level;

public class MultipleCustomUIHud extends CustomUIHud {

    private static Method BUILD_METHOD;

    static {
        try {
            BUILD_METHOD = CustomUIHud.class.getDeclaredMethod("build", UICommandBuilder.class);
            BUILD_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            BUILD_METHOD = null;
            MultipleHUD.getInstance().getLogger().at(Level.SEVERE).log("Could not find method 'build' in CustomUIHud");
            MultipleHUD.getInstance().getLogger().at(Level.SEVERE).log(e.getMessage());
        }
    }

    private final HashMap<String, CustomUIHud> customHuds;

    public MultipleCustomUIHud(@NonNullDecl PlayerRef playerRef, HashMap<String, CustomUIHud> customHuds) {
        super(playerRef);
        this.customHuds = customHuds;
    }

    @Override
    protected void build(@NonNullDecl UICommandBuilder uiCommandBuilder) {
        for (String key : customHuds.keySet()) {
            var hud = customHuds.get(key);
            try {
                if (BUILD_METHOD != null) {
                    BUILD_METHOD.invoke(hud, uiCommandBuilder);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public HashMap<String, CustomUIHud> getCustomHuds() {
        return customHuds;
    }
}
