package frc.spartanlib.math;

public class MathUtils {

  public static double clamp(double value, double min, double max) {
    if (value > max)
      return max;
    else if (value < min)
      return min;
    else
      return value;
  }

  public static double deadband(double value, double deadband) {
    if (Math.abs(value) < deadband)
      return 0;
    else
      return value;
  }

  public static boolean epsilon(double a, double b) {
    return Math.abs(a - b) < 1e-9; // Literally just a tolerance because lazy
  }

  public static boolean epsilon(double a, double b, double epsilon) {
    return Math.abs(a - b) < epsilon;
  }

}
