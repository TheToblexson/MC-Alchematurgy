package net.toblexson.alchematurgy.world.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public abstract class ModMenuBlockEntity extends BlockEntity implements MenuProvider
{
    /**
     * The block entity's container data.
     */
    protected final ContainerData data;

    /**
     * The block entity's item stack handler.
     */
    public ItemStackHandler inventory;

    public ModMenuBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, int inventorySize)
    {
        super(pType, pPos, pBlockState);
        data = initialiseData();
        inventory = createInventory(inventorySize);
    }

    protected ItemStackHandler createInventory(int inventorySize)
    {
        return new ItemStackHandler(inventorySize)
        {
            /**
             * Updates the server when the inventory is modified.
             * @param slot The slot that has been modified.
             */
            @Override
            protected void onContentsChanged(int slot)
            {
                setChanged();
                assert level != null;
                if(!level.isClientSide())
                    level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),3);
            }
        };
    }

    /**
     * Drops the inventory using the Containers helper method.
     */
    public void dropInventory()
    {
        SimpleContainer container = new SimpleContainer(inventory.getSlots());
        for(int i = 0; i < inventory.getSlots(); i++)
            container.setItem(i, inventory.getStackInSlot(i));
        assert this.level != null;
        Containers.dropContents(this.level, this.worldPosition, container);
    }

    /**
     * Save additional data to the tag.
     * @param tag The tag being written to.
     * @param registries The registries.
     */
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
    }

    /**
     * Read additional data from the tag.
     * @param tag The tag being read from.
     * @param registries The registries.
     */
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
    }

    /**
     * Create an update packet.
     * @return The new client-bound block entity packet.
     */
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    /**
     * Create an update tag.
     * @param registries The registries.
     * @return The new compound tag.
     */
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries)
    {
        return saveWithoutMetadata(registries);
    }

    /**
     * Initialise the container data for the container.
     * @return the initialised container data.
     */
    protected abstract ContainerData initialiseData();
}
