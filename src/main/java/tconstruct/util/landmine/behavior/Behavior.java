package tconstruct.util.landmine.behavior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tconstruct.util.landmine.Helper;

/**
 * 
 * @author fuj1n
 *
 */
public abstract class Behavior
{

    public static HashMap<ItemStack, Behavior> behaviorsListItems = new HashMap<ItemStack, Behavior>();
    public static HashMap<ItemStack, Behavior> behaviorsListBlocks = new HashMap<ItemStack, Behavior>();
    protected static Behavior defaultBehavior;

    public static Behavior dummy = new BehaviorDummy();
    public static Behavior utilityMode = new BehaviorPreventExplode();
    public static Behavior explosive = new BehaviorExplosive();
    public static Behavior firework = new BehaviorFirework();
    public static Behavior blockThrow = new BehaviorBlockThrow();
    public static Behavior potion = new BehaviorPotion();
    public static Behavior fireball = new BehaviorFirecharge();
    public static Behavior spawn = new BehaviorSpawnEgg();
    public static Behavior shoot = new BehaviorProjectile();
    public static Behavior shear = new BehaviorShears();

    public static void registerBuiltInBehaviors ()
    {
        defaultBehavior = new BehaviorDefault();

        addBehavior(new ItemStack(Items.stick), dummy);
        addBehavior(new ItemStack(Items.redstone), utilityMode);
        addBehavior(new ItemStack(Blocks.torch), blockThrow);
        addBehavior(new ItemStack(Items.gunpowder), explosive);
        addBehavior(new ItemStack(Blocks.tnt), explosive);
        addBehavior(new ItemStack(Items.fireworks), firework);
        addBehavior(new ItemStack(Items.potionitem), potion);
        addBehavior(new ItemStack(Items.fire_charge), fireball);
        addBehavior(new ItemStack(Items.spawn_egg), spawn);
        addBehavior(new ItemStack(Items.arrow), shoot);
        addBehavior(new ItemStack(Items.snowball), shoot);
        addBehavior(new ItemStack(Items.ender_pearl), shoot);
        addBehavior(new ItemStack(Items.shears), shear);

        //Make sure the part below this comment is executed last(to avoid conflicts)
        Iterator i1 = Block.blockRegistry.iterator();
        while (i1.hasNext())
        {
            Object ob = i1.next();
            if (ob != null && ob instanceof Block)
            {
                Block b = (Block) ob;
                if (b.getMaterial().isOpaque() && b.renderAsNormalBlock() && !b.canProvidePower() && !(b instanceof ITileEntityProvider) && !behaviorsListBlocks.containsKey(new ItemStack(b)))
                {
                    addBehavior(new ItemStack(b), blockThrow);
                }
            }
        }
    }

    public static Behavior getBehaviorFromStack (ItemStack par1ItemStack)
    {
        if (par1ItemStack == null)
        {
            return null;
        }

        if (par1ItemStack.getItem() instanceof ItemBlock)
        {
            if (!behaviorsListBlocks.isEmpty())
            {
                return behaviorsListBlocks.get(par1ItemStack);
            }
            else
            {
                return null;
            }
        }
        else
        {
            if (!behaviorsListItems.isEmpty())
            {
                return behaviorsListItems.get(par1ItemStack);
            }
            else
            {
                return null;
            }
        }
    }

    public static Behavior getDefaulBehavior ()
    {
        return defaultBehavior;
    }

    public static void addBehavior (ItemStack par1ItemStack, Behavior par2Behavior)
    {
        if (par1ItemStack.getItem() instanceof ItemBlock)
        {
            if (!behaviorsListBlocks.containsKey(par1ItemStack))
            {
                behaviorsListBlocks.put(par1ItemStack, par2Behavior);
            }
        }
        else
        {
            if (!behaviorsListItems.containsKey(par1ItemStack))
            {
                behaviorsListItems.put(par1ItemStack, par2Behavior);
            }
        }
    }

    public abstract void executeLogic (World par1World, int par2, int par3, int par4, ItemStack par5ItemStack, Entity triggerer, boolean willBlockBeRemoved);

    public int getStackLimit (ItemStack par1ItemStack)
    {
        return 1;
    }

    public void getInformation (ItemStack par1ItemStack, List par2List)
    {
    }

    //Will return false if the effect does not stack among the slots
    public boolean effectStacks ()
    {
        return true;
    }

    public EnumFacing getFacing (World par1World, int par2, int par3, int par4)
    {
        ForgeDirection dir = Helper.convertMetaToForgeOrientation(par1World.getBlockMetadata(par2, par3, par4));

        switch (dir)
        {
        case DOWN:
            return EnumFacing.UP;
        case UP:
            return EnumFacing.DOWN;
        case WEST:
            return EnumFacing.WEST;
        case EAST:
            return EnumFacing.EAST;
        case SOUTH:
            return EnumFacing.NORTH;
        case NORTH:
            return EnumFacing.SOUTH;
        default:
            return EnumFacing.UP;
        }
    }

    public boolean doesBehaviorPreventRemovalOfBlock (ItemStack par1ItemStack)
    {
        return false;
    }

    public boolean isOffensive (ItemStack par1ItemStack)
    {
        return true;
    }

    public boolean isBehaviorExchangableWithOffensive (ItemStack par1ItemStack)
    {
        return true;
    }

    public boolean shouldItemBeRemoved (ItemStack par1ItemStack, boolean willBlockGetRemoved)
    {
        return true;
    }

    public static final int arrayIndexOfStack (ArrayList<ItemStack> stacks, ItemStack item)
    {
        Iterator<ItemStack> i1 = stacks.iterator();

        int index = 0;

        while (i1.hasNext())
        {
            ItemStack stack = i1.next();
            if (stack.isItemEqual(item))
            {
                return index;
            }
            index++;
        }

        return -1;
    }

    public static final boolean arrayContainsEqualStack (ArrayList<ItemStack> stacks, ItemStack item)
    {
        Iterator<ItemStack> i1 = stacks.iterator();

        while (i1.hasNext())
        {
            ItemStack stack = i1.next();
            if (stack.isItemEqual(item))
            {
                return true;
            }
        }

        return false;
    }

    public boolean overridesDefault ()
    {
        return false;
    }

}