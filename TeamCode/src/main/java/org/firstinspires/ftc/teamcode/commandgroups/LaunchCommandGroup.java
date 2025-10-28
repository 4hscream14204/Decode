package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;


public class LaunchCommandGroup extends SequentialCommandGroup {
    public LaunchCommandGroup(RobotBase robotBase){
        addCommands(
                new InstantCommand(()->robotBase.launcherSubsystem.setLaunchVelocity(robotBase.limelightSubsystem.getHorizontalDistance(0))));


    }
}
