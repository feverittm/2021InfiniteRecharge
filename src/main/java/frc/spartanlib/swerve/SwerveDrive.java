package frc.spartanlib.swerve;

import frc.spartanlib.helpers.SwerveMixerData;
import frc.spartanlib.swerve.module.SwerveModule;

import edu.wpi.first.wpilibj2.command.Subsystem;

public abstract class SwerveDrive implements Subsystem {

  protected double mWheelBase, mTrackWidth;

  protected SwerveModule[] mModules;

  public SwerveDrive(double pWheelBase, double pTrackWidth) {
    mWheelBase = pWheelBase;
    mTrackWidth = pTrackWidth;
  }

  public SwerveMixerData getSwerveData(double pForward, double pStrafe, double pRotation, double pAngle) {
    return getSwerveData(pForward, pStrafe, pRotation, true, pAngle);
  }

  public SwerveMixerData getSwerveData(double pForward, double pStrafe, double pRotation) {
    return getSwerveData(pForward, pStrafe, pRotation, false, 0.0);
  }

  /**
   * Basically 95% leveraged from Jack In The Bot
   */
  private SwerveMixerData getSwerveData(double pForward, double pStrafe, double pRotation, boolean pIsFieldOriented, double pAngle) {
    SwerveMixerData smd = new SwerveMixerData();
    smd.setForward(pForward);
    smd.setStrafe(pStrafe);
    smd.setRotate(pRotation);

    if (pIsFieldOriented) {
      double angleRad = Math.toRadians(pAngle);
      double temp = pForward * Math.cos(angleRad) + pStrafe * Math.sin(angleRad);
      pStrafe = -pForward * Math.sin(angleRad) + pStrafe * Math.cos(angleRad);
      pForward = temp;
    }

    double a = pStrafe - pRotation * (mWheelBase / mTrackWidth);
    double b = pStrafe + pRotation * (mWheelBase / mTrackWidth);
    double c = pForward - pRotation * (mTrackWidth / mWheelBase);
    double d = pForward + pRotation * (mTrackWidth / mWheelBase);

    double[] angles = new double[] { Math.atan2(b, c) * 180 / Math.PI, Math.atan2(b, d) * 180 / Math.PI,
        Math.atan2(a, d) * 180 / Math.PI, Math.atan2(a, c) * 180 / Math.PI };

    double[] speeds = new double[] { Math.sqrt(b * b + c * c), Math.sqrt(b * b + d * d), Math.sqrt(a * a + d * d),
        Math.sqrt(a * a + c * c) };

    double max = speeds[0];

    for (double speed : speeds) {
      if (speed > max) {
        max = speed;
      }
    }

    double mod = 1;
    if (max > 1) {
      mod = 1 / max;

      for (int i = 0; i < 4; i++) {
        speeds[i] *= mod;

        angles[i] %= 360;
        if (angles[i] < 0)
          angles[i] += 360;
      }
    }

    smd.setAngles(angles);
    smd.setSpeeds(speeds);
    return smd;
  }

  public void setSwerveInput(SwerveMixerData smd) {
    for (int i = 0; i < mModules.length; i++) {
      if (Math.abs(smd.getForward()) > 0.05 || Math.abs(smd.getStrafe()) > 0.05 || Math.abs(smd.getRotate()) > 0.05) {
        mModules[i].setTargetAngle(smd.getAngles()[i]);
      } else {
        mModules[i].setTargetAngle(mModules[i].getTargetAngle());
      }
      mModules[i].setTargetSpeed(smd.getSpeeds()[i] * 1);
    }
  }

  public void updateAzimuthPID(double p, double i, double d) {
    for (SwerveModule mod : mModules) {
      mod.updateAzimuthPID(p, i, d);
    }
  }

  public void setDriveEncoders(double count) {
    for (SwerveModule mod : mModules) {
      mod.setDriveEncoder(count);
    }
  }

  public void updateAzimuthPID(int id, double p, double i, double d) { mModules[id].updateAzimuthPID(p, i, d); }

  public SwerveModule getModule(int index) { return mModules[index]; }

  public SwerveModule[] getModules() { return mModules; }

}
