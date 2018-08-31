/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package soup.widget.util

import android.animation.Animator
import android.animation.TimeInterpolator
import android.content.Context
import android.util.ArrayMap
import android.util.Property
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import java.util.*

/**
 * Utility methods for working with animations.
 */
object AnimUtils {

    private var fastOutSlowIn: Interpolator? = null
    private var fastOutLinearIn: Interpolator? = null
    private var linearOutSlowIn: Interpolator? = null

    @JvmStatic
    fun getFastOutSlowInInterpolator(context: Context): Interpolator? {
        if (fastOutSlowIn == null) {
            fastOutSlowIn = AnimationUtils.loadInterpolator(context,
                    android.R.interpolator.fast_out_slow_in)
        }
        return fastOutSlowIn
    }

    fun getFastOutLinearInInterpolator(context: Context): Interpolator? {
        if (fastOutLinearIn == null) {
            fastOutLinearIn = AnimationUtils.loadInterpolator(context,
                    android.R.interpolator.fast_out_linear_in)
        }
        return fastOutLinearIn
    }

    fun getLinearOutSlowInInterpolator(context: Context): Interpolator? {
        if (linearOutSlowIn == null) {
            linearOutSlowIn = AnimationUtils.loadInterpolator(context,
                    android.R.interpolator.linear_out_slow_in)
        }
        return linearOutSlowIn
    }

    /**
     * Linear interpolate between a and b with parameter t.
     */
    fun lerp(a: Float, b: Float, t: Float): Float = a + (b - a) * t


    /**
     * An implementation of [android.util.Property] to be used specifically with fields of
     * type
     * `float`. This type-specific subclass enables performance benefit by allowing
     * calls to a [set()][.set] function that takes the primitive
     * `float` type and avoids autoboxing and other overhead associated with the
     * `Float` class.
     *
     * @param <T> The class on which the Property is declared.
    </T> */
    abstract class FloatProperty<T>(name: String) : Property<T, Float>(Float::class.java, name) {

        /**
         * A type-specific override of the [.set] that is faster when dealing
         * with fields of type `float`.
         */
        abstract fun setValue(`object`: T, value: Float)

        override fun set(`object`: T, value: Float?) {
            setValue(`object`, value!!)
        }
    }

    /**
     * An implementation of [android.util.Property] to be used specifically with fields of
     * type
     * `int`. This type-specific subclass enables performance benefit by allowing
     * calls to a [set()][.set] function that takes the primitive
     * `int` type and avoids autoboxing and other overhead associated with the
     * `Integer` class.
     *
     * @param <T> The class on which the Property is declared.
    </T> */
    abstract class IntProperty<T>(name: String) : Property<T, Int>(Int::class.java, name) {

        /**
         * A type-specific override of the [.set] that is faster when dealing
         * with fields of type `int`.
         */
        abstract fun setValue(`object`: T, value: Int)

        override fun set(`object`: T, value: Int?) {
            setValue(`object`, value!!)
        }

    }

    /**
     * https://halfthought.wordpress.com/2014/11/07/reveal-transition/
     *
     *
     * Interrupting Activity transitions can yield an OperationNotSupportedException when the
     * transition tries to pause the animator. Yikes! We can fix this by wrapping the Animator:
     */
    class NoPauseAnimator(private val animator: Animator) : Animator() {

        private val listeners = ArrayMap<Animator.AnimatorListener, Animator.AnimatorListener>()

        override fun addListener(listener: Animator.AnimatorListener) {
            val wrapper = AnimatorListenerWrapper(this, listener)
            if (!listeners.containsKey(listener)) {
                listeners[listener] = wrapper
                animator.addListener(wrapper)
            }
        }

        override fun cancel() {
            animator.cancel()
        }

        override fun end() {
            animator.end()
        }

        override fun getDuration(): Long {
            return animator.duration
        }

        override fun getInterpolator(): TimeInterpolator {
            return animator.interpolator
        }

        override fun setInterpolator(timeInterpolator: TimeInterpolator) {
            animator.interpolator = timeInterpolator
        }

        override fun getListeners(): ArrayList<Animator.AnimatorListener> {
            return ArrayList(listeners.keys)
        }

        override fun getStartDelay(): Long {
            return animator.startDelay
        }

        override fun setStartDelay(delayMS: Long) {
            animator.startDelay = delayMS
        }

        override fun isPaused(): Boolean {
            return animator.isPaused
        }

        override fun isRunning(): Boolean {
            return animator.isRunning
        }

        override fun isStarted(): Boolean {
            return animator.isStarted
        }

        /* We don't want to override pause or resume methods because we don't want them
         * to affect animator.
        public void pause();
        public void resume();
        public void addPauseListener(AnimatorPauseListener listener);
        public void removePauseListener(AnimatorPauseListener listener);
        */

        override fun removeAllListeners() {
            listeners.clear()
            animator.removeAllListeners()
        }

        override fun removeListener(listener: Animator.AnimatorListener) {
            val wrapper = listeners[listener]
            if (wrapper != null) {
                listeners.remove(listener)
                animator.removeListener(wrapper)
            }
        }

        override fun setDuration(durationMS: Long): Animator {
            animator.duration = durationMS
            return this
        }

        override fun setTarget(target: Any) = animator.setTarget(target)

        override fun setupEndValues() = animator.setupEndValues()

        override fun setupStartValues() = animator.setupStartValues()

        override fun start() = animator.start()
    }

    internal class AnimatorListenerWrapper(private val animator: Animator,
                                           private val listener: Animator.AnimatorListener) :
            Animator.AnimatorListener {

        override fun onAnimationStart(animator: Animator) =
                listener.onAnimationStart(this.animator)

        override fun onAnimationEnd(animator: Animator) =
                listener.onAnimationEnd(this.animator)

        override fun onAnimationCancel(animator: Animator) =
                listener.onAnimationCancel(this.animator)

        override fun onAnimationRepeat(animator: Animator) =
                listener.onAnimationRepeat(this.animator)
    }
}
