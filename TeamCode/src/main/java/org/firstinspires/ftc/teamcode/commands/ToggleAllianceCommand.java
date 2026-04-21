package org.firstinspires.ftc.teamcode.commands;

import android.provider.ContactsContract;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.base.DataStorage;
import org.firstinspires.ftc.teamcode.base.DecodeEnums;
import org.firstinspires.ftc.teamcode.base.RobotBase;

public class ToggleAllianceCommand extends CommandBase {

    @Override
    public void initialize(){
        if(DataStorage.alliance == DecodeEnums.Alliance.RED){
            DataStorage.alliance = DecodeEnums.Alliance.BLUE;
        }
        else{
            DataStorage.alliance = DecodeEnums.Alliance.RED;
        }
    }
}
