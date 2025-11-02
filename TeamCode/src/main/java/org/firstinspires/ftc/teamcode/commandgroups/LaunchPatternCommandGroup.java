package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;

public class LaunchPatternCommandGroup extends SequentialCommandGroup {
    TransferPatternCommandGroup transferPatternCommandGroup;
    public LaunchPatternCommandGroup(RobotBase robotBase){
        transferPatternCommandGroup = new TransferPatternCommandGroup(robotBase);
        addCommands(
                new LaunchCommandGroup(robotBase),
                new WaitCommand(250),
                new InstantCommand(()->transferPatternCommandGroup.schedule()),
                new WaitCommand(1500),
                new InstantCommand(()->robotBase.launcherSubsystem.setVelocity(0))
        );
    }
}
