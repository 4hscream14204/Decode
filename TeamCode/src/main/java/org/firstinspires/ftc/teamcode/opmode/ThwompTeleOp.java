package org.firstinspires.ftc.teamcode.opmode;

import static com.arcrobotics.ftclib.kotlin.extensions.gamepad.GamepadExExtKt.whileActiveContinuous;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.pedropathing.follower.Follower;
import com.pedropathing.ftc.PoseConverter;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.PedroCoordinates;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.commandgroups.general.ChangeHeadingLockCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.InitSorterLightsCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.Launch3ArtifactsNoSortingCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.LaunchOneGreen;
import org.firstinspires.ftc.teamcode.commandgroups.general.LaunchOnePurple;
import org.firstinspires.ftc.teamcode.commandgroups.general.LaunchPatternCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.LaunchTwoPurple;
import org.firstinspires.ftc.teamcode.commandgroups.general.SetAllLaunchVelocityCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.SetAllVelocityCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.ToggleAlliance;
import org.firstinspires.ftc.teamcode.commandgroups.general.Transfer3BallsNoCameraCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferResetCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.UpdateLightsCommandGroup;
import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;
import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.CameraLight;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;
import org.firstinspires.ftc.teamcode.subsystems.RGBLightSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SorterCamera;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;

@TeleOp(name = "Thwomp TeleOp")
public class ThwompTeleOp extends OpMode {
    RobotBase robotBase;
    GamepadEx backupController;
    GamepadEx mainController;
    boolean isFieldCentric = true;
    ElapsedTime timer;
    Follower follower;
    double velocity;
    Supplier<PathChain> pathChain;
    Pose gatePose = new Pose(126, 73, Math.toRadians(0));
    boolean automatedDrive;
    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        robotBase = new RobotBase(hardwareMap);
        follower = Constants.createFollower(hardwareMap);
        robotBase.sorterCameraSubsystem.getAnalysis();
        robotBase.chassisSubsystem.frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robotBase.chassisSubsystem.frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robotBase.chassisSubsystem.backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robotBase.chassisSubsystem.backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        if(DataStorage.alliance == DecodeEnums.Alliance.BLUE){
            robotBase.limelightSubsystem.initLimelight(Limelight.limelightPipelines.BLUEGOAL);
            pathChain = ()-> follower.pathBuilder() //Lazy Curve Generation
                    .addPath(new Path(new BezierLine(follower::getPose, gatePose.mirror())))
                    .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, gatePose.getHeading(), 1))
                    .build();
        }
        else{
            robotBase.limelightSubsystem.initLimelight(Limelight.limelightPipelines.REDGOAL);
            pathChain = ()-> follower.pathBuilder() //Lazy Curve Generation
                    .addPath(new Path(new BezierLine(follower::getPose, gatePose)))
                    .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, gatePose.getHeading(), 1))
                    .build();
        }

        timer = new ElapsedTime();

        robotBase.chassisSubsystem.pinpoint.setPosition(PoseConverter.poseToPose2D(new Pose(88, 8, Math.toRadians(90)) /*DataStorage.endPosition*/, PedroCoordinates.INSTANCE));

        //new InitSorterLightsCommandGroup(robotBase);

        mainController = new GamepadEx(gamepad1);
        backupController = new GamepadEx(gamepad2);

        //Main Driver keybinds

        mainController.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                        .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->follower.followPath(pathChain.get())), new InstantCommand(()->automatedDrive = true)));

        mainController.getGamepadButton(GamepadKeys.Button.START)
                .whenPressed(()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->isFieldCentric = !isFieldCentric)
                ));

        mainController.getGamepadButton(GamepadKeys.Button.BACK)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.chassisSubsystem.resetIMU())));

       /* mainController.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(
                        new ChangeHeadingLockCommandGroup(robotBase)
                ));

        mainController.getGamepadButton(GamepadKeys.Button.Y)
                        .whenPressed(()->CommandScheduler.getInstance().schedule(
                                new SetAllLaunchVelocityCommandGroup(robotBase, robotBase.launcherSubsystemLeft.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(0)))
                        ));

        mainController.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(
                        new Transfer3BallsNoCameraCommandGroup(robotBase)
                ));*/

        mainController.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(
                        new ChangeHeadingLockCommandGroup(robotBase)
                ));

        mainController.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new Launch3ArtifactsNoSortingCommandGroup(robotBase)));

        mainController.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new LaunchOnePurple(robotBase)));

        mainController.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new LaunchOneGreen(robotBase)));
        mainController.getGamepadButton(GamepadKeys.Button.Y)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new LaunchTwoPurple(robotBase)));

        mainController.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new LaunchPatternCommandGroup(robotBase)));

        /*mainController.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new SetAllVelocityCommandGroup(robotBase, 0)));

        mainController.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new SetAllVelocityCommandGroup(robotBase, (velocity = velocity + 10))));

        mainController.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new SetAllVelocityCommandGroup(robotBase, (velocity = velocity - 10))));

        mainController.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new SetAllVelocityCommandGroup(robotBase, (velocity = velocity + 100))));

        mainController.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new SetAllVelocityCommandGroup(robotBase, (velocity = velocity - 100))));*/

        new Trigger(()-> mainController.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1)
                .or(new Trigger(()-> mainController.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1))
                .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakeSubsystem.intake(mainController.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) - mainController.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)))))
                .whenInactive (()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakeSubsystem.intake(0))));

        //Backup Driver Keybinds

        backupController.getGamepadButton(GamepadKeys.Button.START)
                        .whenPressed(()->CommandScheduler.getInstance().schedule(new ToggleAlliance(robotBase)));

        backupController.getGamepadButton(GamepadKeys.Button.X)
                .toggleWhenPressed(new InstantCommand(()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)), new InstantCommand(()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.HOME)));

        backupController.getGamepadButton(GamepadKeys.Button.A)
                .toggleWhenPressed(new InstantCommand(()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)), new InstantCommand(()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.HOME)));

        backupController.getGamepadButton(GamepadKeys.Button.B)
                .toggleWhenPressed(new InstantCommand(()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)), new InstantCommand(()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.HOME)));

        backupController.getGamepadButton(GamepadKeys.Button.Y)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new TransferResetCommandGroup(robotBase)));

        backupController.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(()->CommandScheduler.getInstance().schedule(
                        new SetAllVelocityCommandGroup(robotBase, 750)
                ));

        backupController.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new SetAllVelocityCommandGroup(robotBase, 1950)));


        backupController.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new SetAllVelocityCommandGroup(robotBase, 2200)));

        backupController.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(
                        new SetAllVelocityCommandGroup(robotBase, 1500)
                ));

        //Custom Triggers for Sorter and such

        /*new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchLeft() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightLeftSubsystem.setColor(RGBLightSubsystem.Colors.PURPLE))));

        new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchLeft() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightLeftSubsystem.setColor(RGBLightSubsystem.Colors.GREEN))));

        /*new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightMiddleSubsystem.setColor(RGBLightSubsystem.Colors.PURPLE))));

        new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightMiddleSubsystem.setColor(RGBLightSubsystem.Colors.GREEN))));

        /*new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchRight() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
               .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightRightSubsystem.setColor(RGBLightSubsystem.Colors.PURPLE))));

        new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchRight() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightRightSubsystem.setColor(RGBLightSubsystem.Colors.GREEN))));

        /*new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchLeft() != PredominantColorProcessor.Swatch.ARTIFACT_PURPLE && robotBase.sorterCameraSubsystem.getClosestSwatchLeft() != PredominantColorProcessor.Swatch.ARTIFACT_GREEN && robotBase.ejectorLeftSubsystem.getPosition() != SorterServo.ServoPosition.LAUNCH)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightLeftSubsystem.setColor(RGBLightSubsystem.Colors.BLUE))));

        new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() != PredominantColorProcessor.Swatch.ARTIFACT_PURPLE && robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() != PredominantColorProcessor.Swatch.ARTIFACT_GREEN && robotBase.ejectorMiddleSubsystem.getPosition() != SorterServo.ServoPosition.LAUNCH)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightMiddleSubsystem.setColor(RGBLightSubsystem.Colors.BLUE))));

        new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchRight() != PredominantColorProcessor.Swatch.ARTIFACT_PURPLE && robotBase.sorterCameraSubsystem.getClosestSwatchRight() != PredominantColorProcessor.Swatch.ARTIFACT_GREEN && robotBase.ejectorRightSubsystem.getPosition() != SorterServo.ServoPosition.LAUNCH)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightRightSubsystem.setColor(RGBLightSubsystem.Colors.BLUE))));*/

        new Trigger(()-> timer.seconds() > 129)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()-> mainController.gamepad.rumble(500))));

        new Trigger(()->automatedDrive && mainController.wasJustPressed(GamepadKeys.Button.DPAD_UP) || !follower.isBusy())
                .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.chassisSubsystem.drive(mainController.getLeftY(), mainController.getLeftX(), mainController.getRightX(), robotBase.chassisSubsystem.bolSnapToTarget, isFieldCentric, robotBase.limelightSubsystem.getTargetX())), new InstantCommand(()->automatedDrive = false)));
    }

    @Override
    public void init_loop(){
        telemetry.addData("Is Running", robotBase.limelightSubsystem.limelight.isRunning());
        telemetry.addData("Is connected: ", robotBase.limelightSubsystem.limelight.isConnected());
        telemetry.addData("Hue L", robotBase.sorterCameraSubsystem.getHue(SorterCamera.ArtifactSlot.LEFT));
        telemetry.addData("Hue M", robotBase.sorterCameraSubsystem.getHue(SorterCamera.ArtifactSlot.MIDDLE));
        telemetry.addData("Hue R", robotBase.sorterCameraSubsystem.getHue(SorterCamera.ArtifactSlot.RIGHT));

    }

    @Override
    public void start(){
        follower.setStartingPose(DataStorage.endPosition);
        //new InitSorterLightsCommandGroup(robotBase);
        timer.reset();
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        follower.update();
        mainController.readButtons();
        backupController.readButtons();
        robotBase.sorterCameraSubsystem.getAnalysis();
        robotBase.chassisSubsystem.pinpoint.update();
        robotBase.limelightSubsystem.updateLimelight();
        //robotBase.RGBLightRightSubsystem.setColor(RGBLightSubsystem.Colors.PURPLE);
        //robotBase.RGBLightMiddleSubsystem.setColor(RGBLightSubsystem.Colors.PURPLE);
        robotBase.RGBLightLeftSubsystem.setColor(RGBLightSubsystem.Colors.NO);
        //new UpdateLightsCommandGroup(robotBase);
        robotBase.chassisSubsystem.drive(mainController.getLeftY(), mainController.getLeftX(), mainController.getRightX(), robotBase.chassisSubsystem.bolSnapToTarget, isFieldCentric, robotBase.limelightSubsystem.getTargetX());
        //telemetry.addData("This is new code 7", true);
        telemetry.addData("Alliance", DataStorage.alliance);
        telemetry.addData("Heading", robotBase.chassisSubsystem.pinpoint.getHeading(AngleUnit.DEGREES));
        telemetry.addData("Is Field Centric", robotBase.chassisSubsystem.isFieldCentric);
        //telemetry.addData("Intake Power", robotBase.intakeSubsystem.intakeMotor.getPower());
        telemetry.addData("Left Closest Swatch", robotBase.sorterCameraSubsystem.getClosestSwatchLeft());
        telemetry.addData("Middle Closest Swatch", robotBase.sorterCameraSubsystem.getClosestSwatchMiddle());
        telemetry.addData("Right Closest Swatch", robotBase.sorterCameraSubsystem.getClosestSwatchRight());
        //telemetry.addData("Distance", robotBase.limelightSubsystem.getHorizontalDistance(-18.5));
        //telemetry.addData("Odometry Distance", robotBase.limelightSubsystem.getHorizontalDistance(follower));
        telemetry.addData("Heading Lock", robotBase.chassisSubsystem.bolSnapToTarget);
        //telemetry.addData("Left", robotBase.launcherSubsystemLeft.getVelocity());
        //telemetry.addData("Middle", robotBase.launcherSubsystemMiddle.getVelocity());
        //telemetry.addData("Right", robotBase.launcherSubsystemRight.getVelocity());
        //telemetry.addData("Target Velocity", robotBase.launcherSubsystemLeft.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(0)));
        //telemetry.addData("Is Running", robotBase.limelightSubsystem.limelight.isRunning());
        //telemetry.addData("Is connected: ", robotBase.limelightSubsystem.limelight.isConnected());
        telemetry.addData("Hue L", robotBase.sorterCameraSubsystem.getHue(SorterCamera.ArtifactSlot.LEFT));
        telemetry.addData("Hue M", robotBase.sorterCameraSubsystem.getHue(SorterCamera.ArtifactSlot.MIDDLE));
        telemetry.addData("Hue R", robotBase.sorterCameraSubsystem.getHue(SorterCamera.ArtifactSlot.RIGHT));
        telemetry.addData("Sat L", robotBase.sorterCameraSubsystem.getSaturation(SorterCamera.ArtifactSlot.LEFT));
        telemetry.addData("Sat M", robotBase.sorterCameraSubsystem.getSaturation(SorterCamera.ArtifactSlot.MIDDLE));
        telemetry.addData("Sat R", robotBase.sorterCameraSubsystem.getSaturation(SorterCamera.ArtifactSlot.RIGHT));
        telemetry.addData("Time", timer.seconds());
        telemetry.addData("Limelight Z", robotBase.limelightSubsystem.getTargetZ());
        //telemetry.addData("Follower Pose", follower.getPose());
        telemetry.addData("Automated drive", automatedDrive);
        telemetry.addData("Launch Velocity", robotBase.launcherSubsystemLeft.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(0)));
        telemetry.addData("Real Velocity", robotBase.launcherSubsystemLeft.getVelocity());
        telemetry.update();
    }

    @Override
    public void stop(){
        robotBase.limelightSubsystem.limelight.stop();
    }
}
