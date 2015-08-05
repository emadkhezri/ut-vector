/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.emad.utvector.core;

import java.awt.Point;

/**
 *
 * @author tnp
 */
public class BezierSpline
{
    /// <summary>
    /// Get open-ended Bezier Spline Control Points.
    /// </summary>
    /// <param name="knots">Input Knot Bezier spline points.</param>
    /// <param name="firstControlPoints">Output First Control points
    /// array of knots.Length - 1 length.</param>
    /// <param name="secondControlPoints">Output Second Control points
    /// array of knots.Length - 1 length.</param>
    /// <exception cref="ArgumentNullException"><paramref name="knots"/>
    /// parameter must be not null.</exception>
    /// <exception cref="ArgumentException"><paramref name="knots"/>
    /// array must contain at least two points.</exception>

    public void GetCurveControlPoints(Point[] knots, Point[] firstControlPoints,
            Point[] secondControlPoints) throws
            Exception
    {
        if (knots == null)
        {
            throw new Exception("knots");
        }
        int n = knots.length - 1;
        if (n < 1)
        {
            return;
        }
        if (n == 1)
        { // Special case: Bezier curve should be a straight line.
            // 3P1 = 2P0 + P3
            firstControlPoints[0] = new Point();
            double x = (2 * knots[0].getX() + knots[1].getX()) / 3;
            double y = (2 * knots[0].getY() + knots[1].getY()) / 3;
            firstControlPoints[0].setLocation(x, y);
            
            secondControlPoints[0] = new Point();
            // P2 = 2P1 – P0
            x = 2 * firstControlPoints[0].getX() - knots[0].getX();
            y = 2 * firstControlPoints[0].getY() - knots[0].getY();
            secondControlPoints[0].setLocation(x, y);
            return;
        }

        // Calculate first Bezier control points
        // Right hand side vector
        double[] rhs = new double[n];

        // Set right hand side X values
        for (int i = 1; i < n - 1; ++i)
        {
            rhs[i] = 4 * knots[i].getX() + 2 * knots[i + 1].getX();
        }
        rhs[0] = knots[0].getX() + 2 * knots[1].getX();
        rhs[n - 1] = (8 * knots[n - 1].getX() + knots[n].getX()) / 2.0;
        // Get first control points X-values
        double[] x = GetFirstControlPoints(rhs);

        // Set right hand side Y values
        for (int i = 1; i < n - 1; ++i)
        {
            rhs[i] = 4 * knots[i].getY() + 2 * knots[i + 1].getY();
        }
        rhs[0] = knots[0].getY() + 2 * knots[1].getY();
        rhs[n - 1] = (8 * knots[n - 1].getY() + knots[n].getY()) / 2.0;
        // Get first control points Y-values
        double[] y = GetFirstControlPoints(rhs);

        // Fill output arrays.
        //firstControlPoints = new Point[n];
        //secondControlPoints = new Point[n];
        for (int i = 0; i < n; ++i)
        {
            // First control point
            firstControlPoints[i] = new Point();
            firstControlPoints[i].setLocation(x[i], y[i]);
            // Second control point
            if (i < n - 1)
            {
                secondControlPoints[i] = new Point();
                secondControlPoints[i].setLocation(2 * knots[i + 1].x - x[i + 1], 2 * knots[i
                        + 1].y - y[i + 1]);
            }
            else
            {
                secondControlPoints[i] = new Point();
                secondControlPoints[i].setLocation((knots[n].x + x[n - 1]) / 2,
                        (knots[n].y + y[n - 1]) / 2);
            }
        }
    }

    /// <summary>
    /// Solves a tridiagonal system for one of coordinates (x or y)
    /// of first Bezier control points.
    /// </summary>
    /// <param name="rhs">Right hand side vector.</param>
    /// <returns>Solution vector.</returns>
    private static double[] GetFirstControlPoints(double[] rhs)
    {
        int n = rhs.length;
        double[] x = new double[n]; // Solution vector.
        double[] tmp = new double[n]; // Temp workspace.

        double b = 2.0;
        x[0] = rhs[0] / b;
        for (int i = 1; i < n; i++) // Decomposition and forward substitution.
        {
            tmp[i] = 1 / b;
            b = (i < n - 1 ? 4.0 : 3.5) - tmp[i];
            x[i] = (rhs[i] - x[i - 1]) / b;
        }
        for (int i = 1; i < n; i++)
        {
            x[n - i - 1] -= tmp[n - i] * x[n - i]; // Backsubstitution.
        }
        return x;
    }

}
