/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {

  private Solenoid mIntakePiston;
  private CANSparkMax mMotor1;
  private CANEncoder encoder;

  private Intake() {
    mMotor1 = new CANSparkMax(Constants.Ports.INTAKE_MOTOR, MotorType.kBrushless);
    mIntakePiston = new Solenoid(Constants.Ports.INTAKE_SOLENOID);

    mMotor1.restoreFactoryDefaults();

    encoder = mMotor1.getEncoder();

    mIntakePiston.set(false); // Default action is to pull intake up into the robot

    mMotor1.setSmartCurrentLimit(50);
    mMotor1.setIdleMode(IdleMode.kCoast);

    register();
  }

  public void setPercent(double percent) {
    mMotor1.set(percent);
  }

  public double getRPM() {
    return encoder.getVelocity();
  }

  public void togglePiston() {
    mIntakePiston.set(!mIntakePiston.get());
  }

  public void setPiston(boolean extend) {
    mIntakePiston.set(extend);
  }

  public boolean getPiston() {
    return mIntakePiston.get();
  }

  @Override
  public void periodic() {
    updateSmartDashboard();
  }

  public void updateSmartDashboard() {
    SmartDashboard.putNumber("Intake/RPMs", getRPM());
    SmartDashboard.putBoolean("Intake/Intake Position", getPiston());
  }

  private static Intake instance;

  public static Intake getInstance() {
    if (instance == null)
      instance = new Intake();
    return instance;
  }
}
