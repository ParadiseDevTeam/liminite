package me.prdis.liminite

import me.prdis.liminite.hud.LiminiteHud
import me.prdis.liminite.timer.LiminiteTimer
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.MinecraftClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object LiminiteClient : ClientModInitializer {
    val logger: Logger = LoggerFactory.getLogger("liminite")
    val client: MinecraftClient = MinecraftClient.getInstance()

    override fun onInitializeClient() {
        logger.info("Liminite mod has been initialized!")

        logger.info("Registering Liminite timer listener...")
        LiminiteTimer.register()

        logger.info("Registering Liminite HUD listener...")
        LiminiteHud.register()

        logger.info("End onInitializeClient")
    }
}
