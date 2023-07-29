package net.github.buiawpkgew1;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class ModKeyEvents {
    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
                    if (HabiModMain.isFlyKeyPressed()) {
                        // 开启飞行
                        client.player.getAbilities().allowFlying = true;
                    } else {
                        // 关闭飞行
                        client.player.getAbilities().allowFlying = false;
                        client.player.getAbilities().flying = false;
                    }
                    if (HabiModMain.isNightVisionKeyPressed()) {
                        // 开启夜视
                        client.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 200, 0, true, false));
                    } else {
                        // 移除夜视效果
                        client.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
                    }

                }

        );
    }
}

