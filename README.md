## Servers Who Do Not Want O-Animal Conversions
For servers who don't want automatic o-animal conversions,
download the 'livestock-overhaul-common.toml' config file
and put it into your server config folder *before*
starting the server. Since LO converts animals by default,
starting the server before adding the custom config file
will replace loaded animals.

## Known Issues
A list of any currently unfixed (or can't be fixed on our end) issues!

```
-Animals t-posing randomly. You have to fix this on your
end by turning off Entity Shadows on your options *and*
on your shaders. This is a Geckolib issue; we can't fix it on our end.

-Player not positioned correctly on horse 'Bow' emote. 
```

## Licensing & Copyrights

You do not have permission to redistribute this mod. 

**Code** licensing goes under **GNU**; which means you may use it under their standards (as seen in CODE_LICENSE.txt).
Please credit the original (this) source code!

**Asset** licensing goes under **All Rights Reserved**; 
you may ***not*** use models, textures, animations, or any other assets in your standalone mod(s) or other projects.
Altering and re-distribution/ sale of my assets are not permitted, under any circumstance.

Texture Packs and Livestock Overhaul-Dependent Addons are okay, and encouraged, provided they're free and for the public to use!


## Livestock Overhaul: Credits

EvangeliX - Code, Bug Fix Help (Thank you!)

Buu - Animations (Thank you!)

FrostedWyvern - Animations (Thank you!)

DragN0007 - Producer, Assets, Code

## Livestock Overhaul Custom Resources
Livestock Overhaul comes with a built-in custom texture option for its animals.
This means you can create your own textures, whilst still keeping the OG ones from the mod intact.
No coding from you required. It's as simple as a texture pack!

I would suggest putting the pack server-side and having your players always have it on. If they remove it
and re-enable it, and they still can't see the texture, they should restart their game.

### Make sure to set your resource pack up correctly.
Import the GeckoLib geo models into Blockbench and create your texture.

For example, import 'horse_overhauled.geo.json' into Blockbench, and create your new texture(s).

You must have an 'assets' folder within your texture pack, and a working MCMETA file. Example:

```
my_wondrous_texture_pack > assets & mcmeta > horse_textures > my_horsie.png
                                             ^ start with this folder when using the summon commands, NOT the pack name!
```

## Tester Template For Beginners

There's a 'readme_custom_textures_template.zip' texture pack added to this repository. You can use it to make your own
resource pack with ease. It comes with a pre-made "test_horsie.png" texture that you can spawn in to make sure it works.
If you do not want test_horsie (as beautiful as his big-red-self is), simply delete him. 

Examples for the Template Texture Pack:

**Custom Base Color**
```
/summon dragnlivestock:o_horse ~ ~ ~ {Variant_Texture:"base_colors:test_horsie.png"} 
```
**Custom Pattern Overlay**
```
/summon dragnlivestock:o_horse ~ ~ ~ {Overlay_Texture:"pattern_overlays:test_pattern.png"} 
```
**Both**
```
/summon dragnlivestock:o_horse ~ ~ ~ {Variant_Texture:"base_colors:my_custom_texture_name.png", Overlay_Texture:"pattern_overlays:my_custom_pattern_name.png"}
```

### Animals That Currently Have Dynamic Resources

```
O-Horse
/summon dragnlivestock:o_horse ~ ~ ~ {Texture:".png"}

O-Mule
/summon dragnlivestock:o_mule ~ ~ ~ {Texture:".png"}

O-Donkey
/summon dragnlivestock:o_donkey ~ ~ ~ {Texture:".png"}

O-Rabbit
/summon dragnlivestock:o_rabbit ~ ~ ~ {Texture:".png"}

Overworld Unicorn
/summon dragnlivestock:overworld_unicorn ~ ~ ~ {Texture:".png"}

Nether Unicorn
/summon dragnlivestock:nether_unicorn ~ ~ ~ {Texture:".png"}

End Unicorn
/summon dragnlivestock:end_unicorn ~ ~ ~ {Texture:".png"}
```
