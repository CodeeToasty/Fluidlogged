package de.leximon.fluidlogged.mixin;

import de.leximon.fluidlogged.Fluidlogged;
import de.leximon.fluidlogged.core.FluidloggedConfig;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Block.class)
public class BlockMixin
{
    @Shadow private BlockState defaultBlockState;

    @Redirect(method = "registerDefaultState", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/Block;defaultBlockState:Lnet/minecraft/world/level/block/state/BlockState;", opcode = Opcodes.PUTFIELD))
    private void injectDefaultState(Block instance, BlockState value) {
        if(FluidloggedConfig.getCompatibilityMode())
            defaultBlockState = Fluidlogged.isVanillaWaterloggable(instance) && value.hasProperty(BlockStateProperties.WATERLOGGED)
                    ? value.setValue(Fluidlogged.FLUIDLOGGED, "")
                    : value;
        else
        defaultBlockState = value.hasProperty(BlockStateProperties.WATERLOGGED) ? value.setValue(Fluidlogged.FLUIDLOGGED, "") : value;
    }
}
