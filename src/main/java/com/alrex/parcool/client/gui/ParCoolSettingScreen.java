package com.alrex.parcool.client.gui;

import com.alrex.parcool.client.gui.widget.WidgetListView;
import com.alrex.parcool.utilities.ColorUtil;
import com.alrex.parcool.utilities.FontUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.alrex.parcool.ParCoolConfig.CONFIG_CLIENT;

public class ParCoolSettingScreen extends Screen {
	private final int width = 400;
	private final int height = 225;
	private final int xOffset = 10;
	private final int yOffset = 10;
	private final ButtonSet[] itemList = new ButtonSet[]{
			new ButtonSet("infinite stamina", CONFIG_CLIENT.infiniteStamina::set, CONFIG_CLIENT.infiniteStamina::get),
			new ButtonSet("substitute Sprint for Fast-Run", CONFIG_CLIENT.substituteSprintForFastRun::set, CONFIG_CLIENT.substituteSprintForFastRun::get),
			new ButtonSet("replace Fast-Run with Sprint", CONFIG_CLIENT.replaceSprintWithFastRun::set, CONFIG_CLIENT.replaceSprintWithFastRun::get),
			new ButtonSet("hide stamina HUD", CONFIG_CLIENT.hideStaminaHUD::set, CONFIG_CLIENT.hideStaminaHUD::get),
			new ButtonSet("use light Stamina HUD", CONFIG_CLIENT.useLightHUD::set, CONFIG_CLIENT.useLightHUD::get),
			new ButtonSet("auto-turning when WallJump", CONFIG_CLIENT.autoTurningWallJump::set, CONFIG_CLIENT.autoTurningWallJump::get),
			new ButtonSet("disable WallJump toward walls", CONFIG_CLIENT.disableWallJumpTowardWall::set, CONFIG_CLIENT.disableWallJumpTowardWall::get),
			new ButtonSet("disable a camera rotation of Rolling", CONFIG_CLIENT.disableCameraRolling::set, CONFIG_CLIENT.disableCameraRolling::get),
			new ButtonSet("disable a camera rotation of Flipping", CONFIG_CLIENT.disableCameraFlipping::set, CONFIG_CLIENT.disableCameraFlipping::get),
			new ButtonSet("disable a camera animation of Horizontal Wall-Run", CONFIG_CLIENT.disableCameraHorizontalWallRun::set, CONFIG_CLIENT.disableCameraHorizontalWallRun::get),
			new ButtonSet("disable a camera animation of Vault", CONFIG_CLIENT.disableCameraVault::set, CONFIG_CLIENT.disableCameraVault::get),
			new ButtonSet("disable double-tapping for dodge", CONFIG_CLIENT.disableDoubleTappingForDodge::set, CONFIG_CLIENT.disableDoubleTappingForDodge::get),
			new ButtonSet("disable crawl in air", CONFIG_CLIENT.disableCrawlInAir::set, CONFIG_CLIENT.disableCrawlInAir::get),
			new ButtonSet("disable vault in air", CONFIG_CLIENT.disableVaultInAir::set, CONFIG_CLIENT.disableVaultInAir::get),
			new ButtonSet("disable falling animation", CONFIG_CLIENT.disableFallingAnimation::set, CONFIG_CLIENT.disableFallingAnimation::get),
			new ButtonSet("disable animations", CONFIG_CLIENT.disableAnimation::set, CONFIG_CLIENT.disableAnimation::get),
			new ButtonSet("disable first person view animatons", CONFIG_CLIENT.disableFPVAnimation::set, CONFIG_CLIENT.disableFPVAnimation::get),
			new ButtonSet("enable roll when player is creative", CONFIG_CLIENT.enableRollWhenCreative::set, CONFIG_CLIENT.enableRollWhenCreative::get),
			new ButtonSet("ParCool is active", CONFIG_CLIENT.parCoolActivation::set, CONFIG_CLIENT.parCoolActivation::get)
	};
	private final WidgetListView<CheckboxButton> buttons = new WidgetListView<CheckboxButton>(
			0, 0, 0, 0,
			Arrays.stream(itemList)
					.map((ButtonSet item) ->
							new CheckboxButton
									(
											0, 0, 0, 0,
											new TranslationTextComponent(item.name),
											item.getter.getAsBoolean()
									))
					.collect(Collectors.toList()),
			Minecraft.getInstance().font.lineHeight + 11
	);

	public ParCoolSettingScreen(ITextComponent titleIn) {
		super(titleIn);
	}

	//render?
	@Override
	public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);

		MainWindow window = Minecraft.getInstance().getWindow();
		buttons.setX((window.getGuiScaledWidth() - width) + xOffset);
		buttons.setY((window.getGuiScaledHeight() - height) + yOffset);
		buttons.setWidth(width - (xOffset * 2));
		buttons.setHeight(height - (yOffset * 2));

		renderBackground(p_230430_1_, ColorUtil.getColorCodeFromARGB(0x77, 0x66, 0x66, 0xCC));
		buttons.render(p_230430_1_, Minecraft.getInstance().font, p_230430_2_, p_230430_3_, p_230430_4_);
		FontUtil.drawCenteredText(p_230430_1_, new StringTextComponent("ParCool Settings"), window.getGuiScaledWidth() / 2, yOffset, 0x8888FF);
		FontUtil.drawCenteredText(p_230430_1_, new StringTextComponent("↓"), window.getGuiScaledWidth() / 2, window.getGuiScaledHeight() - yOffset, 0x8888FF);
	}

	//renderBackground?
	@Override
	public void renderBackground(MatrixStack p_230446_1_) {
		super.renderBackground(p_230446_1_);
	}

	//renderBackground?
	@Override
	public void renderBackground(MatrixStack p_238651_1_, int p_238651_2_) {
		super.renderBackground(p_238651_1_, p_238651_2_);
	}

	//mouseScrolled?
	@Override
	public boolean mouseScrolled(double x, double y, double value) {
		if (buttons.contains(x, y)) {
			buttons.scroll((int) -value);
		}
		return true;
	}

	private static class ButtonSet {
		final String name;
		final Consumer<Boolean> setter;
		final BooleanSupplier getter;

		ButtonSet(String name, Consumer<Boolean> setter, BooleanSupplier getter) {
			this.name = name;
			this.getter = getter;
			this.setter = setter;
		}
	}

	//mouseClicked?
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int type) {//type:1->right 0->left
		if (buttons.contains(mouseX, mouseY)) {
			Tuple<Integer, CheckboxButton> item = buttons.clicked(mouseX, mouseY, type);
			if (item == null) return false;
			if (item.getA() < 0 || itemList.length <= item.getA()) return false;
			ButtonSet selected = itemList[item.getA()];

			item.getB().onPress();
			selected.setter.accept(item.getB().selected());

			PlayerEntity player = Minecraft.getInstance().player;
			if (player != null) player.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0f, 1.0f);
		}
		return false;
	}
}
