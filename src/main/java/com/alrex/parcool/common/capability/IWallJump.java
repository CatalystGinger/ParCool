package com.alrex.parcool.common.capability;

import com.alrex.parcool.ParCool;
import com.alrex.parcool.common.capability.impl.WallJump;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IWallJump {
	public double getJumpPower();

	//only in Client
	public boolean canWallJump(ClientPlayerEntity player);

	//only in Client
	public Vector3d getJumpDirection(ClientPlayerEntity player);

	public int getStaminaConsumption();

	public static class WallJumpStorage implements Capability.IStorage<IWallJump> {
		@Override
		public void readNBT(Capability<IWallJump> capability, IWallJump instance, Direction side, INBT nbt) {
		}

		@Nullable
		@Override
		public INBT writeNBT(Capability<IWallJump> capability, IWallJump instance, Direction side) {
			return null;
		}
	}

	public static IWallJump get(PlayerEntity entity) {
		LazyOptional<IWallJump> optional = entity.getCapability(WallJumpProvider.WALL_JUMP_CAPABILITY);
		if (!optional.isPresent()) return null;
		return optional.orElseThrow(IllegalStateException::new);
	}

	public static class WallJumpProvider implements ICapabilityProvider {
		@CapabilityInject(IWallJump.class)
		public static final Capability<IWallJump> WALL_JUMP_CAPABILITY = null;
		public static final ResourceLocation CAPABILITY_LOCATION = new ResourceLocation(ParCool.MOD_ID, "capability.parcool.walljump");

		private LazyOptional<IWallJump> instance = LazyOptional.of(WALL_JUMP_CAPABILITY::getDefaultInstance);

		@Nonnull
		@Override
		public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
			return cap == WALL_JUMP_CAPABILITY ? instance.cast() : LazyOptional.empty();
		}

		@Nonnull
		@Override
		public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
			return cap == WALL_JUMP_CAPABILITY ? instance.cast() : LazyOptional.empty();
		}
	}

	public static class WallJumpRegistry {
		@SubscribeEvent
		public static void register(FMLCommonSetupEvent event) {
			CapabilityManager.INSTANCE.register(IWallJump.class, new IWallJump.WallJumpStorage(), WallJump::new);
		}
	}
}
