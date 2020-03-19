package red.man10.man10voteplugin.votecoin

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import red.man10.man10voteplugin.Man10VotePlugin
import java.util.*


class VoteCoin(private val pl:Man10VotePlugin):CommandExecutor{

    val prefix = "§d§l[§f§k§lXX§a§lH§2§la§e§lp§d§lp§b§ly§6§lCoin§f§k§lXX§a§l]§r"
    var gotPlayers = mutableListOf<Player>()
    var stat = 0
    var voteCoinEnable = true
    var whenVoted  = 0L
    var count = 0
    var id = 0

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

        pl.saveConfig()
    }

    //コインを手に入れる
    fun getCoin(p:Player,id:Int){

        if (!p.hasPermission("man10voting.getcoin")){
            p.sendMessage("$prefix§c§lあなたはコインを手に入れることができません!")
            return
        }

        if (this.id != id){
            p.sendMessage("$prefix§cこのコインはもう受け取れないようです！")
            return
        }

        if (!voteCoinEnable){
            p.sendMessage("$prefix§e§l現在はコインが手に入らないようです")
            return
        }

        if (((Date().time - whenVoted)/1000) > 180){
            p.sendMessage("$prefix§c§lどうやら時間切れのようだ...")
            return
        }

        if (gotPlayers.contains(p))return

        stat ++
        gotPlayers.add(p)

        p.inventory.addItem(coinStack)
        p.sendMessage("$prefix§f§k§lXX§a§lH§2§la§e§lp§d§lp§b§ly§6§lCoin§f§k§lXX§f§lを受け取りました!");
    }

    fun voting(p:Player){

        if (!voteCoinEnable)return

        if (count>=4){

            this.id = Random().nextInt()

            val tc = TextComponent()
            Bukkit.broadcastMessage("${prefix}§a§l${p.name}さんが投票しました!§3§l5回目の投票です!")
            tc.text = "${prefix}§b§l§kaa§e§l§f §k§lXX§a§lH§2§la§e§lp§d§lp§b§ly§6§lCoin§f§k§lXX§f§lゲット->§6§l§n<ここをクリック>§f §b§l§kaa"
            tc.color = ChatColor.AQUA
            tc.isBold = true
            tc.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/man10votecoin getcoin $id")
            val ht = "§e§l§k|§f§l If you click here,you get one §e§lHappy§6§lCoin§f§l chance §e§l§k|"
            tc.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder(ht).create())
            Bukkit.spigot().broadcast(tc)

            for (player in Bukkit.getOnlinePlayers()) {
                val loc: Location = player.location
                loc.world.playSound(loc, Sound.RECORD_WAIT, 0.1f, 1f)
            }

            whenVoted = Date().time
            count = 0
            gotPlayers.clear()

            return
        }
        val tc = TextComponent()
        tc.text = "$prefix§a§l${p.name}さんが投票しました!§6§lみんなも投票しよう!§a§l§n[/vote]"
        tc.color = ChatColor.AQUA
        tc.isBold = true
        tc.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vote")
        val ht = "§e§l投票のURLを開きます"
        tc.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder(ht).create())
        Bukkit.spigot().broadcast(tc)
        Bukkit.broadcastMessage("$prefix§f§k§lXX§a§lH§2§la§e§lp§d§lp§b§ly§6§lCoin§f§k§lXX§f§lゲットまで...§e§l§n" + (4 - count) + "回§e§l")
        count ++

        //音を鳴らす
        for (player in Bukkit.getOnlinePlayers()) {
            val loc: Location = player.location
            loc.world.playSound(loc, Sound.RECORD_STRAD, 0.1f, 1f)
        }


    }


    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {

        if (label != "man10votecoin")return false

        if (args.isNullOrEmpty()){

            return false
        }

        val cmd = args[0]

        if (sender !is Player){

            if (args[0] == "voting"){
                voting(Bukkit.getPlayer(args[1]))
                return true
            }

            return false
        }

        if (cmd == "getcoin"){
            getCoin(sender,args[1].toInt())
            return true
        }

        if (!sender.hasPermission("man10voting.op"))return false


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

        if (cmd == "stat"){
            sender.sendMessage("§e§l$stat")
            return true
        }

        return false
    }

}