package com.yourname.yourmod.item;

import com.yourname.yourmod.entity.BugattiVeyronEntity;
import com.yourname.yourmod.entity.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BugattiVeyronItem extends Item {
    
    public BugattiVeyronItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        BlockPos spawnPos;
        
        if (blockState.getCollisionShape(level, blockPos).isEmpty()) {
            spawnPos = blockPos;
        } else {
            spawnPos = blockPos.above();
        }
        
        // Check space for Veyron (slightly smaller than Chiron)
        if (!hasEnoughSpace(level, spawnPos)) {
            return InteractionResult.FAIL;
        }
        
        // Spawn the Veyron
        BugattiVeyronEntity veyron = ModEntityTypes.BUGATTI_VEYRON.get().create(level);
        if (veyron != null) {
            veyron.setPos(spawnPos.getX() + 0.5, spawnPos.getY() + 0.1, spawnPos.getZ() + 0.5);
            veyron.setYRot(context.getPlayer() != null ? context.getPlayer().getYRot() : 0);
            
            // Full fuel in creative, more fuel in survival (Veyron is efficient)
            if (context.getPlayer() != null && context.getPlayer().isCreative()) {
                veyron.setFuelLevel(1200); // Full fuel for creative mode
            } else {
                veyron.setFuelLevel(800); // Good fuel for survival mode
            }
            
            level.addFreshEntity(veyron);
            level.playSound(null, spawnPos, SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 0.8F, 0.8F);
            
            ItemStack itemStack = context.getItemInHand();
            if (!context.getPlayer().isCreative()) {
                itemStack.shrink(1);
            }
        }
        
        return InteractionResult.SUCCESS;
    }
    
    private boolean hasEnoughSpace(Level level, BlockPos pos) {
        // Check slightly smaller area for Veyron
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < 2; z++) {
                    BlockPos checkPos = pos.offset(x - 1, y, z);
                    if (!level.getBlockState(checkPos).isAir() && 
                        !level.getBlockState(checkPos).getBlock().toString().contains("grass")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
