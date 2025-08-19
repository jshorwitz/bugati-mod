package com.yourname.yourmod.item;

import com.yourname.yourmod.YourMod;
import com.yourname.yourmod.entity.ModEntityTypes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = 
        DeferredRegister.create(ForgeRegistries.ITEMS, YourMod.MOD_ID);
    
    // === BUGATTI CARS - Available in Creative Mode ===
    
    // Bugatti Chiron - Flagship balanced car
    public static final RegistryObject<Item> BUGATTI_CAR = ITEMS.register("bugatti_car",
        () -> new BugattiCarItem(new Item.Properties()
            .stacksTo(16) // Allow multiple cars in creative
            .rarity(Rarity.EPIC)
            .tab(CreativeModeTab.TAB_TRANSPORTATION)));
    
    // Bugatti Veyron - Classic, fuel efficient
    public static final RegistryObject<Item> BUGATTI_VEYRON = ITEMS.register("bugatti_veyron",
        () -> new GenericBugattiItem(new Item.Properties()
            .stacksTo(16)
            .rarity(Rarity.RARE)
            .tab(CreativeModeTab.TAB_TRANSPORTATION),
            ModEntityTypes.BUGATTI_VEYRON, 1200, 800, 0.8F));
    
    // Bugatti Divo - Track-focused, best handling
    public static final RegistryObject<Item> BUGATTI_DIVO = ITEMS.register("bugatti_divo",
        () -> new GenericBugattiItem(new Item.Properties()
            .stacksTo(16)
            .rarity(Rarity.EPIC)
            .tab(CreativeModeTab.TAB_TRANSPORTATION),
            ModEntityTypes.BUGATTI_DIVO, 800, 400, 1.2F));
    
    // Bugatti Type 35 - Vintage racer, smallest
    public static final RegistryObject<Item> BUGATTI_TYPE35 = ITEMS.register("bugatti_type35",
        () -> new GenericBugattiItem(new Item.Properties()
            .stacksTo(16)
            .rarity(Rarity.UNCOMMON)
            .tab(CreativeModeTab.TAB_TRANSPORTATION),
            ModEntityTypes.BUGATTI_TYPE35, 600, 400, 1.4F));
    
    // Bugatti Centodieci - Ultra-rare, expensive
    public static final RegistryObject<Item> BUGATTI_CENTODIECI = ITEMS.register("bugatti_centodieci",
        () -> new GenericBugattiItem(new Item.Properties()
            .stacksTo(4) // More exclusive
            .rarity(Rarity.EPIC)
            .tab(CreativeModeTab.TAB_TRANSPORTATION),
            ModEntityTypes.BUGATTI_CENTODIECI, 1100, 550, 1.5F));
    
    // Bugatti Bolide - Track monster, fastest
    public static final RegistryObject<Item> BUGATTI_BOLIDE = ITEMS.register("bugatti_bolide",
        () -> new GenericBugattiItem(new Item.Properties()
            .stacksTo(8) // Limited track car
            .rarity(Rarity.EPIC)
            .tab(CreativeModeTab.TAB_TRANSPORTATION),
            ModEntityTypes.BUGATTI_BOLIDE, 700, 300, 2.0F));
    
    // === CAR PARTS for crafting ===
    public static final RegistryObject<Item> CAR_ENGINE = ITEMS.register("car_engine",
        () -> new Item(new Item.Properties()
            .stacksTo(16)
            .rarity(Rarity.RARE)
            .tab(CreativeModeTab.TAB_MATERIALS)));
            
    public static final RegistryObject<Item> CAR_WHEEL = ITEMS.register("car_wheel",
        () -> new Item(new Item.Properties()
            .stacksTo(16)
            .tab(CreativeModeTab.TAB_MATERIALS)));
            
    public static final RegistryObject<Item> CAR_CHASSIS = ITEMS.register("car_chassis",
        () -> new Item(new Item.Properties()
            .stacksTo(16)
            .rarity(Rarity.UNCOMMON)
            .tab(CreativeModeTab.TAB_MATERIALS)));
    
    // === SPAWN EGGS - Creative Mode Alternative ===
    public static final RegistryObject<ForgeSpawnEggItem> BUGATTI_SPAWN_EGG = ITEMS.register("bugatti_spawn_egg",
        () -> new ForgeSpawnEggItem(ModEntityTypes.BUGATTI_CAR, 0x2B6CC4, 0xC0C0C0, 
            new Item.Properties().tab(CreativeModeTab.TAB_MISC).rarity(Rarity.EPIC)));
    
    public static final RegistryObject<ForgeSpawnEggItem> BUGATTI_VEYRON_SPAWN_EGG = ITEMS.register("bugatti_veyron_spawn_egg",
        () -> new ForgeSpawnEggItem(ModEntityTypes.BUGATTI_VEYRON, 0x1a1a1a, 0x2B6CC4, 
            new Item.Properties().tab(CreativeModeTab.TAB_MISC).rarity(Rarity.RARE)));
    
    public static final RegistryObject<ForgeSpawnEggItem> BUGATTI_DIVO_SPAWN_EGG = ITEMS.register("bugatti_divo_spawn_egg",
        () -> new ForgeSpawnEggItem(ModEntityTypes.BUGATTI_DIVO, 0xFF4500, 0x000000, 
            new Item.Properties().tab(CreativeModeTab.TAB_MISC).rarity(Rarity.EPIC)));
    
    public static final RegistryObject<ForgeSpawnEggItem> BUGATTI_TYPE35_SPAWN_EGG = ITEMS.register("bugatti_type35_spawn_egg",
        () -> new ForgeSpawnEggItem(ModEntityTypes.BUGATTI_TYPE35, 0x4169E1, 0x8B4513, 
            new Item.Properties().tab(CreativeModeTab.TAB_MISC).rarity(Rarity.UNCOMMON)));
    
    public static final RegistryObject<ForgeSpawnEggItem> BUGATTI_CENTODIECI_SPAWN_EGG = ITEMS.register("bugatti_centodieci_spawn_egg",
        () -> new ForgeSpawnEggItem(ModEntityTypes.BUGATTI_CENTODIECI, 0xFFFFFF, 0x8A2BE2, 
            new Item.Properties().tab(CreativeModeTab.TAB_MISC).rarity(Rarity.EPIC)));
    
    public static final RegistryObject<ForgeSpawnEggItem> BUGATTI_BOLIDE_SPAWN_EGG = ITEMS.register("bugatti_bolide_spawn_egg",
        () -> new ForgeSpawnEggItem(ModEntityTypes.BUGATTI_BOLIDE, 0x000000, 0xFF0000, 
            new Item.Properties().tab(CreativeModeTab.TAB_MISC).rarity(Rarity.EPIC)));
    
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
