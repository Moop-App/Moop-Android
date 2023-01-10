/*
 * Copyright 2023 SOUP
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
package soup.movie.log

import timber.log.Timber

object Logger {

    interface Tree {
        fun log(priority: Int, tag: String?, message: String, t: Throwable?)
    }

    fun plant(tree: Tree) {
        Timber.plant(tree.asTimber())
    }

    fun v(message: String, vararg args: Any?) {
        Timber.v(message = message, args = args)
    }

    fun v(t: Throwable?, message: String, vararg args: Any?) {
        Timber.v(t = t, message = message, args = args)
    }

    fun v(t: Throwable?) {
        Timber.v(t = t)
    }

    fun d(message: String, vararg args: Any?) {
        Timber.d(message = message, args = args)
    }

    fun d(t: Throwable?, message: String, vararg args: Any?) {
        Timber.d(t = t, message = message, args = args)
    }

    fun d(t: Throwable?) {
        Timber.d(t = t)
    }

    fun i(message: String, vararg args: Any?) {
        Timber.i(message = message, args = args)
    }

    fun i(t: Throwable?, message: String, vararg args: Any?) {
        Timber.i(t = t, message = message, args = args)
    }

    fun i(t: Throwable?) {
        Timber.i(t = t)
    }

    fun w(message: String, vararg args: Any?) {
        Timber.w(message = message, args = args)
    }

    fun w(t: Throwable?, message: String, vararg args: Any?) {
        Timber.w(t = t, message = message, args = args)
    }

    fun w(t: Throwable?) {
        Timber.w(t = t)
    }

    fun e(message: String, vararg args: Any?) {
        Timber.e(message = message, args = args)
    }

    fun e(t: Throwable?, message: String, vararg args: Any?) {
        Timber.e(t = t, message = message, args = args)
    }

    fun e(t: Throwable?) {
        Timber.e(t = t)
    }

    fun wtf(message: String, vararg args: Any?) {
        Timber.wtf(message = message, args = args)
    }

    fun wtf(t: Throwable?, message: String, vararg args: Any?) {
        Timber.wtf(t = t, message = message, args = args)
    }

    fun wtf(t: Throwable?) {
        Timber.wtf(t = t)
    }

    private fun Tree.asTimber(): Timber.Tree {
        val logTree: Tree = this
        return object : Timber.Tree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                logTree.log(priority, tag, message, t)
            }
        }
    }
}
