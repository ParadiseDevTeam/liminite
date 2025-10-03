package me.prdis.liminite.timer

import me.prdis.liminite.LiminiteClient
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.server.MinecraftServer
import net.minecraft.util.WorldSavePath
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.*

class LiminiteTimer : ServerTickEvents.EndTick {
    override fun onEndTick(server: MinecraftServer) {
        REMAINING_COUNT = 60 - (server.ticks / 20)

        if (server.ticks % 1200 == 0) {
            LiminiteClient.logger.info("Saving server...")
            server.saveAll(false, true, true)
            LiminiteClient.logger.info("Save operation completed.")

            LiminiteClient.logger.info("Creating a backup of server...")
            LiminiteClient.logger.info("Tick is ${server.ticks}")

            val currentFilePath = server.getSavePath(WorldSavePath.ROOT).parent

            LiminiteClient.logger.info("Current server path is $currentFilePath")

            val localDateTime = LocalDateTime.now()
            val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")
            val time = dtf.format(localDateTime)

            val backupFilePath = currentFilePath.parent.resolve("${currentFilePath.name}_${time}_${server.ticks}")

            LiminiteClient.logger.info("Backup server destination is $backupFilePath")

            try {
                @OptIn(ExperimentalPathApi::class)
                currentFilePath.copyToRecursively(backupFilePath, { source, target, exception ->
                    if (exception !is FileSystemException) {
                        LiminiteClient.logger.error("Failed to copy $source to $target - ${exception.message}")

                        exception.printStackTrace()
                    }

                    OnErrorResult.SKIP_SUBTREE
                }, followLinks = true, overwrite = false)

                val worldLock = backupFilePath.resolve("session.lock")
                val netherLock = backupFilePath.resolve("DIM-1").resolve("session.lock")
                val endLock = backupFilePath.resolve("DIM1").resolve("session.lock")

                worldLock.deleteIfExists()
                netherLock.deleteIfExists()
                endLock.deleteIfExists()
            } catch (e: Exception) {
                LiminiteClient.logger.error("Failed to copy backup file ${backupFilePath}. Report this to the developer immediately!")
                e.printStackTrace()
            }
        }
    }

    companion object {
        val INSTANCE = LiminiteTimer()
        var REMAINING_COUNT = 0

        fun register() {
            ServerTickEvents.END_SERVER_TICK.register(INSTANCE)
        }
    }
}