package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;

public class ToggleLaunchZoneCommandGroup extends SequentialCommandGroup {
    public ToggleLaunchZoneCommandGroup(){
        if(DataStorage.launchZone == DecodeEnums.LaunchZone.CLOSE){
            addCommands(
                    new InstantCommand(()->DataStorage.launchZone = DecodeEnums.LaunchZone.FAR)
            );
        }
        else{
            addCommands(
                    new InstantCommand(()->DataStorage.launchZone = DecodeEnums.LaunchZone.CLOSE)
            );
        }
    }
}
