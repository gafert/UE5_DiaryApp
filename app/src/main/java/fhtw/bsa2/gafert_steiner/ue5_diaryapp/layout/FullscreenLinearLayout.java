package fhtw.bsa2.gafert_steiner.ue5_diaryapp.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by michi on 02.06.17.
 */

public class FullscreenLinearLayout extends LinearLayout {
    private DisplayMetrics mMetrics;
    private AttributeSet attributeSet;

    public FullscreenLinearLayout(Context context) {
        super(context);
        mMetrics = new DisplayMetrics();
    }

    public FullscreenLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mMetrics = new DisplayMetrics();

    }

    public FullscreenLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMetrics = new DisplayMetrics();

    }

    public FullscreenLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mMetrics = new DisplayMetrics();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(mMetrics);

        // status bar height
        int statusBarHeight = 0;
        int resourceIdStatBar = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceIdStatBar > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceIdStatBar);
        }

        // action bar height
        int actionBarHeight = 0;
        final TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize }
        );
        actionBarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        // navigation bar height
        int navigationBarHeight = 0;
        int resourceIdBar = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceIdBar > 0) {
            navigationBarHeight = getResources().getDimensionPixelSize(resourceIdBar);
        }

        // Ste height to viewport height
        int height = mMetrics.heightPixels - actionBarHeight - navigationBarHeight - statusBarHeight + 10; //10 as the navigation bar is not the full heigth

        // Keep Width
        int width = widthMeasureSpec;

        super.onMeasure(width, height);
        setMeasuredDimension(width,height);
    }
}
