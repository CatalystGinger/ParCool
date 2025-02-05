package com.alrex.parcool;

import com.alrex.parcool.client.input.KeyBindings;
import com.alrex.parcool.common.capability.capabilities.Capabilities;
import com.alrex.parcool.common.command.ParCoolCommands;
import com.alrex.parcool.common.item.ItemRegistry;
import com.alrex.parcool.common.registries.EventBusForgeRegistry;
import com.alrex.parcool.common.registries.EventBusModRegistry;
import com.alrex.parcool.proxy.ClientProxy;
import com.alrex.parcool.proxy.CommonProxy;
import com.alrex.parcool.proxy.ServerProxy;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ParCool.MOD_ID)
public class ParCool {
	public static final String MOD_ID = "parcool";
	private static final String PROTOCOL_VERSION = "1.0";
	public static final SimpleChannel CHANNEL_INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(ParCool.MOD_ID, "message"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);
	public static final CommonProxy PROXY = DistExecutor.unsafeRunForDist(
			() -> ClientProxy::new,
			() -> ServerProxy::new
	);

	public static final Logger LOGGER = LogManager.getLogger();

	//only in Client
	public static boolean isActive() {
		return ParCoolConfig.CONFIG_CLIENT.ParCoolActivation.get();
	}

	//only in Client
	public static void setActivation(boolean activation) {
		ParCoolConfig.CONFIG_CLIENT.canWallJump.get();
		ParCoolConfig.CONFIG_CLIENT.ParCoolActivation.set(activation);
	}

	public ParCool() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loaded);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doServerStuff);
		FMLJavaModLoadingContext.get().getModEventBus().register(ItemRegistry.class);
		MinecraftForge.EVENT_BUS.addListener(this::registerCommand);

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.addListener(this::serverStarting);

		ModLoadingContext context = ModLoadingContext.get();
		context.registerConfig(ModConfig.Type.CLIENT, ParCoolConfig.spec);
	}

	private void loaded(FMLLoadCompleteEvent event) {
	}

	private void setup(final FMLCommonSetupEvent event) {
		EventBusForgeRegistry.register(MinecraftForge.EVENT_BUS);
		EventBusModRegistry.registry(FMLJavaModLoadingContext.get().getModEventBus());
		Capabilities.registerAll(CapabilityManager.INSTANCE);
		PROXY.registerMessages(CHANNEL_INSTANCE);
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		KeyBindings.register(event);
		EventBusForgeRegistry.registerClient(MinecraftForge.EVENT_BUS);
		EventBusModRegistry.registerClient(FMLJavaModLoadingContext.get().getModEventBus());
	}

	private void doServerStuff(final FMLDedicatedServerSetupEvent event) {
	}

	private void serverStarting(final FMLServerAboutToStartEvent event) {

	}

	private void processIMC(final InterModProcessEvent event) {
	}

	private void registerCommand(final RegisterCommandsEvent event) {
		ParCoolCommands.register(event.getDispatcher());
	}
}
