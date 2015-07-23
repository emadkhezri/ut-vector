/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.emad;

import ij.process.ByteProcessor;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author tnp
 */
public class Vectorization
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        File input = null;
        try
        {
            input = new File("sample.jpg");
            File output = GrayScale.rgbToGray2(input);
            BufferedImage inputImage = ImageIO.read(output);
            Thresholding thresholding = new Thresholding(120);
            thresholding.threshold(inputImage, inputImage);
            ImageIO.write(inputImage, "jpg", new File("thresholded-sample.jpg"));
            BufferedImage outputImage = new BufferedImage(inputImage.getWidth(),
                    inputImage.getHeight(),
                    BufferedImage.TYPE_BYTE_GRAY);
            MedianFilter medianFilter = new MedianFilter(3);
            medianFilter.filter(inputImage, outputImage);
            ImageIO.write(outputImage, "jpg",
                    new File("medianFilter-sample.jpg"));
            thresholding.binarize(outputImage, outputImage);
            ImageIO.write(outputImage, "jpg", new File("binary-sample.jpg"));
            new ByteProcessor(outputImage).skeletonize();
            ImageIO.write(outputImage, "jpg", new File("skeleton-sample.jpg"));
        }
        catch (Exception e)
        {

        }
    }

}
