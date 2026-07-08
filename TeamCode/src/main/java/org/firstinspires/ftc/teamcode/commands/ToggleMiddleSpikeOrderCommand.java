package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.opmode.auto.cri.BluePrismAuto;

public class ToggleMiddleSpikeOrderCommand extends CommandBase {
    public ToggleMiddleSpikeOrderCommand(){
    }
    @Override
    public void initialize(){
        if(BluePrismAuto.middleSpikeOrder == BluePrismAuto.SpikeOrder.NONE){
            BluePrismAuto.middleSpikeOrder = BluePrismAuto.SpikeOrder.FIRST;
        }
        else if(BluePrismAuto.middleSpikeOrder == BluePrismAuto.SpikeOrder.FIRST){
            BluePrismAuto.middleSpikeOrder = BluePrismAuto.SpikeOrder.SECOND;
        }
        else if(BluePrismAuto.middleSpikeOrder == BluePrismAuto.SpikeOrder.SECOND){
            BluePrismAuto.middleSpikeOrder = BluePrismAuto.SpikeOrder.THIRD;
        }
        else{
            BluePrismAuto.middleSpikeOrder = BluePrismAuto.SpikeOrder.NONE;
        }
    }
}
