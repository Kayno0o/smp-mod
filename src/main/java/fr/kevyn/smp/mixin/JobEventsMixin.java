package fr.kevyn.smp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.daqem.jobsplus.event.triggers.JobEvents;
import com.daqem.jobsplus.player.JobsPlayer;
import com.daqem.jobsplus.player.job.Job;

import fr.kevyn.smp.event.OnPlayerLevelUpEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mixin(JobEvents.class)
public class JobEventsMixin {

  @Inject(method = "onJobLevelUp", at = @At("TAIL"))
  private static void smp$onJobLevelUp(JobsPlayer player, Job job, CallbackInfo ci) {
    OnPlayerLevelUpEvent event = new OnPlayerLevelUpEvent(player, job);
    NeoForge.EVENT_BUS.post(event);
  }
}
