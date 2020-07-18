package io.github.f77.simplechan.ui.swipe_configs

import android.content.Context
import io.github.f77.simplechan.R
import io.github.f77.simplechan.swipes_decoration_utils.ItemSwipeTouchCallback

class EditSwipeConfig(context: Context) : ItemSwipeTouchCallback.SwipeConfig() {
    override var label: String? = "EDIT"
    override var labelColor: Int? = resolveColorFromAttr(context, R.attr.colorSwipeLabel)
    override var backgroundColor: Int? = resolveColorFromAttr(context, R.attr.colorBackgroundSwipeEditItem)
    override var icon: Int? = android.R.drawable.ic_menu_edit
}
