package com.manuelpeinado.multichoiceadapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

import com.manuelpeinado.multichoiceadapter.R;

public class CheckableImageView extends ImageView implements Checkable {
    private static final int[] CHECKED_STATE_SET = { android.R.attr.state_checked };
    private StateListDrawable stateList;
    private boolean mChecked;

    public CheckableImageView(Context context) {
        super(context);
    }

    public CheckableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CheckableImageView,
                R.attr.checkableImageViewStyle, R.style.MultiChoiceAdapter_DefaultCheckableImageViewStyle);
        stateList = (StateListDrawable)a.getDrawable(R.styleable.CheckableImageView_android_foreground);
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (stateList != null) {
            stateList.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            stateList.draw(canvas);
        }
    }

    /**************************/
    /** Checkable **/
    /**************************/

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();
        }
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }

    /**************************/
    /** Drawable States **/
    /**************************/

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        if (stateList != null) {
            int[] myDrawableState = getDrawableState();
            stateList.setState(myDrawableState);
            invalidate();
        }
    }
}
