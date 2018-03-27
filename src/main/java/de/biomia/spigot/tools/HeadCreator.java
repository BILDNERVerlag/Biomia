package de.biomia.spigot.tools;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.biomia.spigot.OfflineBiomiaPlayer;
import de.biomia.universal.SkinValue;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.TileEntitySkull;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.UUID;

public class HeadCreator {

    public static ItemStack getSkull(OfflineBiomiaPlayer name) {

        String url = "http://textures.minecraft.net/texture/" + SkinValue.getSkin(name);

        ItemStack head = new ItemStack(Material.SKULL_ITEM, (short) 3);
        if (url.isEmpty()) return head;

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = getNonPlayerProfile(url);
        Field profileField;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

    public static void setSkullUrl(String code, Block block) {

        String url = "http://textures.minecraft.net/texture/" + code;

        block.setType(Material.SKULL);
        Skull skullData = (Skull) block.getState();
        skullData.setSkullType(SkullType.PLAYER);

        TileEntitySkull skullTile = (TileEntitySkull) ((CraftWorld) block.getWorld()).getHandle().getTileEntity(new BlockPosition(block.getX(), block.getY(), block.getZ()));
        if (skullTile == null)
            return;
        skullTile.setGameProfile(getNonPlayerProfile(url));
        skullTile.setRotation(new Random().nextInt(360));
        block.setData((byte) 1);
        block.getState().update(true);
    }

    private static GameProfile getNonPlayerProfile(String skinURL) {
        GameProfile newSkinProfile = new GameProfile(UUID.randomUUID(), null);
        newSkinProfile.getProperties().put("textures", new Property("textures", Base64Coder.encodeString("{textures:{SKIN:{url:\"" + skinURL + "\"}}}")));
        return newSkinProfile;
    }

}
