## Servers Who Do Not Want O-Animal Conversions
For servers who don't want automatic o-animal conversions,
download the 'dragnlivestock-common.toml' config file
and put it into your server config folder *before*
starting the server. Since LO converts animals by default,
starting the server before adding the custom config file
will replace loaded animals.

## Livestock Overhaul Custom Resources
Livestock Overhaul comes with a built-in custom texture option for its animals.
This means you can create your own textures, whilst still keeping the OG ones from the mod intact.
No coding from you required. It's as simple as a texture pack!

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
/summon dragnlivestock:o_horse_entity ~ ~ ~ {Variant_Texture:"base_colors:test_horsie.png"} 
```
**Custom Pattern Overlay**
```
/summon dragnlivestock:o_horse_entity ~ ~ ~ {Overlay_Texture:"pattern_overlays:test_pattern.png"} 
```
**Both**
```
/summon dragnlivestock:o_horse_entity ~ ~ ~ {Variant_Texture:"base_colors:my_custom_texture_name.png", Overlay_Texture:"pattern_overlays:my_custom_pattern_name.png"}
```

### Summon command legend

```
O-Horse
/summon dragnlivestock:o_horse_entity ~ ~ ~ {Texture:".png"}

O-Mule
/summon dragnlivestock:o_mule_entity ~ ~ ~ {Texture:".png"}

O-Donkey
/summon dragnlivestock:o_donkey_entity ~ ~ ~ {Texture:".png"}

O-Cow
/summon dragnlivestock:o_cow_entity ~ ~ ~ {Texture:".png"}

O-Chicken
/summon dragnlivestock:o_chicken_entity ~ ~ ~ {Texture:".png"}

O-Sheep
/summon dragnlivestock:o_sheep_entity ~ ~ ~ {Texture:".png"}

O-Pig
/summon dragnlivestock:o_pig_entity ~ ~ ~ {Texture:".png"}

O-Rabbit
/summon dragnlivestock:o_rabbit_entity ~ ~ ~ {Texture:".png"}

O-Llama
/summon dragnlivestock:o_llama_entity ~ ~ ~ {Texture:".png"}

O-Bee
/summon dragnlivestock:o_bee_entity ~ ~ ~ {Texture:".png"}\

O-Salmon
/summon dragnlivestock:o_salmon_entity ~ ~ ~ {Texture:".png"}

O-Cod
/summon dragnlivestock:o_cod_entity ~ ~ ~ {Texture:".png"}
```
