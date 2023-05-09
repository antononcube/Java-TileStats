//   MIT License
//
//   Java-TileStats
//   Copyright (c) 2023 Anton Antonov
//
//   Permission is hereby granted, free of charge, to any person obtaining a copy
//   of this software and associated documentation files (the "Software"), to deal
//   in the Software without restriction, including without limitation the rights
//   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//   copies of the Software, and to permit persons to whom the Software is
//   furnished to do so, subject to the following conditions:
//
//   The above copyright notice and this permission notice shall be included in all
//   copies or substantial portions of the Software.
//
//   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//   SOFTWARE.

package main.java.data.TileStats;

import java.util.Comparator;
import java.util.Objects;

public class Point2D {

    private String id;
    public void setId(String id) { this.id = id;}
    public String getId() { return id; }

    public double getX() { return x; }

    public double getY() { return y;}

    private final double x;

    private final double y;


    public Point2D(String id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public double dist(Point2D v) {
        return dist(v.x, v.getY());
    }

    public double dist(double x, double y) {
        final double x_d = x - this.x;
        final double y_d = y - this.y;
        return (double) Math.sqrt(x_d * x_d + y_d * y_d);
    }

    public Point2D add(Point2D p) {
        //return new Point2D(this.getId() + "+" + p.getId(), this.getX() + p.getX(), this.getY() + p.getY());
        return new Point2D("", this.getX() + p.getX(), this.getY() + p.getY());
    }

    public Point2D diff(Point2D p) {
        //return new Point2D("diff", this.getX() - p.getX(), this.getY() - p.getY());
        return new Point2D("", this.getX() - p.getX(), this.getY() - p.getY());
    }


    public Point2D mult(double f) {
        //return new Point2D(this.getId() + "*" + f, this.getX() * f, this.getY() * f);
        return new Point2D("", this.getX() * f, this.getY() * f);
    }
    double dot(Point2D v) {
        return v.getX() * x + v.getY() * y;
    }

    double dot(double lat1, double lon1) {
        return lat1 * x + lon1 * y;
    }
    @Override
    public String toString() { return "<id=" + id + ",x=" + x + ",y=" + y + ">"; }

    public String toJSON() { return "{ \"id\" :\"" + id + "\", \"x\" : " + x + ", \"y\" : " + y + "}"; }

    public static Comparator<Point2D> byDistance(Point2D point) {
        return byDistance(point.getX(), point.getY());
    }

    public static Comparator<Point2D> byDistance(double x, double y) {
        return (v1, v2) -> {
            double d1 = v1.dist(x, y);
            double d2 = v2.dist(x, y);
            return Double.compare(d1, d2);
        };
    }

    public static Comparator<Point2D> byEval(Point2D point) {
        return (v1, v2) -> {
            double d = point.eval(v1.getX(), v1.getY()) - point.eval(v2.getX(), v2.getY());
            return (int)(d * 1000000d);
        };
    }

    public static Comparator<Point2D> byEval(double x, double y) {
        return (v1, v2) -> {
            double d =  v1.eval(x, y) - v2.eval(x, y);
            return (int)(d * 1000000d);
        };
    }

    // This is 1-norm, e.g. Norm[{x1,y1} - {x2,y2}, 1]
    private double eval(double x, double y){
        double dX = Math.abs(x - this.getX());
        double dY = Math.abs(y - this.getY());
        return (dX + dY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Point2D that = (Point2D) o;
        return Double.compare(that.getX(), getX()) == 0 && Double.compare(that.getY(), getY()) == 0 && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getX(), getY());
    }
}
