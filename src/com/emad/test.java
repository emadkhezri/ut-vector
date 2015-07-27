/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.emad;

import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author tnp
 */
public class test
{

    public static void main(String[] args) throws
            TransformerConfigurationException, TransformerException
    {

        ///////////////////////////////////////////////////////////////////
        // We are using a constant available on the SVGDOMImplementation,
        // but we could have used "http://www.w3.org/2000/svg".
//            String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
//            DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
//            Document doc = impl.createDocument(svgNS, "svg", null);
//            Element svgRoot = doc.getDocumentElement();
//            svgRoot.setAttributeNS(null, "width", "400");
//            svgRoot.setAttributeNS(null, "height", "450");
//            
//            Element polyLine;
//            polyLine = doc.createElementNS(svgNS,"polyline");
//            polyLine.setAttributeNS(null, "points", "20,20 40,40, 60,60");
//            svgRoot.appendChild(polyLine);
//            String savepath = "test.svg";
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        Document doc = impl.createDocument(svgNS, "svg", null);

// Get the root element (the 'svg' element).
        Element svgRoot = doc.getDocumentElement();

// Set the width and height attributes on the root 'svg' element.
        svgRoot.setAttributeNS(null, "width", "400");
        svgRoot.setAttributeNS(null, "height", "450");

// Create the rectangle.
        Element rectangle = doc.createElementNS(svgNS, "rect");
        rectangle.setAttributeNS(null, "x", "10");
        rectangle.setAttributeNS(null, "y", "20");
        rectangle.setAttributeNS(null, "width", "100");
        rectangle.setAttributeNS(null, "height", "50");
        rectangle.setAttributeNS(null, "fill", "red");

// Attach the rectangle to the root 'svg' element.
        svgRoot.appendChild(rectangle);
        //
        // write the content into xml file// output DOM XML to console 
        Transformer transformer = TransformerFactory.newInstance().
                newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        FileOutputStream fileOutputStream=null;
        try
        {
            fileOutputStream = new FileOutputStream(new File("emad.xml"));
            fileOutputStream.write("hello world".getBytes());
        }
        catch (Exception ex)
        {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        }
        StreamResult result = new StreamResult(fileOutputStream);
        transformer.transform(source, result);
    }

}
