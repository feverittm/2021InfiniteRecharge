/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
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
  private WPI_VictorSPX mMotor1;

  private Hopper() {
    mMotor1 = new WPI_VictorSPX(Constants.Ports.HOPPER_MOTOR);
    mIntakeIR = new DigitalInput(Constants.Ports.INTAKE_IR);
    mShooterIR = new DigitalInput(Constants.Ports.SHOOTER_IR);
    mOverflowIR = new DigitalInput(Constants.Ports.OVERFLOW_IR);
    mMotor1.configFactoryDefault(10);

    mMotor1.setNeutralMode(NeutralMode.Brake);
    
    mMotor1.setInverted(true);

    SmartDashboard.putNumber("Driver/Set Ball Count", mBallCount); // Driver tab is stuff for drive team specifically to
                                                                   // edit.
    register();
  }

  public void setSpeed(double speed) {
    mMotor1.set(ControlMode.PercentOutput, speed);
  }

  /**
   * Allows drivers to update the amount of balls in the hopper before the match
   * starts.
   */
  public void updateBallCount() {
    mBallCount = (int) NetworkTableInstance.getDefault().getTable("SmartDashboard").getEntry("Driver/Set Ball Count")
        .getDouble(3);
  }

  public boolean getShooterBall() {
    return !mShooterIR.get();
  }

  public boolean getIntakeBall() {
    return !mIntakeIR.get();
  }

  public boolean getOverflowBall() {
    return !mOverflowIR.get();
  }

  public void updateSmartDashboard(){
    
    SmartDashboard.putNumber("Hopper/Ball Count", mBallCount);
    SmartDashboard.putBoolean("Hopper/Hopper Move", Math.abs(mMotor1.getMotorOutputPercent()) > 0.0);

    if (Robot.verbose) {
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
