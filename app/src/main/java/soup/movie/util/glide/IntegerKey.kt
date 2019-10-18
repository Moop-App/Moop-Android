package soup.movie.util.glide

import com.bumptech.glide.load.Key
import java.nio.ByteBuffer
import java.security.MessageDigest

data class IntegerKey(private val value: Int) : Key {

    override fun updateDiskCacheKey(md: MessageDigest) {
        md.update(ByteBuffer.allocate(Integer.SIZE).putInt(value).array())
    }
}
