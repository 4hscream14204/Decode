package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.pedropathing.follower.Follower;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class LockPedroCG extends SequentialCommandGroup {
    RobotBase robotBase;
    Follower follower;
    boolean automatedDrive;
    public LockPedroCG(RobotBase m_robotBase, Follower m_follower, boolean m_automatedDrive){
        robotBase = m_robotBase;
        follower = m_follower;
    }

    @Override
    public void initialize(){
        addCommands(
                new InstantCommand(()->follower.turnToDegrees((Math.toDegrees(follower.getHeading()) + robotBase.limelightSubsystem.getTargetX()))),
                new WaitUntilCommand(()->!follower.isTurning()),
                new InstantCommand(()->automatedDrive = false)
        );
    }

}
