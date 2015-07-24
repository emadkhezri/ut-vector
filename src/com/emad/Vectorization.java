/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.emad;

import ij.process.ByteProcessor;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public Vectorization()
    {
    }

    public static void main(String[] args)
    {
        try
        {
            Vectorization v = new Vectorization();
            BufferedImage outputImage = v.preProcessing();

            PixelUtility pixelUtil = new PixelUtility(outputImage);
            pixelUtil.processAmbiguityPoints();
            LinkedHashMap map = pixelUtil.getAmbiguityMap();
            PathSegmentation pathSegmentation = new PathSegmentation(outputImage, pixelUtil);
            int segmentNo=0;
            for(int j=0;j<outputImage.getHeight();j++)
                for(int i=0;i<outputImage.getWidth();i++)
                {
                    if(new ByteProcessor(outputImage).get(i, j)==0)
                    {
                        pathSegmentation.segmentation(new Point(i, j), "Segment"+segmentNo);
                        segmentNo++;
                    }
                }
            LinkedHashMap pathMap = pathSegmentation.getSegmentMap();
            System.out.println("End");
        }
        catch (Exception e)
        {

        }
    }

    private BufferedImage preProcessing()
    {
        try
        {
            File input = null;
            input = new File("sample.jpg");
            File output = GrayScale.rgbToGray2(input);
            BufferedImage inputImage = ImageIO.read(output);
            Thresholding thresholding = new Thresholding(120);
            thresholding.threshold(inputImage, inputImage);
            ImageIO.write(inputImage, "bmp", new File("thresholded-sample.bmp"));
            BufferedImage outputImage = new BufferedImage(inputImage.getWidth(),
                    inputImage.getHeight(),
                    BufferedImage.TYPE_BYTE_GRAY);
            MedianFilter medianFilter = new MedianFilter(3);
            medianFilter.filter(inputImage, outputImage);
            ImageIO.write(outputImage, "bmp",
                    new File("medianFilter-sample.bmp"));
            thresholding.binarize(outputImage, outputImage);
            ImageIO.write(outputImage, "bmp", new File("binary-sample.bmp"));
            new ByteProcessor(outputImage).skeletonize();
            ImageIO.write(outputImage, "bmp", new File("skeleton-sample.bmp"));
        return outputImage;
        }
        catch (IOException ex)
        {
            Logger.getLogger(Vectorization.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
