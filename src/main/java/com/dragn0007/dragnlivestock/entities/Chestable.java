package com.dragn0007.dragnlivestock.entities;

import net.minecraft.sounds.SoundSource;

import javax.annotation.Nullable;

public interface Chestable {
    boolean isChestable();

    void equipChest(@Nullable SoundSource soundSource);

    boolean isChested();
}
