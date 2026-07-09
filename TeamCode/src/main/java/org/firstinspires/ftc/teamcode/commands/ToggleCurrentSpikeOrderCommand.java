package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.opmode.auto.cri.BluePrismAuto;

public class ToggleCurrentSpikeOrderCommand extends CommandBase {
    public ToggleCurrentSpikeOrderCommand(){
    }
    @Override
    public void initialize(){
        if(BluePrismAuto.currentSpikeOrder == BluePrismAuto.SpikeOrder.NONE){
            BluePrismAuto.currentSpikeOrder = BluePrismAuto.SpikeOrder.FIRST;
        }
        else if(BluePrismAuto.currentSpikeOrder == BluePrismAuto.SpikeOrder.FIRST){
            BluePrismAuto.currentSpikeOrder = BluePrismAuto.SpikeOrder.SECOND;
        }
        else if(BluePrismAuto.currentSpikeOrder == BluePrismAuto.SpikeOrder.SECOND){
            BluePrismAuto.currentSpikeOrder = BluePrismAuto.SpikeOrder.THIRD;
        } else if (BluePrismAuto.currentSpikeOrder == BluePrismAuto.SpikeOrder.THIRD) {
            BluePrismAuto.currentSpikeOrder = BluePrismAuto.SpikeOrder.PARK;
        } else{
            BluePrismAuto.currentSpikeOrder = BluePrismAuto.SpikeOrder.NONE;
        }
    }
}
