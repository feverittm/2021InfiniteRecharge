# 2021InfiniteRecharge
Update of last year's swerve drive robot to use the 2021WPILIB framework.

https://github.com/Team997Coders/2020InfiniteRecharge.git

* Move all of SpartanLib into project (helps the build)
* Remove the Climber Subsystem/Commands
* Remove the LED Manager and all LED control
* Simplify control of the Limelight and move to the main project, I don't like the PID stuff in the Limelight subsystemn.
* Update the Vision controls
* Leave in all of the old Command Based programming stuff (OI and stuff in robot.java) for now.
* Simplify the Hand controllers to only need a single Xbox controller to operate all functions on the robot.

* Updated code to match what is actually on the robot
* * Single motor/controller on the Hopper
* * Single motor/controller on the Intake
* * Add in the mAzimuthController definition in the swerve module contructor

Link to Spartanlib:
https://github.com/feverittm/2021InfiniteRecharge.git


