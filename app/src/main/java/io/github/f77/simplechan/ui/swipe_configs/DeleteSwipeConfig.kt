package io.github.f77.simplechan.ui.swipe_configs

import android.content.Context
import io.github.f77.simplechan.R
import io.github.f77.simplechan.swipes_decoration_utils.ItemSwipeTouchCallback

class DeleteSwipeConfig(context: Context) : ItemSwipeTouchCallback.SwipeConfig() {
    override var label: String? = "DELETE"
    override var labelColor: Int? = resolveColorFromAttr(context, R.attr.colorSwipeLabel)
    override var backgroundColor: Int? = resolveColorFromAttr(context, R.attr.colorBackgroundSwipeDeleteItem)
    override var icon: Int? = android.R.drawable.ic_menu_delete
}
