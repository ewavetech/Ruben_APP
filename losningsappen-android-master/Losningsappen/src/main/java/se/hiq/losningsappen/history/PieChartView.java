package se.hiq.losningsappen.history;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import se.hiq.losningsappen.R;

/**
 * Created by petterstenberg on 2014-08-14.
 */
public class PieChartView extends ImageView {

    private int circleDiameter = 0;
    private Paint greenPaint;
    private RectF arcBounds;
    private float drawingAngle = 0;
    private float padding;

    public PieChartView(Context context) {
        super(context);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        AttributeSet attrs1 = attrs;
        init();
    }

    private void init() {
        Paint redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redPaint.setColor(getResources().getColor(R.color.red));
        redPaint.setStyle(Paint.Style.FILL);

        greenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        greenPaint.setColor(getResources().getColor(R.color.green));
        greenPaint.setStyle(Paint.Style.FILL);


        padding = getResources().getDimension(R.dimen.status_image_padding);
        circleDiameter = (int) ((getResources().getDimension(R.dimen.status_image_diameter) - padding));

        arcBounds = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (arcBounds.isEmpty()) arcBounds.set(padding, padding, circleDiameter, circleDiameter);

//        canvas.drawCircle(circleRadius, circleRadius, circleRadius, redPaint);
        canvas.drawArc(arcBounds, -90, drawingAngle, true, greenPaint);
    }

    public void setCompletedRatio(float ratio) {
        drawingAngle = ratio * 360;
        invalidate();
    }
}
