/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.emad.utvector.core;

import com.emad.utvector.core.GeneralTypes.ClassifierType;
import static com.emad.utvector.core.GeneralTypes.ClassifierType.SVM;
import com.emad.utvector.core.GeneralTypes.SimplifyAlgorithm;
import static com.emad.utvector.core.GeneralTypes.SimplifyAlgorithm.Douglas_Peucker;

/**
 *
 * @author tnp
 */
public class Constants
{

    public static String INPUT_FILE_NAME = "sample.bmp";
    public static String IMAGE_FORMAT_NAME="bmp";
    public static String OUTPUT_FILE_NAME = "sample.svg";
    public static boolean NEED_MEDIAN_FILTER = true;
    public static boolean NEED_GRAY_SCALE_THRESHOLD = true;
    public static int MEDIAN_FILTER_FRAME_SIZE = 3;
    public static int GRAY_SCALE_THRESHOLD = 120;
    public static String DATASET_FILE_NAME = "dataset.csv";
    public static ClassifierType CLASSIFIER_TYPE = SVM;
    public static int KNN_NUMBER = 5;
    public static int JUNCTION_RECOVERY_STEP = 4;
    public static String SEGMENT_PATH_CLASS = "PATH";
    public static SimplifyAlgorithm SIMPLIFY_ALGORITHM = Douglas_Peucker;
    public static double SIMPLIFY_TOLERANCE = 0.9;

    public static boolean initializeConstant(String key, String value)
    {
        switch (key)
        {
        case "INPUT_FILE_NAME":
            INPUT_FILE_NAME = value;
            break;
            
        case "OUTPUT_FILE_NAME":
            OUTPUT_FILE_NAME = value;
            break;

        case "NEED_MEDIAN_FILTER":
            NEED_MEDIAN_FILTER = Boolean.valueOf(value);
            break;

        case "NEED_GRAY_SCALE_THRESHOLD":
            NEED_GRAY_SCALE_THRESHOLD = Boolean.valueOf(value);
            break;

        case "MEDIAN_FILTER_FRAME_SIZE":
            MEDIAN_FILTER_FRAME_SIZE = Integer.valueOf(value);
            break;

        case "GRAY_SCALE_THRESHOLD":
            GRAY_SCALE_THRESHOLD = Integer.valueOf(value);
            break;

        case "CLASSIFIER_TYPE":
            CLASSIFIER_TYPE = ClassifierType.valueOf(value);
            break;

        case "DATASET_FILE_NAME":
            DATASET_FILE_NAME = value;
            break;

        case "KNN_NUMBER":
            KNN_NUMBER = Integer.valueOf(value);
            break;

        case "JUNCTION_RECOVERY_STEP":
            JUNCTION_RECOVERY_STEP = Integer.valueOf(value);
            break;

        case "SIMPLIFY_ALGORITHM":
            SIMPLIFY_ALGORITHM = SimplifyAlgorithm.valueOf(value);
            break;

        case "SIMPLIFY_TOLERANCE":
            SIMPLIFY_TOLERANCE = Double.valueOf(value);
            break;
        default:
            System.out.println("Invalid Parameters.");
            return false;
        }
        return true;
    }

}
