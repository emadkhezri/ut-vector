/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.emad;

import static com.emad.Constants.JUNCTION_RECOVERY_STEP;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author tnp
 */
public class Junction
{

    private Point coordinate;
    private int degree;
    private final int step = JUNCTION_RECOVERY_STEP;
    private ArrayList<Point> innerPoints;
    private ArrayList<Point> outerPoints;
    private final PixelUtility pixelUtil;
    private boolean[][] checkMatrix = new boolean[step * 4 + 1][step * 4 + 1];
    
    public double[] getFeatureArray()
    {
        double featureArray[] = new double[12];
        int i=0;
        for(Point p: innerPoints)
        {
            featureArray[i++]=p.x;
            featureArray[i++]=p.y;
        }
        
        for(Point p: outerPoints)
        {
            featureArray[i++]=p.x;
            featureArray[i++]=p.y;
        }
        return featureArray;
    }

    public Junction(Point coordinate, int degree, PixelUtility pixelUtil)
    {
        this.coordinate = coordinate;
        this.degree = degree;
        this.pixelUtil = pixelUtil;
        innerPoints = new ArrayList<>(3);
        outerPoints = new ArrayList<>(3);
        create();
    }
    

    private void create()
    {
        check(coordinate);
        Point p0 = coordinate;
        Point p1 = coordinate;
        Point p2 = coordinate;

        int i = 0;
        while (i < 2 * step)
        {
            p0 = getUncekedNeighbour(p0);
            check(p0);
            p1 = getUncekedNeighbour(p1);
            check(p1);
            p2 = getUncekedNeighbour(p2);
            check(p2);
            i++;
            if (i == step)
            {
                innerPoints.add(convertToLocal(p0));
                innerPoints.add(convertToLocal(p1));
                innerPoints.add(convertToLocal(p2));
            }
            if (i == step * 2)
            {
                outerPoints.add(convertToLocal(p0));
                outerPoints.add(convertToLocal(p1));
                outerPoints.add(convertToLocal(p2));
            }
        }

    }

    private Point getUncekedNeighbour(Point point)
    {
        ArrayList<Point> points = pixelUtil.getBlackNeigboursPosition(point);
        for (Point p : points)
        {
            if (!isCheck(p))
            {
                return p;
            }
        }
        return point;
    }

    private void check(Point p)
    {
        int xCoordinate = (p.x - coordinate.x) + step * 2;
        int yCoordinate = (p.y - coordinate.y) + step * 2;
        checkMatrix[xCoordinate][yCoordinate] = true;
    }

    private boolean isCheck(Point p)
    {
        int xCoordinate = (p.x - coordinate.x) + step * 2;
        int yCoordinate = (p.y - coordinate.y) + step * 2;
        return checkMatrix[xCoordinate][yCoordinate];
    }

    private Point convertToLocal(Point p)
    {
        int xCoordinate = (p.x - coordinate.x) + step * 2;
        int yCoordinate = (p.y - coordinate.y) + step * 2;
        return new Point(xCoordinate, yCoordinate);
    }

}
