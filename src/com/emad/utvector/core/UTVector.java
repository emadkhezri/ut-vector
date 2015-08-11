/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.emad.utvector.core;

import static com.emad.utvector.core.Constants.DATASET_FILE_NAME;
import static com.emad.utvector.core.Constants.INPUT_FILE_NAME;
import static com.emad.utvector.core.Constants.OUTPUT_FILE_NAME;
import static com.emad.utvector.core.Constants.SEGMENT_PATH_CLASS;
import static com.emad.utvector.core.Constants.SIMPLIFY_TOLERANCE;
import static com.emad.utvector.core.GeneralTypes.ClassifierType.SVM;
import static com.emad.utvector.core.GeneralTypes.SimplifyAlgorithm.Douglas_Peucker;
import ij.process.ByteProcessor;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author tnp
 */
public class UTVector
{

    /**
     * @param args
     */
    public UTVector(String[] args)
    {
        for (String arg : args)
        {
            String key_value[] = arg.split("=");
            if (!Constants.initializeConstant(key_value[0], key_value[1]))
            {
                return;
            }
        }
    }

    public static void main(String[] args)
    {
        try
        {
            UTVector utVector = new UTVector(args);
            File imageFile = new File(INPUT_FILE_NAME);
            BufferedImage inputImage = ImageIO.read(imageFile);
            BufferedImage image;
            image = utVector.preProcess(inputImage);
            LinkedHashMap<String, ArrayList<Point>> segmentMap;
            segmentMap = utVector.process(image);
            utVector.postProcess(image, segmentMap);
        }
        catch (Exception ex)
        {
            System.out.println("Vectorization failed.\n" + ex.toString());
            Logger.getLogger(UTVector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public LinkedHashMap<String, ArrayList<Point>> process(BufferedImage image)
    {
        PixelUtility pixelUtil = new PixelUtility(image);
        pixelUtil.processAmbiguityPoints();

        SmartJunctionRecover smartJunc;
        smartJunc = new SmartJunctionRecover(SVM, DATASET_FILE_NAME);
        PathSegmentation pathSegmentation = new PathSegmentation(image, pixelUtil, smartJunc);
        int segmentNo = 0;
        for (int j = 0; j < image.getHeight(); j++)
        {
            for (int i = 0; i < image.getWidth(); i++)
            {
                if (new ByteProcessor(image).get(i, j) == 0)
                {
                    if (pathSegmentation.segmentation(new Point(i, j), SEGMENT_PATH_CLASS
                            + segmentNo))
                    {
                        segmentNo++;
                    }
                }
            }
        }
        LinkedHashMap<String, ArrayList<Point>> pathMap = pathSegmentation.getSegmentMap();
        return pathMap;
    }

    public BufferedImage preProcess(BufferedImage inputImage) throws IOException
    {
        BufferedImage outputImage = inputImage;
        Thresholding thresholding = new Thresholding(Constants.GRAY_SCALE_THRESHOLD);
        if (Constants.NEED_GRAY_SCALE_THRESHOLD)
        {
            outputImage = thresholding.threshold(inputImage);
        }
        MedianFilter medianFilter = new MedianFilter(Constants.MEDIAN_FILTER_FRAME_SIZE);
        if (Constants.NEED_MEDIAN_FILTER)
        {
            outputImage = medianFilter.filter(outputImage);
        }
        outputImage = thresholding.binarize(outputImage);
        new ByteProcessor(outputImage).skeletonize();
        return outputImage;
    }

    public void postProcess(BufferedImage image,
            LinkedHashMap<String, ArrayList<Point>> pathSegment) throws Exception
    {
        SVGGenerator svg = new SVGGenerator(image.getWidth(),
                image.getHeight());
        Simplify<Point> simplify = new Simplify<>(new Point[0]);
        // here we have an array with hundreds of points
        BezierSpline bezierSpline = new BezierSpline();
        for (String label : (Set<String>)pathSegment.keySet())
        {
            ArrayList<Point> pointsList = pathSegment.get(label);
            Point[] allPoints = new Point[pointsList.size()];
            allPoints = ((ArrayList<Point>)pathSegment.get(label)).toArray(allPoints);
            Point[] lessPoints = simplify.simplify(allPoints, SIMPLIFY_TOLERANCE,
                    Douglas_Peucker);
            Point firstControlPoints[] = new Point[lessPoints.length - 1];
            Point secondControlPoints[] = new Point[lessPoints.length - 1];
            bezierSpline.GetCurveControlPoints(lessPoints, firstControlPoints, secondControlPoints);
            svg.addPath(label, lessPoints, firstControlPoints, secondControlPoints);
        }

        svg.saveToFile(OUTPUT_FILE_NAME);
        //svg.showSVG();
    }

}
