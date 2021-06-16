/*
 * Copyright 2021 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package soup.movie.settings.impl

import android.content.SharedPreferences
import androidx.annotation.CallSuper
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class Preference<T>(
    protected open val defaultValue: T
) : ReadWriteProperty<Any, T> {

    private val channel = ConflatedBroadcastChannel(defaultValue)

    @CallSuper
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        channel.offer(value)
    }

    fun asFlow(): Flow<T> = channel.asFlow()
}

open class LongPreference(
    private val preferences: SharedPreferences,
    private val name: String,
    override val defaultValue: Long
) : Preference<Long>(defaultValue) {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Long {
        return preferences.getLong(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) {
        super.setValue(thisRef, property, value)
        preferences.edit { putLong(name, value) }
    }
}

open class IntPreference(
    private val preferences: SharedPreferences,
    private val name: String,
    override val defaultValue: Int
) : Preference<Int>(defaultValue) {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return preferences.getInt(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        super.setValue(thisRef, property, value)
        preferences.edit { putInt(name, value) }
    }
}

open class FloatPreference(
    private val preferences: SharedPreferences,
    private val name: String,
    override val defaultValue: Float
) : Preference<Float>(defaultValue) {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Float {
        return preferences.getFloat(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Float) {
        super.setValue(thisRef, property, value)
        preferences.edit { putFloat(name, value) }
    }
}

open class BooleanPreference(
    private val preferences: SharedPreferences,
    private val name: String,
    override val defaultValue: Boolean
) : Preference<Boolean>(defaultValue) {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return preferences.getBoolean(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        super.setValue(thisRef, property, value)
        preferences.edit { putBoolean(name, value) }
    }
}

open class StringPreference(
    private val preferences: SharedPreferences,
    private val name: String,
    override val defaultValue: String
) : Preference<String>(defaultValue) {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        return preferences.getString(name, defaultValue) ?: defaultValue
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
        super.setValue(thisRef, property, value)
        preferences.edit { putString(name, value) }
    }
}
