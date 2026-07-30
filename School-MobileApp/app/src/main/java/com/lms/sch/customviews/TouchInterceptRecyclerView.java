package com.lms.sch.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class TouchInterceptRecyclerView extends RecyclerView {

    public final GestureDetector gestureDetector;
    public Boolean isSwipeActivated = false;

    public TouchInterceptRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.gestureDetector = new GestureDetector(context, createGestureListener());
    }

    public GestureDetector.OnGestureListener createGestureListener() {
        return new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (e1 == null || e2 == null) {
                    return false;
                }
                float x1 = e1.getX();
                float y1 = e1.getY();

                float x2 = e2.getX();
                float y2 = e2.getY();

                Direction direction = getDirection(x1, y1, x2, y2);
                return onSwipe(direction);
            }

            private boolean onSwipe(Direction direction) {
                Log.d("#########", direction.toString());
                /*if(isCircleList && viewPager != null){
                    if(direction == Direction.up){
                        viewPager.setUserInputEnabled(false);
                    }
                    else  if(direction == Direction.down){
                        viewPager.setUserInputEnabled(false);
                    }
                    else  if(direction == Direction.left){
                        viewPager.setUserInputEnabled(true);
                    }
                    else  if(direction == Direction.right){
                        viewPager.setUserInputEnabled(true);
                    }
                }
                else {
                    if (direction == Direction.left || direction == Direction.right) {
                        // Handle business logic starting here. <------
                        if(direction == Direction.right && dashBoardActivity != null){
                            dashBoardActivity.openDrawer();
                        }
                    }
                }*/
                return false;
            }

            private Direction getDirection(float x1, float y1, float x2, float y2) {
                double angle = getAngle(x1, y1, x2, y2);
                return Direction.fromAngle(angle);
            }

            private double getAngle(float x1, float y1, float x2, float y2) {
                double rad = Math.atan2(y1 - y2, x2 - x1) + Math.PI;
                return (rad * 180 / Math.PI + 180) % 360;
            }
        };
    }

    public enum Direction {
        up,
        down,
        left,
        right;

        public static Direction fromAngle(double angle) {
            if (inRange(angle, 45, 135)) {
                return Direction.up;
            } else if (inRange(angle, 0, 45) || inRange(angle, 315, 360)) {
                return Direction.right;
            } else if (inRange(angle, 225, 315)) {
                return Direction.down;
            } else {
                return Direction.left;
            }

        }

        private static boolean inRange(double angle, float init, float end) {
            return (angle >= init) && (angle < end);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(isSwipeActivated){
            gestureDetector.onTouchEvent(ev);
        }
        /*if(areAllItemsCompletelyWithinViewPort()) {
            gestureDetector.onTouchEvent(ev);
        }*/

        // This dispatches the event downstream to children. We need this to handle things like item click.
        return super.dispatchTouchEvent(ev);
    }

    public boolean areAllItemsCompletelyWithinViewPort() {
        if (getAdapter() == null) {
            return false;
        }
        LayoutManager layoutManager = getLayoutManager();
        int firstItemPosition = 0;
        int lastItemPosition = 0;
        if (layoutManager instanceof LinearLayoutManager) {
            firstItemPosition = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
            lastItemPosition = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
        }
        return firstItemPosition == 0 && lastItemPosition == getAdapter().getItemCount() - 1;
    }

}
