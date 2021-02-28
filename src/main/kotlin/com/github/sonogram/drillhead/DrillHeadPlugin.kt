package com.github.sonogram.drillhead

import com.github.monun.tap.fake.FakeEntityServer
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.regions.Region
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin

class SpinHeadPlugin : JavaPlugin(), Listener, Runnable {
    val cubes = arrayListOf<FakeCube>()
    override fun onEnable() {
        DrillHead.fakeEntityServer = FakeEntityServer.create(this)
        server.run {
            pluginManager.registerEvents(this@SpinHeadPlugin, this@SpinHeadPlugin)
            scheduler.runTaskTimer(this@SpinHeadPlugin, this@SpinHeadPlugin, 0L, 1L)
        }
    }

    override fun run() {
        DrillHead.fakeEntityServer.update()
        cubes.forEach { it.update() }
        cubes.removeIf { !it.valid }
    }
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        DrillHead.fakeEntityServer.addPlayer(event.player)
    }
    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        DrillHead.fakeEntityServer.removePlayer(event.player)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = sender as Player
        val selection = player.selection ?: return true
        val world = player.world
        val blocks = arrayListOf<Block>()
        selection.forEach { v ->
            blocks.add(world.getBlockAt(v.x, v.y, v.z))
        }
        val cube = FakeCube(Location(world, 0.0, 30.0, 0.0), blocks)
        this.cubes += cube
        return true
    }
}
val Player.selection: Region?
    get() {
        return try {
            WorldEdit.getInstance().sessionManager[BukkitAdapter.adapt(this)]?.run {
                getSelection(selectionWorld)
            }
        } catch(e: Exception) {
            null
        }
    }