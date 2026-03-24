package org.firstinspires.ftc.teamcode.pedropathing.routes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.commandgroups.auto.AutoTransferAndLaunchCommandGroup;
import org.firstinspires.ftc.teamcode.commandgroups.auto.AutoTransferAndLaunchNoPatternCG;
import org.firstinspires.ftc.teamcode.commandgroups.general.SetAllVelocityCommandGroup;
import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;
import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Hood;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;

public class RedTurretSorted extends OpMode {

        RobotBase robotBase;
        Follower follower;
        Pose startPose = new Pose(111.62, 135.55, Math.toRadians(180));
        Pose parkingPose = new Pose(106,74,Math.toRadians(0));
        Pose preLaunchPose = new Pose(88,91,Math.toRadians(49));
        Pose launchPose = new Pose(88,91 ,Math.toRadians(49));
        Pose linesUpToOpenRamp = new Pose(121,78,Math.toRadians(90));
      //PGP starts
        Pose opensRamp = new Pose(126,78,Math.toRadians(90));
        Pose linesUpToMiddleSpike = new Pose(97,58,Math.toRadians(0));
        Pose intakesAllMiddle = new Pose(132,58,Math.toRadians(0));
        Pose linesUpToTopRow = new Pose(97,83,Math.toRadians(0));
        Pose intakesFirstBallFromTopRow = new Pose(116,83,Math.toRadians(0));
        Pose linesUpToBottomRow = new Pose(97,34,Math.toRadians(0));
        Pose intakesFirst2BallsFromBottomRow =new Pose(120,34,Math.toRadians(0));
        Pose intakesLast2BallsFromTop = new Pose(129,83,Math.toRadians(0));
        Pose intakesLastBallFromTop = new Pose(125,84,Math.toRadians(0));
       //PGPends & PPGstarts
        Pose intakesFirst2BallsTopRow = new Pose(120,83,Math.toRadians(0));
        Pose intakesFirstBallBottomRow = new Pose (116,34,Math.toRadians(0));
        Pose intakesLast2BallsFromBottom = new Pose(132,34,Math.toRadians(0));
        // PPGends & GPPstarts
        int secondsToWait = 0;
        double dblTargetLaunchVel = 1850;
        double dblPreLaunchVel = 1850;
        double dblSortLaunchVel = 1850;
        ElapsedTime timer;


        PathChain launchPreload;
        PathChain linesUpToMiddleRow;
        PathChain intakesMiddleRow;
        PathChain linesUpToOpenGate;
        PathChain opensGate;
        PathChain goesToShootMiddle;
        PathChain pgpLinesUpToTopRow1;
        PathChain pgpIntakes1stBallTopRow;
        PathChain pgpLinesUpToBottomRow1;
        PathChain pgpIntake2BallsFromBottomRow;
        PathChain pgpGoesToShoot1stTime;
        PathChain linesUpToTopRow2;
        PathChain pgpIntakes2BallsFromTopRow;
        PathChain linesUpToBottomRow2;





