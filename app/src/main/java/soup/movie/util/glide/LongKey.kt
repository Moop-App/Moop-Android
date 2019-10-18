package soup.movie.util.glide

import com.bumptech.glide.load.Key
import java.nio.ByteBuffer
import java.security.MessageDigest

data class LongKey(private val value: Long) : Key {

    override fun updateDiskCacheKey(md: MessageDigest) {
        md.update(ByteBuffer.allocate(Integer.SIZE).putLong(value).array())
    }
}