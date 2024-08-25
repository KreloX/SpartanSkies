package krelox.spartanskies.mixin;

import com.legacy.blue_skies.asm_hooks.PlayerHooks;
import krelox.spartanskies.SpartanSkies;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerHooks.class)
public class PlayerHooksMixin {
    @Redirect(
            method = "isNerfableTool",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z")
    )
    private static boolean spartanskies_isNerfableTool(String modId, Object namespace) {
        return modId.equals(namespace) || SpartanSkies.MODID.equals(namespace);
    }
}
