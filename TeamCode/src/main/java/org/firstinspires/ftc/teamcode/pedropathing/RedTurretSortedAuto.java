package org.firstinspires.ftc.teamcode.pedropathing;

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
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.base.DataStorage;
import org.firstinspires.ftc.teamcode.base.DecodeEnums;
import org.firstinspires.ftc.teamcode.base.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Hood;

public class RedTurretSortedAuto extends OpMode {

    RobotBase robotBase;
    Follower follower;
    Pose startPose = new Pose(111.62, 135.55, Math.toRadians(180));
    Pose pushOtherBot =  new Pose (84, 10 ,Math.toRadians(90));
    Pose parkingPose = new Pose(117,10,Math.toRadians(90));
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
    Pose intakesLastBallFromBottom = new Pose(125,34,Math.toRadians(0));
    //PGPends & PPGstarts
    Pose intakesFirst2BallsTopRow = new Pose(120,83,Math.toRadians(0));
    Pose intakesFirstBallBottomRow = new Pose (116,34,Math.toRadians(0));
    Pose intakesLast2BallsFromBottom = new Pose(132,34,Math.toRadians(0));
    Pose intakesLastBallFromTop = new Pose(129,83,Math.toRadians(0));
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
    PathChain pgpIntakesBallFromBottomRow;
    PathChain pgpGoesToShoot2ndTime;
    PathChain pgpPushesOtherRobot;
    PathChain pgpPark;
    //   PGP Ends PPG starts
    PathChain ppgLinesUpToTopRow1;
    PathChain ppgIntakes2BallsFromTopRow;
    PathChain ppgLinesUpToBottomRow1;
    PathChain ppgIntakes1stBallBottomRow;
    PathChain ppgGoesToShoot1stTime;
    PathChain ppgLinesUpToBottomRow2;
    PathChain ppgIntakes2BallsFromBottom;
    PathChain ppgLinesUpToTopRow2;
    PathChain ppgIntakesLastBallTopRow;
    PathChain ppgGoesToShoot2ndTime;
    PathChain ppgPushesOtherRobot;
    PathChain ppgPark;
    // PPG Ends GPP starts
    PathChain gppLinesUpToBottomRow1;
    PathChain gppIntakes1stBallBottomRow;
    PathChain gppLinesUpToTopRow1;
    PathChain gppIntakes2FromTopRow;
    PathChain gppGoesToShoot1stTime;
    PathChain gppLinesUpToTopRow2;
    PathChain gppIntakesLastBallTop;
    PathChain gppLinesUpToBottomRow2;
    PathChain gppIntakes2BallsBottomRow;
    PathChain gppGoesToShoot2ndTime;
    PathChain gppPushesOtherRobot;
    PathChain gppPark;







