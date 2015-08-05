/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.emad;

import ij.process.ByteProcessor;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;

/**
 *
 * @author tnp
 */
public class PathSegmentation
{

    private final ByteProcessor image;
    LinkedHashMap<String, ArrayList<Point>> segmentMap = new LinkedHashMap<>();
    private boolean check[][];
    private PixelUtility pixelUtil;
    private SmartJunctionRecover smartJunc;

    public PathSegmentation(BufferedImage image, PixelUtility pixelUtil,
            SmartJunctionRecover smartJunc)
    {
        this.image = new ByteProcessor(image);
        this.smartJunc = smartJunc;
        check = new boolean[image.getWidth()][image.getHeight()];
        for (boolean[] row : check)
        {
            Arrays.fill(row, false);
        }
        this.pixelUtil = pixelUtil;
    }

    public boolean segmentation(Point point, String label)
    {
        if (check[point.x][point.y] != true)
        {
            ArrayList<Point> tempPoints = pixelUtil.getBlackNeigboursPosition(point);
            ArrayList<Point> leftArray = new ArrayList<>();
            ArrayList<Point> rightArray = new ArrayList<>();;
            leftArray.add(point);
            check[point.x][point.y] = true;
            if (tempPoints.size() > 1)
            {
                interSegmentation(tempPoints.get(0), leftArray);
                interSegmentation(tempPoints.get(1), rightArray);
            }
            Collections.reverse(leftArray);
            leftArray.addAll(rightArray);
            segmentMap.put(label, leftArray);
            return true;
        }
        return false;
    }

    private void interSegmentation(Point point, ArrayList<Point> segmentArray)
    {
        if (!pixelUtil.isAmbiguity(point))
        {
            segmentArray.add(point);
            check[point.x][point.y] = true;
            ArrayList<Point> neighbours = pixelUtil.getBlackNeigboursPosition(point);

            Point ambigPoint = null;
            for (Point p : neighbours)
            {
                if (pixelUtil.isAmbiguity(p) && !check[p.x][p.y])
                {
                    ambigPoint = p;
                }
            }

            if (ambigPoint == null)
            {
                    for (Point p : neighbours)
                    {
                        if (!check[p.x][p.y])
                        {
                            if(neighbours.size() > 2 && hasAmbigNeighbour(p))
                                continue;
                            interSegmentation(p, segmentArray);
                            break;
                        }
                }
            }
            else
            {
                if (!check[ambigPoint.x][ambigPoint.y])
                {
                    interSegmentation(ambigPoint, segmentArray);
                }
            }
        }
        else
        {
            segmentJunction(point, segmentArray);
        }
    }

    private boolean segmentJunction(Point point, ArrayList<Point> segmentArray)
    {
        ArrayList<Point> neighbourPoints = pixelUtil.getBlackNeigboursPosition(point);
        if (segmentArray == null || segmentArray.isEmpty())
        {
            return false;
        }
        if (neighbourPoints.size() == 3)
        {
            int checkNumber = getCheckPixelCount(neighbourPoints);
            if (checkNumber == 2)
            {
                check[point.x][point.y] = true;
                segmentArray.add(point);
                for (Point p : pixelUtil.getBlackNeigboursPosition(point))
                {
                    if (!check[p.x][p.y])
                    {
                        interSegmentation(p, segmentArray);
                    }
                }
            }
            else if (checkNumber == 1)
            {
                Point lastPoint = segmentArray.get(segmentArray.size() - 1);
                Junction junc = new Junction(point, 3, pixelUtil);
                int configType = smartJunc.getJunctionType(junc);
                switch (configType)
                {
                case 1:
                {
                    if (lastPoint.equals(neighbourPoints.get(1)))
                    {
                        check[point.x][point.y] = true;
                        segmentArray.add(point);
                        interSegmentation(neighbourPoints.get(0), segmentArray);
                    }
                    else if (lastPoint.equals(neighbourPoints.get(0)))
                    {
                        check[point.x][point.y] = true;
                        segmentArray.add(point);
                        interSegmentation(neighbourPoints.get(1), segmentArray);
                    }
                    break;
                }
                case 2:
                {
                    if (lastPoint.equals(neighbourPoints.get(0)))
                    {
                        check[point.x][point.y] = true;
                        segmentArray.add(point);
                        interSegmentation(neighbourPoints.get(2), segmentArray);
                    }
                    else if (lastPoint.equals(neighbourPoints.get(2)))
                    {
                        check[point.x][point.y] = true;
                        segmentArray.add(point);
                        interSegmentation(neighbourPoints.get(0), segmentArray);
                    }
                    break;
                }
                case 3:
                {
                    if (lastPoint.equals(neighbourPoints.get(1)))
                    {
                        check[point.x][point.y] = true;
                        segmentArray.add(point);
                        interSegmentation(neighbourPoints.get(2), segmentArray);
                    }
                    else if (lastPoint.equals(neighbourPoints.get(2)))
                    {
                        check[point.x][point.y] = true;
                        segmentArray.add(point);
                        interSegmentation(neighbourPoints.get(1), segmentArray);
                    }
                    break;
                }
                default:
                    return false;
                }
            }
        }
        return true;
    }

    public LinkedHashMap<String, ArrayList<Point>> getSegmentMap()
    {
        return segmentMap;
    }

    private int getCheckPixelCount(ArrayList neighbourPoints)
    {
        int checkNumber = 0;
        for (Point p : (ArrayList<Point>)neighbourPoints)
        {
            if (check[p.x][p.y])
            {
                checkNumber++;
            }
        }
        return checkNumber;
    }

    private boolean hasAmbigNeighbour(Point point)
    {
        for (Point p : pixelUtil.getBlackNeigboursPosition(point))
        {
            if (pixelUtil.isAmbiguity(p))
            {
                return true;
            }
        }
        return false;
    }

}
