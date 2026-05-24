package com.buuz135.mhud;

import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;

public class EmptyHUD extends CustomUIHud {

    public EmptyHUD(PlayerRef playerRef) {
        super(playerRef, "empty");
    }

    @Override
    public void build(@Nonnull UICommandBuilder builder) {
    }
}
