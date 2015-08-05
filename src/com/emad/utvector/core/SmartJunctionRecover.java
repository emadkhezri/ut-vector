/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.emad.utvector.core;

import static com.emad.utvector.core.Constants.KNN_NUMBER;
import com.emad.utvector.core.GeneralTypes.ClassifierType;
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

    private Classifier classifier;
    Dataset dataSet;

    public SmartJunctionRecover(ClassifierType type)
    {
        if (type == ClassifierType.KNN)
        {
            classifier = new KNearestNeighbors(KNN_NUMBER);
        }
        else if (type == ClassifierType.SVM)
        {
            classifier = new LibSVM();
        }
        dataSet = new DefaultDataset();
    }

    public SmartJunctionRecover(ClassifierType type, String dataSetFileName)
    {
        if (type == ClassifierType.KNN)
        {
            classifier = new KNearestNeighbors(KNN_NUMBER);
        }
        else if (type == ClassifierType.SVM)
        {
            classifier = new LibSVM();
        }
        try
        {
            dataSet = FileHandler.loadDataset(new File(dataSetFileName),0,",");
        }
        catch (IOException ex)
        {
            Logger.getLogger(SmartJunctionRecover.class.getName()).log(Level.SEVERE, null, ex);
        }
//        Instance myInstances[] = (Instance[])dataSet.toArray();
        classifier.buildClassifier(dataSet);
    }

    public void train(BufferedImage buffImage, PixelUtility pixelUtil)
    {
        ByteProcessor image = new ByteProcessor(buffImage);
        Scanner in = new Scanner(System.in);
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
                    dataSet.add(instance);
                }
            }
        }
        classifier.buildClassifier(dataSet);
    }

    public void saveDataSet(String datasetName)
    {
        try
        {
            FileHandler.exportDataset(dataSet, new File(datasetName), false, ",");
        }
        catch (IOException ex)
        {
            Logger.getLogger(SmartJunctionRecover.class.getName()).log(Level.SEVERE,
                    "Can not create file dataset.", ex);
        }
    }

    public int getJunctionType(Junction junc)
    {
        Object configType = classifier.classify(new DenseInstance(junc.getFeatureArray()));
        return (Integer.valueOf(configType.toString()));
    }

}
