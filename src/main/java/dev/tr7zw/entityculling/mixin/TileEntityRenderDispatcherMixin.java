package dev.tr7zw.entityculling.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.entityculling.EntityCullingMod;
import dev.tr7zw.entityculling.ducks.CullableExt;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;

@Mixin(TileEntityRendererDispatcher.class)
public class TileEntityRenderDispatcherMixin {

    //? if = 1.12.2 {
/*
    @Inject(method = "Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;render(Lnet/minecraft/tileentity/TileEntity;DDDFIF)V", at = @At("HEAD"), cancellable = true)
    public void renderTileEntityAt(TileEntity blockEntity, double x, double y, double z, float partialTicks, int destroyStage, float p_192854_10_, CallbackInfo info) {
    *///? } else if = 1.8.9 {
    
    @Inject(method = "renderTileEntityAt(Lnet/minecraft/tileentity/TileEntity;DDDFI)V", at = @At("HEAD"), cancellable = true)
        public void renderTileEntityAt(TileEntity blockEntity, double x, double y,
            double z, float partialTicks, int destroyStage, CallbackInfo info) {
     
    //? } else {
    /*@Inject(method = "Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;renderTileEntityAt(Lnet/minecraft/tileentity/TileEntity;DDDF)V", at = @At("HEAD"), cancellable = true)
    public void renderTileEntityAt(TileEntity blockEntity, double p_147549_2_, double p_147549_4_, double p_147549_6_, float p_147549_8_, CallbackInfo info) {
    *///? }
        if (!((CullableExt) blockEntity).entityCulling$isForcedVisible() && ((CullableExt) blockEntity).entityCulling$isCulled()) {
            EntityCullingMod.instance.skippedBlockEntities++;
            info.cancel();
            return;
        }
        EntityCullingMod.instance.renderedBlockEntities++;
    }

}
