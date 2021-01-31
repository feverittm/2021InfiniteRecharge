package frc.spartanlib.helpers;

public final class SwerveMixerData {

    private double[] speeds = null, angles = null;
    private double forward = 0, strafe = 0, rotate = 0;

    public double[] getSpeeds() { return speeds; }
    public double[] getAngles() { return angles; }
    public double getForward() { return forward; }
    public double getStrafe() { return strafe; }
    public double getRotate() { return rotate; }
    public void setSpeeds(double[] speeds) { this.speeds = speeds; }
    public void setAngles(double[] angles) { this.angles = angles; }
    public void setForward(double forward) { this.forward = forward; }
    public void setStrafe(double strafe) { this.strafe = strafe; }
    public void setRotate(double rotate) { this.rotate = rotate; }

}
