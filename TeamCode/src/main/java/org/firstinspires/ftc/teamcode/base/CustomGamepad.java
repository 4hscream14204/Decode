package org.firstinspires.ftc.teamcode.base;

import com.bylazar.gamepad.Gamepad;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.commands.GateHeadingCommand;
import org.firstinspires.ftc.teamcode.commands.ToggleAllianceCommand;
import org.firstinspires.ftc.teamcode.commands.TransferCommand;
import org.firstinspires.ftc.teamcode.subsystems.IntakePivot;

public class CustomGamepad {
    GamepadEx gamepad;
    RobotBase robotBase;
    Follower follower;
    public CustomGamepad(GamepadEx m_gamepad, RobotBase m_robotBase, Follower m_follower){
        gamepad = m_gamepad;
        robotBase = m_robotBase;
        follower = m_follower;
    }

    public void player1(){
        gamepad.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(()-> CommandScheduler.getInstance().schedule(new InstantCommand(()->robotBase.intakePivotSubsystem.setPosition(IntakePivot.PivotPosition.INTAKE))));

        gamepad.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new TransferCommand(robotBase, follower)));

        gamepad.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                        .whenPressed(()->CommandScheduler.getInstance().schedule(new GateHeadingCommand(robotBase)));

        gamepad.getGamepadButton(GamepadKeys.Button.START)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new InstantCommand(()->follower.setPose(new Pose(92, 10, Math.toRadians(90))))));
    }

    public void player2(){


        gamepad.getGamepadButton(GamepadKeys.Button.START)
                .whenPressed(()->CommandScheduler.getInstance().schedule(new ToggleAllianceCommand()));
    }
}
