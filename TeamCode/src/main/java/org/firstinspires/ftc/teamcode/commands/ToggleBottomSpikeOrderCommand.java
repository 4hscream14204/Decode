package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.opmode.auto.cri.BluePrismAuto;

public class ToggleBottomSpikeOrderCommand extends CommandBase {
    public ToggleBottomSpikeOrderCommand(){
    }
    @Override
    public void initialize(){
        if(BluePrismAuto.bottomSpikeOrder == BluePrismAuto.SpikeOrder.NONE){
            BluePrismAuto.bottomSpikeOrder = BluePrismAuto.SpikeOrder.FIRST;
        }
        else if(BluePrismAuto.bottomSpikeOrder == BluePrismAuto.SpikeOrder.FIRST){
            BluePrismAuto.bottomSpikeOrder = BluePrismAuto.SpikeOrder.SECOND;
        }
        else if(BluePrismAuto.bottomSpikeOrder == BluePrismAuto.SpikeOrder.SECOND){
            BluePrismAuto.bottomSpikeOrder = BluePrismAuto.SpikeOrder.THIRD;
        }
        else{
            BluePrismAuto.bottomSpikeOrder = BluePrismAuto.SpikeOrder.NONE;
        }
    }
}
