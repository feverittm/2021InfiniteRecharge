package frc.spartanlib.swerve.module;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import frc.spartanlib.controllers.MiniPID;
import frc.spartanlib.math.Vector2;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// TODO: Update the proto module to match all the newly added additions from MerlinModule
public class ProtoModule extends SwerveModule<MiniPID, VictorSPX, VictorSPX> {

  private final int ALIGNMENT_TIMEOUT = 1250; // Milliseconds until I start complaining
  private final double ALIGNMENT_TOLERANCE = 2.5; // Tolerance in degrees
  private double lastGoodAlignment;

  public ProtoModule(int pID, int pAzimuthID, int pDriveID, int pEncoderID, double pEncoderZero,
      double pP, double pI, double pD) {

    super(pID, pEncoderID, pEncoderZero);

    mAzimuth = new VictorSPX(pAzimuthID);
    mAzimuth.setInverted(true);
    //azimuth.setNeutralMode(NeutralMode.Brake);
    mDrive = new VictorSPX(pDriveID);
    mAzimuthEncoder = new AnalogInput(pEncoderID);
    this.mEncoderZero = pEncoderZero;

    mAzimuthController = new MiniPID(pP, pI, pD);
    mAzimuthController.setOutputLimits(-1, 1);

    // setDefaultCommand(defaultCommand); // Not entirely sure if this would work but hek try it
  }

  @Override
  public void setAzimuthSpeed(double speed) {
    mAzimuth.set(ControlMode.PercentOutput, speed);
  }

  @Override
  public void setDriveSpeed(double speed) {
    mDrive.set(ControlMode.PercentOutput, speed);
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

  @Override
  public void updateSmartDashboard() {
    SmartDashboard.putNumber("[" + mID + "] Module Encoder", getRawEncoder());
    SmartDashboard.putNumber("[" + mID + "] Module Angle", getAngle());
    SmartDashboard.putNumber("[" + mID + "] Module Target Angle", getTargetAngle());
    SmartDashboard.putNumber("[" + mID + "] Module Target Speed", getTargetSpeed());

    double target = getTargetAngle();
    double actual = getAngle();
    if (Math.abs(target - actual) <= ALIGNMENT_TOLERANCE) {
      lastGoodAlignment = System.currentTimeMillis();
      SmartDashboard.putBoolean("[" + mID + "] Module Alignment Warning", true);
    } else {
      if (lastGoodAlignment + ALIGNMENT_TIMEOUT < System.currentTimeMillis()) {
        SmartDashboard.putBoolean("[" + mID + "] Module Alignment Warning", false);
      }
    }
  }

  // Leaving this to be changed just because I want the option for change in the update functions for testing
  @Override
  public void update() {
    double error = getAzimuthError();
    double output = mAzimuthController.getOutput(0, error);
    setAzimuthSpeed(output);
    setDriveSpeed(getTargetSpeed());
  }

  @Override
  public void invertDrive(boolean pA) {
    mDrive.setInverted(pA);
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
  public void invertDrive(boolean pA, boolean internal) {
    mDrive.setInverted(pA);
    driveInverted = pA;
  }

  @Override
  public void updateAzimuthPID(double p, double i, double d) {
    mAzimuthController.setP(p);
    mAzimuthController.setI(i);
    mAzimuthController.setD(d);
  }

  public void resetAzimuthController() {
    mAzimuthController.reset();
  }

}
