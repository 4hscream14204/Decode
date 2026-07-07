package org.firstinspires.ftc.teamcode.commands;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.base.DataStorage;
import org.firstinspires.ftc.teamcode.base.DecodeEnums;

public class ResetPositionCommand extends CommandBase {
    Follower follower;
    public ResetPositionCommand(Follower m_follower){
        follower = m_follower;
    }

    @Override
    public void initialize(){
        if(DataStorage.alliance == DecodeEnums.Alliance.RED){
            follower.setPose(new Pose(170, 126, Math.toRadians(180)));
        }
        else{
            follower.setPose(new Pose(10, 125, Math.toRadians(0)));
        }
    }
}
