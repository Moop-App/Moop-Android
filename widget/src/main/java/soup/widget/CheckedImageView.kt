package soup.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Checkable
import android.widget.ImageView

import java.lang.reflect.InvocationTargetException

@SuppressLint("AppCompatCustomView")
class CheckedImageView @JvmOverloads constructor(context: Context,
                                                 attrs: AttributeSet? = null,
                                                 defStyleAttr: Int = R.attr.checkedImageViewStyle,
                                                 defStyleRes: Int = 0) :
        ImageView(context, attrs, defStyleAttr, defStyleRes), Checkable {

    private var checked: Boolean = false
    private var broadcasting: Boolean = false

    private var onCheckedChangeListener: OnCheckedChangeListener? = null

    init {
        val a = context.obtainStyledAttributes(
                attrs, R.styleable.CheckedImageView, defStyleAttr, defStyleRes)

        val checked = a.getBoolean(R.styleable.CheckedImageView_checked, false)
        isChecked = checked

        a.recycle()
    }

    override fun toggle() {
        isChecked = !checked
    }

    override fun isChecked(): Boolean {
        return checked
    }

    override fun setChecked(checked: Boolean) {
        if (this.checked != checked) {
            this.checked = checked
            refreshDrawableState()
            notifyViewAccessibilityStateChangedIfNeeded(
                    AccessibilityEvent.CONTENT_CHANGE_TYPE_UNDEFINED)

            // Avoid infinite recursions if setChecked() is called from a listener
            if (broadcasting) {
                return
            }

            broadcasting = true
            onCheckedChangeListener?.onCheckedChanged(this, this.checked)
            broadcasting = false
        }
    }

    fun setOnCheckedChangeListener(listener: OnCheckedChangeListener) {
        onCheckedChangeListener = listener
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(buttonView: CheckedImageView, isChecked: Boolean)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        return super.onCreateDrawableState(extraSpace + 1).also {
            if (isChecked) {
                View.mergeDrawableStates(it, CHECKED_STATE_SET)
            }
        }
    }

    override fun onInitializeAccessibilityEvent(event: AccessibilityEvent) {
        super.onInitializeAccessibilityEvent(event)
        with(event) {
            className = CheckedImageView::class.java.name
            isChecked = checked
        }
    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        with(info) {
            className = CheckedImageView::class.java.name
            isCheckable = true
            isChecked = checked
        }
    }

    /** call @hide API in View.java using reflection  */
    private fun notifyViewAccessibilityStateChangedIfNeeded(changeType: Int) {
        try {
            val method = android.view.View::class.java.getMethod(
                    "notifyViewAccessibilityStateChangedIfNeeded",
                    *arrayOf<Class<*>>(Int::class.javaPrimitiveType!!))
            method.invoke(this, *arrayOf<Any>(changeType))
        } catch (e: NoSuchMethodException) {
        } catch (e: IllegalAccessException) {
        } catch (e: InvocationTargetException) {
        }
    }

    companion object {

        private val CHECKED_STATE_SET = intArrayOf(android.R.attr.state_checked)
    }
}