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
    private final boolean check[][];
    private final PixelUtility pixelUtil;

    public PathSegmentation(BufferedImage image, PixelUtility pixelUtil)
    {
        this.image = new ByteProcessor(image);
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
                    segmentArray=new ArrayList<>();
                    segmentMap.put(label, segmentArray);
                }

                segmentArray.add(point);
                check[point.x][point.y] = true;

                 for (Point p : pixelUtil.getBlackNeigboursPosition(point))
                {
                    if(pixelUtil.isAmbiguity(p))
                        return false;
                }
                for(Point p : pixelUtil.getBlackNeigboursPosition(point))
                {
                    segmentation(p, label);
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
