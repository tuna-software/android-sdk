package com.tunasoftware.tunasdk.java.listners;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tunasoftware.tunasdk.java.interfaces.RecyclerViewOnClickListener;

public class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {

    private final RecyclerViewOnClickListener mClickListener;
    private final GestureDetector mGestureDetector;

    public RecyclerViewTouchListener(Context context, RecyclerView recycleView, RecyclerViewOnClickListener clickListener) {
        this.mClickListener = clickListener;
        this.mGestureDetector = this.createGestureDetector(context, recycleView, clickListener);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (child != null && this.mClickListener != null && this.mGestureDetector.onTouchEvent(motionEvent)) {
            this.mClickListener.onClick(child, recyclerView.getChildAdapterPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    private GestureDetector createGestureDetector(final @NonNull Context context, final @NonNull RecyclerView recyclerView, final RecyclerViewOnClickListener onClickListener) {
        return new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if (child != null && onClickListener != null) {
                    onClickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                }
            }
        });
    }
}
