package commands;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.subsystems.Arm;
import org.firstinspires.ftc.teamcode.subsystems.Claw;


public class PickupAndExtendCommandGroup extends SequentialCommandGroup {
    public PickupAndExtendCommandGroup(Arm arm, Claw claw) { /*write all the subsystems that will be used in the command group*/
        addCommands(
                new InstantCommand(()->claw.closeClaw()),
                new WaitCommand(200),
                new InstantCommand(()->arm.goToPosition(Arm.ArmPosition.HIGH))
                );
    }
}
