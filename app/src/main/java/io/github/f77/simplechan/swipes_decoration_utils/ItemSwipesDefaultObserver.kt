package io.github.f77.simplechan.swipes_decoration_utils

import androidx.recyclerview.widget.RecyclerView
import io.github.f77.simplechan.bloc_utils.action.interfaces.ActionInterface
import io.github.f77.simplechan.bloc_utils.action.interfaces.ItemChangedActionInterface
import io.github.f77.simplechan.bloc_utils.action.interfaces.ItemMovedActionInterface
import io.github.f77.simplechan.bloc_utils.action.interfaces.ItemRemovedActionInterface

class ItemSwipesDefaultObserver {
    companion object {
        fun notifyItemsChanges(action: ActionInterface, adapter: RecyclerView.Adapter<*>) {
            when (action) {
                is ItemRemovedActionInterface -> {
                    adapter.notifyItemRemoved(action.position)
                }
                is ItemChangedActionInterface -> {
                    adapter.notifyItemChanged(action.position)
                }
                is ItemMovedActionInterface -> {
                    adapter.notifyItemMoved(action.fromPosition, action.toPosition)
                }
            }
        }
    }
}
