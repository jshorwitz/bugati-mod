package com.yourname.yourmod.item;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.registries.RegistryObject;
import java.util.function.Supplier;

public class GenericBugattiItem extends Item {
    private final RegistryObject<?> entityTypeSupplier;
    private final int creativeFuel;
    private final int survivalFuel;
    private final float soundPitch;
    
    public GenericBugattiItem(Properties properties, RegistryObject<?> entityType, 
                             int creativeFuel, int survivalFuel, float soundPitch) {
        super(properties);
        this.entityTypeSupplier = entityType;
        this.creativeFuel = creativeFuel;
        this.survivalFuel = survivalFuel;
        this.soundPitch = soundPitch;
    }
    
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        BlockPos spawnPos = blockState.getCollisionShape(level, blockPos).isEmpty() ? blockPos : blockPos.above();
        
        if (!hasEnoughSpace(level, spawnPos)) {
            return InteractionResult.FAIL;
        }
        
        // Spawn the car
        EntityType<? extends Animal> entityType = (EntityType<? extends Animal>) entityTypeSupplier.get();
        Entity entity = entityType.create(level);
        if (entity instanceof Animal car) {
            car.setPos(spawnPos.getX() + 0.5, spawnPos.getY() + 0.1, spawnPos.getZ() + 0.5);
            car.setYRot(context.getPlayer() != null ? context.getPlayer().getYRot() : 0);
            
            // Set fuel based on creative/survival mode
            if (context.getPlayer() != null && context.getPlayer().isCreative()) {
                setFuel(car, creativeFuel);
            } else {
                setFuel(car, survivalFuel);
            }
            
            level.addFreshEntity(car);
            level.playSound(null, spawnPos, SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 1.0F, soundPitch);
            
            ItemStack itemStack = context.getItemInHand();
            if (context.getPlayer() == null || !context.getPlayer().isCreative()) {
                itemStack.shrink(1);
            }
        }
        
        return InteractionResult.SUCCESS;
    }
    
    private void setFuel(Animal car, int fuel) {
        try {
            var fuelMethod = car.getClass().getMethod("setFuelLevel", int.class);
            fuelMethod.invoke(car, fuel);
        } catch (Exception e) {
            // Fuel setting failed - car will start with default fuel
        }
    }
    
    private boolean hasEnoughSpace(Level level, BlockPos pos) {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < 2; z++) {
                    BlockPos checkPos = pos.offset(x - 1, y, z);
                    BlockState state = level.getBlockState(checkPos);
                    if (!state.isAir() && !state.getBlock().toString().contains("grass")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
