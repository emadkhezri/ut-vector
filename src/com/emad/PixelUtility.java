/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.emad;

import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author tnp
 */
public class PixelUtility
{

    private final BufferedImage image;

    public PixelUtility(BufferedImage inputImage)
    {
        this.image = inputImage;
    }

    public LinkedHashMap getAmbiguityRegion()
    {
        LinkedHashMap ambiguityRegionMap = new LinkedHashMap();
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
                degree = getBlackNeighboursCount(neighbours, i, j);
                if (degree >= 3)
                {
                    for (int k = 0; k < 3; k++)
                    {
                        for (int l = 0; l < 3; l++)
                        {
                            if (i == k && i == 1)
                                ;//
                            else

                            {
                                
                            }
                        }
                    }
                    ambiguityRegionMap.put(new Point(i, j), degree);
                }
            }
        }
        return ambiguityRegionMap;
    }

    public int getBlackNeighboursCount(double neigbours[][], int x, int y)
    {
        new ByteProcessor(image).getNeighborhood(x, y, neigbours);
        int count = 0;
        for (int i = 0; i < neigbours.length; i++)
        {
            for (int j = 0; j < neigbours.length; j++)
            {
                if (i == j & i == neigbours.length / 2)
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

}
