package eft.weapons.builds.exports.web

import eft.weapons.builds.tree.weaponBuilds
import eft.weapons.builds.utils.Items
import eft.weapons.builds.utils.Locale.itemName
import eft.weapons.builds.utils.Locale.itemShortName
import eft.weapons.builds.utils.printJson
import eft.weapons.builds.utils.stringBuilder
import org.testng.annotations.Test
import java.nio.file.Files
import java.nio.file.Paths

class WebWeaponTreeBuilderTest {

    @Test
    fun `can export ak-74n`() {
        val weapon = Items["5644bd2b4bdc2d3b4c8b4572"]
        val itemTree = weaponBuilds(weapon)
        val weaponTree = processTree(itemTree)
        printJson(weaponTree)
        val tempFile = createTempFile(suffix = ".json")
        tempFile.writeText(stringBuilder(weaponTree))
        println(tempFile.absolutePath)
    }

    @Test
    fun `can export all weapons`() {
        val tempDir = createTempDir()
        val weaponTypes = Items.all().filter { it.parent == "5422acb9af1c889c16000029" }
        for (weaponType in weaponTypes) {
            val categoryName = itemName(weaponType.id).split(' ').joinToString("") { it.capitalize() }
            println("Exporting $categoryName")
            val weapons = Items.all().filter { it.parent == weaponType.id }
            for (weapon in weapons) {
                println("Exporting $categoryName - ${itemShortName(weapon.id)}")
                val itemTree = weaponBuilds(weapon)
                val weaponTree = processTree(itemTree)
                Paths.get(tempDir.absolutePath, categoryName).toFile().mkdirs()
                val file = Files.createFile(Paths.get(tempDir.absolutePath, categoryName, weaponTree.id + ".json")).toFile()
                file.createNewFile()
                file.writeText(stringBuilder(weaponTree))
            }
        }
        println(tempDir.absoluteFile)
    }

    @Test
    fun `can export weapons list`() {
        val weaponTypes = Items.all().filter { it.parent == "5422acb9af1c889c16000029" }
        val items: MutableList<WebWeaponListEntry> = mutableListOf()
        for (weaponType in weaponTypes) {
            val categoryName = itemName(weaponType.id).split(' ').joinToString("") { it.capitalize() }
            val weapons = Items.all().filter { it.parent == weaponType.id }
            for (weapon in weapons) {
                items.add(WebWeaponListEntry(weapon.id, categoryName, itemName(weapon.id)))
            }
        }
        printJson(items)
    }
}