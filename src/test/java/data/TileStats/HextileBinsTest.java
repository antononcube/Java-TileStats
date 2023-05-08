//        MIT License
//
//        Java-TileStats
//        Copyright (c) 2023 Anton Antonov
//
//        Permission is hereby granted, free of charge, to any person obtaining a copy
//        of this software and associated documentation files (the "Software"), to deal
//        in the Software without restriction, including without limitation the rights
//        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//        copies of the Software, and to permit persons to whom the Software is
//        furnished to do so, subject to the following conditions:
//
//        The above copyright notice and this permission notice shall be included in all
//        copies or substantial portions of the Software.
//
//        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//        SOFTWARE.

package test.java.data.TileStats;

import main.java.data.TileStats.HextileBins;
import main.java.data.TileStats.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HextileBinsTest {

    final ArrayList<Point2D> data10 = new ArrayList<>(Arrays.asList(
            new Point2D("1", 12.1841, 39.986),
            new Point2D("2", 3.80378, 38.1123),
            new Point2D("3", 3.08667, 35.7043),
            new Point2D("4", 13.6901, 38.7686),
            new Point2D("5", 8.74651, 37.1141),
            new Point2D("6", 10.2802, 42.8688),
            new Point2D("7", 11.1619, 38.4382),
            new Point2D("8", 11.7848, 43.0898),
            new Point2D("9", 6.74477, 43.8341),
            new Point2D("10", 7.0733, 41.1099)));
    @org.junit.jupiter.api.Test
    void pointHextileCenterGroups() {

        HextileBins hexBins = new HextileBins();

        Map<Point2D, ArrayList<Map.Entry<Point2D, Double>>> binned = hexBins.pointHextileCenterGroups(this.data10, 10.);

        //System.out.println(binned);
        //binned.forEach((k, v) -> System.out.println(k + "->" + v));
        //<id=,x=15.0,y=43.301270189221924>->[<id=1,x=12.1841,y=39.986>=1.0, <id=4,x=13.6901,y=38.7686>=1.0, <id=6,x=10.2802,y=42.8688>=1.0, <id=8,x=11.7848,y=43.0898>=1.0]
        //<id=,x=0.0,y=34.64101615137754>->[<id=2,x=3.80378,y=38.1123>=1.0, <id=3,x=3.08667,y=35.7043>=1.0]
        //<id=,x=10.0,y=34.64101615137754>->[<id=5,x=8.74651,y=37.1141>=1.0, <id=7,x=11.1619,y=38.4382>=1.0]
        //<id=,x=5.0,y=43.301270189221924>->[<id=9,x=6.74477,y=43.8341>=1.0, <id=10,x=7.0733,y=41.1099>=1.0]


        assertEquals(binned.size(), 4);

        assertEquals(binned.get(new Point2D("",15.0,43.301270189221924)).size(), 4);
        assertEquals(binned.get(new Point2D("",5.0,43.301270189221924)).size(), 2);
        assertEquals(binned.get(new Point2D("",0.0,34.64101615137754)).size(), 2);
        assertEquals(binned.get(new Point2D("",10.0,34.64101615137754)).size(), 2);
    }

    @org.junit.jupiter.api.Test
    void pointHextileCenterBins() {

        HextileBins hexBins = new HextileBins();

        Map<Point2D,Double> binned = hexBins.pointHextileCenterBins(this.data10,10, "size");

        //System.out.println(binned);
        //{<id=,x=15.0,y=43.301270189221924>=4.0, <id=,x=0.0,y=34.64101615137754>=2.0, <id=,x=10.0,y=34.64101615137754>=2.0, <id=,x=5.0,y=43.301270189221924>=2.0}

        assertEquals(binned.size(), 4);
    }
}