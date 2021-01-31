package frc.spartanlib.motion.path;

import java.util.ArrayList;
import java.util.List;

public class MotionPath {
  
  private MotionSpline[] mSplines;
  private Point[] mPoints;

  public MotionPath(Waypoint... waypoints) {
    //for (int i = 0; i < waypoints.length; i++) {
      //System.out.println("[" + i + "] X=" + waypoints[i].getPosition().x + " Y=" + waypoints[i].getPosition().y);
    //}
    mSplines = new MotionSpline[waypoints.length - 1];
    //System.out.println("Num Splines: " + mSplines.length);
    for (int i = 0; i < waypoints.length - 1; i++) {
      mSplines[i] = new MotionSpline(waypoints[i], waypoints[i + 1]);
    }
  }

  public void calculate(int steps) {
    List<Point> points = new ArrayList<Point>();
    //System.out.println("Spline Length: " + mSplines.length);
    for (int i = 0; i < mSplines.length; i++) {
      //System.out.println("I: " + i);
      mSplines[i].calulate(steps);
    }

    //System.out.println("Check 0");

    for (int i = 0; i < mSplines.length; i++) {

      Point[] p = mSplines[i].getPoints();
      int offset = 1;
      if (i == mSplines.length - 1) offset = 0;

      for (int f = 0; f < p.length - offset; f++) {
        //System.out.println("F: " + f);
        points.add(p[f]);
      }

    }

    //System.out.println("Check 1");

    mPoints = new Point[points.size()];
    for (int i = 0; i < mPoints.length; i++) {
      mPoints[i] = points.get(i);
    }
  }

  public Point[] getPoints() { return mPoints; }

}
