package dev.tr7zw.entityculling.mixin;

import dev.tr7zw.entityculling.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

//? if ornithe {
/*@Mixin(net.minecraft.client.gui.GuiOverlayDebug.class)
public class DebugHudMixin {

    @Inject(method = "call", at = @At("RETURN"), cancellable = true)
    protected void call(CallbackInfoReturnable<List<String>> cr) {
        EntityCullingMod.instance.addOverlayInfo(cr.getReturnValue());
    }

}
*///? } else {

@Mixin(net.minecraft.client.Minecraft.class)
public class DebugHudMixin {

}

//? }
