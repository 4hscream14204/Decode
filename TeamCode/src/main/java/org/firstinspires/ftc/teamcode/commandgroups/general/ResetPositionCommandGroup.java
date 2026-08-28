package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;

public class ResetPositionCommandGroup extends CommandBase {
    Follower follower;
    public ResetPositionCommandGroup(Follower m_follower){
        follower = m_follower;
    }

    @Override
    public void initialize(){
        if(DataStorage.alliance == DecodeEnums.Alliance.RED){
            follower.setPose(new Pose(124, 67, Math.toRadians(180)));
        }
        else{
            follower.setPose(new Pose(19, 79, Math.toRadians(0)));
        }
    }
}
