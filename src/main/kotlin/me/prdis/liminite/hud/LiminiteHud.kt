package me.prdis.liminite.hud

import me.prdis.liminite.LiminiteClient.client
import me.prdis.liminite.timer.LiminiteTimer
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter

class LiminiteHud : HudRenderCallback {
    override fun onHudRender(drawContext: DrawContext, tickDeltaManager: RenderTickCounter) {
        val text = "${LiminiteTimer.REMAINING_COUNT.coerceAtLeast(0)}초"
        val matrices = drawContext.matrices
        val scaleFactor = client.window.scaleFactor

        matrices.push()

        val inverseScale = (1.0 / scaleFactor).toFloat()

        matrices.scale(inverseScale, inverseScale, 1F)
        matrices.scale(5F, 5F, 1F)

        val offset = 30

        val x = (drawContext.scaledWindowWidth * scaleFactor.toInt()) / 5 - offset

        drawContext.drawText(client.textRenderer, text, x, 10, 0xFFFFFF, true)

        matrices.pop()
    }

    companion object {
        val INSTANCE = LiminiteHud()

        fun register() {
            HudRenderCallback.EVENT.register(INSTANCE)
        }
    }
}