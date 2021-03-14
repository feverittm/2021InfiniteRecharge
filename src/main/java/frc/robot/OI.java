package frc.robot;

import frc.robot.commands.shooter.*;
import frc.robot.subsystems.Intake;
import frc.robot.commands.hopper.*;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.intake.IntakeMove;

public class OI {
  private double axisPos;
  public XboxController gamepad1;
  private JoystickButton buttonA;
  //private JoystickButton buttonB;
  private JoystickButton buttonX;
  private JoystickButton buttonY;
  private JoystickButton buttonStart;
  private JoystickButton buttonRightBumper; 
  private JoystickButton buttonLeftBumper; 


  private OI() {
    gamepad1 = new XboxController(0);

    buttonA = new JoystickButton(gamepad1, XboxController.Button.kA.value);
    //buttonB = new JoystickButton(gamepad1, XboxController.Button.kB.value);
    buttonX = new JoystickButton(gamepad1, XboxController.Button.kX.value);
    buttonY = new JoystickButton(gamepad1, XboxController.Button.kY.value);
    buttonRightBumper = new JoystickButton(gamepad1, XboxController.Button.kBumperRight.value);
    buttonLeftBumper = new JoystickButton(gamepad1, XboxController.Button.kBumperLeft.value);
    buttonStart = new JoystickButton(gamepad1, XboxController.Button.kStart.value);

    buttonRightBumper.whileHeld(new IntakeMove(Constants.Values.INTAKE_IN, true)/*new ShooterStream(Constants.Values.SHOOTER_RPM)*/);
    buttonLeftBumper.whileHeld(new IntakeMove(Constants.Values.INTAKE_EJECT, false));//7.5 /*new ShooterStreamAutoTarget(Constants.Values.SHOOTER_RPM)*/
    buttonStart.whenPressed(() -> Intake.getInstance().togglePiston());

    buttonX.whileHeld(new HopperMove(Constants.Values.HOPPER_EJECT_SPEED));
    buttonY.whileHeld(new HopperMove(Constants.Values.HOPPER_INTAKE_SPEED));
    buttonA.whileHeld(new ShooterStream(Constants.Values.SHOOTER_RPM));

    /*
    buttonY.whileHeld(new AutoFaceTargetAndDrive());
    buttonRightBumper.whileHeld(new ShooterBasic(0.55));

    buttonY2.whileHeld(new ShooterStream(Constants.Values.SHOOTER_RPM));
    buttonRightBumper2.whileHeld(new IntakeMove(Constants.Values.INTAKE_IN, true));
    buttonLeftBumper2.whileHeld(new IntakeMove(Constants.Values.INTAKE_EJECT, false));
    */
    
    
  }

  public double getGamepad1Axis(int portNum) {
    axisPos = gamepad1.getRawAxis(portNum);
    if (Math.abs(axisPos) < 0.05) {
      axisPos = 0;
    }
    return axisPos;
  }

  /* Not using Gamepad 2 right now.
   *
  public double getGamepad2Axis(int portNum) {
    axisPos = gamepad2.getRawAxis(portNum);
    if (Math.abs(axisPos) < 0.05) {
      axisPos = 0;
    }
    return axisPos;
  }
  */

  private static OI instance;
  public static OI getInstance() { if (instance == null) instance = new OI(); return instance; }

}
