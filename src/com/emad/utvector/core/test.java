/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.emad.utvector.core;

import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author tnp
 */
public class test
{

    public static void main(String[] args)
    {
        try
        {
            BufferedImage inputImage = ImageIO.read(new File("skeleton-sample2.bmp"));
            ByteProcessor byteProc = new ByteProcessor(inputImage);
            double neighbour[][] = new double[3][3];
            byteProc.getNeighborhood(712, 107, neighbour);
            System.out.println("Hello");
        }
        catch (IOException ex)
        {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
