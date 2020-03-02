package red.man10.man10voteplugin.votejackpot

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import red.man10.man10voteplugin.Man10VotePlugin

class VoteJackpot(private val pl : Man10VotePlugin) :CommandExecutor{
    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {

        if (label != "mvj")return false


        return false
    }


}