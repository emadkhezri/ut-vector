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

    public static File rgbToGray2(File input) throws IOException
    {
        BufferedImage image;
        int width;
        int height;
        image = ImageIO.read(input);
        width = image.getWidth();
        height = image.getHeight();

        for (int i = 0; i < height; i++)
        {

            for (int j = 0; j < width; j++)
            {
                Color c = new Color(image.getRGB(j, i));
                int red = (int)(c.getRed() * 0.299);
                int green = (int)(c.getGreen() * 0.587);
                int blue = (int)(c.getBlue() * 0.114);
                Color newColor = new Color(red + green + blue,
                        red + green + blue, red + green + blue);

                image.setRGB(j, i, newColor.getRGB());
            }
        }

        File output = new File("grayScale-" + input.getName());
        ImageIO.write(image, "jpg", output);
        return output;

    }

}
