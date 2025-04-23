package net.toblexson.alchematurgy.world.inventory.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.toblexson.alchematurgy.Alchematurgy;
import net.toblexson.alchematurgy.world.inventory.menu.AlchemicalCrucibleMenu;

public class AlchemicalCrucibleScreen extends AbstractContainerScreen<AlchemicalCrucibleMenu>
{
    private static final ResourceLocation TEXTURE = Alchematurgy.modLoc("textures/gui/container/alchemical_crucible_screen.png");

    /**
     * Create an instance of an alchemical crucible screen.
     * @param menu The alchemical crucible menu instance.
     * @param inventory The player's inventory.
     * @param title The menu's title.
     */
    public AlchemicalCrucibleScreen(AlchemicalCrucibleMenu menu, Inventory inventory, Component title)
    {
        super(menu, inventory, title);
    }

    /**
     * Render the background of the screen.
     * @param graphics The GUI graphics.
     * @param partialTick The current partial tick.
     * @param mouseX The mouse X position.
     * @param mouseY The mouse Y position.
     */
    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }
}
