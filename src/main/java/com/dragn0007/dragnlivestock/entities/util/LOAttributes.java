package com.dragn0007.dragnlivestock.entities.util;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class LOAttributes {
    public static final Attribute ENDURANCE = register("generic.endurance", (new RangedAttribute("attribute.name.generic.endurance", (double)0.7F, 0.0D, 1024.0D)).setSyncable(true));

    private static Attribute register(String p_22291_, Attribute p_22292_) {
        return Registry.register(Registry.ATTRIBUTE, p_22291_, p_22292_);
    }
}
