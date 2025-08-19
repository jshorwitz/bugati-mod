package com.yourname.yourmod.item;

import com.yourname.yourmod.entity.BugattiCarEntity;
import com.yourname.yourmod.entity.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BugattiCarItem extends Item {
    
    public BugattiCarItem(Properties properties) {
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
        
        // Find suitable spawn position
        if (blockState.getCollisionShape(level, blockPos).isEmpty()) {
            spawnPos = blockPos;
        } else {
            spawnPos = blockPos.above();
        }
        
        // Check if there's enough space (3x2x2)
        if (!hasEnoughSpace(level, spawnPos)) {
            return InteractionResult.FAIL;
        }
        
        // Spawn the Bugatti
        BugattiCarEntity bugatti = ModEntityTypes.BUGATTI_CAR.get().create(level);
        if (bugatti != null) {
            bugatti.setPos(spawnPos.getX() + 0.5, spawnPos.getY() + 0.1, spawnPos.getZ() + 0.5);
            bugatti.setYRot(context.getPlayer() != null ? context.getPlayer().getYRot() : 0);
            
            // Start with full fuel in creative mode, half fuel otherwise
            if (context.getPlayer() != null && context.getPlayer().isCreative()) {
                bugatti.setFuelLevel(1000); // Full fuel for creative mode
            } else {
                bugatti.setFuelLevel(500); // Half fuel for survival mode
            }
            
            level.addFreshEntity(bugatti);
            level.playSound(null, spawnPos, SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            
            // Consume item
            ItemStack itemStack = context.getItemInHand();
            if (!context.getPlayer().isCreative()) {
                itemStack.shrink(1);
            }
        }
        
        return InteractionResult.SUCCESS;
    }
    
    private boolean hasEnoughSpace(Level level, BlockPos pos) {
        // Check 3x2x2 area for the car
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < 2; z++) {
                    BlockPos checkPos = pos.offset(x - 1, y, z);
                    if (!level.getBlockState(checkPos).isAir() && 
                        !level.getBlockState(checkPos).is(Blocks.GRASS) &&
                        !level.getBlockState(checkPos).is(Blocks.TALL_GRASS)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
