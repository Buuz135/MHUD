package com.buuz135.mhud.testing;

import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class TestUIHUD extends CustomUIHud {

    private final String file;

    public TestUIHUD(@NonNullDecl PlayerRef playerRef, String file) {
        super(playerRef, file);
        this.file = file;
    }

    @Override
    protected void build(@NonNullDecl UICommandBuilder uiCommandBuilder) {
        uiCommandBuilder.append("Pages/" + file);
    }
}
