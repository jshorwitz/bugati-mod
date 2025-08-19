package com.yourname.yourmod.entity;

import com.yourname.yourmod.YourMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = 
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, YourMod.MOD_ID);
    
    // Bugatti Chiron - Flagship balanced car
    public static final RegistryObject<EntityType<BugattiCarEntity>> BUGATTI_CAR = ENTITY_TYPES.register("bugatti_car",
        () -> EntityType.Builder.of(BugattiCarEntity::new, MobCategory.MISC)
            .sized(2.5F, 1.2F) // Width x Height
            .clientTrackingRange(10)
            .updateInterval(3)
            .build("bugatti_car"));
    
    // Bugatti Veyron - Classic, fuel efficient
    public static final RegistryObject<EntityType<BugattiVeyronEntity>> BUGATTI_VEYRON = ENTITY_TYPES.register("bugatti_veyron",
        () -> EntityType.Builder.of(BugattiVeyronEntity::new, MobCategory.MISC)
            .sized(2.4F, 1.1F)
            .clientTrackingRange(10)
            .updateInterval(3)
            .build("bugatti_veyron"));
    
    // Bugatti Divo - Track-focused, best handling
    public static final RegistryObject<EntityType<BugattiDivoEntity>> BUGATTI_DIVO = ENTITY_TYPES.register("bugatti_divo",
        () -> EntityType.Builder.of(BugattiDivoEntity::new, MobCategory.MISC)
            .sized(2.3F, 1.0F) // Lower and narrower (track car)
            .clientTrackingRange(12)
            .updateInterval(2)
            .build("bugatti_divo"));
    
    // Bugatti Type 35 - Vintage racer, smallest
    public static final RegistryObject<EntityType<BugattiType35Entity>> BUGATTI_TYPE35 = ENTITY_TYPES.register("bugatti_type35",
        () -> EntityType.Builder.of(BugattiType35Entity::new, MobCategory.MISC)
            .sized(1.8F, 0.9F) // Smallest car
            .clientTrackingRange(8)
            .updateInterval(3)
            .build("bugatti_type35"));
    
    // Bugatti Centodieci - Ultra-rare, expensive
    public static final RegistryObject<EntityType<BugattiCentodieciEntity>> BUGATTI_CENTODIECI = ENTITY_TYPES.register("bugatti_centodieci",
        () -> EntityType.Builder.of(BugattiCentodieciEntity::new, MobCategory.MISC)
            .sized(2.6F, 1.3F) // Larger luxury car
            .clientTrackingRange(12)
            .updateInterval(2)
            .build("bugatti_centodieci"));
    
    // Bugatti Bolide - Track monster, fastest
    public static final RegistryObject<EntityType<BugattiBolideEntity>> BUGATTI_BOLIDE = ENTITY_TYPES.register("bugatti_bolide",
        () -> EntityType.Builder.of(BugattiBolideEntity::new, MobCategory.MISC)
            .sized(2.8F, 0.8F) // Wide and low (race car)
            .clientTrackingRange(15)
            .updateInterval(1)
            .build("bugatti_bolide"));
    
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
