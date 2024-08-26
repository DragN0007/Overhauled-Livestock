package com.dragn0007.dragnlivestock.datagen.biglooter;

import com.dragn0007.dragnlivestock.blocks.LOBlocks;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class LOBlockLootTables extends BlockLoot {
    @Override
    protected void addTables() {
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return LOBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
