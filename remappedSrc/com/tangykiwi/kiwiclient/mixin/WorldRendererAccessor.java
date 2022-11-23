package com.tangykiwi.kiwiclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.WorldRenderer;

@Mixin(WorldRenderer.class)
public interface WorldRendererAccessor {

    @Accessor
    public abstract Framebuffer getEntityOutlinesFramebuffer();

    @Accessor
    public abstract void setEntityOutlinesFramebuffer(Framebuffer framebuffer);

    @Accessor
    public abstract PostEffectProcessor getEntityOutlineShader();

    @Accessor
    public abstract void setEntityOutlineShader(PostEffectProcessor shaderEffect);

    @Accessor
    public abstract Frustum getFrustum();

    @Accessor
    public abstract void setFrustum(Frustum frustum);
}
