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
        renderWaterCover(graphics, x, y);
        renderFlame(graphics, x, y);
    }

    private void renderFlame(GuiGraphics graphics, int x, int y)
    {
        if (menu.isBurning())
        {
            int height = menu.fireAmount();
            int offset = 14 - height;
            graphics.blit(TEXTURE, x + 56, y + 36 + offset, 176, 15 + offset, 14, height);
        }
    }

    private void renderWaterCover(GuiGraphics graphics, int x, int y)
    {
        if (menu.hasWater())
        {
            int height = menu.waterAmount();
            int offset = 4 - height;
            graphics.blit(TEXTURE, x + 87, y + 54 + offset, 176, 11 + offset, 10, height);
        }
    }

    private void renderProgressArrow(GuiGraphics graphics, int x, int y)
    {
        if (menu.isCrafting())
            graphics.blit(TEXTURE, x + 78, y + 36, 176, 0, menu.getCraftingArrowProgress(), 11);
    }

    /**
     * Renders the GUI (this calls all the sub-methods, most of which is handled by the super).
     * @param graphics The GuiGraphics object used for rendering.
     * @param mouseX The x-coordinate of the mouse cursor.
     * @param mouseY The y-coordinate of the mouse cursor.
     * @param partialTick The partial tick time.
     */
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }
}
