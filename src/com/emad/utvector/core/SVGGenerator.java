/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.emad.utvector.core;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;
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
import org.w3c.dom.svg.SVGDocument;

/**
 *
 * @author tnp
 */
public class SVGGenerator
{

    private final String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
    private Document svgDocument;

    public SVGGenerator(Integer width, Integer height)
    {
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        svgDocument = impl.createDocument(svgNS, "svg", null);

        // Get the root element (the 'svg' element).
        Element svgRoot = svgDocument.getDocumentElement();

        // Set the width and height attributes on the root 'svg' element.
        svgRoot.setAttributeNS(null, "width", width.toString());
        svgRoot.setAttributeNS(null, "height", height.toString());
    }

    public void addPolyLine(String label, ArrayList<Point> points)
    {
        StringBuilder pointsBuilder = new StringBuilder();
        for (Point point : points)
        {
            pointsBuilder.append(point.x);
            pointsBuilder.append(",");
            pointsBuilder.append(point.y);
            pointsBuilder.append(" ");
        }

        Element polyLineElement = svgDocument.createElementNS(svgNS, "polyline");
        polyLineElement.setAttributeNS(null, "style", "fill:none;stroke:black;stroke-width:1");
        polyLineElement.setAttributeNS(null, "points", pointsBuilder.toString());
        polyLineElement.setAttributeNS(null, "id", label);

        Element rootElement = svgDocument.getDocumentElement();
        rootElement.appendChild(polyLineElement);
    }

    public void addPath(String label, Point[] knots, Point[] firstControlPoints,
            Point[] secondControlPoints)
    {
        StringBuilder pathBuilder = new StringBuilder("");
        pathBuilder.append("M" + knots[0].getX() + "," + knots[0].getY() + " ");
        for (int i = 0; i < firstControlPoints.length; i++)
        {
            pathBuilder.append("C" + firstControlPoints[i].getX() + "," + firstControlPoints[i].
                    getY() + " ");
            pathBuilder.append(secondControlPoints[i].getX() + "," + secondControlPoints[i].getY()
                    + " ");
            pathBuilder.append(knots[i + 1].getX() + "," + knots[i + 1].getY() + " ");
        }

        Element polyLineElement = svgDocument.createElementNS(svgNS, "path");
        polyLineElement.setAttributeNS(null, "style", "fill:none;stroke:black;stroke-width:1");
        polyLineElement.setAttributeNS(null, "d", pathBuilder.toString());
        polyLineElement.setAttributeNS(null, "class", label);

        Element rootElement = svgDocument.getDocumentElement();
        rootElement.appendChild(polyLineElement);
    }

    public void saveToFile(String fileName) throws
            TransformerConfigurationException, FileNotFoundException, TransformerException,
            IOException
    {
        Transformer transformer = TransformerFactory.newInstance().
                newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(svgDocument);
        FileOutputStream fileOutputStream = null;
        fileOutputStream = new FileOutputStream(new File(fileName));
        StreamResult result = new StreamResult(fileOutputStream);
        transformer.transform(source, result);
        fileOutputStream.close();
    }

    public void showSVG()
    {
        // Display the document.
//        JSVGCanvas canvas = new JSVGCanvas();
//        JFrame f = new JFrame();
//        f.getContentPane().add(canvas);
//        canvas.setSVGDocument((SVGDocument)svgDocument);
//        f.pack();
//        f.setVisible(true);
    }

}
