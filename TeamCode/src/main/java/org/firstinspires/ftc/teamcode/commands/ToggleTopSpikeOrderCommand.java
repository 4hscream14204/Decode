package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.opmode.auto.cri.BluePrismAuto;

public class ToggleTopSpikeOrderCommand extends CommandBase {
    public ToggleTopSpikeOrderCommand(){
    }
    @Override
    public void initialize(){
        if(BluePrismAuto.topSpikeOrder == BluePrismAuto.SpikeOrder.NONE){
            BluePrismAuto.topSpikeOrder = BluePrismAuto.SpikeOrder.FIRST;
        }
        else if(BluePrismAuto.topSpikeOrder == BluePrismAuto.SpikeOrder.FIRST){
            BluePrismAuto.topSpikeOrder = BluePrismAuto.SpikeOrder.SECOND;
        }
        else if(BluePrismAuto.topSpikeOrder == BluePrismAuto.SpikeOrder.SECOND){
            BluePrismAuto.topSpikeOrder = BluePrismAuto.SpikeOrder.THIRD;
        }
        else{
            BluePrismAuto.topSpikeOrder = BluePrismAuto.SpikeOrder.NONE;
        }
    }
}
