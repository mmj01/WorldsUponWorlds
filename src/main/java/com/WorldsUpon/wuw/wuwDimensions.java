package com.WorldsUpon.wuw;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class wuwDimensions {
    public static final ResourceKey<Level> FILLER_SECTOR_A = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath("wuw", "filler_sector_a")
    );
    public static final ResourceKey<DimensionType> FILLER = ResourceKey.create(
            Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath("wuw", "filler")
    );
    public static final ResourceKey<Level> TOP_SECTOR_A = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath("wuw", "top_sector_a")
    );
    public static final ResourceKey<Level> END_SECTOR_A = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath("wuw", "end_sector_a")
    );
    public static final ResourceKey<Level> TOP_SECTOR_B = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath("wuw", "top_sector_b")
    );
}

