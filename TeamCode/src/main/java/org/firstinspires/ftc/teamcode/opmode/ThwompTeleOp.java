package org.firstinspires.ftc.teamcode.opmode;


import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.pedropathing.follower.Follower;
import com.pedropathing.ftc.PoseConverter;
import com.pedropathing.geometry.PedroCoordinates;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.skeletonarmy.marrow.zones.CircleZone;
import com.skeletonarmy.marrow.zones.Point;
import com.skeletonarmy.marrow.zones.PolygonZone;
import com.skeletonarmy.marrow.zones.Zone;

import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.commandgroups.general.ChangeHeadingLockCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.DynamicVelocityCommand;
import org.firstinspires.ftc.teamcode.commandgroups.general.Launch3ArtifactsDynamicCG;
import org.firstinspires.ftc.teamcode.commandgroups.general.Launch3ArtifactsNoSortingCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.LaunchOneGreen;
import org.firstinspires.ftc.teamcode.commandgroups.general.LaunchOnePurple;
import org.firstinspires.ftc.teamcode.commandgroups.general.LaunchPatternCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.LaunchTwoPurple;
import org.firstinspires.ftc.teamcode.commandgroups.general.SetAllVelocityCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.ToggleAlliance;
import org.firstinspires.ftc.teamcode.commandgroups.general.ToggleIntakeBlockerCG;
import org.firstinspires.ftc.teamcode.commandgroups.general.ToggleTiltCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.Transfer3BallsNoCameraCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferResetCommandGroup;
import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;
import org.firstinspires.ftc.teamcode.robotbase.Color;
import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.robotbase.PrismAnimations;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.CameraLight;
import org.firstinspires.ftc.teamcode.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.subsystems.Hood;
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
    double velocity = 1500;
    Supplier<PathChain> pathChain;
    Pose gatePose = new Pose(126, 73, Math.toRadians(0));
    Pose redGoalPose = new Pose(132, 137);
    Pose blueGoalPose = new Pose(132, 137).mirror();
    boolean automatedDrive;
    Servo prism;

    PolygonZone robotZone;
    PolygonZone closeLaunchZone;
    PolygonZone farLaunchZone;
    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        robotBase = new RobotBase(hardwareMap);
        follower = Constants.createFollower(hardwareMap);
        prism = hardwareMap.get(Servo.class, "prism");
        robotZone = new PolygonZone(18, 18);
        closeLaunchZone = new PolygonZone(new Point(144, 144), new Point(72, 55), new Point(0, 144));
        farLaunchZone = new PolygonZone(new Point(36, 0), new Point(72, 48), new Point(110, 0));
        CommandScheduler.getInstance().schedule(new InstantCommand(()->prism.setPosition(0.225)));
        robotBase.sorterCameraSubsystem.getAnalysis();
        robotBase.chassisSubsystem.frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robotBase.chassisSubsystem.frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robotBase.chassisSubsystem.backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robotBase.chassisSubsystem.backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        if(DataStorage.alliance == DecodeEnums.Alliance.BLUE){
            robotBase.limelightSubsystem.initLimelight(Limelight.limelightPipelines.BLUEGOAL);
            /*pathChain = ()-> follower.pathBuilder() //Lazy Curve Generation
                    .addPath(new Path(new BezierLine(follower::getPose, gatePose.mirror())))
                    .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, gatePose.getHeading(), 1))
                    .build();*/
            robotBase.chassisSubsystem.pinpoint.setPosition(PoseConverter.poseToPose2D(new Pose(DataStorage.endPosition.getX(), DataStorage.endPosition.getY(), (DataStorage.endPosition.getHeading())), PedroCoordinates.INSTANCE));
        }
        else{
            robotBase.limelightSubsystem.initLimelight(Limelight.limelightPipelines.REDGOAL);
            /*pathChain = ()-> follower.pathBuilder() //Lazy Curve Generation
                    .addPath(new Path(new BezierLine(follower::getPose, gatePose)))
                    .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, gatePose.getHeading(), 1))
                    .build();*/
            robotBase.chassisSubsystem.pinpoint.setPosition(PoseConverter.poseToPose2D(new Pose(DataStorage.endPosition.getX(), DataStorage.endPosition.getY(), (DataStorage.endPosition.getHeading())), PedroCoordinates.INSTANCE));
        }

        timer = new ElapsedTime();



        //new InitSorterLightsCommandGroup(robotBase);

        mainController = new GamepadEx(gamepad1);
        backupController = new GamepadEx(gamepad2);

        //Main Driver keybinds

        /*mainController.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                        .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->follower.followPath(pathChain.get())), new InstantCommand(()->automatedDrive = true)));*/

        mainController.getGamepadButton(GamepadKeys.Button.OPTIONS)
                .whenPressed(()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->isFieldCentric = !isFieldCentric)
                ));

        mainController.getGamepadButton(GamepadKeys.Button.SHARE)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.chassisSubsystem.resetIMU())));

        mainController.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(
                        new ChangeHeadingLockCommandGroup(robotBase)
                ));

        mainController.getGamepadButton(GamepadKeys.Button.CROSS)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new Launch3ArtifactsDynamicCG(robotBase, follower)/*new Launch3ArtifactsNoSortingCommandGroup(robotBase)*/));

        mainController.getGamepadButton(GamepadKeys.Button.CIRCLE)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new LaunchOnePurple(robotBase)));

        mainController.getGamepadButton(GamepadKeys.Button.SQUARE)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new LaunchOneGreen(robotBase)));
        mainController.getGamepadButton(GamepadKeys.Button.TRIANGLE)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new LaunchTwoPurple(robotBase)));

        mainController.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new LaunchPatternCommandGroup(robotBase)));

        new Trigger(()-> mainController.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1)
                .or(new Trigger(()-> mainController.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1))
                .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakeSubsystem.intake(mainController.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) - mainController.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)))))
                .whenInactive (()->CommandScheduler.getInstance().schedule(
                        new InstantCommand(()->robotBase.intakeSubsystem.intake(0))));

        mainController.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                        .whenPressed(()->CommandScheduler.getInstance().schedule(new SetAllVelocityCommandGroup(robotBase, (velocity += 20))));

        mainController.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new SetAllVelocityCommandGroup(robotBase, (velocity -= 20))));

        mainController.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                        .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.hoodSubsystem.setPosition(Hood.HoodPosition.CLOSE))));

        mainController.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.hoodSubsystem.setPosition(Hood.HoodPosition.FAR))));

        mainController.getGamepadButton(GamepadKeys.Button.PS)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new ToggleTiltCommandGroup(robotBase)));

        //Backup Driver Keybinds

        backupController.getGamepadButton(GamepadKeys.Button.OPTIONS)
                        .whenPressed(()->CommandScheduler.getInstance().schedule(new ToggleAlliance(robotBase)));

        backupController.getGamepadButton(GamepadKeys.Button.SQUARE)
                .toggleWhenPressed(new InstantCommand(()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)), new InstantCommand(()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.HOME)));

        backupController.getGamepadButton(GamepadKeys.Button.CROSS)
                .toggleWhenPressed(new InstantCommand(()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)), new InstantCommand(()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.HOME)));

        backupController.getGamepadButton(GamepadKeys.Button.CIRCLE)
                .toggleWhenPressed(new InstantCommand(()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)), new InstantCommand(()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.HOME)));

        backupController.getGamepadButton(GamepadKeys.Button.TRIANGLE)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new TransferResetCommandGroup(robotBase)));

        /*backupController.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(()->CommandScheduler.getInstance().schedule(
                        new SetAllVelocityCommandGroup(robotBase, 750)
                ));*/

        backupController.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new SetAllVelocityCommandGroup(robotBase, 1950)));


        backupController.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new SetAllVelocityCommandGroup(robotBase, 2200)));

        backupController.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .toggleWhenPressed(new InstantCommand(()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)),  new InstantCommand(()->robotBase.ejectorLeftSubsystem.setPosition(SorterServo.ServoPosition.HOME)))
                .toggleWhenPressed(new InstantCommand(()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)), new InstantCommand(()->robotBase.ejectorMiddleSubsystem.setPosition(SorterServo.ServoPosition.HOME)))
                .toggleWhenPressed(new InstantCommand(()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.LAUNCH)), new InstantCommand(()->robotBase.ejectorRightSubsystem.setPosition(SorterServo.ServoPosition.HOME)));

        //Custom Triggers for Sorter and such

        new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchLeft() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightLeftSubsystem.setColor(RGBLightSubsystem.Colors.PURPLE))));

        new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchLeft() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightLeftSubsystem.setColor(RGBLightSubsystem.Colors.GREEN))));

        new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightMiddleSubsystem.setColor(RGBLightSubsystem.Colors.PURPLE))));

        new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightMiddleSubsystem.setColor(RGBLightSubsystem.Colors.GREEN))));

        new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchRight() == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
               .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightRightSubsystem.setColor(RGBLightSubsystem.Colors.PURPLE))));

        new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchRight() == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightRightSubsystem.setColor(RGBLightSubsystem.Colors.GREEN))));

        new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchLeft() != PredominantColorProcessor.Swatch.ARTIFACT_PURPLE && robotBase.sorterCameraSubsystem.getClosestSwatchLeft() != PredominantColorProcessor.Swatch.ARTIFACT_GREEN && robotBase.ejectorLeftSubsystem.getPosition() != SorterServo.ServoPosition.LAUNCH)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightLeftSubsystem.setColor(RGBLightSubsystem.Colors.BLUE))));

        new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() != PredominantColorProcessor.Swatch.ARTIFACT_PURPLE && robotBase.sorterCameraSubsystem.getClosestSwatchMiddle() != PredominantColorProcessor.Swatch.ARTIFACT_GREEN && robotBase.ejectorMiddleSubsystem.getPosition() != SorterServo.ServoPosition.LAUNCH)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightMiddleSubsystem.setColor(RGBLightSubsystem.Colors.BLUE))));

        new Trigger(()->robotBase.sorterCameraSubsystem.getClosestSwatchRight() != PredominantColorProcessor.Swatch.ARTIFACT_PURPLE && robotBase.sorterCameraSubsystem.getClosestSwatchRight() != PredominantColorProcessor.Swatch.ARTIFACT_GREEN && robotBase.ejectorRightSubsystem.getPosition() != SorterServo.ServoPosition.LAUNCH)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.RGBLightRightSubsystem.setColor(RGBLightSubsystem.Colors.BLUE))));

        new Trigger(()-> timer.seconds() > 129)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()-> mainController.gamepad.rumble(1000)), new InstantCommand(()->backupController.gamepad.rumble(1000))));

        new Trigger(()->robotZone.isInside(closeLaunchZone))
                .whileActiveOnce(new InstantCommand(()->robotBase.hoodSubsystem.setPosition(Hood.HoodPosition.CLOSE)));

        new Trigger(()->robotZone.isInside(farLaunchZone))
                .whileActiveOnce(new InstantCommand(()->robotBase.hoodSubsystem.setPosition(Hood.HoodPosition.FAR)));

        /*new Trigger(()->automatedDrive && mainController.wasJustPressed(GamepadKeys.Button.DPAD_UP) || !follower.isBusy())
                .whileActiveContinuous(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.chassisSubsystem.drive(mainController.getLeftY(), mainController.getLeftX(), mainController.getRightX(), robotBase.chassisSubsystem.bolSnapToTarget, isFieldCentric, robotBase.limelightSubsystem.getTargetX())), new InstantCommand(()->automatedDrive = false)));*/
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
        follower.setStartingPose(new Pose(DataStorage.endPosition.getX(), DataStorage.endPosition.getY()));
        CommandScheduler.getInstance().schedule(new TransferResetCommandGroup(robotBase));
        //robotBase.hoodSubsystem.setPosition(0.75);
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
        robotZone.setPosition(follower.getPose().getX(), follower.getPose().getY());
        robotZone.setRotation(follower.getHeading());
        CommandScheduler.getInstance().schedule(new DynamicVelocityCommand(robotBase, follower));
        //robotBase.RGBLightRightSubsystem.setColor(RGBLightSubsystem.Colors.PURPLE);
        //robotBase.RGBLightMiddleSubsystem.setColor(RGBLightSubsystem.Colors.PURPLE);
        //robotBase.RGBLightLeftSubsystem.setColor(RGBLightSubsystem.Colors.NO);
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
        telemetry.addData("Limelight Heading Lock", robotBase.chassisSubsystem.bolSnapToTarget);
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
        telemetry.addData("Limelight X", robotBase.limelightSubsystem.getTargetX());
        telemetry.addData("Pedro Distance", robotBase.limelightSubsystem.getHorizontalDistance(follower, redGoalPose));
        telemetry.addData("Velocity", robotBase.launcherSubsystemLeft.getVelocity());
        telemetry.addData("Target Velocity Limelight Blue", robotBase.launcherSubsystemLeft.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(follower, redGoalPose)));
        telemetry.addData("Target Velocity follower Blue", robotBase.launcherSubsystemLeft.getLaunchVelocity(follower.getPose().distanceFrom(redGoalPose)* 2.54));
        telemetry.addData("True left target vel", robotBase.launcherSubsystemLeft.dblTargetVel);
        telemetry.addData("IsAtSpeed", robotBase.launcherSubsystemLeft.isAtSpeed(robotBase.launcherSubsystemLeft.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(follower, redGoalPose))));
        //telemetry.addData("Follower Pose", follower.getPose());
        /*telemetry.addData("Automated drive", automatedDrive);
        telemetry.addData("Launch Velocity", robotBase.launcherSubsystemLeft.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(0)));
        telemetry.addData("Real Velocity", robotBase.launcherSubsystemLeft.getVelocity());
        telemetry.addData("Odometry Distance", robotBase.limelightSubsystem.getHorizontalDistance(follower, redGoalPose));
        telemetry.addData("Velocity", velocity);*/
        telemetry.update();
    }

    @Override
    public void stop(){
        robotBase.limelightSubsystem.limelight.stop();
    }
}
