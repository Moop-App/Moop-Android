package soup.widget

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingListener

@BindingAdapter("android:checked")
fun setChecked(view: FilterChip, checked: Boolean) {
    if (view.isChecked != checked) {
        view.isChecked = checked
    }
}

@BindingAdapter(value = ["android:onCheckedChanged", "android:checkedAttrChanged"], requireAll = false)
fun setListeners(
    view: FilterChip,
    listener: FilterChip.OnCheckedChangeListener?,
    attrChange: InverseBindingListener?
) {
    if (attrChange == null) {
        view.setOnCheckedChangeListener(listener)
    } else {
        view.setOnCheckedChangeListener(object : FilterChip.OnCheckedChangeListener {
            override fun onCheckedChanged(chip: FilterChip, isChecked: Boolean) {
                listener?.onCheckedChanged(chip, isChecked)
                attrChange.onChange()
            }
        })
    }
}
