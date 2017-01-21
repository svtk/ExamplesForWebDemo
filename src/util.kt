package util

fun String.getSubsectionNumber(): String {
    val chapterNumber = substringBefore(".")
    val sectionNumber = substringAfter(".").substringBeforeAny('.', '_')
    return chapterNumber + "." + sectionNumber
}

fun String.getChapterNumber(): String {
    return "ch" + "%02d".format(substringBefore(".").toInt())
}

fun String.getExampleName(): String {
    return substringAfter(getSubsectionNumber() + ".")
}

fun String.getExampleNumber(): Int {
    return substringBeforeAny('.', '_').toInt()
}

fun String.substringBeforeAny(vararg delimiters: Char): String {
    val index = indexOfAny(delimiters)
    return if (index == -1) this else substring(0, index)
}
