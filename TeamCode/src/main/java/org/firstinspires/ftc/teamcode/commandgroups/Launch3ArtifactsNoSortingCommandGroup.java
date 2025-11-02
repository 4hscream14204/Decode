package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class Launch3ArtifactsNoSortingCommandGroup extends SequentialCommandGroup {
    public Launch3ArtifactsNoSortingCommandGroup(RobotBase robotBase){
        addCommands(
                new LaunchCommandGroup(robotBase),
                new WaitCommand(1000),
                new Transfer3BallsNoCameraCommandGroup(robotBase),
                new WaitCommand(1000),
                new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(0))
        );
    }
}
