/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.emad;

import static com.emad.Constants.GRAY_SCALE_THRESHOLD;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author tnp
 */
public class Thresholding
{

    private int threshold = GRAY_SCALE_THRESHOLD;

    public Thresholding()
    {
    }

    public Thresholding(int threshold)
    {
        this.threshold = threshold;
    }

    public void setThreshold(int threshold)
    {
        this.threshold = threshold;
    }

    public int getThreshold()
    {
        return threshold;
    }

    /**
     * Do thresholding on source image
     *
     * @param srcImage
     * @param dstImage
     */
    public void threshold(BufferedImage srcImage, BufferedImage dstImage)
    {
        int height = srcImage.getHeight();
        int width = srcImage.getWidth();

        for (int k = 0; k < height; k++)
        {
            for (int j = 0; j < width; j++)
            {
                Color color = new Color(srcImage.getRGB(j, k));
                int GrayLevel = color.getRed();
                if (GrayLevel > threshold)
                {
                    dstImage.setRGB(j, k, 0xFFFFFF);
                }
                else
                {
                    dstImage.setRGB(j, k, srcImage.getRGB(j, k));
                }
            }

        }
    }

    public void binarize(BufferedImage srcImage, BufferedImage dstImage)
    {
        int height = srcImage.getHeight();
        int width = srcImage.getWidth();

        for (int k = 0; k < height; k++)
        {
            for (int j = 0; j < width; j++)
            {
                Color color = new Color(srcImage.getRGB(j, k));
                int GrayLevel = color.getRed();
                if (GrayLevel > threshold)
                {
                    dstImage.setRGB(j, k, 0xFFFFFF);
                }
                else
                {
                    dstImage.setRGB(j, k, 0);
                }
            }

        }
    }

}
