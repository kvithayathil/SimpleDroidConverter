package com.jedikv.simpleconverter.ui.adapters.gestures;

/**
 * Implement on a viewholder to react to drag and swipe
 * actions relating to {@link ItemTouchHelperAdapter}
 * Created by KV_87 on 22/07/15.
 */
public interface ItemTouchHelperViewHolder {

    void onItemSelected();

    void onItemClear();
}
