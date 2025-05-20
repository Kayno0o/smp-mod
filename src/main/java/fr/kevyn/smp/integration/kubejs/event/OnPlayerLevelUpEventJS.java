package fr.kevyn.smp.integration.kubejs.event;

import com.daqem.jobsplus.player.JobsPlayer;
import com.daqem.jobsplus.player.job.Job;

import dev.latvian.mods.kubejs.entity.KubeEntityEvent;
import fr.kevyn.smp.event.OnPlayerLevelUpEvent;
import net.minecraft.world.entity.Entity;

public class OnPlayerLevelUpEventJS extends OnPlayerLevelUpEvent implements KubeEntityEvent {
  public OnPlayerLevelUpEventJS(JobsPlayer player, Job job) {
    super(player, job);
  }

  @Override
  public Entity getEntity() {
    return this.getJobsPlayer().jobsplus$getPlayer();
  }
}
