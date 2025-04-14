package com.project.clothingstore.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class DividerItemDecorator extends RecyclerView.ItemDecoration {
    private final Paint paint;

    public DividerItemDecorator() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#EEEEEE")); // màu line nhẹ
        paint.setStrokeWidth(1f);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);
            float top = child.getBottom();
            float bottom = top + 1;
            c.drawLine(left, top, right, top, paint);
        }
    }
}

