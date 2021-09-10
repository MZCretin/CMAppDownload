package com.roll.codemao.view;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by cretin on 16/3/11.
 */
public class NoScrollRecyclerView extends RecyclerView {

    public NoScrollRecyclerView(Context context) {
        super(context);
    }

    public NoScrollRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //不出现滚动条
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
