package com.alrex.parcool.common.capability;

import com.alrex.parcool.ParCool;
import com.alrex.parcool.common.capability.impl.CatLeap;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ICatLeap {
	//only in Client
	public boolean canCatLeap(ClientPlayerEntity player);

	//only in Client
	public boolean canReadyLeap(ClientPlayerEntity player);

	//only in Client
	public double getBoostValue(ClientPlayerEntity player);

	public boolean isLeaping();

	public void setLeaping(boolean leaping);

	public boolean isReady();

	public void setReady(boolean ready);

	public void updateReadyTime();

	public int getReadyTime();

	public int getStaminaConsumption();

	public static class CatLeapStorage implements Capability.IStorage<ICatLeap> {
		@Override
		public void readNBT(Capability<ICatLeap> capability, ICatLeap instance, Direction side, INBT nbt) {
		}

		@Nullable
		@Override
		public INBT writeNBT(Capability<ICatLeap> capability, ICatLeap instance, Direction side) {
			return null;
		}
	}

	@Nullable
	public static ICatLeap get(PlayerEntity entity) {
		LazyOptional<ICatLeap> optional = entity.getCapability(CatLeapProvider.CAT_LEAP_CAPABILITY);
		if (!optional.isPresent()) return null;
		return optional.orElseThrow(IllegalStateException::new);
	}

	public static class CatLeapProvider implements ICapabilityProvider {
		@CapabilityInject(ICatLeap.class)
		public static final Capability<ICatLeap> CAT_LEAP_CAPABILITY = null;
		public static final ResourceLocation CAPABILITY_LOCATION = new ResourceLocation(ParCool.MOD_ID, "capability.parcool.catleap");

		private LazyOptional<ICatLeap> instance = LazyOptional.of(CAT_LEAP_CAPABILITY::getDefaultInstance);

		@Nonnull
		@Override
		public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
			return cap == CAT_LEAP_CAPABILITY ? instance.cast() : LazyOptional.empty();
		}

		@Nonnull
		@Override
		public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
			return cap == CAT_LEAP_CAPABILITY ? instance.cast() : LazyOptional.empty();
		}
	}

	public static class CatLeapRegistry {
		@SubscribeEvent
		public static void register(FMLCommonSetupEvent event) {
			CapabilityManager.INSTANCE.register(ICatLeap.class, new ICatLeap.CatLeapStorage(), CatLeap::new);
		}
	}
}