        SequentialCommandGroup route;
        @Override
        public void init() {
            robotBase = new RobotBase(hardwareMap);
            follower = Constants.createFollower(hardwareMap);
            robotBase.limelightSubsystem.initLimelight(Limelight.limelightPipelines.OBELISK);
            robotBase.sorterCameraSubsystem.getAnalysis();



            launchPreload = follower.pathBuilder()
                    .addPath(new BezierLine(startPose,preLaunchPose))
                    .setLinearHeadingInterpolation(startPose.getHeading(),preLaunchPose.getHeading())
                    .build();

            linesUpToMiddleRow = follower.pathBuilder()
                    .addPath(new BezierLine(preLaunchPose,linesUpToMiddleSpike))
                    .setLinearHeadingInterpolation(preLaunchPose.getHeading(), linesUpToMiddleSpike.getHeading())
                    .build();
            intakesMiddleRow = follower.pathBuilder()
                    .addPath(new BezierLine(linesUpToMiddleSpike,intakesAllMiddle))
                    .setLinearHeadingInterpolation(linesUpToMiddleSpike.getHeading(), intakesAllMiddle.getHeading())
                    .build();
            linesUpToOpenGate = follower.pathBuilder()
                    .addPath(new BezierLine(intakesAllMiddle,linesUpToOpenRamp))
                    .setLinearHeadingInterpolation(intakesAllMiddle.getHeading(),launchPose.getHeading())
                    .build();
            opensGate = follower.pathBuilder()
                    .addPath(new BezierLine(linesUpToOpenRamp,opensRamp))
                    .setLinearHeadingInterpolation(linesUpToOpenRamp.getHeading(), opensRamp.getHeading())
                    .build();
            goesToShootMiddle = follower.pathBuilder()
                    .addPath(new BezierLine(intakesAllMiddle,launchPose))
                    .setLinearHeadingInterpolation(intakesAllMiddle.getHeading(), launchPose.getHeading())
                    .build();
            pgpLinesUpToTopRow1 = follower.pathBuilder()
                    .addPath(new BezierLine(launchPose,linesUpToTopRow))
                    .setLinearHeadingInterpolation(launchPose.getHeading(),linesUpToTopRow.getHeading())
                    .build();
            pgpIntakes1stBallTopRow = follower.pathBuilder()
                    .addPath(new BezierLine(linesUpToTopRow,intakesFirstBallFromTopRow))
                    .setLinearHeadingInterpolation(linesUpToTopRow.getHeading(), intakesFirstBallFromTopRow.getHeading())
                    .build();
            pgpLinesUpToBottomRow1 = follower.pathBuilder()
                    .addPath(new BezierLine(intakesFirstBallFromTopRow,linesUpToBottomRow))
                    .setLinearHeadingInterpolation(intakesFirstBallFromTopRow.getHeading(),linesUpToBottomRow.getHeading())
                    .build();
            pgpIntake2BallsFromBottomRow = follower.pathBuilder()
                    .addPath(new BezierLine(linesUpToBottomRow,intakesFirst2BallsFromBottomRow))
                    .setLinearHeadingInterpolation(linesUpToBottomRow.getHeading(), intakesFirst2BallsFromBottomRow.getHeading())
                    .build();
            pgpGoesToShoot1stTime = follower.pathBuilder()
                    .addPath(new BezierLine(intakesFirst2BallsFromBottomRow,launchPose))
                    .setLinearHeadingInterpolation(intakesFirst2BallsFromBottomRow.getHeading(), launchPose.getHeading())
                    .build();
            linesUpToTopRow2 = follower.pathBuilder()
                    .addPath(new BezierLine(launchPose,linesUpToTopRow))
                    .setLinearHeadingInterpolation(launchPose.getHeading(),linesUpToTopRow.getHeading())
                    .build();
            pgpIntakes2BallsFromTopRow = follower.pathBuilder()
                    .addPath(new BezierLine(linesUpToTopRow,intakesLast2BallsFromTop))
                    .setLinearHeadingInterpolation(linesUpToTopRow.getHeading(),intakesLast2BallsFromTop.getHeading())
                    .build();
            linesUpToBottomRow2 = follower.pathBuilder()
                    .addPath(new BezierLine(intakesLast2BallsFromTop,linesUpToBottomRow))
                    .setLinearHeadingInterpolation(intakesLast2BallsFromTop.getHeading(), )



            route = new SequentialCommandGroup(
                    new InstantCommand(()->dblTargetLaunchVel = dblPreLaunchVel),
                    new WaitUntilCommand(()->(secondsToWait) <= timer.milliseconds()),
                    new SetAllVelocityCommandGroup(robotBase, dblTargetLaunchVel),
                    new InstantCommand(()->robotBase.hoodSubsystem.setPosition(Hood.HoodPosition.CLOSE)),
                    new FollowPath(follower,launchPreload,true,1),
                    new AutoTransferAndLaunchNoPatternCG(robotBase, dblTargetLaunchVel),
                    new FollowPath(follower,linesUpToMiddleRow,true,1),
                   // new SetAllVelocityCommandGroup(robotBase, dblTargetLaunchVel),
                    new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                    new FollowPath(follower, intakesMiddleRow,true,1),
                    new FollowPath(follower,linesUpToOpenGate,true,1),
                    new FollowPath(follower,opensGate,true,1),
                    new InstantCommand(()->robotBase.intakeSubsystem.intake(1)),
                    new FollowPath(follower,goesToShootMiddle,true,1),
                    new AutoTransferAndLaunchCommandGroup(robotBase, dblTargetLaunchVel),
                    new FollowPath(follower, pgpLinesUpToTopRow1,true,1),
                    new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                    new FollowPath(follower,pgpIntakes1stBallTopRow,true,1),
                    new FollowPath(follower, pgpLinesUpToBottomRow1,true,1),
                    new FollowPath(follower,pgpIntake2BallsFromBottomRow,true,1),
                    new InstantCommand(()->robotBase.intakeSubsystem.intake(1)),
                    new FollowPath(follower,pgpGoesToShoot1stTime,true,1),
                    new AutoTransferAndLaunchNoPatternCG(robotBase, dblTargetLaunchVel),
                    new FollowPath(follower, linesUpToTopRow2,true,1),
                    new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                    new FollowPath(follower,pgpIntakes2BallsFromTopRow,true,1),
                    new


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
                    new SetAllVelocityCommandGroup(robotBase, dblPreLaunchVel)
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

//}

