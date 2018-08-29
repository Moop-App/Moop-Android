package soup.movie.util

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View =
        LayoutInflater.from(this.context).inflate(layoutRes, this, false)
