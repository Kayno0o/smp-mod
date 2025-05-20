package fr.kevyn.smp.kubejs;

import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.ScriptType;
import fr.kevyn.smp.event.OnPlayerLevelUpEvent;
import fr.kevyn.smp.kubejs.event.OnPlayerLevelUpEventJS;
import fr.kevyn.smp.kubejs.event.SmpJSEvents;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;

public class SmpKubeJSPlugin implements KubeJSPlugin {
  @Override
  public void registerEvents(EventGroupRegistry registry) {
    registry.register(SmpJSEvents.GROUP);
  }

  @Override
  public void init() {
    subscribeToNeoForgeEvents();
  }

  private void subscribeToNeoForgeEvents() {
    NeoForge.EVENT_BUS.addListener(EventPriority.LOW, this::postAddEntityInFrameDataEvent);
  }

  // --

  private void postAddEntityInFrameDataEvent(OnPlayerLevelUpEvent event) {
    SmpJSEvents.ON_PLAYER_LEVEL_UP.post(ScriptType.SERVER,
        new OnPlayerLevelUpEventJS(event.getJobsPlayer(), event.getJob()));
  }
}
