package dev.tr7zw.entityculling.mixin;

import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.*;

import dev.tr7zw.entityculling.EntityCullingMod;
import dev.tr7zw.entityculling.ducks.CullableExt;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;

@Mixin(RenderManager.class)
public abstract class WorldRendererMixin {

    //? if >= 1.8.9 {
    
    @Shadow
    public abstract <T extends Entity> Render<T> getEntityRenderObject(Entity entityIn);
     
    //? } else {
/*
    @Shadow
    public abstract Render getEntityRenderObject(Entity entityIn);
    *///? }

    //? if = 1.12.2 {
    /*@Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/renderer/entity/RenderManager;renderEntity(Lnet/minecraft/entity/Entity;DDDFFZ)V", cancellable = true)
    public void doRenderEntity(Entity entity, double x, double y, double z, float yaw, float partialTicks, boolean p_188391_10_, CallbackInfo info) {
    *///? } else {

    //? if >= 1.8.9 {
    
    @Inject(at = @At("HEAD"), method = "doRenderEntity", cancellable = true)
     
    //? } else {
    //@Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/renderer/entity/RenderManager;func_147939_a(Lnet/minecraft/entity/Entity;DDDFFZ)Z", cancellable = true)
    //? }
    public void doRenderEntity(Entity entity, double x, double y, double z,
            float entityYaw, float partialTicks, boolean p_147939_10_, CallbackInfoReturnable<Boolean> info) {
    
    //? }
        CullableExt cullable = (CullableExt) entity;
        if (!cullable.entityCulling$isForcedVisible() && cullable.entityCulling$isCulled()) {
            if (EntityCullingMod.instance.config.renderNametagsThroughWalls) {
                //? if >= 1.8.9 {
                
                dev.tr7zw.entityculling.RenderHook.handle(getEntityRenderObject(entity), entity, x, y, z);
                 
                //? } else {
/*
                if (entity instanceof EntityLivingBase && getEntityRenderObject(entity) instanceof RenderAccessor) {
                    RenderAccessor livingEntity = (RenderAccessor) getEntityRenderObject(entity);
                    livingEntity.callPassSpecialRender(((EntityLivingBase) entity), x, y, z);
                }
                *///? }
            }
            EntityCullingMod.instance.skippedEntities++;
            info.cancel();
            return;
        }
        EntityCullingMod.instance.renderedEntities++;
        cullable.entityCulling$setOutOfCamera(false);
    }

}
