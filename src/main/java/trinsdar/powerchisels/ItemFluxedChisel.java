package trinsdar.powerchisels;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import ic2.api.item.ElectricItem;
import ic2.api.item.IC2Items;
import ic2.api.recipe.Recipes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import team.chisel.api.IChiselGuiType;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.api.carving.IChiselMode;
import team.chisel.common.init.ChiselTabs;
import team.chisel.common.util.NBTUtil;

import javax.annotation.Nullable;
import java.util.List;

public class ItemFluxedChisel extends Item implements IEnergyStorage, IChiselItem {

    public ItemFluxedChisel() {
        super();
        setMaxStackSize(1);
        setRegistryName("fluxed_chisel");
        setUnlocalizedName(PowerChisels.MODID + "." + "fluxedChisel");
        setCreativeTab(ChiselTabs.tab);
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    public int getCost(){
        return getEnergyStored()/500;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ItemStack empty = new ItemStack(this, 1, 0);
            ItemStack full = new ItemStack(this, 1, 0);
            ElectricItem.manager.discharge(empty, 2.147483647E9D, 2147483647, true, false, false);
            ElectricItem.manager.charge(full, 2.147483647E9D, 2147483647, true, false);
            items.add(empty);
            items.add(full);
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        return 1.0D - (double)getEnergyStored() / getMaxEnergyStored();
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || !ItemStack.areItemsEqual(oldStack, newStack);
    }

    @Override
    public boolean canOpenGui(World world, EntityPlayer player, EnumHand hand) {
        return getEnergyStored() >= getCost();
    }

    @Override
    public IChiselGuiType getGuiType(World world, EntityPlayer player, EnumHand hand) {
        return IChiselGuiType.ChiselGuiType.NORMAL;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List list, ITooltipFlag flag) {
        String base = "item.chisel.chisel.desc.";
        list.add(I18n.format(base + "gui", TextFormatting.AQUA, TextFormatting.GRAY));
        list.add(I18n.format(base + "lc1", TextFormatting.AQUA, TextFormatting.GRAY));
        list.add(I18n.format(base + "lc2", TextFormatting.AQUA, TextFormatting.GRAY));
        list.add("");
        list.add(I18n.format(base + "modes"));
        list.add(I18n.format(base + "modes.selected", TextFormatting.GREEN + I18n.format(NBTUtil.getChiselMode(stack).getUnlocName() + ".name")));
        list.add(I18n.format(base + "delete", TextFormatting.RED, TextFormatting.GRAY));
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();
        if (slot == EntityEquipmentSlot.MAINHAND) {
            if (getEnergyStored() >= getCost()){
                multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Chisel Damage", 2, 0));
            }
        }
        return multimap;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (getEnergyStored() >= getCost()){
            extractEnergy(getCost(), false);
        }
        return super.hitEntity(stack, attacker, target);
    }

    @Override
    public boolean canChisel(World world, EntityPlayer player, ItemStack chisel, ICarvingVariation target) {
        return getEnergyStored() >= getCost();
    }

    @Override
    public ItemStack craftItem(ItemStack chisel, ItemStack source, ItemStack target, EntityPlayer player) {
        if (chisel.isEmpty()) return ItemStack.EMPTY;
        int toCraft = Math.min(source.getCount(), target.getMaxStackSize());
        if (getEnergyStored() >= getCost()) {
            int damageLeft = (getMaxEnergyStored() - (getMaxEnergyStored() - getEnergyStored()))/getCost();
            toCraft = Math.min(toCraft, damageLeft);
            extractEnergy(toCraft*getCost(), false);
        }
        ItemStack res = target.copy();
        source.shrink(toCraft);
        res.setCount(toCraft);
        return res;
    }

    @Override
    public boolean onChisel(World world, EntityPlayer player, ItemStack chisel, ICarvingVariation target) {
        return true;
    }

    @Override
    public boolean canChiselBlock(World world, EntityPlayer player, EnumHand hand, BlockPos pos, IBlockState state) {
        return getEnergyStored() >= getCost();
    }

    @Override
    public boolean supportsMode(EntityPlayer player, ItemStack chisel, IChiselMode mode) {
        return true;
    }



    /* IEnergyStorage */
    public String ENERGY;

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        ItemStack container = new ItemStack(this);
        if (container.getTagCompound() == null) {
            setDefaultEnergyTag(container, 0);
        }
        int stored = Math.min(container.getTagCompound().getInteger(ENERGY), getMaxEnergyStored());
        int receive = Math.min(maxReceive, Math.min(getMaxEnergyStored() - stored, 200));

        if (!simulate) {
            stored += receive;
            container.getTagCompound().setInteger(ENERGY, stored);
        }
        return receive;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        ItemStack container = new ItemStack(this);
        if (container.getTagCompound() == null) {
            setDefaultEnergyTag(container, 0);
        }
        int stored = Math.min(container.getTagCompound().getInteger(ENERGY), getMaxEnergyStored());
        int extract = Math.min(maxExtract, stored);

        if (!simulate) {
            stored -= extract;
            container.getTagCompound().setInteger(ENERGY, stored);
        }
        return extract;
    }

    @Override
    public int getEnergyStored() {
        ItemStack container = new ItemStack(this);
        if (container.getTagCompound() == null) {
            setDefaultEnergyTag(container, 0);
        }
        return Math.min(container.getTagCompound().getInteger(ENERGY), getMaxEnergyStored());
    }

    @Override
    public int getMaxEnergyStored() {
        return 50000;
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    public static ItemStack setDefaultEnergyTag(ItemStack container, int energy) {

        if (!container.hasTagCompound()) {
            container.setTagCompound(new NBTTagCompound());
        }
        container.getTagCompound().setInteger("Energy", energy);

        return container;
    }



    /* Registery */
    public static final ItemFluxedChisel fluxedChisel = new ItemFluxedChisel();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.register(fluxedChisel);
    }
    public static void initRecipe(){

    }

    public void initModel(){
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
