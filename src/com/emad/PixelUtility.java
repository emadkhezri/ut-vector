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
public class PixelUtility
{

    private final BufferedImage image;
    private boolean isAmbiguity[][];
    private LinkedHashMap ambiguityRegionMap = new LinkedHashMap();

    public PixelUtility(BufferedImage inputImage)
    {
        this.image = inputImage;
        isAmbiguity = new boolean[inputImage.getWidth()][inputImage.getHeight()];
        for(boolean[] row:isAmbiguity)
        Arrays.fill(row, false);
    }

    public void processAmbiguityPoints()
    {
        int degree;
        for (int j = 0; j < image.getHeight(); j++)
        {
            for (int i = 0; i < image.getWidth(); i++)
            {
                if (new ByteProcessor(image).get(i, j) != 0)
                {
                    continue;
                }
                double neighbours[][] = new double[3][3];
                new ByteProcessor(image).getNeighborhood(i, j, neighbours);
                degree = getBlackNeighboursCount(neighbours);
                if (degree >= 3)
                {
                    int dense = getBlackNeighbourDense(neighbours);
                    if (dense == 0)
                    {
                        Point p = new Point(i, j);
                        ambiguityRegionMap.put(p, degree);
                        isAmbiguity[i][j] = true;
                    }
                }
            }
        }
    }

    public LinkedHashMap getAmbiguityMap()
    {
        return ambiguityRegionMap;
    }

    private int getBlackNeighboursCount(double neigbours[][])
    {
        int count = 0;
        for (int i = 0; i < neigbours.length; i++)
        {
            for (int j = 0; j < neigbours.length; j++)
            {
                if (i == j && i == neigbours.length / 2)
                {
                    continue;
                }
                else if (neigbours[i][j] == 0)
                {
                    count++;
                }
            }
        }
        return count;

    }

    private int getBlackNeighbourDense(double neighbours[][])
    {
        int dense = 0;
        double[] neighbourArray =
        {
            neighbours[0][0], neighbours[0][1], neighbours[0][2],
            neighbours[1][2], neighbours[2][2], neighbours[2][1],
            neighbours[2][0], neighbours[1][0]
        };
        for (int i = 0; i < 7; i++)
        {
            if (neighbourArray[i] == neighbourArray[i + 1] && neighbourArray[i]
                    == 0)
            {
                dense++;
                i++;
            }
        }
        //check first and last element of array
        if(neighbourArray[0]==neighbourArray[7]&&neighbourArray[7]==0)
            dense++;
        return dense;
    }

    public ArrayList<Point> getBlackNeigboursPosition(Point point)
    {
        ArrayList<Point> neighbourList= new ArrayList<>();
        double neighbours[][] = new double[3][3];
        new ByteProcessor(image).getNeighborhood(point.x, point.y, neighbours);
        for(int i=0;i<neighbours.length;i++)
            for(int j=0;j<neighbours.length;j++)
            {
                if (i == j && i == neighbours.length / 2)
                {
                    continue;
                }
                if(neighbours[i][j]==0)
                {
                    int x= point.x;
                    int y=point.y;
                    neighbourList.add(new Point(x+i-1, y+j-1));
                }
            }
        return neighbourList;
    }

    public boolean isAmbiguity(Point p)
    {
        return isAmbiguity[p.x][p.y];
    }

}
