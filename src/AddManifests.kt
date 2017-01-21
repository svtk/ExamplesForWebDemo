package manifests

import com.fasterxml.jackson.databind.ObjectMapper
import util.getChapterNumber
import util.getExampleName
import util.getExampleNumber
import util.getSubsectionNumber
import java.io.File

fun main(args: Array<String>) {
    val initialDirName = "Kotlin In Action [kotlin-book]"
    val initialDir = File(initialDirName)

//    val resultDirName = "/Users/svtk/Projects2/kotlin-web-demo/kotlin.web.demo.server/examples/Kotlin in Action"
    val resultDirName = "Kotlin In Action [web-demo]"
    File(resultDirName).mkdir()

    val chapters = initialDir.listFiles()
    val chapterNames = chapters.map { it.name }
    val manifest = "/manifest.json"
    writeJson(Chapters(chapterNames), File(resultDirName + manifest))

    val chapterExamplesMap = chapters.associate {
        chapter ->
        val examples = chapter.listFiles()
        chapter.name to examples.filter { it.name.contains("kt") }.map { it.name.removeSuffix(".kt") }
    }
    val groupedBySubsection = chapterExamplesMap.values.flatMap { it }.groupBy(String::getSubsectionNumber)
    val chapterBySection = groupedBySubsection.keys.groupBy(String::getChapterNumber)

    for ((chapter, sections) in chapterBySection) {
        val chapterPath = resultDirName + "/" + chapter
        File(chapterPath).mkdir()
        val chapterInfo = ChapterInfo(chapter, chapter, sections)
        writeJson(chapterInfo, File(chapterPath + manifest))

        for (section in sections) {
            val examples = groupedBySubsection[section] ?: throw AssertionError("$section $chapter")
            val sectionPath = chapterPath + "/" + section
            File(sectionPath).mkdir()
            val fileInfoList = examples.map {
                val newName = it.getExampleName()
                val oldExampleFile = File("$initialDirName/$chapter/$it.kt")
                val newExampleFile = File("$sectionPath/$newName.kt")
                newExampleFile.writeText(oldExampleFile.readText())
                FileInfo(newExampleFile.name)
            }
            val sectionInfo = createSectionInfo(section, fileInfoList.sortedBy { it.filename.getExampleNumber() })
            writeJson(sectionInfo, File(sectionPath + manifest))
        }
    }
}

fun writeJson(data: Any, jsonFile: File) {
    val mapper = ObjectMapper()
    mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, data)
}

class Chapters(val folders: List<String>)
class ChapterInfo(val name: String, val folder: String, val examples: List<String>)
fun createSectionInfo(name: String, files: List<FileInfo>, args: String = "", confType: String = "java")
 = SectionInfo(name, files, args, confType)
class SectionInfo(val name: String, val files: List<FileInfo>, val args: String, val confType: String)
class FileInfo(val filename: String, val modifiable: String = "true")