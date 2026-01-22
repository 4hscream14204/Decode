package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class ChangeLaunchZoneCommandGroup extends SequentialCommandGroup {
    public ChangeLaunchZoneCommandGroup(RobotBase robotBase){
        if(DataStorage.launchZone == DecodeEnums.LaunchZone.CLOSE){
            addCommands(
                    new InstantCommand(()->robotBase.launcherSubsystemLeft.setMaximum(1900)),
                    new InstantCommand(()->robotBase.launcherSubsystemMiddle.setMaximum(1900)),
                    new InstantCommand(()->robotBase.launcherSubsystemRight.setMaximum(1900))
            );
        }
        else{
            addCommands(
                    new InstantCommand(()->robotBase.launcherSubsystemLeft.setMaximum(2300)),
                    new InstantCommand(()->robotBase.launcherSubsystemMiddle.setMaximum(2300)),
                    new InstantCommand(()->robotBase.launcherSubsystemRight.setMaximum(2300))
            );
        }
    }
}
