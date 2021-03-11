package com.tunasoftware.tunasdk.java.interfaces;

import android.view.View;

public interface RecyclerViewOnClickListener {

    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
