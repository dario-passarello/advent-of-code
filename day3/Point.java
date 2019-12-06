package day3;

public class Point {

    private int x;
    private int y;

    public static final Point center = new Point(0,0);

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int manhattanDistance(Point p2) {
        return Math.abs(this.getX() - p2.getX()) + Math.abs(this.getY() - p2.getY());
    }

    public boolean equals(Point p) {
        return this.x == p.getX() && this.y == p.getY();
    }
}
