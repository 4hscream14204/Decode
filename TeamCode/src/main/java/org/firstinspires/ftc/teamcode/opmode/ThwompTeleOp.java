package org.firstinspires.ftc.teamcode.opmode;


import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.pedropathing.follower.Follower;
import com.pedropathing.ftc.PoseConverter;
import com.pedropathing.geometry.PedroCoordinates;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.skeletonarmy.marrow.zones.Point;
import com.skeletonarmy.marrow.zones.PolygonZone;

import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.commandgroups.general.DynamicVelocityCommand;
import org.firstinspires.ftc.teamcode.commandgroups.general.Launch3ArtifactsDynamicCG;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferResetCommandGroup;
import org.firstinspires.ftc.teamcode.opmode.Controllers.BackupControllerKeys;
import org.firstinspires.ftc.teamcode.opmode.Controllers.ChassisControllerKeys;
import org.firstinspires.ftc.teamcode.opmode.Controllers.LauncherControllerKeys;
import org.firstinspires.ftc.teamcode.opmode.Controllers.MainControllerKeys;
import org.firstinspires.ftc.teamcode.opmode.Triggers.RGBLights;
import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;
import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Hood;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;
import org.firstinspires.ftc.teamcode.subsystems.SorterCamera;

import java.util.List;

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
    Servo prism;
    PathChain holdPoint;
    double dblLockOffset = 0;
    double loopTime = 0;
    double previousLoop = 0;
    double xSpeed;
    double ySpeed;
    boolean readyToLaunch;
    double farZoneHeadingOffset = -1;

    PolygonZone robotZone;
    PolygonZone closeLaunchZone;
    PolygonZone farLaunchZone;
    PolygonZone farLaunchZoneBuffer;

    /*MainControllerKeys mainControllerKeys;
    BackupControllerKeys backupControllerKeys;*/
    MainControllerKeys chassisControllerKeys;
    LauncherControllerKeys launcherControllerKeys;
    RGBLights rgbLights;

    List<LynxModule> allHubs;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        robotBase = new RobotBase(hardwareMap);
        follower = Constants.createFollower(hardwareMap);
        prism = hardwareMap.get(Servo.class, "prism");
        robotZone = new PolygonZone(18, 18);
        closeLaunchZone = new PolygonZone(new Point(144, 130), new Point(72, 65), new Point(0, 130));
        farLaunchZone = new PolygonZone(new Point(45, 0), new Point(72, 36), new Point(100, 0));
        farLaunchZoneBuffer = new PolygonZone(new Point(40, 0), new Point(72, 41), new Point(105, 0));
        CommandScheduler.getInstance().schedule(new InstantCommand(()->prism.setPosition(0.225)));
        robotBase.sorterCameraSubsystem.getAnalysis();
        robotBase.chassisSubsystem.frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robotBase.chassisSubsystem.frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robotBase.chassisSubsystem.backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robotBase.chassisSubsystem.backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rgbLights = new RGBLights();

        allHubs = hardwareMap.getAll(LynxModule.class);

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

        mainController = new GamepadEx(gamepad1);
        backupController = new GamepadEx(gamepad2);
        chassisControllerKeys = new MainControllerKeys();
        launcherControllerKeys = new LauncherControllerKeys();
        chassisControllerKeys.addMainController(mainController, robotBase, follower);
        launcherControllerKeys.addLauncherDriver(backupController, robotBase, follower);
        /*mainControllerKeys = new MainControllerKeys();
        backupControllerKeys = new BackupControllerKeys();
        mainControllerKeys.addMainController(mainController, robotBase, follower);

       /* mainController.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                        .whenPressed(()->CommandScheduler.getInstance().schedule(new SetAllVelocityCommandGroup(robotBase, (velocity += 20))));

        mainController.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new SetAllVelocityCommandGroup(robotBase, (velocity -= 20))));*/

        //backupControllerKeys.addBackupController(backupController, robotBase);

        rgbLights.createRGBTriggers(robotBase);

        new Trigger(()-> timer.seconds() > 110)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()-> mainController.gamepad.rumble(1000)), new InstantCommand(()->backupController.gamepad.rumble(1000)), new InstantCommand(()->prism.setPosition(0.894))));

        new Trigger(()->robotZone.isInside(farLaunchZone))
                .whenActive(new InstantCommand(()->robotBase.hoodSubsystem.setPosition(Hood.HoodPosition.FAR)))
                .whenInactive(new InstantCommand(()->robotBase.hoodSubsystem.setPosition(Hood.HoodPosition.CLOSE)))
                ;

        new Trigger(()->robotZone.isInside(farLaunchZone))
                //.and(new Trigger(()-> robotBase.limelightSubsystem.goalInSight()))
                .whenActive(new InstantCommand(()->dblLockOffset = 0/*Math.toRadians(farZoneHeadingOffset)*/))
                //.whenInactive(new InstantCommand(()->dblLockOffset = 0))
                ;

        new Trigger(()->!robotZone.isInside(farLaunchZoneBuffer))
                .whileActiveContinuous(new InstantCommand(()->dblLockOffset = Math.toRadians(0)));

        new Trigger(()-> robotBase.chassisSubsystem.bolSnapToTarget)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->gamepad1.rumble(1, 1, 250)), new InstantCommand(()->prism.setPosition(0.225))))
                .whenInactive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->prism.setPosition(0.444))));

        /*new Trigger(()->readyToLaunch)
                .whenActive(()->CommandScheduler.getInstance().schedule(new Launch3ArtifactsDynamicCG(robotBase, follower, robotBase.chassisSubsystem.isLockingToGate)));*/

        new Trigger(()->robotBase.sorterCameraSubsystem.hasThreeArtifacts())
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.chassisSubsystem.bolSnapToTarget = true)));

        /*new Trigger(()->Math.abs(mainController.getLeftX()) < 0.1 && Math.abs(mainController.getLeftY()) < 0.1 && Math.abs(mainController.getRightX()) > 0.1)
                .whenActive(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->follower.resumePathFollowing()), new HoldPointCommand(follower, follower.getPose(), true)));

        new Trigger(()->Math.abs(mainController.getLeftX()) > 0.09 || Math.abs(mainController.getLeftY()) > 0.09 || Math.abs(mainController.getRightX()) > 0.09)
                .whenActive(()->CommandScheduler.getInstance().schedule(new FollowPathCommand(follower, holdPoint)));

        new Trigger(()->!follower.isBusy())
                .whenActive(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->follower.pausePathFollowing())));*/

        CommandScheduler.getInstance().run();
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

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        follower.setStartingPose(new Pose((DataStorage.endPosition.getX()), (DataStorage.endPosition.getY()), DataStorage.endPosition.getHeading()));
        CommandScheduler.getInstance().schedule(new TransferResetCommandGroup(robotBase));
        timer.reset();
    }

    @Override
    public void loop() {

        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }

        readyToLaunch = robotBase.sorterCameraSubsystem.hasThreeArtifacts() && Math.abs(robotBase.chassisSubsystem.targetHeading - robotBase.chassisSubsystem.pinpoint.getHeading(AngleUnit.RADIANS)) < 0.05 && (robotZone.isInside(closeLaunchZone) || robotZone.isInside(farLaunchZone)) && robotBase.chassisSubsystem.bolSnapToTarget;

        loopTime = timer.milliseconds() - previousLoop;
        previousLoop = timer.milliseconds();
        CommandScheduler.getInstance().run();
        follower.update();
        mainController.readButtons();
        backupController.readButtons();
        robotBase.sorterCameraSubsystem.getAnalysis();
        robotBase.limelightSubsystem.updateLimelight();
        robotZone.setPosition(follower.getPose().getX(), follower.getPose().getY());
        robotZone.setRotation(follower.getHeading());
        CommandScheduler.getInstance().schedule(new DynamicVelocityCommand(robotBase, follower));
        xSpeed = robotBase.chassisSubsystem.pinpoint.getVelX(DistanceUnit.INCH);
        ySpeed = robotBase.chassisSubsystem.pinpoint.getVelY(DistanceUnit.INCH);
        if(DataStorage.alliance == DecodeEnums.Alliance.RED){
            robotBase.chassisSubsystem.drive(mainController.getLeftY(), mainController.getLeftX(), mainController.getRightX(), isFieldCentric, timer, robotBase.limelightSubsystem.getTargetX() - dblLockOffset, follower);
        }
        else{
            robotBase.chassisSubsystem.drive(-mainController.getLeftY(), -mainController.getLeftX(), mainController.getRightX(), isFieldCentric, timer, robotBase.limelightSubsystem.getTargetX() + dblLockOffset, follower);
        }

        /*holdPoint = follower.pathBuilder()
                .addPath(new BezierLine(follower.getPose(), follower.getPose()))
                .setConstantHeadingInterpolation(follower.getHeading())
                .build();*/

        //telemetry.addData("This is new code 7", true);
        telemetry.addData("HeadingOffset", dblLockOffset);
        telemetry.addData("Alliance", DataStorage.alliance);
        telemetry.addData("Target Heading", Math.toDegrees(robotBase.chassisSubsystem.targetHeading));
        telemetry.addData("Heading", robotBase.chassisSubsystem.pinpoint.getHeading(AngleUnit.DEGREES));
        telemetry.addData("Launch Zone", DataStorage.launchZone);
        telemetry.addData("Is Field Centric", robotBase.chassisSubsystem.isFieldCentric);
        telemetry.addData("Limelight Heading Lock", robotBase.chassisSubsystem.bolSnapToTarget);
        telemetry.addData("Is Locking to Gate", robotBase.chassisSubsystem.isLockingToGate);
        //telemetry.addData("Intake Power", robotBase.intakeSubsystem.intakeMotor.getPower());
        telemetry.addData("Left Closest Swatch", robotBase.sorterCameraSubsystem.getClosestSwatchLeft());
        telemetry.addData("Middle Closest Swatch", robotBase.sorterCameraSubsystem.getClosestSwatchMiddle());
        telemetry.addData("Right Closest Swatch", robotBase.sorterCameraSubsystem.getClosestSwatchRight());
        //telemetry.addData("Distance", robotBase.limelightSubsystem.getHorizontalDistance(-18.5));
        //telemetry.addData("Odometry Distance", robotBase.limelightSubsystem.getHorizontalDistance(follower));
        //telemetry.addData("Left", robotBase.launcherSubsystemLeft.getVelocity());
        //telemetry.addData("Middle", robotBase.launcherSubsystemMiddle.getVelocity());
        //telemetry.addData("Right", robotBase.launcherSubsystemRight.getVelocity());
        //telemetry.addData("Target Velocity", robotBase.launcherSubsystemLeft.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(0)));
        //telemetry.addData("Is Running", robotBase.limelightSubsystem.limelight.isRunning());
        //telemetry.addData("Is connected: ", robotBase.limelightSubsystem.limelight.isConnected());
        /*telemetry.addData("Hue L", robotBase.sorterCameraSubsystem.getHue(SorterCamera.ArtifactSlot.LEFT));
        telemetry.addData("Hue M", robotBase.sorterCameraSubsystem.getHue(SorterCamera.ArtifactSlot.MIDDLE));
        telemetry.addData("Hue R", robotBase.sorterCameraSubsystem.getHue(SorterCamera.ArtifactSlot.RIGHT));
        telemetry.addData("Sat L", robotBase.sorterCameraSubsystem.getSaturation(SorterCamera.ArtifactSlot.LEFT));
        telemetry.addData("Sat M", robotBase.sorterCameraSubsystem.getSaturation(SorterCamera.ArtifactSlot.MIDDLE));
        telemetry.addData("Sat R", robotBase.sorterCameraSubsystem.getSaturation(SorterCamera.ArtifactSlot.RIGHT));*/
        telemetry.addData("Has Three Artifacts: ", robotBase.sorterCameraSubsystem.hasThreeArtifacts());
        telemetry.addData("Time", timer.seconds());
        telemetry.addData("Limelight Z", robotBase.limelightSubsystem.getTargetZ());
        telemetry.addData("Limelight X", robotBase.limelightSubsystem.getTargetX());
        //telemetry.addData("Pedro Distance", robotBase.limelightSubsystem.getHorizontalDistance(follower, blueGoalPose));
        telemetry.addData("Velocity", robotBase.launcherSubsystemLeft.getVelocity());
        //telemetry.addData("Target Velocity Limelight Blue", robotBase.launcherSubsystemLeft.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(follower, redGoalPose)));
        //telemetry.addData("Target Velocity follower Blue", robotBase.launcherSubsystemLeft.getLaunchVelocity(follower.getPose().distanceFrom(redGoalPose)* 2.54));
        //telemetry.addData("True left target vel", robotBase.launcherSubsystemLeft.dblTargetVel);
        //telemetry.addData("IsAtSpeed", robotBase.launcherSubsystemLeft.isAtSpeed(robotBase.launcherSubsystemLeft.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(follower, redGoalPose))));
        telemetry.addData("X: ", follower.getPose().getX());
        telemetry.addData("Y: ", follower.getPose().getY());
        telemetry.addData("Loop Time", loopTime);
        telemetry.addData("Distance Pedro", follower.getPose().distanceFrom(redGoalPose) * 2.54);
        telemetry.addData("Inside Close Zone", robotZone.isInside(closeLaunchZone));
        telemetry.addData("Is ready to launch: ", readyToLaunch);
        //telemetry.addData("goal in sight", robotBase.limelightSubsystem.goalInSight());
        //telemetry.addData("lock offset", dblLockOffset);
        //telemetry.addData("Follower Pose", follower.getPose());
        /*telemetry.addData("Automated drive", automatedDrive);
        telemetry.addData("Launch Velocity", robotBase.launcherSubsystemLeft.getLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(0)));
        telemetry.addData("Real Velocity", robotBase.launcherSubsystemLeft.getVelocity());
        telemetry.addData("Odometry Distance", robotBase.limelightSubsystem.getHorizontalDistance(follower, redGoalPose));
        telemetry.addData("Velocity", velocity);*/
    }

    @Override
    public void stop(){
        robotBase.limelightSubsystem.limelight.stop();
    }
}
