package dev.zoid.noDeathAnimation.listeners;

import net.minecraft.world.entity.Pose;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import dev.zoid.noDeathAnimation.util.PacketUtil;

public class DeathEventListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PacketUtil.sendPosePacket(player, Pose.STANDING);
    }
}