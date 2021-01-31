package frc.spartanlib.swerve.module;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.spartanlib.controllers.SpartanPID;
import frc.spartanlib.helpers.PIDConstants;
import frc.spartanlib.math.Vector2;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DelrinModule extends SwerveModule<SpartanPID, CANSparkMax, CANSparkMax> {

  private final int ALIGNMENT_TIMEOUT = 1250; // Milliseconds until I start complaining
  private final double ALIGNMENT_TOLERANCE = 2.5; // Tolerance in degrees

  private double mLastUpdate = Double.NaN;
  private double mLastGoodAlignment;

  private CANPIDController mDriveController;
  private CANEncoder mDriveEncoder;

  public DelrinModule(int pID, int pAzimuthID, int pDriveID, int pEncoderID, double pEncoderZero, PIDConstants pAziConsts, PIDConstants pDriConsts) {
    super(pID, pEncoderID, pEncoderZero);

    mAzimuth = new CANSparkMax(pAzimuthID, MotorType.kBrushless);

    mDrive = new CANSparkMax(pDriveID, MotorType.kBrushless);
    mDriveEncoder = mDrive.getEncoder();
    mDriveController = mDrive.getPIDController();

    mDriveEncoder.setPosition(0.0);

    mDriveController.setOutputRange(-1, 1);
    mDriveController.setP(pDriConsts.P);
    mDriveController.setP(pDriConsts.I);
    mDriveController.setP(pDriConsts.D);

    mAzimuthController = new SpartanPID(pAziConsts);
    mAzimuthController.setMinOutput(-1);
    mAzimuthController.setMaxOutput(1);
  }

  @Override
  protected void setAzimuthSpeed(double pSpeed) {
    mAzimuth.set(pSpeed);
  }

  @Override
  public void setTargetAngle(double angle) {
    super.setTargetAngle(angle);
    mAzimuthController.reset();
    mAzimuthController.setSetpoint(mTargetAngle);
  }

  @Override
  protected void setDriveSpeed(double pSpeed) {
    mDriveController.setIAccum(0.0);
    mDriveController.setReference(pSpeed, ControlType.kDutyCycle);
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
  
  public void setDriveBrakeMode(boolean pMode) {
    if (pMode) mDrive.setIdleMode(IdleMode.kBrake);
    else mDrive.setIdleMode(IdleMode.kCoast);
  }

  @Override
  public void update() {
    mAzimuthController.setSetpoint(0.0);

    double deltaT = 0.0;
    double now = System.currentTimeMillis();
    if (Double.isFinite(mLastUpdate)) deltaT = (now - mLastUpdate) * 1000;
    mLastUpdate = now;

    double adjustedTheta = getAngle();
    while (adjustedTheta < mTargetAngle - 180) adjustedTheta += 360;
    while (adjustedTheta >= mTargetAngle + 180) adjustedTheta -= 360;

    double error = mTargetAngle - adjustedTheta;
    SmartDashboard.putNumber("[" + mID + "] Module Error", error);
    
    double output = mAzimuthController.WhatShouldIDo(adjustedTheta, deltaT);
    SmartDashboard.putNumber("[" + mID + "] Module Spin Speed", output);
    setAzimuthSpeed(output);
    setDriveSpeed(getTargetSpeed() * 1);
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
    return mDriveEncoder.getVelocity();
  }

  @Override
  public Vector2 getSpeedVector() {
    double speed = (getDriveSpeed() * (driveInverted ^ mDrive.getInverted() ? -1 : 1));
    double x = speed * Math.sin((getAngle() * Math.PI) / 180);
    double y = speed * Math.cos((getAngle() * Math.PI) / 180);
    return new Vector2(x, y);
  }

}
