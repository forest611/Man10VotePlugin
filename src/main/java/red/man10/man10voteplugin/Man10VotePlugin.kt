package red.man10.man10voteplugin

import org.bukkit.plugin.java.JavaPlugin

class Man10VotePlugin : JavaPlugin() {

    lateinit var voteCoin: VoteCoin

    override fun onEnable() { // Plugin startup logic

        voteCoin = VoteCoin(this)

        voteCoin.loadConfig()

        getCommand("man10votecoin")!!.setExecutor(voteCoin)


    }

    override fun onDisable() { // Plugin shutdown logic
        voteCoin.saveConfig()
    }
}