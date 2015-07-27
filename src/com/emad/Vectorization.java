/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.emad;

import com.goebl.simplify.Simplify;
import ij.process.ByteProcessor;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Set;
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
            PathSegmentation pathSegmentation
                    = new PathSegmentation(outputImage, pixelUtil);
            int segmentNo = 0;
            for (int j = 0; j < outputImage.getHeight(); j++)
            {
                for (int i = 0; i < outputImage.getWidth(); i++)
                {
                    if (new ByteProcessor(outputImage).get(i, j) == 0)
                    {
                        if (pathSegmentation.segmentation(new Point(i, j),
                                "Segment" + segmentNo))
                        {
                            segmentNo++;
                        }
                    }
                }
            }
            LinkedHashMap pathMap = pathSegmentation.getSegmentMap();
            SVGGenerator svg = new SVGGenerator(outputImage.getWidth(),
                    outputImage.getHeight());
            Simplify<Point> simplify = new Simplify<Point>(new Point[0]);
            // here we have an array with hundreds of points
            double tolerance = 0.7;
            boolean highQuality = true; // Douglas-Peucker, false for Radial-Distance
            for (String label : (Set<String>)pathMap.keySet())
            {
                Point[] allPoints = new Point[((ArrayList<Point>)pathMap.get(label)).size()];
                allPoints = ((ArrayList<Point>)pathMap.get(label)).toArray(allPoints);
                Point[] lessPoints = simplify.simplify(allPoints, tolerance, highQuality);
                svg.addPolyLine(label, new ArrayList<>(Arrays.asList(lessPoints)));
            }

            svg.saveToFile("second.svg");
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
