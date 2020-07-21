package io.github.f77.simplechan.swipes_decoration_utils

import android.content.Context
import android.graphics.Canvas
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import io.github.f77.simplechan.bloc_utils.EventsAwareInterface
import io.github.f77.simplechan.bloc_utils.HasEventsHandler
import io.github.f77.simplechan.bloc_utils.event.Events
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

/**
 * @see <a href="https://github.com/xabaras/RecyclerViewSwipeDecorator">RecyclerViewSwipeDecorator</a>
 */
class ItemSwipeTouchCallback(override val eventsHandler: EventsAwareInterface) : ItemTouchHelper.Callback(),
    HasEventsHandler {

    var leftSwipeConfig: SwipeConfig? = null
    var rightSwipeConfig: SwipeConfig? = null
    var isLongPressDrag: Boolean = true
    var isItemViewSwipe: Boolean = true

    open class SwipeConfig {
        open var label: String? = null
        open var labelColor: Int? = null
        open var backgroundColor: Int? = null
        open var icon: Int? = null
        open var iconTintColor: Int? = null

        open fun resolveColorFromAttr(context: Context, @AttrRes attr: Int): Int {
            val color = TypedValue()
            context.theme.resolveAttribute(attr, color, true)
            return color.data
        }
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        eventsHandler.addEvent(Events.itemMoved(viewHolder.adapterPosition, target.adapterPosition))
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (direction == ItemTouchHelper.START) {
            eventsHandler.addEvent(Events.itemSwipedLeft(viewHolder.adapterPosition))
        } else {
            eventsHandler.addEvent(Events.itemSwipedRight(viewHolder.adapterPosition))
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val builder = RecyclerViewSwipeDecorator
            .Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        leftSwipeConfig?.label?.let { builder.addSwipeLeftLabel(it) }
        leftSwipeConfig?.labelColor?.let { builder.setSwipeLeftLabelColor(it) }
        leftSwipeConfig?.backgroundColor?.let { builder.addSwipeLeftBackgroundColor(it) }
        leftSwipeConfig?.icon?.let { builder.addSwipeLeftActionIcon(it) }
        leftSwipeConfig?.iconTintColor?.let { builder.setSwipeLeftActionIconTint(it) }

        rightSwipeConfig?.label?.let { builder.addSwipeRightLabel(it) }
        rightSwipeConfig?.labelColor?.let { builder.setSwipeRightLabelColor(it) }
        rightSwipeConfig?.backgroundColor?.let { builder.addSwipeRightBackgroundColor(it) }
        rightSwipeConfig?.icon?.let { builder.addSwipeRightActionIcon(it) }
        rightSwipeConfig?.iconTintColor?.let { builder.setSwipeRightActionIconTint(it) }

        builder.create().decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return isLongPressDrag
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return isItemViewSwipe
    }
}
