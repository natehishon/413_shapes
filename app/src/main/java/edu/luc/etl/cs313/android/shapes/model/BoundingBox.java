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
        return new Location(0, 0, f.getShape());
    }

    @Override
    public Location onGroup(final Group g) {
        //TODO
        return null;
    }

    @Override
    public Location onLocation(final Location l) {
        return l;
    }

    @Override
    public Location onRectangle(final Rectangle r) {
        final int height = r.getHeight();
        final int width = r.getWidth();
        return new Location(0, 0, new Rectangle(width, height));
    }

    @Override
    public Location onStroke(final Stroke c) {
        return new Location(0, 0, c.getShape());
    }

    @Override
    public Location onOutline(final Outline o) {
        return new Location(0, 0, o.getShape());
    }

    @Override
    public Location onPolygon(final Polygon s) {
        int xMax = 0;
        int yMax = 0;
        int xMin = Integer.MAX_VALUE;
        int yMin = Integer.MAX_VALUE;
        int y = s.getPoints().get(0).getX();
        int x = s.getPoints().get(0).getX();

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
        return new Location(y, x, new Rectangle(width, height));
    }
}
