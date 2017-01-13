package com.dmonsters.ai;

import java.util.Random;

import com.dmonsters.entity.EntityMutantSteve;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityAIMutantSteveAttack extends EntityAIAttackOnCollide {
    private int raiseArmTicks;
    private EntityMutantSteve mutantSteve;
    private double targetPosX;
    private double targetPosY;
    private double targetPosZ;

    public EntityAIMutantSteveAttack(EntityMutantSteve zombieIn, double speedIn, boolean longMemoryIn) {
        super(zombieIn, speedIn, longMemoryIn);
        this.mutantSteve = zombieIn;
        //System.out.println("Radek AI initialized");
        this.setMutexBits(7);
    }
    
    @Override
    public boolean shouldExecute() {
    	boolean result = super.shouldExecute();
    	if (result)
    		System.out.println("Radek AI shouldExecute value: " + result);
    	return result;
    }
    
    @Override
    public boolean continueExecuting() {
    	boolean result = super.continueExecuting();
    	if (!result)
    		System.out.println("Radek AI continueExecuting value: " + result);
    	return result;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        System.out.println("Radek AI: start at " + this.attacker.getAttackTarget().getPositionVector());
        this.raiseArmTicks = 0;
    }
    
    private int ticks;
    @Override
    public void updateTask() {
        super.updateTask();
        ++this.raiseArmTicks;

        ticks++;
        if (ticks == 20 && !this.mutantSteve.isInWater()) {
        	ticks = 0;
        	DestroyAroundMe(0, 0.25F);
        	DestroyAroundMe(1, 0.5F);
        	DestroyAroundMe(2, 0.75F);
        }
    }
    
    private void DestroyAroundMe(int yOffset, float destroyChance) {
    	IBlockState blockToDestroy;
    	BlockPos blockToDestroyPos;
    	World worldin = this.attacker.worldObj;
    	double y = this.attacker.posY;
    	float hardness;
    	float hardnessTreshold = 5;
    	boolean destroyedBlock = false;
    	float randomChance = 0;
    	Random random = new Random();
    	
    	for (int dx = -1; dx <= 1; ++dx) {
    		for (int dz = -1; dz <= 1; ++dz) {
    			if (dx == 0 && dz == 0)
    				continue;
    	    	blockToDestroyPos = new BlockPos(this.attacker.posX + dx, y + yOffset, this.attacker.posZ + dz);
    	    	blockToDestroy = worldin.getBlockState(blockToDestroyPos);
    	    	if (blockToDestroy.getBlock() != Blocks.air) {
    	        	hardness = blockToDestroy.getBlock().getBlockHardness(this.attacker.worldObj, blockToDestroyPos);
					if (hardness < hardnessTreshold) {
						randomChance = random.nextFloat();
    	            	if (randomChance < destroyChance) {
    	            		this.attacker.worldObj.destroyBlock(blockToDestroyPos, true);
    	            		destroyedBlock = true;
    	            	}
					}
    	    	}
    		} 
    	}
    }
}