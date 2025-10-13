package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.Robot;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class Launch3ArtifactsNoCameraCommandGroup extends SequentialCommandGroup {
    public Launch3ArtifactsNoCameraCommandGroup(RobotBase robotBase){
        addCommands(
                new InstantCommand(()->robotBase.launcherSubsystem.setRPM(robotBase.limelightSubsystem.getLaunchSpeed())),
                new WaitCommand(500),
                new Transfer3BallsNoCameraCommandGroup(robotBase),
                new WaitCommand(1000),
                new InstantCommand(()->robotBase.launcherSubsystem.setRPM(0))
        );
    }
}
