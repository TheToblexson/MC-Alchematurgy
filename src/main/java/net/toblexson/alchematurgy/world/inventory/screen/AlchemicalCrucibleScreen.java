package net.toblexson.alchematurgy.world.inventory.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.toblexson.alchematurgy.Alchematurgy;
import net.toblexson.alchematurgy.world.inventory.menu.AlchemicalCrucibleMenu;

public class AlchemicalCrucibleScreen extends ModScreen<AlchemicalCrucibleMenu>
{
    private static final ResourceLocation TEXTURE = Alchematurgy.modLoc("textures/gui/container/alchemical_crucible_screen.png");

    public AlchemicalCrucibleScreen(AlchemicalCrucibleMenu menu, Inventory inventory, Component title)
    {
        super(menu, inventory, title);
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

        renderBubbles(graphics, x, y);
        renderWaterCover(graphics, x, y);
        renderFlame(graphics, x, y);
        renderTransferArrow(graphics, x, y);
    }

    private void renderTransferArrow(GuiGraphics graphics, int x, int y)
    {
        if (menu.isTransferring())
        {
            int maxLength = 15;
            int length = menu.getTransferArrowSize(maxLength);
            graphics.blit(TEXTURE, x + 107, y + 36, 176, 46, 7, length);
        }
    }

    private void renderFlame(GuiGraphics graphics, int x, int y)
    {
        if (menu.isBurning())
        {
            int height = menu.fireAmount();
            int offset = 14 - height;
            graphics.blit(TEXTURE, x + 55, y + 36 + offset, 176, 4 + offset, 14, height);
        }
    }

    private void renderWaterCover(GuiGraphics graphics, int x, int y)
    {
        if (menu.hasFluid())
        {
            //get the sprite to use
            int sprite = menu.essenceSprite();
            int width = 10;
            int maxHeight = 4;

            int height = menu.FluidAmount(maxHeight);
            int offset = maxHeight - height;
            graphics.blit(TEXTURE, x + 83, y + 54 + offset, 176 + (sprite * width), offset, 10, height);
        }
    }

    private void renderBubbles(GuiGraphics graphics, int x, int y)
    {
        if (menu.isCrafting())
        {
            int maxHeight = 28;
            int height = menu.getCraftingArrowProgress(maxHeight);
            graphics.blit(TEXTURE, x + 82, y + 20 + height, 176, 18 + height, 11, maxHeight - height);
        }
    }
}
