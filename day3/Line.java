package day3;

import java.util.ArrayList;
import java.util.List;

public class Line {
    private Point NWPoint;
    private Point SEPoint;

    public Line(Point start, Direction direction, int length) {
        switch (direction) {
            case UP:
                this.SEPoint =  start;
                this.NWPoint = new Point(start.getX(), start.getY() - length);
                break;
            case DOWN:
                this.NWPoint = start;
                this.SEPoint = new Point(start.getX(), start.getY() + length);
                break;
            case LEFT:
                this.SEPoint = start;
                this.NWPoint = new Point(start.getX() - length, start.getY());
                break;
            case RIGHT:
                this.NWPoint = start;
                this.SEPoint = new Point(start.getX() + length, start.getY());
                break;
        }
    }

    private boolean isHorizontal() {
        return NWPoint.getY() == SEPoint.getY();
    }

    private boolean isVertical() {
        return !isHorizontal();
    }

    public boolean isInLine(Point p) {
        return NWPoint.getX() <= p.getX() && p.getX() <= SEPoint.getX()
                && NWPoint.getY() <= p.getY() && p.getY() <= SEPoint.getY();
    }

    public Point getNWPoint() {
        return NWPoint;
    }

    public Point getSEPoint() {
        return SEPoint;
    }

    public int getLength() {
        if(isHorizontal()) return Math.abs(NWPoint.getX() - SEPoint.getX());
        else return Math.abs(NWPoint.getY() - SEPoint.getY());
    }

    public static List<Point> intersections(Line l1, Line l2) {
        ArrayList<Point> list = new ArrayList<>();
        //If the two horizontal lines are partially overlapping then find all common points, if they exist
        if(l1.isHorizontal() && l2.isHorizontal()) {
            if (l1.getNWPoint().getY() == l1.getNWPoint().getY())
                for (int i = Math.max(l1.getNWPoint().getX(), l2.getNWPoint().getX()); i <= Math.min(l1.getNWPoint().getX(), l2.getNWPoint().getX()); i++)
                    list.add(new Point(i, l1.getNWPoint().getY()));
        }
        //Same as above but the lines are vertical
        else if(l1.isVertical() && l2.isVertical()) {
            if (l1.getNWPoint().getX() == l1.getNWPoint().getX())
                for (int i = Math.max(l1.getNWPoint().getY(), l2.getNWPoint().getY()); i <= Math.min(l1.getNWPoint().getY(), l2.getNWPoint().getY()); i++)
                    list.add(new Point(l1.getNWPoint().getY(),i));
        }
        //If the lines are perpendicular find the intersection point, if it exists
        else {
            /*
             *         NW2                      O----> X
             *          2                       |
             *          2                       |
             *  NW1 1111X1111111111  SE1        V
             *          2                       Y
             *          2
             *          2
             *         SE2
             *  Intersection condition:
             *  Line 1 Y must be between NW2.X and SE2.X
             *  Line 2 X must be between NW1.Y and SE1.Y
             *  Then the 'X' point is in (NW2.X, SE1.Y)
             */
            if(l1.isVertical() && l2.isHorizontal()) {
                Line temp = l1;
                l1 = l2;
                l2 = temp;
            }
            //Now I'm sure that l1 is Horizontal and l2 is Vertical
            if(l1.getNWPoint().getX() <= l2.getNWPoint().getX() && l2.getNWPoint().getX() <= l1.getSEPoint().getX()
                    && l2.getNWPoint().getY() <= l1.getNWPoint().getY() && l1.getNWPoint().getY() <= l2.getSEPoint().getY())
                list.add(new Point(l2.getNWPoint().getX(),l1.getSEPoint().getY()));
        }

        return list;
    }

}
