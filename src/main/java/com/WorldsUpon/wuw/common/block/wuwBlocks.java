package com.WorldsUpon.wuw.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class wuwBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks("wuw");




    public static final DeferredBlock<floorbasic> FLOOR_BASIC = BLOCKS.register("floorbasic",
            () -> new floorbasic(BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK)));
    public static final DeferredBlock<floorbasic> SEC_A_ENDFLOOR = BLOCKS.register("secaendfloor",
            () -> new floorbasic(BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK)));
    public static final DeferredBlock<Block> SUPERWALL = BLOCKS.registerSimpleBlock("superwall",
            BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK));
    public static final DeferredBlock<Block> COAL_ORE = BLOCKS.registerSimpleBlock("coalore",
            BlockBehaviour.Properties.ofFullCopy(Blocks.COAL_ORE));
    public static final DeferredBlock<Block> DIAMOND_ORE = BLOCKS.registerSimpleBlock("diamondore",
            BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN));
    public static final DeferredBlock<Block> IRON_ORE = BLOCKS.registerSimpleBlock("ironore",
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE));
    public static final DeferredBlock<Block> REDSTONE_ORE = BLOCKS.registerSimpleBlock("redstoneore",
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE));
    public static final DeferredBlock<Block> GOLD_ORE = BLOCKS.registerSimpleBlock("goldore",
            BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_ORE));
    public static final DeferredBlock<Block> LAPIS_ORE = BLOCKS.registerSimpleBlock("lapisore",
            BlockBehaviour.Properties.ofFullCopy(Blocks.LAPIS_ORE));
    public static final DeferredBlock<Block> EMERALD_ORE = BLOCKS.registerSimpleBlock("emeraldore",
            BlockBehaviour.Properties.ofFullCopy(Blocks.EMERALD_ORE));
    public static final DeferredBlock<Block> COPPER_ORE = BLOCKS.registerSimpleBlock("copperore",
            BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_ORE));
    public static final DeferredBlock<Block> ALUMINUM = BLOCKS.registerSimpleBlock("aluminumore",
            BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(22.0F, 600.0F));
    public static final DeferredBlock<Block> TITANIUM = BLOCKS.registerSimpleBlock("titaniumore",
            BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(22.0F, 600.0F));
    public static final DeferredBlock<Block> ALLOY = BLOCKS.registerSimpleBlock("alloy",
            BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(55.0F, 1200.0F));
    public static final DeferredBlock<Block> GRANITE = BLOCKS.registerSimpleBlock("granite",
            BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE));
    public static final DeferredBlock<Block> BASALT = BLOCKS.registerSimpleBlock("basalt",
            BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK));



    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
