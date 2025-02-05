package com.alrex.parcool.common.processor;

import com.alrex.parcool.ParCool;
import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.common.capability.IFastRunning;
import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.network.SyncFastRunningMessage;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import java.util.UUID;

public class FastRunningLogic {
	private static final String FAST_RUNNING_MODIFIER_NAME = "parCool.modifier.fastrunnning";
	private static final UUID FAST_RUNNING_MODIFIER_UUID = UUID.randomUUID();
	private static final AttributeModifier FAST_RUNNING_MODIFIER
			= new AttributeModifier(
			FAST_RUNNING_MODIFIER_UUID,
			FAST_RUNNING_MODIFIER_NAME,
			0.041,
			AttributeModifier.Operation.ADDITION
	);

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.side == LogicalSide.SERVER) return;
		PlayerEntity player = event.player;
		IFastRunning fastRunning = IFastRunning.get(player);
		IStamina stamina = IStamina.get(player);
		if (stamina == null || fastRunning == null) return;
		if (fastRunning.isFastRunning()) player.setSprinting(true);

		if (event.phase != TickEvent.Phase.START) return;
		if (!player.isUser() || !ParCoolConfig.CONFIG_CLIENT.ParCoolActivation.get()) return;

		ModifiableAttributeInstance attr = player.getAttribute(Attributes.field_233821_d_);
		if (attr == null) return;

		if (!ParCool.isActive()) {
			if (attr.hasModifier(FAST_RUNNING_MODIFIER)) attr.removeModifier(FAST_RUNNING_MODIFIER);
			return;
		}

		boolean oldFastRunning = fastRunning.isFastRunning();
		fastRunning.setFastRunning(fastRunning.canFastRunning(player));

		fastRunning.updateTime();

		if (fastRunning.isFastRunning() != oldFastRunning) SyncFastRunningMessage.sync(player);

		if (fastRunning.isFastRunning()) {
			if (!attr.hasModifier(FAST_RUNNING_MODIFIER)) attr.func_233769_c_(FAST_RUNNING_MODIFIER);
			stamina.consume(fastRunning.getStaminaConsumption());
		} else {
			if (attr.hasModifier(FAST_RUNNING_MODIFIER)) attr.removeModifier(FAST_RUNNING_MODIFIER);
		}
	}
}
