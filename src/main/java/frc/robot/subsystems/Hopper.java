/*----------------------------------------------------------------------------*/
/* Copyright (c) 2021 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class Hopper implements Subsystem {

  public boolean autoIndexMoving = false;
  public int mBallCount = 3;

  private DigitalInput mIntakeIR, mOverflowIR, mShooterIR;
  private WPI_VictorSPX mHopper;

  private Hopper() {
    mHopper = new WPI_VictorSPX(Constants.Ports.HOPPER_MOTOR);
    mIntakeIR = new DigitalInput(Constants.Ports.INTAKE_IR);
    mShooterIR = new DigitalInput(Constants.Ports.SHOOTER_IR);
    mOverflowIR = new DigitalInput(Constants.Ports.OVERFLOW_IR);
    mHopper.configFactoryDefault(10);

    mHopper.setNeutralMode(NeutralMode.Brake);
    
    mHopper.setInverted(true);

    SmartDashboard.putNumber("Driver/Set Ball Count", mBallCount); // Driver tab is stuff for drive team specifically to

    register();
  }

  public void setSpeed(double speed) {
    mHopper.set(ControlMode.PercentOutput, speed);
  }

  /**
   * Allows drivers to update the amount of balls in the hopper before the match
   * starts.
   */
  public void updateBallCount() {
    mBallCount = (int) NetworkTableInstance.getDefault().getTable("SmartDashboard").getEntry("Driver/Set Ball Count")
        .getDouble(3);
  }

  /**
   * Is there a ball in the shooter ready to fire?
   * @return
   */
  public boolean getShooterBall() {
    return !mShooterIR.get();
  }

  /**
   * Check if a ball is in the Intake ready to be pulled in?
   * @return
   */
  public boolean getIntakeBall() {
    return !mIntakeIR.get();
  }

  /**
   * Is there a ball at the bottom of the hopper?
   * This allows us to sequence the balls in the hopper.  This means that we need to set up the sensor so that we only see
   * one ball at a time.
   * @return 
   */
  public boolean getOverflowBall() {
    return !mOverflowIR.get();
  }

  public void updateSmartDashboard() {
    
    SmartDashboard.putNumber("Hopper/Ball Count", mBallCount);
    
    if (Robot.verbose) {
      SmartDashboard.putBoolean("Hopper/Hopper Move", Math.abs(mHopper.getMotorOutputPercent()) > 0.0);
      SmartDashboard.putBoolean("Hopper/Intake IR Sensor", getIntakeBall());
      SmartDashboard.putBoolean("Hopper/Shooter IR Sensor", getShooterBall());
    }
  }

  @Override
  public void periodic() {
    updateSmartDashboard();
  }

  private static Hopper instance;

  public static Hopper getInstance() {
    if (instance == null)
      instance = new Hopper();
    return instance;
  }
}
