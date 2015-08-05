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
     * @param inputImage
     * @param dstImage
     */
    public BufferedImage threshold(BufferedImage inputImage)
    {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int k = 0; k < height; k++)
        {
            for (int j = 0; j < width; j++)
            {
                Color color = new Color(inputImage.getRGB(j, k));
                int GrayLevel = color.getRed();
                if (GrayLevel > threshold)
                {
                    outputImage.setRGB(j, k, 0xFFFFFF);
                }
                else
                {
                    outputImage.setRGB(j, k, inputImage.getRGB(j, k));
                }
            }

        }
        return outputImage;
    }

    public BufferedImage binarize(BufferedImage inputImage)
    {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int k = 0; k < height; k++)
        {
            for (int j = 0; j < width; j++)
            {
                Color color = new Color(inputImage.getRGB(j, k));
                int GrayLevel = color.getRed();
                if (GrayLevel > threshold)
                {
                    outputImage.setRGB(j, k, 0xFFFFFF);
                }
                else
                {
                    outputImage.setRGB(j, k, 0);
                }
            }

        }
        return outputImage;
    }

}
