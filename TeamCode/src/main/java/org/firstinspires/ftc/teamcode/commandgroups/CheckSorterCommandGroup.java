package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.robotbase.RobotBase;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;

public class CheckSorterCommandGroup extends SequentialCommandGroup {
    double purpleArtifacts;
    double greenArtifacts;
    public CheckSorterCommandGroup(RobotBase robotBase, Webca){
        if(result.closestSwatch == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE){
            purpleArtifacts ++;
        }
    }
}
