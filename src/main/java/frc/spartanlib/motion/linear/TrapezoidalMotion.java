package frc.spartanlib.motion.linear;

public class TrapezoidalMotion {

  private double
    accel = 0,
    maxVelocity = 0,
    timeToCruise = 0,
    timeToSlow = 0,
    distToSlow = 0,
    distance = 0;

  public TrapezoidalMotion(double accel, double maxVel) {
    this.accel = accel;
    this.maxVelocity = maxVel;
  }

  public void initMotion(double distance) {
    this.distance = distance;

    timeToSlow = quadEquation(0.5 * accel, 0, -(distance / 2));
    
    if ((maxVelocity / accel) >= timeToSlow) {
      timeToCruise = 0;
      distToSlow = accel * 0.5 * timeToSlow * timeToSlow;
    } else {
      timeToSlow = maxVelocity / accel;
      distToSlow = accel * 0.5 * timeToSlow * timeToSlow;

      double h = distance - (distToSlow * 2);
      timeToCruise = (h / maxVelocity);
    }
  }

  public double getPosition(double time) {
    // if (time < 0) {
    //   return distance;
    // } else if (time > (timeToSlow * 2) + timeToCruise) {
    //   return 0;
    // }

    double sum = 0;

    if (time > (timeToSlow * 2) + timeToCruise) {
      return distance;
    } else if (time > timeToSlow + timeToCruise) {
      sum += getDistanceAccel(timeToSlow);
      sum += getDistanceCruise(timeToCruise);
      sum += getDistanceDecel(time - (timeToCruise + timeToSlow));
    } else if (time > timeToSlow) {
      sum += getDistanceAccel(timeToSlow);
      sum += getDistanceCruise(time - timeToSlow);
    } else if (time > 0) {
      sum += getDistanceAccel(time);
    }

    return round(sum, 4);
  }

  public double getVelocity(double time) {
    double sum = 0;

    if (time > (timeToSlow * 2) + timeToCruise) {
      return 0;
    } else if (time > timeToSlow + timeToCruise) {
      sum += (-accel * (time - (timeToSlow + timeToCruise))) + (accel * timeToSlow);
    } else if (time > timeToSlow) {
      sum += accel * timeToSlow;
    } else if (time > 0) {
      sum += accel * time;
    }

    return round(sum, 4);
  }

  // sig figs from the ones place
  private double round(double val, int sigFigs) {
    int a = (int)Math.round(val * Math.pow(10, sigFigs));

    return (double)(a / Math.pow(10, sigFigs));
  }

  private double getDistanceAccel(double t) {
    return accel * 0.5 * t * t;
  }

  private double getDistanceDecel(double t) {
    double h =  -accel * 0.5 * t * t;
    h += (accel * timeToSlow) * t;
    return h;
  }

  private double getDistanceCruise(double t) {
    return (accel * timeToSlow) * t;
  }

  public double getAccelTime() { return timeToSlow; }

  public double getCruiseTime() { return timeToCruise; }

  public double getTravelTime() { return (timeToSlow * 2) + timeToCruise; }

  private static double quadEquation(double a, double b, double c) {
    double x = (-b + Math.sqrt((b * b) + (-4 * a * c))) / (2 * a);
    return x;
  }

}
