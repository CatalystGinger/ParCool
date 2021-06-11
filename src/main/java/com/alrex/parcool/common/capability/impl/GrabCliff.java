package com.alrex.parcool.common.capability.impl;

import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.client.input.KeyBindings;
import com.alrex.parcool.client.input.KeyRecorder;
import com.alrex.parcool.common.capability.IGrabCliff;
import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.utilities.PlayerUtils;
import com.alrex.parcool.utilities.WorldUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GrabCliff implements IGrabCliff {
	private boolean grabbing = false;
	private int grabbingTime = 0;
	private int notGrabbingTime = 0;

	@SideOnly(Side.CLIENT)
	@Override
	public boolean canGrabCliff(EntityPlayer player) {
		IStamina stamina = IStamina.get(player);
		if (stamina == null) return false;

		double ySpeed = PlayerUtils.getVelocity(player).y;
		return !stamina.isExhausted() && ySpeed < 0.2 && ParCoolConfig.client.canGrabCliff && KeyBindings.getKeyGrabWall().isKeyDown() && player.getHeldItemMainhand().isEmpty() && player.getHeldItemOffhand().isEmpty() && WorldUtil.existsGrabbableWall(player);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean canJumpOnCliff(EntityPlayer player) {
		return grabbing && ParCoolConfig.client.canGrabCliff && grabbingTime > 3 && KeyRecorder.keyJumpState.isPressed();
	}

	@Override
	public boolean isGrabbing() {
		return grabbing;
	}

	@Override
	public void setGrabbing(boolean grabbing) {
		this.grabbing = grabbing;
	}


	@Override
	public int getGrabbingTime() {
		return grabbingTime;
	}

	@Override
	public int getNotGrabbingTime() {
		return notGrabbingTime;
	}

	@Override
	public void updateTime() {
		if (grabbing) {
			grabbingTime++;
			notGrabbingTime = 0;
		} else {
			notGrabbingTime++;
			grabbingTime = 0;
		}
	}

	@Override
	public int getStaminaConsumptionGrab() {
		return 4;
	}

	@Override
	public int getStaminaConsumptionClimbUp() {
		return 200;
	}
}