    SequentialCommandGroup routePGP;
    SequentialCommandGroup routePPG;
    SequentialCommandGroup routeGPP;
    @Override
    public void init() {
        robotBase = new RobotBase(hardwareMap);
        follower = Constants.createFollower(hardwareMap);
       // robotBase.limelightSubsystem.initLimelight(Limelight.limelightPipelines.OBELISK);
      //  robotBase.sorterCameraSubsystem.getAnalysis();



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
                .setLinearHeadingInterpolation(intakesLast2BallsFromTop.getHeading(), linesUpToBottomRow.getHeading())
                .build();
        pgpIntakesBallFromBottomRow = follower.pathBuilder()
                .addPath(new BezierLine(linesUpToBottomRow,intakesLastBallFromBottom))
                .setLinearHeadingInterpolation(linesUpToBottomRow.getHeading(),intakesLastBallFromBottom.getHeading())
                .build();
        pgpGoesToShoot2ndTime = follower.pathBuilder()
                .addPath(new BezierLine(intakesLastBallFromBottom,launchPose))
                .setLinearHeadingInterpolation(intakesLastBallFromBottom.getHeading(),launchPose.getHeading())
                .build();
        pgpPushesOtherRobot = follower.pathBuilder()
                .addPath(new BezierLine(launchPose,pushOtherBot))
                .setLinearHeadingInterpolation(launchPose.getHeading(),pushOtherBot.getHeading())
                .build();
        pgpPark = follower.pathBuilder()
                .addPath(new BezierLine(pushOtherBot,parkingPose))
                .setLinearHeadingInterpolation(pushOtherBot.getHeading(),parkingPose.getHeading())
                .build();
        //PPG Starts
        ppgLinesUpToTopRow1 = follower.pathBuilder()
                .addPath(new BezierLine(launchPose,linesUpToTopRow))
                .setLinearHeadingInterpolation(launchPose.getHeading(),linesUpToTopRow.getHeading())
                .build();
        ppgIntakes2BallsFromTopRow = follower.pathBuilder()
                .addPath(new BezierLine(linesUpToTopRow,intakesFirst2BallsTopRow))
                .setLinearHeadingInterpolation(linesUpToTopRow.getHeading(),intakesFirst2BallsTopRow.getHeading())
                .build();
        ppgLinesUpToBottomRow1 = follower.pathBuilder()
                .addPath(new BezierLine(intakesFirst2BallsTopRow,linesUpToBottomRow))
                .setLinearHeadingInterpolation(intakesFirst2BallsTopRow.getHeading(),linesUpToBottomRow.getHeading())
                .build();
        ppgIntakes1stBallBottomRow = follower.pathBuilder()
                .addPath(new BezierLine(linesUpToBottomRow,intakesFirstBallBottomRow))
                .setLinearHeadingInterpolation(linesUpToBottomRow.getHeading(),intakesFirstBallBottomRow.getHeading())
                .build();
        ppgGoesToShoot1stTime = follower.pathBuilder()
                .addPath(new BezierLine(intakesFirstBallBottomRow,launchPose))
                .setLinearHeadingInterpolation(intakesFirstBallFromTopRow.getHeading(), launchPose.getHeading())
                .build();
        ppgLinesUpToBottomRow2 = follower.pathBuilder()
                .addPath(new BezierLine(launchPose,linesUpToBottomRow))
                .setLinearHeadingInterpolation(launchPose.getHeading(),linesUpToBottomRow.getHeading())
                .build();
        ppgIntakes2BallsFromBottom = follower.pathBuilder()
                .addPath(new BezierLine(linesUpToBottomRow,intakesLast2BallsFromBottom))
                .setLinearHeadingInterpolation(linesUpToBottomRow.getHeading(),intakesLast2BallsFromBottom.getHeading())
                .build();
        ppgLinesUpToTopRow2 = follower.pathBuilder()
                .addPath(new BezierLine(intakesLast2BallsFromBottom,linesUpToTopRow))
                .setLinearHeadingInterpolation(intakesLast2BallsFromBottom.getHeading(),linesUpToTopRow.getHeading())
                .build();
        ppgIntakesLastBallTopRow = follower.pathBuilder()
                .addPath(new BezierLine(linesUpToTopRow,intakesLastBallFromTop))
                .setLinearHeadingInterpolation(linesUpToTopRow.getHeading(),intakesLastBallFromTop.getHeading())
                .build();
        ppgGoesToShoot2ndTime = follower.pathBuilder()
                .addPath(new BezierLine(intakesLastBallFromTop,launchPose))
                .setLinearHeadingInterpolation(intakesLastBallFromTop.getHeading(),launchPose.getHeading())
                .build();
        ppgPushesOtherRobot = follower.pathBuilder()
                .addPath(new BezierLine(launchPose, pushOtherBot))
                .setLinearHeadingInterpolation(launchPose.getHeading(), pushOtherBot.getHeading())
                .build();
        ppgPark = follower.pathBuilder()
                .addPath(new BezierLine(launchPose,parkingPose))
                .setLinearHeadingInterpolation(launchPose.getHeading(),parkingPose.getHeading())
                .build();
        //GPP Starts
        gppLinesUpToBottomRow1 = follower.pathBuilder()
                .addPath(new BezierLine(launchPose,linesUpToBottomRow))
                .setLinearHeadingInterpolation(launchPose.getHeading(),linesUpToBottomRow.getHeading())
                .build();
        gppIntakes1stBallBottomRow = follower.pathBuilder()
                .addPath(new BezierLine(linesUpToBottomRow,intakesFirstBallBottomRow))
                .setLinearHeadingInterpolation(linesUpToBottomRow.getHeading(),intakesFirstBallBottomRow.getHeading())
                .build();
        gppLinesUpToTopRow1 = follower.pathBuilder()
                .addPath(new BezierLine(intakesFirstBallBottomRow,linesUpToTopRow))
                .setLinearHeadingInterpolation(intakesFirstBallBottomRow.getHeading(),linesUpToTopRow.getHeading())
                .build();
        gppIntakes2FromTopRow = follower.pathBuilder()
                .addPath(new BezierLine(linesUpToTopRow,intakesLast2BallsFromTop))
                .setLinearHeadingInterpolation(linesUpToTopRow.getHeading(),intakesLast2BallsFromTop.getHeading())
                .build();
        gppGoesToShoot1stTime =follower.pathBuilder()
                .addPath(new BezierLine(intakesLast2BallsFromTop,launchPose))
                .setLinearHeadingInterpolation(intakesLast2BallsFromTop.getHeading(),launchPose.getHeading())
                .build();
        gppLinesUpToTopRow2= follower.pathBuilder()
                .addPath(new BezierLine(launchPose,linesUpToTopRow))
                .setLinearHeadingInterpolation(launchPose.getHeading(),linesUpToTopRow.getHeading())
                .build();
        gppIntakesLastBallTop = follower.pathBuilder()
                .addPath(new BezierLine(linesUpToTopRow,intakesLastBallFromTop))
                .setLinearHeadingInterpolation(linesUpToTopRow.getHeading(),intakesLastBallFromTop.getHeading())
                .build();
        gppLinesUpToBottomRow2 = follower.pathBuilder()
                .addPath(new BezierLine(intakesLastBallFromTop,linesUpToBottomRow))
                .setLinearHeadingInterpolation(intakesLastBallFromTop.getHeading(),linesUpToBottomRow.getHeading())
                .build();
        gppIntakes2BallsBottomRow = follower.pathBuilder()
                .addPath(new BezierLine(linesUpToBottomRow,intakesLast2BallsFromBottom))
                .setLinearHeadingInterpolation(linesUpToBottomRow.getHeading(),intakesLast2BallsFromBottom.getHeading())
                .build();
        gppGoesToShoot2ndTime = follower.pathBuilder()
                .addPath(new BezierLine(intakesLast2BallsFromBottom,launchPose))
                .setLinearHeadingInterpolation(intakesLast2BallsFromBottom.getHeading(),launchPose.getHeading())
                .build();
        gppPushesOtherRobot = follower.pathBuilder()
                .addPath(new BezierLine(launchPose,pushOtherBot))
                .setLinearHeadingInterpolation(launchPose.getHeading(), pushOtherBot.getHeading())
                .build();
        gppPark = follower.pathBuilder()
                .addPath(new BezierLine(launchPose,parkingPose))
                .setLinearHeadingInterpolation(launchPose.getHeading(),parkingPose.getHeading())
                .build();



        routePGP = new SequentialCommandGroup(
                new InstantCommand(()->dblTargetLaunchVel = dblPreLaunchVel),
                new WaitUntilCommand(()->(secondsToWait) <= timer.milliseconds()),
                //new SetAllVelocityCommandGroup(robotBase, dblTargetLaunchVel),
                new InstantCommand(()->robotBase.hoodSubsystem.setPosition(Hood.HoodPosition.CLOSE)),
                new FollowPathCommand(follower,launchPreload,true,1),
                //new AutoTransferAndLaunchNoPatternCG(robotBase, dblTargetLaunchVel),
                new FollowPathCommand(follower,linesUpToMiddleRow,true,1),
                // new SetAllVelocityCommandGroup(robotBase, dblTargetLaunchVel),
                //new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                new FollowPathCommand(follower, intakesMiddleRow,true,1),
                new FollowPathCommand(follower,linesUpToOpenGate,true,1),
                new FollowPathCommand(follower,opensGate,true,1),
                //new InstantCommand(()->robotBase.intakeSubsystem.intake(1)),
                new FollowPathCommand(follower,goesToShootMiddle,true,1),
                //new AutoTransferAndLaunchCommandGroup(robotBase, dblTargetLaunchVel),
                new FollowPathCommand(follower, pgpLinesUpToTopRow1,true,1),
                //new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                new FollowPathCommand(follower,pgpIntakes1stBallTopRow,true,1),
                new FollowPathCommand(follower, pgpLinesUpToBottomRow1,true,1),
                new FollowPathCommand(follower,pgpIntake2BallsFromBottomRow,true,1),
                //new InstantCommand(()->robotBase.intakeSubsystem.intake(1)),
                new FollowPathCommand(follower,pgpGoesToShoot1stTime,true,1),
                //new AutoTransferAndLaunchNoPatternCG(robotBase, dblTargetLaunchVel),
                new FollowPathCommand(follower, linesUpToTopRow2,true,1),
                //new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                new FollowPathCommand(follower,pgpIntakes2BallsFromTopRow,true,1),
                new FollowPathCommand(follower, linesUpToBottomRow2,true,1),
                new FollowPathCommand(follower, pgpIntakesBallFromBottomRow,true,1),
                //new InstantCommand(()->robotBase.intakeSubsystem.intake(1)),
                new FollowPathCommand(follower,pgpGoesToShoot2ndTime,true,1),
                //new AutoTransferAndLaunchNoPatternCG(robotBase, dblTargetLaunchVel),
                new FollowPathCommand(follower,pgpPushesOtherRobot,true,1),
                new FollowPathCommand(follower,pgpPark,true,1)

        );

        routePPG = new SequentialCommandGroup(
               // new InstantCommand(()->dblTargetLaunchVel = dblPreLaunchVel),
                new WaitUntilCommand(()->(secondsToWait) <= timer.milliseconds()),
                //new SetAllVelocityCommandGroup(robotBase, dblTargetLaunchVel),
                new InstantCommand(()->robotBase.hoodSubsystem.setPosition(Hood.HoodPosition.CLOSE)),
                new FollowPathCommand(follower,launchPreload,true,1),
                // new AutoTransferAndLaunchNoPatternCG(robotBase, dblTargetLaunchVel),
                new FollowPathCommand(follower,linesUpToMiddleRow,true,1),
                // new SetAllVelocityCommandGroup(robotBase, dblTargetLaunchVel),
                //new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                new FollowPathCommand(follower, intakesMiddleRow,true,1),
                new FollowPathCommand(follower,linesUpToOpenGate,true,1),
                new FollowPathCommand(follower,opensGate,true,1),
                //new InstantCommand(()->robotBase.intakeSubsystem.intake(1)),
                new FollowPathCommand(follower,goesToShootMiddle,true,1),
                //new AutoTransferAndLaunchCommandGroup(robotBase, dblTargetLaunchVel),
                new FollowPathCommand(follower,ppgLinesUpToTopRow1,true,1),
                new FollowPathCommand(follower,ppgIntakes2BallsFromTopRow,true,1),
               // new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                new FollowPathCommand(follower, ppgLinesUpToBottomRow1,true,1),
                new FollowPathCommand(follower,ppgIntakes1stBallBottomRow,true,1),
               // new InstantCommand(()->robotBase.intakeSubsystem.intake(1)),
                new FollowPathCommand(follower,ppgGoesToShoot1stTime,true,1),
               // new AutoTransferAndLaunchCommandGroup(robotBase, dblTargetLaunchVel),
                new FollowPathCommand(follower,ppgLinesUpToBottomRow2,true,1),
                new FollowPathCommand(follower,ppgIntakes2BallsFromBottom,true,1),
                //new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                new FollowPathCommand(follower,ppgLinesUpToTopRow2,true,1),
                new FollowPathCommand(follower,ppgIntakesLastBallTopRow, true,1),
                new FollowPathCommand(follower,ppgGoesToShoot2ndTime,true,1),
               // new AutoTransferAndLaunchCommandGroup(robotBase, dblTargetLaunchVel),
                new FollowPathCommand(follower,ppgPushesOtherRobot,true,1),
                new FollowPathCommand(follower,ppgPark,true,1)



        );

        routeGPP = new SequentialCommandGroup(
                new InstantCommand(()->dblTargetLaunchVel = dblPreLaunchVel),
                new WaitUntilCommand(()->(secondsToWait) <= timer.milliseconds()),
               // new SetAllVelocityCommandGroup(robotBase, dblTargetLaunchVel),
                new InstantCommand(()->robotBase.hoodSubsystem.setPosition(Hood.HoodPosition.CLOSE)),
                new FollowPathCommand(follower,launchPreload,true,1),
                //new AutoTransferAndLaunchNoPatternCG(robotBase, dblTargetLaunchVel),
                new FollowPathCommand(follower,linesUpToMiddleRow,true,1),
                // new SetAllVelocityCommandGroup(robotBase, dblTargetLaunchVel),
               // new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                new FollowPathCommand(follower, intakesMiddleRow,true,1),
                new FollowPathCommand(follower,linesUpToOpenGate,true,1),
                new FollowPathCommand(follower,opensGate,true,1),
               // new InstantCommand(()->robotBase.intakeSubsystem.intake(1)),
                new FollowPathCommand(follower,goesToShootMiddle,true,1),
                //new AutoTransferAndLaunchCommandGroup(robotBase, dblTargetLaunchVel),
                new FollowPathCommand(follower, gppLinesUpToBottomRow1,true,1),
                new FollowPathCommand(follower,gppIntakes1stBallBottomRow,true,1),
                //new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                new FollowPathCommand(follower, gppLinesUpToTopRow1,true,1),
                new FollowPathCommand(follower,gppIntakes2FromTopRow,true,1),
                //new InstantCommand(()->robotBase.intakeSubsystem.intake(1)),
                new FollowPathCommand(follower,gppGoesToShoot1stTime,true,1),
               // new AutoTransferAndLaunchNoPatternCG(robotBase, dblTargetLaunchVel),
                new FollowPathCommand(follower,gppLinesUpToTopRow2,true,1),
                new FollowPathCommand(follower,gppIntakesLastBallTop,true,1),
               // new InstantCommand(()->robotBase.intakeSubsystem.intake(-1)),
                new FollowPathCommand(follower,gppLinesUpToBottomRow2,true,1),
                new FollowPathCommand(follower,gppIntakes2BallsBottomRow,true,1),
                new FollowPathCommand(follower,gppGoesToShoot2ndTime,true,1),
               // new AutoTransferAndLaunchNoPatternCG(robotBase, dblTargetLaunchVel),
                new FollowPathCommand(follower,gppPushesOtherRobot,true,1),
                new FollowPathCommand(follower,gppPark,true,1)



        );




        /*new Trigger(()->follower.getCurrentPathChain() == startPath)
                .whileActiveContinuous(new SetAllLaunchVelocityCommandGroup(robotBase, robotBase.limelightSubsystem.getHorizontalDistance(follower)));*/
    }

