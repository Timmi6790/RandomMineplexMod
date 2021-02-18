package de.timmi6790.rmmod.mixins.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityWither.class)
public abstract class MixinEntityWither extends EntityMob implements IBossDisplayData, IRangedAttackMob {
    @Shadow
    public abstract int getWatchedTargetId(int p_82203_1_);

    @Shadow
    public abstract boolean isArmored();

    @Shadow
    private float[] field_82217_f;

    @Shadow
    private float[] field_82218_g;

    @Shadow
    private float[] field_82221_e;

    @Shadow
    private float[] field_82220_d;

    @Shadow
    protected abstract double func_82214_u(int p_82214_1_);

    @Shadow
    protected abstract float func_82204_b(float p_82204_1_, float p_82204_2_, float p_82204_3_);

    @Shadow
    protected abstract double func_82208_v(int p_82208_1_);

    @Shadow
    protected abstract double func_82213_w(int p_82213_1_);

    @Shadow
    public abstract int getInvulTime();

    public MixinEntityWither() {
        super(null);
    }

    /**
     * Fix particles when invis
     *
     * @author Timmi6790
     */
    @Override
    @Overwrite
    public void onLivingUpdate() {
        this.motionY *= 0.6000000238418579D;

        if (!this.worldObj.isRemote && this.getWatchedTargetId(0) > 0) {
            final Entity entity = this.worldObj.getEntityByID(this.getWatchedTargetId(0));

            if (entity != null) {
                if (this.posY < entity.posY || !this.isArmored() && this.posY < entity.posY + 5.0D) {
                    if (this.motionY < 0.0D) {
                        this.motionY = 0.0D;
                    }

                    this.motionY += (0.5D - this.motionY) * 0.6000000238418579D;
                }

                final double d0 = entity.posX - this.posX;
                final double d1 = entity.posZ - this.posZ;
                final double d3 = d0 * d0 + d1 * d1;

                if (d3 > 9.0D) {
                    final double d5 = MathHelper.sqrt_double(d3);
                    this.motionX += (d0 / d5 * 0.5D - this.motionX) * 0.6000000238418579D;
                    this.motionZ += (d1 / d5 * 0.5D - this.motionZ) * 0.6000000238418579D;
                }
            }
        }

        if (this.motionX * this.motionX + this.motionZ * this.motionZ > 0.05000000074505806D) {
            this.rotationYaw = (float) MathHelper.atan2(this.motionZ, this.motionX) * (180F / (float) Math.PI) - 90.0F;
        }

        super.onLivingUpdate();

        for (int i = 0; i < 2; ++i) {
            this.field_82218_g[i] = this.field_82221_e[i];
            this.field_82217_f[i] = this.field_82220_d[i];
        }

        for (int j = 0; j < 2; ++j) {
            final int k = this.getWatchedTargetId(j + 1);
            Entity entity1 = null;

            if (k > 0) {
                entity1 = this.worldObj.getEntityByID(k);
            }

            if (entity1 != null) {
                final double d11 = this.func_82214_u(j + 1);
                final double d12 = this.func_82208_v(j + 1);
                final double d13 = this.func_82213_w(j + 1);
                final double d6 = entity1.posX - d11;
                final double d7 = entity1.posY + (double) entity1.getEyeHeight() - d12;
                final double d8 = entity1.posZ - d13;
                final double d9 = MathHelper.sqrt_double(d6 * d6 + d8 * d8);
                final float f = (float) (MathHelper.atan2(d8, d6) * 180.0D / Math.PI) - 90.0F;
                final float f1 = (float) (-(MathHelper.atan2(d7, d9) * 180.0D / Math.PI));
                this.field_82220_d[j] = this.func_82204_b(this.field_82220_d[j], f1, 40.0F);
                this.field_82221_e[j] = this.func_82204_b(this.field_82221_e[j], f, 10.0F);
            } else {
                this.field_82221_e[j] = this.func_82204_b(this.field_82221_e[j], this.renderYawOffset, 10.0F);
            }
        }

        if (!this.isInvisible()) {
            final boolean isArmored = this.isArmored();
            for (int l = 0; l < 3; ++l) {
                final double d10 = this.func_82214_u(l);
                final double d2 = this.func_82208_v(l);
                final double d4 = this.func_82213_w(l);
                this.worldObj.spawnParticle(
                        EnumParticleTypes.SMOKE_NORMAL,
                        d10 + this.rand.nextGaussian() * 0.30000001192092896D,
                        d2 + this.rand.nextGaussian() * 0.30000001192092896D,
                        d4 + this.rand.nextGaussian() * 0.30000001192092896D,
                        0.0D,
                        0.0D,
                        0.0D
                );

                if (isArmored && this.worldObj.rand.nextInt(4) == 0) {
                    this.worldObj.spawnParticle(
                            EnumParticleTypes.SPELL_MOB,
                            d10 + this.rand.nextGaussian() * 0.30000001192092896D,
                            d2 + this.rand.nextGaussian() * 0.30000001192092896D,
                            d4 + this.rand.nextGaussian() * 0.30000001192092896D,
                            0.699999988079071D,
                            0.699999988079071D,
                            0.5D
                    );
                }
            }

            if (this.getInvulTime() > 0) {
                for (int i1 = 0; i1 < 3; ++i1) {
                    this.worldObj.spawnParticle(
                            EnumParticleTypes.SPELL_MOB,
                            this.posX + this.rand.nextGaussian() * 1.0D,
                            this.posY + (double) (this.rand.nextFloat() * 3.3F),
                            this.posZ + this.rand.nextGaussian() * 1.0D,
                            0.699999988079071D,
                            0.699999988079071D,
                            0.8999999761581421D
                    );
                }
            }
        }
    }
}
