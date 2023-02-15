package com.alrex.parcool.client.hud.impl;

import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.client.hud.AbstractHUD;
import com.alrex.parcool.client.hud.Position;
import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.capability.Parkourability;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class LightStaminaHUD extends AbstractHUD {
	private int oldValue = 0;
	private long lastChangedTick = 0;

	public LightStaminaHUD(Position pos) {
		super(pos);
	}

	@Override
	public void render(RenderGameOverlayEvent.Pre event, MatrixStack stack) {
		ClientPlayerEntity player = Minecraft.getInstance().player;
		if (player == null || player.isEyeInFluid(FluidTags.WATER)) return;
		if (player.isCreative()) return;

		IStamina stamina = IStamina.get(player);
		Parkourability parkourability = Parkourability.get(player);
		if (stamina == null || parkourability == null) return;

		if (ParCoolConfig.CONFIG_CLIENT.infiniteStamina.get() && parkourability.getActionInfo().isInfiniteStaminaPermitted())
			return;

		if (stamina.get() == 0) return;
		long gameTime = player.level.getGameTime();
		if (stamina.get() != oldValue) {
			lastChangedTick = gameTime;
		} else if (gameTime - lastChangedTick > 40) return;

		oldValue = stamina.get();
		float staminaScale = (float) stamina.get() / stamina.getActualMaxStamina();
		if (staminaScale < 0) staminaScale = 0;
		if (staminaScale > 1) staminaScale = 1;
		Minecraft mc = Minecraft.getInstance();
		int scaledWidth = mc.getWindow().getGuiScaledWidth();
		int scaledHeight = mc.getWindow().getGuiScaledHeight();

		int iconNumber = (int) Math.floor(staminaScale * 10);
		float iconPartial = (staminaScale * 10) - iconNumber;

		mc.getTextureManager().bind(StaminaHUD.STAMINA);
		int baseX = scaledWidth / 2 + 92;
		int y = scaledHeight - 49 + ParCoolConfig.CONFIG_CLIENT.offsetVerticalLightStaminaHUD.get();
		for (int i = 1; i <= 10; i++) {
			int x = baseX - i * 8 - 1;
			int textureX;
			if (iconNumber >= i || (iconNumber + 1 == i && iconPartial > 0.3)) {
				textureX = 0;
			} else if (iconNumber + 1 == i) {
				textureX = 8;
			} else break;
			if (stamina.isExhausted()) {
				textureX += 16;
			}
			AbstractHUD.blit(stack, x, y, textureX, 119f, 8, 9, 128, 128);
		}
	}
}
