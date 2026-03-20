package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class AlignToGateHeadingLock extends SequentialCommandGroup {
    RobotBase robotBase;
    int heading;
    public AlignToGateHeadingLock(RobotBase m_robotBase){
        robotBase = m_robotBase;
        if(DataStorage.alliance == DecodeEnums.Alliance.RED){
            heading = 54;
        }
        else{
            heading = 144;
        }
        addCommands(
                new InstantCommand(()->robotBase.chassisSubsystem.setTargetHeading(heading))
        );
    }

}
