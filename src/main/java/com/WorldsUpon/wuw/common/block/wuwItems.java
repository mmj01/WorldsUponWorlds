package com.WorldsUpon.wuw.common.block;


import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class wuwItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("wuw");

    public static final DeferredItem<Item> FLOOR_BASIC = ITEMS.registerSimpleItem("floorbasicitem");
    public static final DeferredItem<Item> SUPERWALL = ITEMS.registerSimpleItem("superwallitem");
    public static final DeferredItem<Item> SEC_A_ENDFLOOR = ITEMS.registerSimpleItem("secaendflooritem");
    public static final DeferredItem<Item> COAL_ORE = ITEMS.registerSimpleItem("coaloreblock");
    public static final DeferredItem<Item> IRON_ORE = ITEMS.registerSimpleItem("coaloreblock");
    public static final DeferredItem<Item> DIAMOND_ORE = ITEMS.registerSimpleItem("coaloreblock");
    public static final DeferredItem<Item> REDSTONE_ORE = ITEMS.registerSimpleItem("coaloreblock");
    public static final DeferredItem<Item> LAPIS_ORE = ITEMS.registerSimpleItem("coaloreblock");
    public static final DeferredItem<Item> GOLD_ORE = ITEMS.registerSimpleItem("coaloreblock");
    public static final DeferredItem<Item> COPPER_ORE = ITEMS.registerSimpleItem("copperoreblock");
    public static final DeferredItem<Item> EMERALD_ORE = ITEMS.registerSimpleItem("emeraldoreblock");


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
