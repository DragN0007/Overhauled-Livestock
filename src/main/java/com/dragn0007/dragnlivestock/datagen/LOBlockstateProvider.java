package com.dragn0007.dragnlivestock.datagen;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class LOBlockstateProvider extends BlockStateProvider {
    public LOBlockstateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, LivestockOverhaul.MODID, exFileHelper);
    }
    @Override
    protected void registerStatesAndModels() {
    }
}
