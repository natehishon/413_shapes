package edu.luc.etl.cs313.android.shapes.model;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {

    @Override
    public Location onCircle(final Circle c) {
        final int radius = c.getRadius();
        return new Location(-radius, -radius, new Rectangle(2 * radius, 2 * radius));
    }

    @Override
    public Location onFill(final Fill f) {
        return f.getShape().accept(this);
    }

    @Override
    public Location onGroup(final Group g) {
        int xMin = Integer.MAX_VALUE;
        int yMin = Integer.MAX_VALUE;
        int width = 0;
        int height = 0;
        for (Shape s : g.getShapes()) {
            Location l = s.accept(this);
            Rectangle r = (Rectangle) l.getShape();
            if (xMin > l.getX())
                xMin = l.getX();
            if (yMin > l.getY())
                yMin = l.getY();
            if (width < (r.getWidth() + l.getX()))
                width = r.getWidth() + l.getX();
            if (height < (r.getHeight() + l.getY()))
                height = r.getHeight() + l.getY();
        }
        return new Location(xMin, yMin, new Rectangle(width - xMin, height - yMin));
    }

    @Override
    public Location onLocation(final Location l) {
        Location temp = l.getShape().accept(this);
        return new Location(temp.getX() + l.getX(), temp.getY() + l.getY(), temp.getShape());
    }

    @Override
    public Location onRectangle(final Rectangle r) {
        final int height = r.getHeight();
        final int width = r.getWidth();
        return new Location(0, 0, new Rectangle(width, height));
    }

    @Override
    public Location onStroke(final Stroke c) {
        return c.getShape().accept(this);
    }

    @Override
    public Location onOutline(final Outline o) {
        return o.getShape().accept(this);
    }

    @Override
    public Location onPolygon(final Polygon s) {
        int xMax = 0;
        int yMax = 0;
        int xMin = Integer.MAX_VALUE;
        int yMin = Integer.MAX_VALUE;
        for (Point p : s.getPoints()) {
            if (xMax < p.getX())
                xMax = p.getX();
            if (xMin > p.getX())
                xMin = p.getX();
            if (yMax < p.getY())
                yMax = p.getY();
            if (yMin > p.getY())
                yMin = p.getY();
        }
        int width = xMax - xMin;
        int height = yMax - yMin;
        return new Location(xMin, yMin, new Rectangle(width, height));
    }
}
