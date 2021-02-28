package com.github.sonogram.drillhead

import com.github.monun.tap.fake.FakeEntity
import com.github.monun.tap.math.toRadians
import com.github.monun.tap.ref.UpstreamReference
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.ArmorStand
import org.bukkit.inventory.ItemStack
import org.bukkit.util.EulerAngle
import org.bukkit.util.Vector

class FakeBlock(
    cube: FakeCube,
    private val vector: Vector,
    private val item: ItemStack
) {
    private val cubeRef = UpstreamReference(cube)
    private val cube
        get() = cubeRef.get()
    val location: Location
        get() {
            val center = cube.center.clone()
            val offset = vector.clone().multiply(cube.gap).rotateAroundY(-center.yaw.toDouble().toRadians())
            return center.add(offset)
        }
    private val fakeEntity: FakeEntity
    init {
        fakeEntity = DrillHead.fakeEntityServer.spawnEntity(location, ArmorStand::class.java).apply {
            updateMetadata<ArmorStand> {
                isInvisible = true
                isMarker = true
                headPose = EulerAngle.ZERO
            }
            updateEquipment {
                helmet = item
            }
        }
    }
    fun update() {
        fakeEntity.moveTo(location)
        fakeEntity.location.block.type = Material.AIR
        fakeEntity.location.world.playSound(fakeEntity.location, Sound.BLOCK_STONE_BREAK, 1.0F, 1.0F)
    }
    fun remove() {
        fakeEntity.remove()
    }
}