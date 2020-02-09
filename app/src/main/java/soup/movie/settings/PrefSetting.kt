package soup.movie.settings

import android.content.SharedPreferences
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

abstract class PrefSetting<T>(
    private val preferences: SharedPreferences
) : Setting<T> {

    private val channel = ConflatedBroadcastChannel(getDefaultValue(preferences))

    internal abstract fun getDefaultValue(preferences: SharedPreferences): T

    internal abstract fun saveValue(preferences: SharedPreferences, value: T)

    override fun set(value: T) {
        channel.offer(value)
        saveValue(preferences, value)
    }

    override fun get(): T = channel.valueOrNull ?: getDefaultValue(preferences)

    override fun asFlow(): Flow<T> = channel.asFlow()
}
