package edu.luc.etl.cs313.android.shapes.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import edu.luc.etl.cs313.android.shapes.model.BoundingBox;
import edu.luc.etl.cs313.android.shapes.model.Fixtures;
import edu.luc.etl.cs313.android.shapes.model.Location;
import edu.luc.etl.cs313.android.shapes.model.Shape;

public class DrawWidget extends View {

    private final Paint paint = new Paint();

    public DrawWidget(final Context context, final AttributeSet attrs, final int defStyle) {
        this(context);
    }

    public DrawWidget(final Context context, final AttributeSet attrs) {
        this(context);
    }

    public DrawWidget(final Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    @SuppressLint("DrawAllocation")
    protected void onDraw(final Canvas canvas) {
        final Shape shape = Fixtures.complexGroup;
        final Location b = shape.accept(new BoundingBox());
        canvas.translate(-b.getX(), -b.getY());
        b.accept(new Draw(canvas, paint));
        shape.accept(new Draw(canvas, paint));
        canvas.translate(b.getX(), b.getY());
    }
}
