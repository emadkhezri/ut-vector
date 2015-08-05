/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.emad;

import java.awt.Color;
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
public class GrayScale
{

    public static File rgbToGray(File input) throws IOException
    {
        BufferedImage image = ImageIO.read(input);
        if (!image.getColorModel().getColorSpace().isCS_sRGB())
        {
            return input;
        }
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorConvertOp op = new ColorConvertOp(cs, null);
        image = op.filter(image, null);
        File outputFile = new File("grayScale-" + input.getName());
        ImageIO.write(image, "jpg", outputFile);
        return outputFile;
    }

    public BufferedImage rgbToGray2(BufferedImage inputImage) throws IOException
    {
        int width;
        int height;
        width = inputImage.getWidth();
        height = inputImage.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int i = 0; i < height; i++)
        {

            for (int j = 0; j < width; j++)
            {
                Color c = new Color(inputImage.getRGB(j, i));
                int red = (int)(c.getRed() * 0.299);
                int green = (int)(c.getGreen() * 0.587);
                int blue = (int)(c.getBlue() * 0.114);
                Color newColor = new Color(red + green + blue,
                        red + green + blue, red + green + blue);

                outputImage.setRGB(j, i, newColor.getRGB());
            }
        }
        return outputImage;

    }

}
