package com.example.myapplication.ViewModel

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View

/**
 * @param sourceView Source view which needs to be dragged
 * @param targetView Target view to which source needs to be dragged
 *
 * Both view needs to be in same vertical position as it only supports vertical drag
 */
class DragHelper(private val sourceView: View, private val targetView: View) {

    private var initialX = 0.0f
    private var initialY = 0.0f
    private var deltaY = 0.0f

    private var isDragging = false

    private var lastSourceX = 0.0f
    private var lastSourceY = 0.0f

    private var targetRect = Rect()
    private var isReached = false

    var onViewReachedTargetListener: () -> Unit = { }

    var onViewReleasedListener: () -> Unit = { }

    var onViewTouchedListener: () -> Unit = {

    }

    // Start listening to vertical drag of the source view
    @SuppressLint("ClickableViewAccessibility")
    fun setVerticalDrag() {
        setInitialLocation()

        sourceView.setOnTouchListener { view, event ->

            val action: Int = event.action

            if (action == MotionEvent.ACTION_DOWN && !isDragging) {
                isDragging = true
                deltaY = event.y

                onViewTouchedListener()
                return@setOnTouchListener true
            } else if (isDragging) {
                when (action) {
                    MotionEvent.ACTION_MOVE -> {

                        if (!isReached) {
                            view.x = view.x
                            view.y = view.y + event.y - deltaY

                            if (targetRect.contains(view.x.toInt(), view.y.toInt())) {
                                isReached = true
                                onViewReachedTargetListener()
                            }

                            return@setOnTouchListener true
                        } else {
                            return@setOnTouchListener false
                        }

                    }
                    MotionEvent.ACTION_UP -> {

                        if (!isReached) {
                            isDragging = false

                            lastSourceX = event.x
                            lastSourceY = event.y

                            val animatorX = ValueAnimator.ofFloat(view.x, initialX).apply { duration = 1000 }
                            val animatorY = ValueAnimator.ofFloat(view.y, initialY).apply { duration = 1000 }

                            animatorX.addUpdateListener {
                                view.x = it.animatedValue as Float
                            }

                            animatorY.addUpdateListener {
                                view.y = it.animatedValue as Float
                            }

                            animatorX.start()
                            animatorY.start()
                        }


                        onViewReleasedListener()

                        return@setOnTouchListener true
                    }
                    MotionEvent.ACTION_CANCEL -> {

                        view.x = initialX
                        view.y = initialY
                        isDragging = false
                        return@setOnTouchListener true
                    }
                }
            }

            return@setOnTouchListener false
        }
    }

    private fun setInitialLocation() {
        sourceView.viewTreeObserver.addOnGlobalLayoutListener {
            initialX = sourceView.x
            initialY = sourceView.y
        }

        targetView.viewTreeObserver.addOnGlobalLayoutListener {
            targetView.getGlobalVisibleRect(targetRect)
        }
    }


}