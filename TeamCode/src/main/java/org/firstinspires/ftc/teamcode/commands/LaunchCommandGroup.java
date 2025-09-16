package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.opmode.teleop.StarterBotTeleop;

public class LaunchCommandGroup extends SequentialCommandGroup {
public LaunchCommandGroup (DcMotorEx launcher, CRServo leftFeeder, CRServo rightFeeder){
addCommands(
        new InstantCommand(()-> launcher.setVelocity(1125)),
        new WaitCommand(1000),
        new InstantCommand(()-> leftFeeder.setPower(1.0)),
        new InstantCommand(()-> rightFeeder.setPower(1.0))
    //new InstantCommand(()-> robotBase.elbowSubsystem.goToPosition(Elbow.ElbowPosition.PICKUP)),
        );
    }
}
