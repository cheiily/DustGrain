package dustgrain.core

import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.deleteExisting

// throwing method because File.deleteRecursively just returns false and fails silently
fun Path.deleteRecursively() {
    Files.walkFileTree(
        this,
        object : SimpleFileVisitor<Path>() {
            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                file.deleteExisting()
                return FileVisitResult.CONTINUE
            }

            override fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {
                if (exc != null)
                    throw exc
                dir.deleteExisting()
                return FileVisitResult.CONTINUE
            }
        }
    )
}