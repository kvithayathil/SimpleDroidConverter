package com.jedikv.simpleconverter.ui.adapters.gestures;

/**
 *
 * Implement in to any recyclerview adpaters that
 * you want to have drag and swipe gestures for
 *
 * Created by KV_87 on 26/07/15.
 */
public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
