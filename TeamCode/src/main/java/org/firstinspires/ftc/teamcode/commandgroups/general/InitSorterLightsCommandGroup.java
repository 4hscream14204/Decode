package org.firstinspires.ftc.teamcode.commandgroups.general;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.subsystems.CameraLight;

public class InitSorterLightsCommandGroup extends SequentialCommandGroup {
    public InitSorterLightsCommandGroup(RobotBase robotBase) {
        addCommands(
                new InstantCommand(()-> robotBase.cameraLightSubsystemLeft.setShade(CameraLight.Shades.HALF)),
                new InstantCommand(()-> robotBase.cameraLightSubsystemRight.setShade(CameraLight.Shades.HALF))
        );
    }
}
