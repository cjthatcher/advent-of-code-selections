import java.io.File

val caveMap = mutableMapOf<String, Cave>()
val validPaths = mutableSetOf<List<String>>()


fun main(args: Array<String>) {
    File("resources/2021/12.txt").readLines().forEach { populateCaves(it) }

    //Urg. We need to generate a bunch of different maps where each map has a different small cave turned into a TWICE cave.

    caveMap.values.filter { it.visitable == Visitable.LITTLE && !it.name.equals("start") && !it.name.equals("end")}.forEach {
        val trashMap = caveMap.toMutableMap()
        trashMap.put(it.name, Cave(it.name, Visitable.TWICE, it.neighbors))
        exploreCave("start", mutableListOf(), trashMap)
    }

    println("valid paths size: ${validPaths.size}")
//    validPaths.toList().sortedBy { it.size }.forEach { println(it) }
}

fun exploreCave(node: String, pathSoFar: MutableList<String>, caves: Map<String, Cave>) {
    val currentCave = caves.get(node) ?: return
    if (currentCave.visitable == Visitable.LITTLE && pathSoFar.contains(currentCave.name)) {
        return
    }

    if (currentCave.visitable == Visitable.TWICE && pathSoFar.count { it.equals(currentCave.name) } >= 2) {
        return
    }

    val updatedPath = mutableListOf(*pathSoFar.toTypedArray(), node)

    if (currentCave.name.equals("end")) {
        validPaths.add(updatedPath.toList())
        return
    }

    currentCave.neighbors.forEach { neighbor -> exploreCave(neighbor, updatedPath, caves) }
}

fun populateCaves(line: String) {
    val c1 = line.split("-")[0]
    val c2 = line.split("-")[1]

    if (!caveMap.containsKey(c1)) {
        caveMap.put(c1, Cave(c1, if (c1.toCharArray()[0].isUpperCase()) Visitable.BIG else Visitable.LITTLE))
    }
    if (!caveMap.containsKey(c2)) {
        caveMap.put(c2, Cave(c2, if (c2.toCharArray()[0].isUpperCase()) Visitable.BIG else Visitable.LITTLE))
    }

    caveMap.get(c1)?.neighbors?.add(c2)
    caveMap.get(c2)?.neighbors?.add(c1)
}

data class Cave(val name: String, val visitable: Visitable, var neighbors: MutableSet<String> = mutableSetOf())

enum class Visitable {
    BIG, LITTLE, TWICE
}