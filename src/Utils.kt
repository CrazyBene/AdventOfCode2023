import java.io.IOException
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun readInput(fileName: String): List<String> {
    val path = Path("src/$fileName.txt")
    return try {
        path.readLines()
    } catch (e: IOException) {
        error("Could not find $fileName.txt file in src directory.")
    }
}

inline fun <reified T> List<List<T>>.transpose(): List<List<T>> {
    val cols = this[0].size
    val rows = this.size
    return List(cols) { j ->
        List(rows) { i ->
            this[i][j]
        }
    }
}