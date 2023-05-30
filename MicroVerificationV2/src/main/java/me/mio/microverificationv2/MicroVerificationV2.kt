package me.mio.microverificationv2

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class MicroVerificationV2: JavaPlugin(), Listener, CommandExecutor {

    private val verificationPrefix = "§3ᴍɪᴄʀᴏ§aᴠᴇʀɪꜰɪᴄᴀᴛɪᴏɴ §6ᴠ2"; private val verificationCodes = mutableMapOf<UUID, String>()

    override fun onEnable() { server.pluginManager.registerEvents(this, this); getCommand("verify")!!.setExecutor(this) }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) { return false }

        if (args.isEmpty()) {
            val verificationCode = generateVerificationCode(); verificationCodes[sender.uniqueId] = verificationCode
            sender.sendMessage("$verificationPrefix §aTo verify your account, please type: §a$verificationCode") } else { sender.sendMessage("§cCorrect Usage: /verify") }

        return true
    }

    @EventHandler
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        val player = event.player; val verificationCode = verificationCodes[player.uniqueId]

        if (verificationCode != null) {
            event.isCancelled = true

            if (event.message.equals(verificationCode, ignoreCase = true)) { player.sendMessage("$verificationPrefix §aYou've been successfully verified!"); verificationCodes.remove(player.uniqueId) } else { player.sendMessage("$verificationPrefix §cIncorrect verification code.") }
        }
    }

    private fun generateVerificationCode(): String {
        val verificationCodeLength = 6; val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; val verificationCodeBuilder = StringBuilder()
        for (i in 1..verificationCodeLength) { val randomChar = chars.random(); verificationCodeBuilder.append(randomChar) }
        return verificationCodeBuilder.toString()
    }
}
