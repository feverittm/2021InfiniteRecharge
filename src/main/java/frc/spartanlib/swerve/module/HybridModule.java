package frc.spartanlib.swerve.module;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import frc.spartanlib.controllers.MiniPID;
import frc.spartanlib.math.Vector2;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HybridModule extends SwerveModule<MiniPID, WPI_TalonSRX, WPI_VictorSPX> {

  private final int ALIGNMENT_TIMEOUT = 1250; // Milliseconds until I start complaining
  private final double ALIGNMENT_TOLERANCE = 2.5; // Tolerance in degrees
  private double mLastGoodAlignment;

  public HybridModule(int pID, int pAzimuthID, int pDriveID, int pEncoderID, double pEncoderZero, double pP, double pI,
      double pD) {

    super(pID, pEncoderID, pEncoderZero);

    mAzimuth = new WPI_TalonSRX(pAzimuthID);
    invertAzimuth(true);
    mDrive = new WPI_VictorSPX(pDriveID);

    mAzimuthController = new MiniPID(pP, pI, pD);
    mAzimuthController.setOutputLimits(-200, 200);
  }

  @Override
  protected void setAzimuthSpeed(double pSpeed) {
    mAzimuth.set(ControlMode.PercentOutput, pSpeed);
  }

  @Override
  protected void setDriveSpeed(double pSpeed) {
    mDrive.set(ControlMode.PercentOutput, pSpeed);
  }

  @Override
  public void invertDrive(boolean pA) {
    mDrive.setInverted(pA);
  }

  @Override
  public void invertDrive(boolean pA, boolean internal) {
    mDrive.setInverted(pA);
    driveInverted = pA;
  }

  @Override
  public void invertAzimuth(boolean pA) {
    mAzimuth.setInverted(pA);
  }

  @Override
  public void setDriveBrakeMode(boolean pMode) {
    if (pMode) mDrive.setNeutralMode(NeutralMode.Brake);
    else mDrive.setNeutralMode(NeutralMode.Coast);
  }

  @Override
  public void update() {
    double error = getAzimuthError();
    SmartDashboard.putNumber("[" + mID + "] Module Error", error);
    double output = mAzimuthController.getOutput(0, error);
    SmartDashboard.putNumber("[" + mID + "] Module Spin Speed", output);
    setAzimuthSpeed(output);
    setDriveSpeed(getTargetSpeed() * 0.75);
  }

  @Override
  public void updateSmartDashboard() {
    SmartDashboard.putNumber("[" + mID + "] Module Encoder", getRawEncoder());
    SmartDashboard.putNumber("[" + mID + "] Module Angle", getAngle());
    SmartDashboard.putNumber("[" + mID + "] Module Target Angle", getTargetAngle());
    SmartDashboard.putNumber("[" + mID + "] Module Target Speed", getTargetSpeed());

    double target = getTargetAngle();
    double actual = getAngle();
    if (Math.abs(target - actual) <= ALIGNMENT_TOLERANCE) {
      mLastGoodAlignment = System.currentTimeMillis();
      SmartDashboard.putBoolean("[" + mID + "] Module Alignment Warning", true);
    } else {
      if (mLastGoodAlignment + ALIGNMENT_TIMEOUT < System.currentTimeMillis()) {
        SmartDashboard.putBoolean("[" + mID + "] Module Alignment Warning", false);
      }
    }
  }

  @Override
  public void updateAzimuthPID(double pP, double pI, double pD) {
    mAzimuthController.setP(pP);
    mAzimuthController.setI(pI);
    mAzimuthController.setD(pD);
    System.out.println(pD);
  }

  @Override
  public double getDriveSpeed() {
    return mTargetSpeed;
  }

  @Override
  public Vector2 getSpeedVector() {
    double speed = (getDriveSpeed() * (driveInverted ^ mDrive.getInverted() ? -1 : 1));
    double x = speed * Math.sin((getAngle() * Math.PI) / 180);
    double y = speed * Math.cos((getAngle() * Math.PI) / 180);
    return new Vector2(x, y);
  }

}
