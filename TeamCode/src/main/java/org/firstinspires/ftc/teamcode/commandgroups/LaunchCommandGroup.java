package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.subsystems.Launcher;


public class LaunchCommandGroup extends SequentialCommandGroup {
    public LaunchCommandGroup(DcMotor launcher1, DcMotor launcher2, DcMotor launcher3){
        addCommands(
                new InstantCommand(()->launcher1.setPower(.7)),
                new WaitCommand(1000),
                new InstantCommand(()->launcher2.setPower(.7)),
                new WaitCommand(1000),
                new InstantCommand(()->launcher3.setPower(.7)));


    }
}
