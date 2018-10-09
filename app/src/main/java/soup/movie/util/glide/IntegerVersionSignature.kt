package soup.movie.util.glide

import com.bumptech.glide.load.Key
import java.nio.ByteBuffer
import java.security.MessageDigest

class IntegerVersionSignature(private val currentVersion: Int) : Key {

    override fun equals(other: Any?): Boolean = when (other) {
        is IntegerVersionSignature -> currentVersion == other.currentVersion
        else -> false
    }

    override fun hashCode(): Int = currentVersion

    override fun updateDiskCacheKey(md: MessageDigest) {
        md.update(ByteBuffer.allocate(Integer.SIZE).putInt(currentVersion).array())
    }
}