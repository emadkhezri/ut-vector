/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.emad;

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
            File output = rgb2gray(input);
            BufferedImage inputImage = ImageIO.read(output);
            Thresholding thresholding = new Thresholding(100);
            thresholding.threshold(inputImage, inputImage);
            ImageIO.write(inputImage, "jpg", new File("emad2.jpg"));
            BufferedImage outputImage = new BufferedImage(inputImage.getWidth(),
                    inputImage.getHeight(),
                    BufferedImage.TYPE_BYTE_GRAY);
            MedianFilter medianFilter = new MedianFilter(3);
            medianFilter.filter(inputImage, outputImage);
            ImageIO.write(outputImage, "jpg", new File("emad.jpg"));
        }
        catch (Exception e)
        {

        }
    }

    public static File rgb2gray(File input) throws IOException
    {
        BufferedImage image = ImageIO.read(input);
        if (!image.getColorModel().getColorSpace().isCS_sRGB())
        {
            return input;
        }
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorConvertOp op = new ColorConvertOp(cs, null);
        image = op.filter(image, null);
        File outputFile = new File("output-" + input.getName());
        ImageIO.write(image, "jpg", outputFile);
        return outputFile;
    }

}
