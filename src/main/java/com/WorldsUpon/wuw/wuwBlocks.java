package com.WorldsUpon.wuw;

import com.WorldsUpon.wuw.common.block.floorbasic;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class wuwBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks("wuw");
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("wuw");



    public static final DeferredBlock<floorbasic> FLOOR_BASIC = BLOCKS.register("floorbasic",
            () -> new floorbasic(BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK)));

    public static final DeferredBlock<floorbasic> SECAENDFLOOR = BLOCKS.register("secaendfloor",
            () -> new floorbasic(BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK)));

    public static final DeferredBlock<Block> SUPERWALL = BLOCKS.registerSimpleBlock("superwall",
            BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK));


    public static final DeferredItem<BlockItem> FLOOR_BASIC_ITEM = ITEMS.registerSimpleBlockItem("floorbasicitem", FLOOR_BASIC);
    public static final DeferredItem<BlockItem> SUPERWALL_ITEM = ITEMS.registerSimpleBlockItem("superwallitem", SUPERWALL);
    public static final DeferredItem<BlockItem> SECAENDFLOOR_ITEM = ITEMS.registerSimpleBlockItem("secaendflooritem", SECAENDFLOOR);




    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }
}
