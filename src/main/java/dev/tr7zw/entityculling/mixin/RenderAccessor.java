package dev.tr7zw.entityculling.mixin;

import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

//? if >= 1.8.9 {

@Mixin(net.minecraft.client.renderer.entity.Render.class)
public interface RenderAccessor<T extends Entity> {
    @Invoker
    void callRenderName(T entity, double x, double y, double z);
}

//? } else {
/*
@Mixin(net.minecraft.client.renderer.entity.RendererLivingEntity.class)
public interface RenderAccessor {

    @Invoker
    void callPassSpecialRender(EntityLivingBase p_77033_1_, double p_77033_2_, double p_77033_4_, double p_77033_6_);

}
*///? }