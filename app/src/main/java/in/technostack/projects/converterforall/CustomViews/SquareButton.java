package in.technostack.projects.converterforall.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Malik on 03-09-2018.
 */

public class SquareButton extends Button {
    public SquareButton(Context context) {
        super(context);
    }

    public SquareButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int height=(width*58)/100;
        setMeasuredDimension(width, height);
    }
}
