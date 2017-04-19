
package com.emad.utvector.core;


import java.awt.Point;
import java.util.ArrayList;
import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;

/**
 * Class used to fit a bezier curve to a list of points.
 * Imposes 4 constraints leaving 4 unknowns which are optimized using simulated annealing.
 * 
 * @author jhero
 *
 */

public class BezierFit {

	public static void main(String[] args){

		ArrayList<Point> points = new ArrayList<Point>();

		points.add(new Point(0, 0));
		points.add(new Point(1, 1));
		points.add(new Point(2, 0));
		points.add(new Point(3, 2));
		
		bestFitTest(points);

	}
	
	private static void bestFitTest(ArrayList<Point> points){
		BezierFit bf = new BezierFit();
		
		Point[] controlPoints = bf.bestFit(points);
		
		System.out.print("X:");
		for(Point p : points) System.out.print(p.getX() + ",");
		System.out.println();
				
		System.out.print("Y:");
		for(Point p : points) System.out.print(p.getY() + ",");
		System.out.println();
				
		System.out.print("Bx:");
		for(double ti = 0; ti <= 1; ti += 0.01) System.out.print(bf.pointOnCurve(ti, controlPoints[0], controlPoints[1], controlPoints[2], controlPoints[3]).getX() +"," );
		System.out.println();
				
		System.out.print("By:");
		for(double ti = 0; ti <= 1; ti += 0.01) System.out.print(bf.pointOnCurve(ti, controlPoints[0], controlPoints[1], controlPoints[2], controlPoints[3]).getY() +"," );
		System.out.println();
				
		System.out.print("Cx:");
		for(Point p : controlPoints) System.out.print(p.getX() + ",");
		System.out.println();
				
		System.out.print("Cy:");
		for(Point p : controlPoints) System.out.print(p.getY() + ",");
		System.out.println();
				
	}

	/**
	 * Computes the best bezier fit of the supplied points using a simple RSS minimization.
	 * Returns a list of 4 points, the control points
	 * @param points
	 * @return
	 */
	public Point[] bestFit(ArrayList<Point> points){
		Matrix M = M();
		Matrix Minv;
		if(M.det() == 0)Minv = M.invSPD();
		else Minv = M.inv();
		Matrix U = U(points);
		Matrix UT = U.transpose();
		Matrix X = X(points);
		Matrix Y = Y(points);
		
		Matrix A = UT.mtimes(U);
		Matrix B;
		if(A.det() == 0) B = A.invSPD();
		else B = A.inv();
		Matrix C = Minv.mtimes(B);
		Matrix D = C.mtimes(UT);
		Matrix E = D.mtimes(X);
		Matrix F = D.mtimes(Y);
		
		Point[] P = new Point[4];
		for(int i = 0; i < 4; i++){
			double x = E.getAsDouble(i, 0);
			double y = F.getAsDouble(i, 0);
			
			Point p = new Point();
                        p.setLocation(x, y);
			P[i] = p;
		}
		
		return P;
	}
	
	private Matrix Y(ArrayList<Point> points){
		Matrix Y = MatrixFactory.fill(0.0, points.size(), 1);
		
		for(int i = 0; i < points.size(); i++)
			Y.setAsDouble(points.get(i).getY(), i, 0);
		
		return Y;
	}
	
	private Matrix X(ArrayList<Point> points){
		Matrix X = MatrixFactory.fill(0.0, points.size(), 1);
		
		for(int i = 0; i < points.size(); i++)
			X.setAsDouble(points.get(i).getX(), i, 0);
		
		return X;
	}
	
	private Matrix U(ArrayList<Point> points){
		double[] npls = normalizedPathLengths(points);
		
		Matrix U = MatrixFactory.fill(0.0, npls.length, 4);
		for(int i = 0; i < npls.length; i++){
			U.setAsDouble(Math.pow(npls[i], 3), i, 0);
			U.setAsDouble(Math.pow(npls[i], 2), i, 1);
			U.setAsDouble(Math.pow(npls[i], 1), i, 2);
			U.setAsDouble(Math.pow(npls[i], 0), i, 3);
		}

		return U;
	}
	
	private Matrix M(){
		Matrix M = MatrixFactory.fill(0.0, 4, 4);
		M.setAsDouble(-1, 0, 0);
		M.setAsDouble( 3, 0, 1);
		M.setAsDouble(-3, 0, 2);
		M.setAsDouble( 1, 0, 3);
		M.setAsDouble( 3, 1, 0);
		M.setAsDouble(-6, 1, 1);
		M.setAsDouble( 3, 1, 2);
		M.setAsDouble( 0, 1, 3);
		M.setAsDouble(-3, 2, 0);
		M.setAsDouble( 3, 2, 1);
		M.setAsDouble( 0, 2, 2);
		M.setAsDouble( 0, 2, 3);
		M.setAsDouble( 1, 3, 0);
		M.setAsDouble( 0, 3, 1);
		M.setAsDouble( 0, 3, 2);
		M.setAsDouble( 0, 3, 3);
		return M;
	}

	/**
	 * Computes b(t).
	 * @param t
	 * @param v1
	 * @param v2
	 * @param v3
	 * @param v4
	 * @return
	 */
	private Point pointOnCurve(double t, Point v1, Point v2, Point v3, Point v4){
		Point p;

		double x1 = v1.getX();
		double x2 = v2.getX();
		double x3 = v3.getX();
		double x4 = v4.getX();

		double y1 = v1.getY();
		double y2 = v2.getY();
		double y3 = v3.getY();
		double y4 = v4.getY();

		double xt, yt;

		xt = x1 * Math.pow((1-t),3) 
				+ 3 * x2 * t * Math.pow((1-t), 2)
				+ 3 * x3 * Math.pow(t,2) * (1-t)
				+ x4 * Math.pow(t,3);

		yt = y1 * Math.pow((1-t),3) 
				+ 3 * y2 * t * Math.pow((1-t), 2)
				+ 3 * y3 * Math.pow(t,2) * (1-t)
				+ y4 * Math.pow(t,3);

		p = new Point();
                p.setLocation(xt, yt);

		return p;
	}

	/** Computes the percentage of path length at each point. Can directly be used as t-indices into the bezier curve. */
	private double[] normalizedPathLengths(ArrayList<Point> points){
		double pathLength[] = new double[points.size()];

		pathLength[0] = 0;

		for(int i = 1; i < points.size(); i++){
			Point p1 = points.get(i);
			Point p2 = points.get(i-1);
			double distance = Math.sqrt(Math.pow(p1.getX() - p2.getX(),2) + Math.pow(p1.getY() - p2.getY(),2));
			pathLength[i] += pathLength[i-1] + distance;
		}

		double [] zpl = new double[pathLength.length];
		for(int i = 0; i < zpl.length; i++)
			zpl[i] = pathLength[i] / pathLength[pathLength.length-1];

		return zpl;
	}

}