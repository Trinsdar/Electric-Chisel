package trinsdar.nanochisel;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import ic2.api.item.ElectricItem;
import ic2.api.item.IC2Items;
import ic2.api.item.IElectricItem;
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
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
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

public class ItemNanoChisel extends Item implements IElectricItem, IChiselItem {
    public static String classic = "ic2-classic-spmod";

    public ItemNanoChisel() {
        super();
        setMaxStackSize(1);
        setRegistryName("nanochisel");
        setUnlocalizedName(NanoChisel.MODID + "." + "nanoChisel");
        setCreativeTab(ChiselTabs.tab);
    }

    @Override
    public boolean isFull3D() {
        return true;
    }


    public boolean isClassicLoaded(){
        if (Loader.isModLoaded(classic)){
            return true;
        }else {
            return false;
        }
    }

    public double getCost(ItemStack stack){
       return getMaxCharge(stack)/2500;
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
        if (isClassicLoaded()){
            return true;
        }else {
            return ElectricItem.manager.getCharge(stack) != this.getMaxCharge(stack);
        }
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        return 1.0D - ElectricItem.manager.getCharge(stack) / this.getMaxCharge(stack);
    }

    @Override
    public boolean canOpenGui(World world, EntityPlayer player, EnumHand hand) {
        return ElectricItem.manager.getCharge(player.getHeldItem(hand)) >= getCost(player.getHeldItem(hand));
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
            if (ElectricItem.manager.getCharge(stack) >= getCost(stack)){
                multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Chisel Damage", 4, 0));
            }
        }
        return multimap;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (isClassicLoaded()){
            if (ElectricItem.manager.getCharge(stack) >= 100){
                ElectricItem.manager.use(stack, 100, attacker);
            }
        }else {

        }
        return super.hitEntity(stack, attacker, target);
    }

    @Override
    public boolean canChisel(World world, EntityPlayer player, ItemStack chisel, ICarvingVariation target) {
        return ElectricItem.manager.getCharge(chisel) >= getCost(chisel);
    }

    @Override
    public ItemStack craftItem(ItemStack chisel, ItemStack source, ItemStack target, EntityPlayer player) {
        if (chisel.isEmpty()) return ItemStack.EMPTY;
        int toCraft = Math.min(source.getCount(), target.getMaxStackSize());
        if (ElectricItem.manager.getCharge(chisel) >= getCost(chisel)) {
            int damageLeft = ((int)this.getMaxCharge(chisel) - ((int)this.getMaxCharge(chisel) - (int)ElectricItem.manager.getCharge(chisel)))/(int)getCost(chisel);
            toCraft = Math.min(toCraft, damageLeft);
            ElectricItem.manager.use(chisel,toCraft*getCost(chisel), player);
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
        return ElectricItem.manager.getCharge(player.getHeldItem(hand)) >= getCost(player.getHeldItem(hand));
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
    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        if (isClassicLoaded()){
            return 100000;
        }else {
            return 1000000;
        }

    }

    @Override
    public int getTier(ItemStack itemStack) {
        if (isClassicLoaded()){
            return 2;
        }else {
            return 3;
        }
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        if (isClassicLoaded()){
            return 100;
        }else {
            return 1000;
        }
    }

    public static final ItemNanoChisel nanoChisel = new ItemNanoChisel();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.register(nanoChisel);
    }
    public static void initRecipe(){
        Recipes.advRecipes.addRecipe(new ItemStack(nanoChisel), "  C", " C ", "E  ", 'C', IC2Items.getItem("crafting", "carbon_plate"), 'E', IC2Items.getItem("energy_crystal"));
    }

    public void initModel(){
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
