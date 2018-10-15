package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import java.util.List;

import edu.luc.etl.cs313.android.shapes.model.Circle;
import edu.luc.etl.cs313.android.shapes.model.Fill;
import edu.luc.etl.cs313.android.shapes.model.Group;
import edu.luc.etl.cs313.android.shapes.model.Location;
import edu.luc.etl.cs313.android.shapes.model.Outline;
import edu.luc.etl.cs313.android.shapes.model.Point;
import edu.luc.etl.cs313.android.shapes.model.Polygon;
import edu.luc.etl.cs313.android.shapes.model.Rectangle;
import edu.luc.etl.cs313.android.shapes.model.Shape;
import edu.luc.etl.cs313.android.shapes.model.Stroke;
import edu.luc.etl.cs313.android.shapes.model.Visitor;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

    private final Canvas canvas;
    private final Paint paint;

    public Draw(final Canvas canvas, final Paint paint) {
        this.canvas = canvas;
        this.paint = paint;
        paint.setStyle(Style.STROKE);
    }

    @Override
    public Void onCircle(final Circle c) {
        canvas.drawCircle(0, 0, c.getRadius(), paint);
        return null;
    }

    @Override
    public Void onStroke(final Stroke c) {
        int color = paint.getColor();
        paint.setColor(c.getColor());
        c.getShape().accept(this);
        paint.setColor(color);
        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        Style style = paint.getStyle();
        paint.setStyle(Style.FILL_AND_STROKE);
        f.getShape().accept(this);
        paint.setStyle(style);
        return null;
    }

    @Override
    public Void onGroup(final Group g) {
        for (Shape s : g.getShapes()) {
            s.accept(this);
        }
        return null;
    }

    @Override
    public Void onLocation(final Location l) {
        canvas.translate(l.getX(), l.getY());
        l.getShape().accept(this);
        canvas.translate(-l.getX(), -l.getY());
        return null;
    }

    @Override
    public Void onRectangle(final Rectangle r) {
        canvas.drawRect(0, 0, r.getWidth(), r.getHeight(), paint);
        return null;
    }

    @Override
    public Void onOutline(Outline o) {
        Style style = paint.getStyle();
        paint.setStyle(Style.STROKE);
        o.getShape().accept(this);
        paint.setStyle(style);
        return null;
    }

    @Override
    public Void onPolygon(final Polygon s) {
        int size = s.getPoints().size();
        float[] points = new float[size * 4];

        // Skipping the first point
        List<? extends Point> remainingPoints = s.getPoints().subList(1, size);
        int i = 2;
        for(Point p : remainingPoints) {
            points[i++] = p.getX();
            points[i++] = p.getY();
            points[i++] = p.getX();
            points[i++] = p.getY();
        }

        // Getting X and Y for the first point
        float startX = s.getPoints().get(0).getX();
        float startY = s.getPoints().get(0).getY();
        points[0] = startX;
        points[1] = startY;

        // Last X and Y must match first point's X and Y
        points[points.length - 2] = startX;
        points[points.length - 1] = startY;

        canvas.drawLines(points, paint);
        return null;
    }
}
