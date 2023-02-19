package com.alrex.parcool.client.animation.impl;

import com.alrex.parcool.client.animation.Animator;
import com.alrex.parcool.client.animation.PlayerModelTransformer;
import com.alrex.parcool.common.action.impl.ClingToCliff;
import com.alrex.parcool.common.capability.Parkourability;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerEntity;

public class ClingToCliffAnimator extends Animator {
	@Override
	public boolean shouldRemoved(PlayerEntity player, Parkourability parkourability) {
		return !parkourability.get(ClingToCliff.class).isDoing();
	}

	@Override
	public void animatePost(PlayerEntity player, Parkourability parkourability, PlayerModelTransformer transformer) {
		double zAngle = 10 + 20 * Math.sin(24 * parkourability.get(ClingToCliff.class).getArmSwingAmount());
		transformer
				.rotateLeftArm(
						(float) Math.toRadians(-160f),
						0,
						(float) Math.toRadians(zAngle)
				)
				.rotateRightArm(
						(float) Math.toRadians(-160),
						0,
						(float) Math.toRadians(-zAngle)
				)
				.makeArmsNatural()
				.makeLegsLittleMoving();
		PlayerModel model = transformer.getRawModel();
		model.leftLeg.xRot /= 3;
		model.leftLeg.yRot /= 3;
		model.leftLeg.zRot /= 3;
		model.rightLeg.xRot /= 3;
		model.rightLeg.yRot /= 3;
		model.rightLeg.zRot /= 3;
		transformer
				.end();
	}
}
