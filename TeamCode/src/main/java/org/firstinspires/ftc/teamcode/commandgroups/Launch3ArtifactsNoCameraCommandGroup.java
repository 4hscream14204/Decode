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
                new LaunchCommandGroup(robotBase),
                new Transfer3BallsNoCameraCommandGroup(robotBase),
                new WaitCommand(1500),
                new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(0))
        );
    }
}
