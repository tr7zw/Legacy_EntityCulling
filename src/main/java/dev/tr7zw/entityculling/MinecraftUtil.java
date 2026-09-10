package dev.tr7zw.entityculling;

import net.minecraft.client.*;
import net.minecraft.client.entity.*;
import net.minecraft.world.*;

public class MinecraftUtil {

    public static World getWorld() {
        //? if = 1.12.2 {
        //return Minecraft.getMinecraft().world;
        //? } else {
        
        return Minecraft.getMinecraft().theWorld;
         
        //? }
    }

    public static EntityPlayerSP getPlayer() {
        //? if = 1.12.2 {
        //return Minecraft.getMinecraft().player;
        //? } else {
        
        return Minecraft.getMinecraft().thePlayer;
         
        //? }
    }

}
