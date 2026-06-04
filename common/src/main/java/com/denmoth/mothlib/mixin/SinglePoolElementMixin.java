package com.denmoth.mothlib.mixin;

import com.denmoth.mothlib.worldgen.processor.MothTreeMatchProcessor;
import com.denmoth.mothlib.worldgen.processor.RandomWheatProcessor;
import com.denmoth.mothlib.worldgen.processor.WindmillBearingProcessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SinglePoolElement.class)
public class SinglePoolElementMixin {
    @Inject(method = "getSettings", at = @At("RETURN"))
    private void mothlib$injectGlobalProcessors(Rotation rotation, BoundingBox boundingBox, boolean keepJigsaws, CallbackInfoReturnable<StructurePlaceSettings> cir) {
        StructurePlaceSettings settings = cir.getReturnValue();
        settings.addProcessor(MothTreeMatchProcessor.INSTANCE);
        settings.addProcessor(RandomWheatProcessor.INSTANCE);
        settings.addProcessor(WindmillBearingProcessor.INSTANCE);
    }
}
