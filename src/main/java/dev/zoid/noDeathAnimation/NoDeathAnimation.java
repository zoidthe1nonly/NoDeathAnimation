package dev.zoid.noDeathAnimation;

import org.bukkit.plugin.java.JavaPlugin;
import dev.zoid.noDeathAnimation.listeners.DeathEventListener;

public final class NoDeathAnimation extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new DeathEventListener(), this);
    }

    @Override
    public void onDisable() {

    }
}
