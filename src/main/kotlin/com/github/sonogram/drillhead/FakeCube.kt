package com.github.sonogram.drillhead

import com.google.common.collect.ImmutableList
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class FakeCube(val center: Location, blocks: List<Block>) {
    private val fakeBlocks: List<FakeBlock>
    var valid = true
    var gap = 0.625
    init {
        val fakeBlocks = arrayListOf<FakeBlock>()
        val centerX = (blocks.minOf { it.x } + blocks.maxOf { it.x }) / 2.0
        val centerY = (blocks.minOf { it.y }).toDouble()
        val centerZ = (blocks.minOf { it.z } + blocks.maxOf { it.z }) / 2.0
        for(block in blocks) {
            val type = block.type; if(block.type.isAir || !block.type.isSolid) continue
            val vector = Vector(block.x - centerX, block.y - centerY, block.z - centerZ)
            val fakeBlock = FakeBlock(this, vector, ItemStack(type))
            fakeBlocks += fakeBlock
        }
        this.fakeBlocks = ImmutableList.copyOf(fakeBlocks)
    }
    private var rotateSpeed = 0.0F
    private var ticks = 0
    private var ySpeed = 0.0
    fun update() {
        ticks++
        if(ticks < 100) {
            rotateSpeed += 0.1F
        }
        if(ticks == 100) {
            ySpeed = 1.0
        }
        fakeBlocks.forEach { it.update() }
        center.yaw += rotateSpeed
        center.y -= ySpeed
    }
    fun remove() {
        valid = false
        fakeBlocks.forEach { it.remove() }
    }
}