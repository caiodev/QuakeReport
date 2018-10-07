package com.example.android.quakereport.utils

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent

class RecyclerItemTouchListener(context: Context, recyclerView: RecyclerView?,
                                private val clickListener: ClickListener) : RecyclerView.OnItemTouchListener {

    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            recyclerView?.findChildViewUnder(e.x, e.y)?.let { child ->
                clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child))
            }
        }
    })

    override fun onTouchEvent(p0: RecyclerView, p1: MotionEvent) {

    }

    override fun onInterceptTouchEvent(recyclerView: RecyclerView, motionEvent: MotionEvent): Boolean {

        recyclerView.findChildViewUnder(motionEvent.x, motionEvent.y)?.let { child ->

            if (gestureDetector.onTouchEvent(motionEvent)) {
                clickListener.onClick(child, recyclerView.getChildLayoutPosition(child))
            }
        }

        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }
}