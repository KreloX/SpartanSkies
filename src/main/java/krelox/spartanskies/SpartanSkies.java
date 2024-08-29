package krelox.spartanskies;

import com.google.common.collect.ImmutableMap;
import com.legacy.blue_skies.data.objects.tags.SkiesItemTags;
import com.legacy.blue_skies.items.util.SkiesItemTier;
import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import com.oblivioussp.spartanweaponry.data.recipe.TagCookingRecipeBuilder;
import krelox.spartantoolkit.*;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@Mod(SpartanSkies.MODID)
public class SpartanSkies extends SpartanAddon {
    public static final String MODID = "spartanskies";

    public static final WeaponMap WEAPONS = new WeaponMap();
    public static final DeferredRegister<Item> ITEMS = itemRegister(MODID);
    public static final DeferredRegister<WeaponTrait> TRAITS = traitRegister(MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = tabRegister(MODID);

    // Traits
    public static final RegistryObject<WeaponTrait> SCORCHING = registerTrait(TRAITS,
            new BetterWeaponTrait("scorching", MODID, WeaponTrait.TraitQuality.POSITIVE) {
                public String getDescription() {
                    return String.format(Locale.US, "Sets foes on fire for %d seconds", (int) magnitude);
                }

                public void onHitEntity(WeaponMaterial material, ItemStack stack, LivingEntity target, LivingEntity attacker, Entity projectile) {
                    target.setSecondsOnFire(((int) magnitude));
                }
            }.setMagnitude(3).setUniversal());

    // Materials
    public static final List<SpartanMaterial> MATERIALS = new ArrayList<>();

    public static final SpartanMaterial PYROPE = material(SkiesItemTier.PYROPE, SkiesItemTags.PYROPE);
    public static final SpartanMaterial AQUITE = material(SkiesItemTier.AQUITE, SkiesItemTags.AQUITE);
    public static final SpartanMaterial DIOPSIDE = material(SkiesItemTier.DIOPSIDE, SkiesItemTags.DIOPSIDE).setAttackSpeedModifier(-0.4);
    public static final SpartanMaterial CHAROITE = material(SkiesItemTier.CHAROITE, SkiesItemTags.CHAROITE);
    public static final SpartanMaterial HORIZONITE = material(SkiesItemTier.HORIZONITE, SkiesItemTags.HORIZONITE, SCORCHING);

    @SafeVarargs
    private static SpartanMaterial material(SkiesItemTier tier, TagKey<Item> repairTag, RegistryObject<WeaponTrait>... traits) {
        SpartanMaterial material = new SpartanMaterial(tier.name().toLowerCase(), MODID, tier, repairTag, traits);
        MATERIALS.add(material);
        return material;
    }

    @SuppressWarnings("unused")
    public static final RegistryObject<CreativeModeTab> SPARTAN_SKIES_TAB = registerTab(TABS, MODID,
            () -> WEAPONS.get(AQUITE, WeaponType.GREATSWORD).get(),
            (parameters, output) -> ITEMS.getEntries().forEach(item -> output.accept(item.get())));

    public SpartanSkies() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        registerSpartanWeapons(ITEMS);
        ITEMS.register(bus);
        TRAITS.register(bus);
        TABS.register(bus);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        super.buildCraftingRecipes(consumer);
        ImmutableMap.Builder<String, Item> mapBuilder = new ImmutableMap.Builder<>();
        WEAPONS.forEach((pair, weapon) -> {
            if (pair.first().equals(HORIZONITE)) {
                mapBuilder.put(pair.second().name().toLowerCase(), weapon.get());
            }
        });
        ImmutableMap<String, Item> ingredientMap = mapBuilder.build();
        TagCookingRecipeBuilder.smelting(ingredientMap, RecipeCategory.MISC, SkiesItemTags.HORIZONITE_NUGGETS, 0.1f, 200)
                .save(consumer, new ResourceLocation(MODID, HORIZONITE.getMaterialName() + "_nugget_from_smelting_" + HORIZONITE.getMaterialName() + "_weapons"));
        TagCookingRecipeBuilder.blasting(ingredientMap, RecipeCategory.MISC, SkiesItemTags.HORIZONITE_NUGGETS, 0.1f, 100)
                .save(consumer, new ResourceLocation(MODID, HORIZONITE.getMaterialName() + "_nugget_from_blasting_" + HORIZONITE.getMaterialName() + "_weapons"));
    }

    @Override
    public String modid() {
        return MODID;
    }

    @Override
    public List<SpartanMaterial> getMaterials() {
        return MATERIALS;
    }

    @Override
    public WeaponMap getWeaponMap() {
        return WEAPONS;
    }
}
