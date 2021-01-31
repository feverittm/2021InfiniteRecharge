package frc.spartanlib.swerve.module;

import frc.spartanlib.math.Vector2;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj2.command.Subsystem;

public abstract class SwerveModule<AziCont, Azi, Dri> implements Subsystem {

  public SwerveModule(int pID, int pEncoderID, double pEncoderZero) {
    this.mID = pID;
    this.mAzimuthEncoder = new AnalogInput(pEncoderID);
    this.mEncoderZero = pEncoderZero;
  }

  public boolean driveInverted = true;

  protected AziCont mAzimuthController;
  protected Dri mDrive;
  protected Azi mAzimuth;

  protected AnalogInput mAzimuthEncoder;
  protected final double ENCODER_MAX = 5;
  protected double mEncoderZero;

  protected double mMaxSpeed = 0.999;

  public int mID;
  protected double mTargetAngle = 0, mTargetSpeed = 0;
  protected double mLastError = 0, mLastOutput = 0;

  // public abstract void setTargetAngle(double angle);
  // public abstract void setTargetSpeed(double speed);
  protected abstract void setAzimuthSpeed(double pSpeed);
  protected abstract void setDriveSpeed(double pSpeed);
  public abstract void invertDrive(boolean pA);
  public abstract void invertDrive(boolean pA, boolean internal);
  public abstract void invertAzimuth(boolean pA);
  public abstract void setDriveBrakeMode(boolean pMode);

  public abstract void update();
  public abstract void updateSmartDashboard();
  public abstract void updateAzimuthPID(double pP, double pI, double pD);

  // public abstract double getTargetAngle();
  // public abstract double getTargetSpeed();
  // protected abstract double getAzimuthError();
  public abstract double getDriveSpeed();
  public abstract Vector2 getSpeedVector();
  public void setDriveEncoder(double count) { }

  // It won't adjust on the fly if it goes past 90 degrees but it will if it passes 180
  public double getAzimuthError() {
    double current = getAngle();
    double error = mTargetAngle - current;

    return limitRange(error, -180, 180);
  }

  public void setTargetSpeed(double speed) { mTargetSpeed = speed; }

  public void setTargetAngle(double angle) {
    double p = limitRange(angle, 0, 360);
    double current = getAngle();

    double delta = current - p;

    if (delta > 180) {
      p += 360;
    } else if (delta < -180) {
      p -= 360;
    }

    delta = current - p;
    if (delta > 90 || delta < -90) {
      if (delta > 90)
        p += 180;
      else if (delta < -90)
        p -= 180;
        invertDrive(!driveInverted);
    } else {
      invertDrive(driveInverted);
    }

    this.mTargetAngle = p;
  }

  public double getAngle() {
    return encoderToAngle(getEncoderParsed(), true);
  }

  public double getRawEncoder() { return mAzimuthEncoder.getVoltage(); }

  public double getEncoderParsed() {
    double a = getRawEncoder() - mEncoderZero;
    return limitRange(a, 0, ENCODER_MAX);
  }

  public double encoderToAngle(double val, boolean isParsed) {
    if (!isParsed) {
      val = val - mEncoderZero;
      val = limitRange(val, 0, ENCODER_MAX);
    }

    double mod = val / ENCODER_MAX;
    return 360 * mod;
  }

  public double angleToEncoder(double val) { return (ENCODER_MAX * val) / 360; }

  public double limitRange(double a, double min, double max) {
    while (a < min) a += (max - min);
    while (a >= max) a -= (max - min);
    return a;
  }

  public void setMaxSpeed(double maxSpeed) {
    mMaxSpeed = maxSpeed;
  }

  public double getTargetAngle() { return mTargetAngle; }
  public double getTargetSpeed() { return mTargetSpeed; }
  public double getLastError() { return mLastError; }
  public double getLastOutput() { return mLastOutput; }

}
