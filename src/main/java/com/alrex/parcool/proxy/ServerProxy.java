package com.alrex.parcool.proxy;

import com.alrex.parcool.common.network.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@OnlyIn(Dist.DEDICATED_SERVER)
public class ServerProxy extends CommonProxy {
	@Override
	public void registerMessages(SimpleChannel instance) {
		instance.registerMessage(
				0,
				ResetFallDistanceMessage.class,
				ResetFallDistanceMessage::encode,
				ResetFallDistanceMessage::decode,
				ResetFallDistanceMessage::handle
		);
		instance.registerMessage(
				1,
				SetActionPossibilityMessage.class,
				SetActionPossibilityMessage::encode,
				SetActionPossibilityMessage::decode,
				SetActionPossibilityMessage::handle
		);
		instance.registerMessage(
				2,
				ShowActionPossibilityMessage.class,
				ShowActionPossibilityMessage::encode,
				ShowActionPossibilityMessage::decode,
				ShowActionPossibilityMessage::handle
		);
		instance.registerMessage(
				3,
				StartRollMessage.class,
				StartRollMessage::encode,
				StartRollMessage::decode,
				StartRollMessage::handleServer
		);
		instance.registerMessage(
				4,
				SyncCatLeapMessage.class,
				SyncCatLeapMessage::encode,
				SyncCatLeapMessage::decode,
				SyncCatLeapMessage::handleServer
		);
		instance.registerMessage(
				5,
				SyncCrawlMessage.class,
				SyncCrawlMessage::encode,
				SyncCrawlMessage::decode,
				SyncCrawlMessage::handleServer
		);
		instance.registerMessage(
				6,
				SyncDodgeMessage.class,
				SyncDodgeMessage::encode,
				SyncDodgeMessage::decode,
				SyncDodgeMessage::handleServer
		);
		instance.registerMessage(
				7,
				SyncFastRunningMessage.class,
				SyncFastRunningMessage::encode,
				SyncFastRunningMessage::decode,
				SyncFastRunningMessage::handleServer
		);
		instance.registerMessage(
				8,
				SyncGrabCliffMessage.class,
				SyncGrabCliffMessage::encode,
				SyncGrabCliffMessage::decode,
				SyncGrabCliffMessage::handleServer
		);
		instance.registerMessage(
				9,
				SyncRollReadyMessage.class,
				SyncRollReadyMessage::encode,
				SyncRollReadyMessage::decode,
				SyncRollReadyMessage::handleServer
		);
		instance.registerMessage(
				10,
				SyncStaminaMessage.class,
				SyncStaminaMessage::encode,
				SyncStaminaMessage::decode,
				SyncStaminaMessage::handleServer
		);
	}
}
