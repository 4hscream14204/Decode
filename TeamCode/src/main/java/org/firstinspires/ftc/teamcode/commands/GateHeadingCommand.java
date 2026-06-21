package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.base.DataStorage;
import org.firstinspires.ftc.teamcode.base.DecodeEnums;
import org.firstinspires.ftc.teamcode.base.RobotBase;

public class GateHeadingCommand extends CommandBase {
    RobotBase robotBase;
    public GateHeadingCommand(RobotBase m_robotBase){
        robotBase = m_robotBase;
    }

    @Override
    public void initialize(){
        if(DataStorage.alliance == DecodeEnums.Alliance.RED){
            robotBase.chassisSubsystem.setTargetHeading(37);
        }
        else{
            robotBase.chassisSubsystem.setTargetHeading(137);
        }
    }
}
