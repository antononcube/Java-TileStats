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

import java.util.*;

public class HextileBins {

    private double tileSize;

    public void setTileSize(double ts) {
        this.tileSize = ts;
    }

    public double getTileSize() {
        return this.tileSize;
    }

    private final ArrayList<Point2D> tileCoords = new ArrayList<>(Arrays.asList(
            new Point2D("1", 0., 0.),
            new Point2D("2", 1., 0.),
            new Point2D("3", 1. / 2., Math.sqrt(3.) / 2.),
            new Point2D("4", 0., Math.sqrt(3.)),
            new Point2D("5", 1., Math.sqrt(3.))));

    public ArrayList<Point2D> referenceHexagon() {
        ArrayList<Point2D> res = new ArrayList<>();
        res.add(new Point2D("hex1", 1. / 2., Math.sqrt(3.) / 2.));
        res.add(new Point2D("hex2", -(1. / 2.), Math.sqrt(3.) / 2.));
        res.add(new Point2D("hex3", -1., 0.));
        res.add(new Point2D("hex4", -1. / 2., -Math.sqrt(3.) / 2));
        res.add(new Point2D("hex5", 1. / 2., -Math.sqrt(3.) / 2.));
        res.add(new Point2D("hex6", 1., 0.));
        return res;
    }

    public Point2D nearestWithinTile(Point2D p) {
        return this.nearestWithinTile(p.getX(), p.getY());
    }

    public Point2D nearestWithinTile(double x, double y) {
        Comparator<Point2D> compare = Point2D.byDistance(x, y);
        return this.tileCoords.stream().min(compare).orElse(null);
    }

    public Point2D tileContaining(Point2D p) {
        double x = Math.floor(p.getX());
        double y = Math.sqrt(3.) * Math.floor(p.getY() / Math.sqrt(3.));
        return new Point2D("new", x, y);
    }

    public Point2D nearestHexagon(Point2D p) {
        Point2D tile = this.tileContaining(p);
        //Point2D relative = new Point2D( "new", p.getX() - tile.getX(), p.getY() - tile.getY());
        Point2D relative = p.diff(tile);
        Point2D nwt = this.nearestWithinTile(relative);
        return tile.add(nwt);
    }

    public Map<Point2D, ArrayList<Map.Entry<Point2D, Double>>> hextileCenterGroups(ArrayList<Map.Entry<Point2D, Double>> data, double binSize) {
        Map<Point2D, ArrayList<Map.Entry<Point2D, Double>>> res = new HashMap<>();

        data.forEach(p -> {
            Point2D nwt = this.nearestHexagon(p.getKey().mult(1. / binSize)).mult(binSize);
            if (res.get(nwt) == null) {
                ArrayList<Map.Entry<Point2D, Double>> ar = new ArrayList<>();
                ar.add(p);
                res.put(nwt, ar);
            } else {
                res.get(nwt).add(p);
            }
        });

        return res;
    }

    public Map<Point2D, ArrayList<Map.Entry<Point2D, Double>>> pointHextileCenterGroups(ArrayList<Point2D> data, double binSize) {
        ArrayList<Map.Entry<Point2D, Double>> res = new ArrayList<>();
        data.forEach(p -> {
            res.add(Map.entry(p, 1.0));
        });
        return this.hextileCenterGroups(res, binSize);
    }

    public Map<Point2D, Double> hextileCenterBins(ArrayList<Map.Entry<Point2D, Double>> data, double binSize, String aggrFunc) {
        Map<Point2D, ArrayList<Map.Entry<Point2D, Double>>> binned = this.hextileCenterGroups(data, binSize);
        Map<Point2D, Double> res = new HashMap<>();
        if (aggrFunc.equals("length") || aggrFunc.equals("size")) {
            binned.forEach((k, v) -> {
                res.put(k, (double) v.size());
            });
        } else if (aggrFunc.equals("mean") || aggrFunc.equals("average")) {
            binned.forEach((k, v) -> {
                double sum = 0;
                for (Map.Entry<Point2D, Double> e : v) {
                    sum += e.getValue();
                }
                res.put(k, sum / v.size());
            });
        } else if (aggrFunc.equals("sum") || aggrFunc.equals("total")) {
            binned.forEach((k, v) -> {
                double sum = 0;
                for (Map.Entry<Point2D, Double> e : v) {
                    sum += e.getValue();
                }
                res.put(k, sum);
            });
        }
        return res;
    }

    public Map<Point2D, Double> pointHextileCenterBins(ArrayList<Point2D> data, double binSize, String aggrFunc) {
        ArrayList<Map.Entry<Point2D, Double>> res = new ArrayList<>();
        data.forEach(p -> {
            res.add(Map.entry(p, 1.0));
        });
        return this.hextileCenterBins(res, binSize, aggrFunc);
    }
}
