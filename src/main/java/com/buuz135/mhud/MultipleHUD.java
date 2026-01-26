package com.buuz135.mhud;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;

public class MultipleHUD extends JavaPlugin {

    private static MultipleHUD instance;

    public static MultipleHUD getInstance() {
        return instance;
    }

    public MultipleHUD(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
        getLogger().atInfo().log(instance.toString());
    }

    @Override
    protected void setup() {
        super.setup();
        //this.getEntityStoreRegistry().registerSystem(new CustomTickingSystem());
    }

    public void setCustomHud(Player player, PlayerRef playerRef, String hudIdentifier, CustomUIHud customHud) {
        CustomUIHud currentCustomHud = player.getHudManager().getCustomHud();
        if (currentCustomHud instanceof MultipleCustomUIHud multipleCustomUIHud) {
            multipleCustomUIHud.add(hudIdentifier, customHud);
        } else {
            MultipleCustomUIHud mchud = new MultipleCustomUIHud(playerRef);
            player.getHudManager().setCustomHud(playerRef, mchud);
            mchud.add(hudIdentifier, customHud);
            if (currentCustomHud != null) {
                mchud.add("Unknown", currentCustomHud);
            }
        }
    }

    @Deprecated
    public void hideCustomHud(Player player, PlayerRef playerRef, String hudIdentifier) {
        hideCustomHud(player, hudIdentifier);
    }
    public void hideCustomHud(Player player, String hudIdentifier) {
        var currentCustomHud = player.getHudManager().getCustomHud();

        if (currentCustomHud instanceof MultipleCustomUIHud multipleCustomUIHud) {
            multipleCustomUIHud.getCustomHuds().remove(hudIdentifier);

            if (multipleCustomUIHud.getCustomHuds().isEmpty()) {
                player.getHudManager().setCustomHud(playerRef, new EmptyHUD(playerRef));
            } else {
                player.getHudManager().setCustomHud(playerRef, multipleCustomUIHud);
                multipleCustomUIHud.show();
            }
        }
    }
}