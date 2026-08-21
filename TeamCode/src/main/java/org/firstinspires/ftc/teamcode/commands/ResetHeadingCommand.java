package org.firstinspires.ftc.teamcode.commands;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.base.DataStorage;
import org.firstinspires.ftc.teamcode.base.DecodeEnums;

public class ResetHeadingCommand extends CommandBase {
    Follower follower;
    public ResetHeadingCommand(Follower m_follower){
        follower = m_follower;
    }

    @Override
    public void initialize(){
        if(DataStorage.alliance == DecodeEnums.Alliance.RED){
            follower.setPose(new Pose(132, 78, Math.toRadians(180)));
        }
        else{
            follower.setPose(new Pose(18, 77, Math.toRadians(0)));
        }
    }
}
