package net.toblexson.alchematurgy.world.inventory.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.toblexson.alchematurgy.Alchematurgy;
import net.toblexson.alchematurgy.world.inventory.menu.AlchemicalCrucibleMenu;

/**
 * The alchemical crucible screen.
 */
public class AlchemicalCrucibleScreen extends AbstractContainerScreen<AlchemicalCrucibleMenu>
{
    /**
     * The location of the background texture.
      */
    private static final ResourceLocation BACKGROUND_LOCATION = Alchematurgy.modLoc("textures/gui/container/alchemical_crucible_screen.png");

    /**
     * Create an alchemical crucible screen
     * @param menu The menu.
     * @param playerInventory The player inventory.
     * @param title The title component.
     */
    public AlchemicalCrucibleScreen(AlchemicalCrucibleMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title);
    }

    /**
     * Render the screen.
     * @param graphics      The GuiGraphics object used for rendering.
     * @param mouseX        The x-coordinate of the mouse cursor.
     * @param mouseY        The y-coordinate of the mouse cursor.
     * @param partialTick   The partial tick time.
     */
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
    {
        renderBackground(graphics,mouseX,mouseY,partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    /**
     * Renders the background of the screen
     * @param graphics      The GuiGraphics object used for rendering.
     * @param partialTick   The partial tick time.
     * @param mouseX        The x-coordinate of the mouse cursor.
     * @param mouseY        The y-coordinate of the mouse cursor.
     */
    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY)
    {
        graphics.blit(BACKGROUND_LOCATION, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }
}
