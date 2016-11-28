package dev.saxionroosters.general;

import android.view.View;

/**
 * Created by jelle on 28/11/2016.
 *
 * Can be used for the RecyclerView as a clicklistener on list items.
 */
public interface ClickListener {
    void onClick(View v, int position, boolean isLongClick);
}
