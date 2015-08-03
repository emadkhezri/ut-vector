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
    int counter = 0;

//    private MyNeuralNetwork myNeuralNetwork;
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
        ArrayList<Point> segmentArray = new ArrayList<>();
        if (check[point.x][point.y] != true)
        {
            if (!pixelUtil.isAmbiguity(point))
            {
                segmentArray = segmentMap.get(label);
                if (segmentArray == null)
                {
                    segmentArray = new ArrayList<>();
                    segmentMap.put(label, segmentArray);
                }

                segmentArray.add(point);
                check[point.x][point.y] = true;

                for (Point p : pixelUtil.getBlackNeigboursPosition(point))
                {
                    if (pixelUtil.isAmbiguity(p))
                    {
                        return false;
                    }
                }
                for (Point p : pixelUtil.getBlackNeigboursPosition(point))
                {
                    segmentation(p, label);
                }
            }
            else
            {
                ArrayList neighbourPoints = pixelUtil.getBlackNeigboursPosition(
                        point);
                if (neighbourPoints.size() == 3)
                {
                    Junction junc = new Junction(point, 3, pixelUtil);
                    int configType = smartJunc.getJunctionType(junc);
                    counter++;
                    System.out.println(counter + "- Config Type for x= " + point.x
                            + " Config Type for y= "
                            + point.y + " = " + configType);
                }
            }
            return true;
        }
        return false;
    }

    public LinkedHashMap<String, ArrayList<Point>> getSegmentMap()
    {
        return segmentMap;
    }

}
