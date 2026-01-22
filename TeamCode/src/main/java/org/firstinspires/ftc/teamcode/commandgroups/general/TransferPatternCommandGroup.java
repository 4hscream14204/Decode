package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.robotbase.DataStorage;
import org.firstinspires.ftc.teamcode.robotbase.DecodeEnums;
import org.firstinspires.ftc.teamcode.robotbase.RobotBase;


public class TransferPatternCommandGroup extends SequentialCommandGroup {
    RobotBase robotBase;


    public TransferPatternCommandGroup(RobotBase m_robotBase) {
        robotBase = m_robotBase;
    }
        @Override
        public void initialize() {
            if (DataStorage.pattern == DecodeEnums.Patterns.PPG) {
                addCommands(
                        new TransferTwoPurpleCommandGroup(robotBase),
                        new WaitCommand(500),
                        new TransferGreenBallCommandGroup(robotBase),
                        //new WaitCommand(500),
                        new TransferResetCommandGroup(robotBase)
                );
            } else if (DataStorage.pattern == DecodeEnums.Patterns.GPP) {
                addCommands(
                        new TransferGreenBallCommandGroup(robotBase),
                        new WaitCommand(500),
                        new TransferTwoPurpleCommandGroup(robotBase),
                        //new WaitCommand(500),
                       new TransferResetCommandGroup(robotBase)
                );
            } else if (DataStorage.pattern == DecodeEnums.Patterns.PGP) {
                addCommands(
                        new TransferPurpleBallCommandGroup(robotBase),
                        new WaitCommand(500),
                        new TransferGreenBallCommandGroup(robotBase),
                        new WaitCommand(500),
                        new TransferPurpleBallCommandGroup(robotBase),
                        new TransferResetCommandGroup(robotBase)
                );
            }

            //Have to call the super classes initalize as that is what tells the scheduler to run them
            super.initialize();
        }

    }


