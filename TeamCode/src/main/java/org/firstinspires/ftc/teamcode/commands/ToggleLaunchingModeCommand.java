package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.base.DataStorage;
import org.firstinspires.ftc.teamcode.base.DecodeEnums;

public class ToggleLaunchingModeCommand extends CommandBase {

    @Override
    public void initialize(){
        if(DataStorage.launchingMode == DecodeEnums.LaunchingMode.GOAL){
            DataStorage.launchingMode = DecodeEnums.LaunchingMode.SPECTRUM;
        }
        else{
            DataStorage.launchingMode = DecodeEnums.LaunchingMode.GOAL;
        }
    }
}
