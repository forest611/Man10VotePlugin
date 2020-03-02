package red.man10.man10voteplugin.votecoin

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import red.man10.man10voteplugin.Man10VotePlugin

class VoteCoin(private val pl:Man10VotePlugin):CommandExecutor{

    val prefix = "§d§l[§f§k§lXX§a§lH§2§la§e§lp§d§lp§b§ly§6§lCoin§f§k§lXX§a§l]§r"
    var stat = 0
    var voteCoinEnable = true

    lateinit var coinStack : ItemStack

    fun loadConfig(){

        pl.saveDefaultConfig()

        val c = pl.config!!
        coinStack = c.getItemStack("voting.item")

        stat = c.getInt("voting.stat")

    }

    fun saveConfig(){

        val c = pl.config!!

        c.set("voting.enable",voteCoinEnable)
        c.set("voting.stat",stat)
        c.set("voting.item",coinStack)
    }


    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {

        if (sender !is Player)return false

        if (label != "votecoin")return false

        if (!sender.hasPermission("man10voting.op"))return false


        if (args.isNullOrEmpty()){

            return false
        }

        val cmd = args[0]

        //手持ちコインを登録(config紛失用)
        if (cmd == "register"){

            val item = sender.inventory.itemInMainHand
            coinStack = item
            Thread(Runnable {
                saveConfig()
            }).start()
            return true

        }

        if (cmd == "on"){
            voteCoinEnable = true
            sender.sendMessage("§e§lハッピーコインをonにしました")
            return true
        }

        if (cmd == "off"){
            voteCoinEnable = false
            sender.sendMessage("§e§lハッピーコインをoffにしました")
            return true
        }

        return false
    }


}