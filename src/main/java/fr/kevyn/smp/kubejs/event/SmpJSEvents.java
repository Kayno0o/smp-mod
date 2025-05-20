package fr.kevyn.smp.kubejs.event;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface SmpJSEvents {
  EventGroup GROUP = EventGroup.of("SmpEvents");

  EventHandler ON_PLAYER_LEVEL_UP = GROUP.common("onPlayerLevelUp", () -> OnPlayerLevelUpEventJS.class);
}
