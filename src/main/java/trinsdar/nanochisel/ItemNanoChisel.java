package trinsdar.nanochisel;

import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import team.chisel.api.IChiselGuiType;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.api.carving.IChiselMode;
import team.chisel.common.util.NBTUtil;

import javax.annotation.Nullable;
import java.util.List;

public class ItemNanoChisel extends Item implements IElectricItemManager, IChiselItem {

    public ItemNanoChisel() {
        super();
        setMaxStackSize(1);
        setRegistryName("nanochisel");
        setUnlocalizedName(NanoChisel.MODID + "." + "nanoChisel");
        setCreativeTab(CreativeTabs.TOOLS);
    }

    @Override
    public boolean isFull3D() {
        return true;
    }


    @Override
    public boolean canOpenGui(World world, EntityPlayer player, EnumHand hand) {
        return getCharge(player.getHeldItem(hand)) >= 80;
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



    }

    @Override
    public boolean canChisel(World world, EntityPlayer player, ItemStack chisel, ICarvingVariation target) {
        return getCharge(chisel) >= 80;
    }

    @Override
    public boolean onChisel(World world, EntityPlayer player, ItemStack chisel, ICarvingVariation target) {
        discharge(chisel, 80, 1, false, false, false);
        return true;
    }

    @Override
    public boolean canChiselBlock(World world, EntityPlayer player, EnumHand hand, BlockPos pos, IBlockState state) {
        return getCharge(player.getHeldItem(hand)) >= 80;
    }

    @Override
    public boolean supportsMode(EntityPlayer player, ItemStack chisel, IChiselMode mode) {
        return true;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || !ItemStack.areItemsEqual(oldStack, newStack);
    }


    @Override
    public double charge(ItemStack itemStack, double v, int i, boolean b, boolean b1) {
        return 100;
    }

    @Override
    public double discharge(ItemStack itemStack, double v, int i, boolean b, boolean b1, boolean b2) {
        return 80;
    }

    @Override
    public double getCharge(ItemStack itemStack) {
        return 0;
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public boolean canUse(ItemStack item, double v) {
        return getCharge(item) >= 80;
    }

    @Override
    public boolean use(ItemStack itemStack, double v, EntityLivingBase entityLivingBase) {
        return true;
    }

    @Override
    public void chargeFromArmor(ItemStack itemStack, EntityLivingBase entityLivingBase) {

    }

    @Override
    public String getToolTip(ItemStack itemStack) {
        return null;
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return 1;
    }

    public static final ItemNanoChisel nanoChisel = new ItemNanoChisel();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.register(nanoChisel);
    }
    public void initModel(){
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
