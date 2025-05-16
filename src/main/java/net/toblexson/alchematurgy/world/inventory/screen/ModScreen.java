package net.toblexson.alchematurgy.world.inventory.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.toblexson.alchematurgy.world.inventory.menu.ModMenu;

public abstract class ModScreen<M extends ModMenu> extends AbstractContainerScreen<M>
{
    public ModScreen(M menu, Inventory inventory, Component title)
    {
        super(menu, inventory, title);
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
