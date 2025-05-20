package fr.kevyn.smp.event;

import com.daqem.jobsplus.player.JobsPlayer;
import com.daqem.jobsplus.player.job.Job;

import net.neoforged.bus.api.Event;

public class OnPlayerLevelUpEvent extends Event {

  private final Job job;
  private final JobsPlayer jobsPlayer;

  public OnPlayerLevelUpEvent(JobsPlayer jobsPlayer, Job job) {
    this.jobsPlayer = jobsPlayer;
    this.job = job;
  }

  public Job getJob() {
    return this.job;
  }

  public JobsPlayer getJobsPlayer() {
    return this.jobsPlayer;
  }
}
