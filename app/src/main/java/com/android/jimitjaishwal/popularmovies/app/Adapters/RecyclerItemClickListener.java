package com.android.jimitjaishwal.popularmovies.app.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Jimit Jaishwal on 8/1/2016.
 */
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {



    public interface OnItemClickListener {

        void onItemClick(View v, int position);
    }

    private OnItemClickListener clickListener;
    private GestureDetector detector;

    public RecyclerItemClickListener(Context mContext, final RecyclerView recyclerView, final OnItemClickListener clickListener){

        this.clickListener = clickListener;
        detector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (childView != null && clickListener != null){
                    clickListener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView));
                }

            }
        });
    }


    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {

        View childView = view.findChildViewUnder(e.getX(), e.getY());

        if (childView != null && clickListener != null && detector.onTouchEvent(e)) {
            clickListener.onItemClick(childView, view.getChildAdapterPosition(childView));
        }
        return false;
    }


    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {


    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
