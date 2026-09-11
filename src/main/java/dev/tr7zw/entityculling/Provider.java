package dev.tr7zw.entityculling;

import com.logisticscraft.occlusionculling.DataProvider;

import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.*;
import net.minecraft.world.World;
//? if = 1.12.2 {
/*
import net.minecraft.util.math.*;
 
*///? }

public class Provider implements DataProvider {

    private final Minecraft client = Minecraft.getMinecraft();
    //? if >= 1.8.9 {
    
    private final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
     
    //? }
    private World world = null;

    @Override
    public boolean prepareChunk(int chunkX, int chunkZ) {
        world = MinecraftUtil.getWorld();
        return world != null;
    }

    @Override
    public boolean isOpaqueFullCube(int x, int y, int z) {
        //? if = 1.12.2 {
        /*Block block = world.getBlockState(pos.setPos(x, y, z)).getBlock();
        return block.isOpaqueCube(block.getDefaultState());
        *///? } else if = 1.8.9 {
        
        return world.getBlockState(pos.set(x, y, z)).getBlock().isOpaqueCube();
        
        //? } else {
        //return world.getBlock(x, y, z).isOpaqueCube();
        //? }
    }

    @Override
    public void cleanup() {
        world = null;
    }

}
