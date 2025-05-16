package net.toblexson.alchematurgy.world.inventory.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.toblexson.alchematurgy.Alchematurgy;
import net.toblexson.alchematurgy.world.inventory.menu.AlchemicalPurifierMenu;

public class AlchemicalPurifierScreen extends ModScreen<AlchemicalPurifierMenu>
{
    private static final ResourceLocation TEXTURE = Alchematurgy.modLoc("textures/gui/container/alchemical_purifier_screen.png");

    public AlchemicalPurifierScreen(AlchemicalPurifierMenu pMenu, Inventory pPlayerInventory, Component pTitle)
    {
        super(pMenu, pPlayerInventory, pTitle);
    }

    /**
     * Render the background of the screen.
     * @param graphics The GuiGraphics object used for rendering.
     * @param partialTick The partial tick time.
     * @param mouseX The x-coordinate of the mouse cursor.
     * @param mouseY The y-coordinate of the mouse cursor.
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

        renderProgressArrow(graphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics graphics, int x, int y)
    {
        if (menu.isCrafting())
            graphics.blit(TEXTURE, x + 80, y + 34, 176, 0, menu.getCraftingArrowProgress(), 16);
    }
}
