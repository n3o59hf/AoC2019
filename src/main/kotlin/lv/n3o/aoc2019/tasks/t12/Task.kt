package lv.n3o.aoc2019.tasks.t12

import lv.n3o.aoc2019.coords.*
import lv.n3o.aoc2019.tasks.Task
import lv.n3o.aoc2019.tasks.cleanLines
import lv.n3o.aoc2019.tasks.lcm

@Suppress("unused")
class Task : Task() {
    val data by lazy {
        input
            .cleanLines
            .map { it.replace("<", "").replace(">", "") }
            .map { it.split(",").map { it.split("=")[1].toInt() } }
            .map { (x, y, z) ->
                Moon(C3(x, y, z), C3(0, 0, 0))
            }
    }

    fun moonSequence() = sequence {
        var moons = data
        yield(moons)
        while (true) {
            moons = moons
                .map { moon -> moon.updateVelocity(moons - moon) }
                .map { moon -> moon.updatePosition() }
            yield(moons)
        }
    }

    override fun a(): String ="${moonSequence().drop(1000).first().map(Moon::energy).sum()}"

    override fun b(): String {
        val cycleStartX = data.map { moon -> moon.pos.x to moon.vel.x }
        val cycleStartY = data.map { moon -> moon.pos.y to moon.vel.y }
        val cycleStartZ = data.map { moon -> moon.pos.z to moon.vel.z }

        var cycleX = 0L
        var cycleY = 0L
        var cycleZ = 0L

        var cycle = -1L

        val moonSequence = moonSequence().iterator()
        while (cycleX * cycleY * cycleZ == 0L) {
            cycle++
            moonSequence.next().let { moons ->
                if (cycleX == 0L) {
                    if (cycleStartX == moons.map { it.pos.x to it.vel.x }) cycleX = cycle
                }
                if (cycleY == 0L) {
                    if (cycleStartY == moons.map { it.pos.y to it.vel.y }) cycleY = cycle
                }
                if (cycleZ == 0L) {
                    if (cycleStartZ == moons.map { it.pos.z to it.vel.z }) cycleZ = cycle
                }
            }
        }

        return "${lcm(lcm(cycleX, cycleY), cycleZ)}"
    }
}

data class Moon(val pos: Coord3d, val vel: Coord3d) {
    val energy = C3(0, 0, 0).distance(pos) * C3(0, 0, 0).distance(vel)

    fun updateVelocity(otherMoons: Iterable<Moon>): Moon {
        var newVelocity = vel
        otherMoons.forEach { other ->
            newVelocity += (other.pos - pos).unit()
        }
        return Moon(pos, newVelocity)
    }

    fun updatePosition() = Moon(pos + vel, vel)
}