package recorder.sound.aliagus.com.soundrecorder.adapter

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import kotlinx.android.synthetic.main.costom_content.view.*

/**
 * Created by ali on 07/03/18.
 */
class RecyclerItemTouchHelper(
        dragDirs: Int,
        swipeDirs: Int,
        private var listener: ListenerRecyclerItemTouchHelper
) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null) {
            val foregroundView = (viewHolder as AdapterPlaylist.ViewHolderContent).itemView.relative_foreground
            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(foregroundView)
        }
    }

    override fun onChildDrawOver(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val foregroundView = (viewHolder as AdapterPlaylist.ViewHolderContent).itemView.relative_foreground
        ItemTouchHelper.Callback.getDefaultUIUtil().onDrawOver(
                c,
                recyclerView,
                foregroundView,
                dX,
                dY,
                actionState,
                isCurrentlyActive
        )
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?) {
        val foregroundView = (viewHolder as AdapterPlaylist.ViewHolderContent).itemView.relative_foreground
        ItemTouchHelper.Callback.getDefaultUIUtil().clearView(foregroundView)
    }

    override fun onChildDraw(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val foregroundView = (viewHolder as AdapterPlaylist.ViewHolderContent).itemView.relative_foreground
        ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(
                c,
                recyclerView,
                foregroundView,
                dX,
                dY,
                actionState,
                isCurrentlyActive
        )
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        if (viewHolder != null) {
            listener.onSwipeItem(viewHolder, direction, viewHolder.adapterPosition)
        }
    }

    interface ListenerRecyclerItemTouchHelper {
        fun onSwipeItem(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int)
    }
}