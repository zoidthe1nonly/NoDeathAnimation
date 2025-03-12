package dev.zoid.noDeathAnimation.util;

import java.lang.reflect.Field;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Pose;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketUtil {

    private static final EntityDataAccessor<Pose> POSE_ACCESSOR;
    private static final Field ENTITY_DATA_FIELD;
    private static final Field CONNECTION_FIELD;

    static {
        try {
            POSE_ACCESSOR = EntityDataSerializers.POSE.createAccessor(6);
            ENTITY_DATA_FIELD = findField(net.minecraft.world.entity.Entity.class, SynchedEntityData.class);
            CONNECTION_FIELD = findField(ServerPlayer.class, ServerGamePacketListenerImpl.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize NMS fields", e);
        }
    }

    private static Field findField(Class<?> clazz, Class<?> fieldType) throws NoSuchFieldException {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType() == fieldType) {
                field.setAccessible(true);
                return field;
            }
        }
        throw new NoSuchFieldException("No field of type " + fieldType.getName() + " found in " + clazz.getName());
    }

    public static void sendPosePacket(Player player, Pose pose) {
        try {
            CraftPlayer craftPlayer = (CraftPlayer) player;
            ServerPlayer nmsPlayer = craftPlayer.getHandle();

            SynchedEntityData entityData = (SynchedEntityData) ENTITY_DATA_FIELD.get(nmsPlayer);
            entityData.set(POSE_ACCESSOR, pose);

            ServerGamePacketListenerImpl connection = (ServerGamePacketListenerImpl) CONNECTION_FIELD.get(nmsPlayer);
            ClientboundSetEntityDataPacket packet = new ClientboundSetEntityDataPacket(
                    nmsPlayer.getId(),
                    entityData.packDirty()
            );

            connection.send(packet);
        } catch (Exception e) {
            player.getServer().getLogger().severe("Failed to send pose packet for " + player.getName());
        }
    }
}
