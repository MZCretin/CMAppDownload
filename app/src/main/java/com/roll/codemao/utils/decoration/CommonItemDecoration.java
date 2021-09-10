package com.roll.codemao.utils.decoration;

import android.graphics.Paint;
import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;


/**
 * Created by lt on 2018/4/9.
 */
public class CommonItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;
    private Paint mPaint;
    private int left, top, right, bottom;

    public CommonItemDecoration(int mSpace) {
        this.mSpace = mSpace;
    }

    public CommonItemDecoration(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    /**
     * 获取Item的偏移量
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        // 获取位置
//        int position = parent.getChildAdapterPosition(view);

        if (mSpace == 0) {
            outRect.set(left, top, right, bottom);//设置矩形参数
        } else {
            outRect.set(mSpace, mSpace, mSpace, mSpace);//设置矩形参数
        }
    }

    public CommonItemDecoration(int space, int spaceColor) {
        this.mSpace = space;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(spaceColor);
        mPaint.setStyle(Paint.Style.FILL);
    }
}
