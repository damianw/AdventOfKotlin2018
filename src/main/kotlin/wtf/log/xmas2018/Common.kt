package wtf.log.xmas2018

import okio.Source
import okio.source
import java.io.BufferedReader

/**
 * Convenience function for opening a resource as a [BufferedReader]
 */
fun openResource(name: String): BufferedReader {
    return ClassLoader.getSystemResourceAsStream(name).bufferedReader()
}

/**
 * Convenience function for opening a resource as a [Source]
 */
fun openResourceAsSource(name: String): Source {
    return ClassLoader.getSystemResource(name).openStream().source()
}
