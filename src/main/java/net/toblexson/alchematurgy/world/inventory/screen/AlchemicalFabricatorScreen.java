package net.toblexson.alchematurgy.world.inventory.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.toblexson.alchematurgy.Alchematurgy;
import net.toblexson.alchematurgy.Essence;
import net.toblexson.alchematurgy.registry.ModDataMaps;
import net.toblexson.alchematurgy.world.inventory.menu.AlchemicalFabricatorMenu;

public class AlchemicalFabricatorScreen extends ModScreen<AlchemicalFabricatorMenu>
{

    private static final ResourceLocation TEXTURE = Alchematurgy.modLoc("textures/gui/container/alchemical_fabricator_screen.png");

    public AlchemicalFabricatorScreen(AlchemicalFabricatorMenu menu, Inventory inventory, Component title)
    {
        super(menu, inventory, title);
        imageHeight = 220;
        this.inventoryLabelY = this.imageHeight - 94;
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

        renderEssenceBars(graphics, x, y);
        renderWarnings(graphics, x, y);
    }

    private void renderWarnings(GuiGraphics graphics, int x, int y)
    {
        ModDataMaps.Essences required = menu.getRequiredEssences();
        ModDataMaps.Essences current = menu.getEssenceCounts();
        animateWarning(graphics, required.air(), current.air(), x + 8, x + 17, Essence.Air.index);
    }

    private void animateWarning(GuiGraphics graphics, float max, float current, int x, int y, int index)
    {
        //TODO
    }

    private void renderEssenceBars(GuiGraphics graphics, int x, int y)
    {
        ModDataMaps.Essences requiredEssences = menu.getRequiredEssences();
        animateBar(graphics, requiredEssences.air(), x + 80, y + 58, 176, 3, Essence.Air.index);
        animateBar(graphics, requiredEssences.earth(), x + 80, y + 62, 176, 6, Essence.Earth.index);
        animateBar(graphics, requiredEssences.fire(), x + 80, y + 66, 176, 9, Essence.Fire.index);
        animateBar(graphics, requiredEssences.water(), x + 80, y + 70, 176, 12, Essence.Water.index);
        animateBar(graphics, requiredEssences.life(), x + 80, y + 74, 176, 15, Essence.Life.index);
        animateBar(graphics, requiredEssences.magic(), x + 80, y + 78, 176, 18, Essence.Magic.index);
    }

    private void animateBar(GuiGraphics graphics, float max, int x, int y, int u, int v, int essenceIndex)
    {
        if (max > 0)
        {
            //active background
            graphics.blit(TEXTURE, x, y, 176, 0, 22, 3);
            //bar
            graphics.blit(TEXTURE, x, y, u, v, menu.getEssenceBarProgress(essenceIndex), 3);
        }
    }
}
