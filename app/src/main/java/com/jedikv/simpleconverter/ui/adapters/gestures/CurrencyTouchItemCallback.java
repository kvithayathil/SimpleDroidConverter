package com.jedikv.simpleconverter.ui.adapters.gestures;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import timber.log.Timber;

/**
 * Simple implementation of {@link ItemTouchHelperAdapter} to handle dragging
 * or swiping gestures for a recycler view list item
 *
 * Derived from <a href="https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-b9456d2b1aaf">this blog</a>
 * <br/>
 * Created by KV_87 on 26/07/15.
 */
public class CurrencyTouchItemCallback extends ItemTouchHelper.Callback {

    public static final String TAG = CurrencyTouchItemCallback.class.getSimpleName();

    private ItemTouchHelperAdapter itemTouchHelperAdapter;

    private static final float ALPHA_FULL = 1.0f;


    public CurrencyTouchItemCallback(ItemTouchHelperAdapter adapter) {
        Timber.tag(TAG);

        itemTouchHelperAdapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        itemTouchHelperAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // remove from adapter
        itemTouchHelperAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        // Fade out the view as it is swiped out of the parent's bounds
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View itemView = viewHolder.itemView;
            final float alpha = ALPHA_FULL - Math.abs(dX) / (float) itemView.getWidth();
            itemView.setAlpha(alpha);
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {

        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            //Perform action when an action is performed on this list item
            if(viewHolder instanceof ItemTouchHelperViewHolder) {
                ((ItemTouchHelperViewHolder)viewHolder).onItemSelected();
            }
        } else {
            super.onSelectedChanged(viewHolder, actionState);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        //Restore the alpha once the drag has finished
        viewHolder.itemView.setAlpha(ALPHA_FULL);

        if(viewHolder instanceof ItemTouchHelperViewHolder) {
            ((ItemTouchHelperViewHolder)viewHolder).onItemClear();
        }
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }
}
