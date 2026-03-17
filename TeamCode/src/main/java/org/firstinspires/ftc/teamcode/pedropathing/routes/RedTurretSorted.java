package org.firstinspires.ftc.teamcode.pedropathing.routes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.commandgroups.general.SetAllVelocityCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.general.TransferResetCommandGroup;
import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;
import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Hood;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;
import org.firstinspires.ftc.teamcode.subsystems.SorterServo;

public class RedTurretSorted extends OpMode {

        RobotBase robotBase;
        Follower follower;
        Pose startPose = new Pose(111.62, 135.55, Math.toRadians(180));
        Pose preLaunchPose = new Pose(88,91,Math.toRadians(49));
        Pose linesUpToOpenRamp = new Pose(121,78,Math.toRadians(90));
        Pose opensRamp = new Pose(126,78,Math.toRadians(90));
        Pose linesUpToMiddleRowPgp = new Pose(97,58,Math.toRadians(0));
        Pose intakesMiddleRowPgp = new Pose(132,58,Math.toRadians(0));
        Pose linesUpToTopRowPgp = new Pose(97,83,Math.toRadians(0));
        Pose intakes1ballFromTopRow = new Pose(116,83,Math.toRadians(0));
        Pose linesUpToBottomRowPgp = new Pose(97,34,Math.toRadians(0));
        Pose intakes2BallsFromBottomRow =new Pose(120,34,Math.toRadians(0));
        Pose intakesLast2BallsFromTop = new Pose(129,83,Math.toRadians(0));
        Pose


        double dblLaunchVel = 1700;

        PathChain startPath;
        PathChain launchPreload;
        PathChain intakeMiddleRowPathLineUp;


        SequentialCommandGroup route;
        @Override
        public void init() {
            robotBase = new RobotBase(hardwareMap);
            follower = Constants.createFollower(hardwareMap);
            robotBase.limelightSubsystem.initLimelight(Limelight.limelightPipelines.OBELISK);
            robotBase.sorterCameraSubsystem.getAnalysis();


            intakeMiddleRowPathLineUp = follower.pathBuilder()
                    .addPath(new BezierLine(launchPose, intakeMiddleLineUp))
                    .setLinearHeadingInterpolation(launchPose.getHeading(), intakeMiddleLineUp.getHeading())
                    .build();



            route = new SequentialCommandGroup(


            );
        /*new Trigger(()->follower.getCurrentPathChain() == startPath)
                .whileActiveContinuous(new SetAllLaunchVelocityCommandGroup(robotBase, robotBase.limelightSubsystem.getHorizontalDistance(follower)));*/
        }

        @Override
        public void init_loop() {
            robotBase.sorterCameraSubsystem.getAnalysis();
        }

        @Override
        public void start() {
            follower.setStartingPose(startPose);
            CommandScheduler.getInstance().schedule(route);
        }

        @Override
        public void loop() {
            CommandScheduler.getInstance().run();
            CommandScheduler.getInstance().schedule(
                    new SetAllVelocityCommandGroup(robotBase, dblLaunchVel)
            );
            follower.update();
            telemetry.addData("Launch Velocity", robotBase.launcherSubsystemLeft.getVelocity());
            telemetry.addData("Limelight Distance", robotBase.limelightSubsystem.getHorizontalDistance(follower));
            telemetry.addData("X", follower.getPose().getX());
            telemetry.addData("Y: ", follower.getPose().getY());
            telemetry.addData("Heading: ", Math.toDegrees(follower.getPose().getHeading()));
            telemetry.update();
        }
        @Override
        public void stop(){
            Pose endPose = new Pose(follower.getPose().getX()+7, follower.getPose().getY()-4, follower.getPose().getHeading());
            robotBase.limelightSubsystem.limelight.stop();
            DataStorage.endPosition = endPose;
            DataStorage.alliance = DecodeEnums.Alliance.RED;
        }
    }

}

