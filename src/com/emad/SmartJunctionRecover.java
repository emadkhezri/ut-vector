/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.emad;

import ij.process.ByteProcessor;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import libsvm.LibSVM;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.KNearestNeighbors;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.FileHandler;

/**
 *
 * @author tnp
 */
public class SmartJunctionRecover
{
    private Classifier knn;

    public SmartJunctionRecover()
    {
        knn = new KNearestNeighbors(5);
    }
    
    public SmartJunctionRecover(String dataSetFileName)
    {
        Dataset dataSet=null;
        try
        {
            dataSet = FileHandler.loadDataset(new File(dataSetFileName),4,"\t");
        }
        catch (IOException ex)
        {
            Logger.getLogger(SmartJunctionRecover.class.getName()).log(Level.SEVERE, null, ex);
        }
        knn = new LibSVM();
        knn.buildClassifier(dataSet);
    }

    public void train(BufferedImage buffImage, PixelUtility pixelUtil)
    {
        ByteProcessor image = new ByteProcessor(buffImage);
        Scanner in = new Scanner(System.in);
        Dataset data = new DefaultDataset();
        for (int j = 0; j < image.getHeight(); j++)
        {
            for (int i = 0; i < image.getWidth(); i++)
            {
                if (pixelUtil.isAmbiguity(new java.awt.Point(i, j)))
                {
                    System.out.println("Set configuration type for junction:");
                    System.out.println("X=" + i + " Y=" + j);
                    int configType = in.nextInt();
                    Junction junc = new Junction(new java.awt.Point(i, j), 3, pixelUtil);

                    Instance instance = new DenseInstance(junc.getFeatureArray(), configType);
                    data.add(instance);
                }
            }
        }
        try
        {
            FileHandler.exportDataset(data, new File("trained.data"));
        }
        catch (IOException ex)
        {
            Logger.getLogger(SmartJunctionRecover.class.getName()).log(Level.SEVERE, null, ex);
        }
        knn.buildClassifier(data);
    }

    public int getJunctionType(Junction junc)
    {
        Object configType = knn.classify(new DenseInstance(junc.getFeatureArray()));
        return ((Integer)configType);
    }

}
