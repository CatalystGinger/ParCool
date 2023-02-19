package com.alrex.parcool.common.registries;

import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.client.hud.HUDRegistry;
import com.alrex.parcool.client.hud.Position;
import com.alrex.parcool.client.hud.impl.RollDefermentHUD;
import com.alrex.parcool.client.hud.impl.StaminaHUDController;
import com.alrex.parcool.client.input.KeyRecorder;
import com.alrex.parcool.common.action.ActionProcessor;
import com.alrex.parcool.common.event.*;
import net.minecraftforge.eventbus.api.IEventBus;

public class EventBusForgeRegistry {
	public static void register(IEventBus bus) {
		bus.register(EventPlayerJump.class);
		bus.register(EventAttachCapability.class);
		bus.register(EventSendPermissions.class);
		bus.register(EventPlayerFall.class);
		bus.register(new ActionProcessor());
	}

	public static void registerClient(IEventBus bus) {
		bus.register(HUDRegistry.getInstance());
		HUDRegistry.getInstance().getHuds().add(
				new StaminaHUDController(
						new Position(
								ParCoolConfig.CONFIG_CLIENT.alignHorizontalStaminaHUD.get(),
								ParCoolConfig.CONFIG_CLIENT.alignVerticalStaminaHUD.get(),
								ParCoolConfig.CONFIG_CLIENT.marginHorizontalStaminaHUD.get(),
								ParCoolConfig.CONFIG_CLIENT.marginVerticalStaminaHUD.get()
						)
				));
		HUDRegistry.getInstance().getHuds().add(
				new RollDefermentHUD()
		);
		bus.register(KeyRecorder.class);
		bus.register(EventOpenSettingsParCool.class);
	}
}
