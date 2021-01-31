package frc.spartanlib.helpers;

public class RoboMisc {

    public static double[] arcadeDriveMixer(double _left, double _right) {
        double left = _left + _right;
        double right = _left - _right;

        double[] a = new double[2];
        a[0] = deadBand(-1.0, 1.0, left);
        a[1] = deadBand(-1.0, 1.0, right);
        return a;
    }

    public static double deadBand(double min, double max, double val) {
        if (val > max) {
            return max;
        } else if (val < min) {
            return min;
        } else {
            return val;
        }
    }

    public static String testFunction() {
        return "Test Successful";
    }

}