    @Override
    public void init_loop() {
    //    robotBase.sorterCameraSubsystem.getAnalysis();
    }

    @Override
    public void start() {
        follower.setStartingPose(startPose);
        CommandScheduler.getInstance().schedule(routePGP);
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        CommandScheduler.getInstance().schedule(
    //            new SetAllVelocityCommandGroup(robotBase, dblPreLaunchVel)
        );
     //   robotBase.limelightSubsystem.updateLimelight();
       // robotBase.sorterCameraSubsystem.getAnalysis();
        //follower.update();
        //if(robotBase.limelightSubsystem.id == 23){
          //  DataStorage.pattern = DecodeEnums.Patterns.PPG;
        //}
        //else if(robotBase.limelightSubsystem.id == 22){
          //  DataStorage.pattern = DecodeEnums.Patterns.PGP;
        //}
        //else if (robotBase.limelightSubsystem.id == 21){
//            DataStorage.pattern = DecodeEnums.Patterns.GPP;
       // }//
      //else{
        DataStorage.pattern = DecodeEnums.Patterns.PPG;
        //}
        follower.update();
       // telemetry.addData("Launch Velocity", robotBase.launcherSubsystemLeft.getVelocity());
      //  telemetry.addData("Limelight Distance", robotBase.limelightSubsystem.getHorizontalDistance(follower));
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y: ", follower.getPose().getY());
        telemetry.addData("Heading: ", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.update();
    }
    @Override
    public void stop(){
        Pose endPose = new Pose(follower.getPose().getX()+7, follower.getPose().getY()-4, follower.getPose().getHeading());
       // robotBase.limelightSubsystem.limelight.stop();
        DataStorage.endPosition = endPose;
        DataStorage.alliance = DecodeEnums.Alliance.RED;
    }
}

//}

