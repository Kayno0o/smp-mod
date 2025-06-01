package fr.kevyn.smp.network;

import java.util.function.Consumer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketHandlerUtils {
  private PacketHandlerUtils() {}

  // Utility method for safe server handling
  public static void enqueueWork(IPayloadContext context, Runnable work) {
    context.enqueueWork(work).exceptionally(e -> {
      context.disconnect(Component.translatable("networking.smp.failed", e.getMessage()));
      return null;
    });
  }

  public static void enqueueServerWork(IPayloadContext context, Consumer<ServerPlayer> work) {
    enqueueWork(context, () -> {
      if (context.player() instanceof ServerPlayer player) {
        work.accept(player);
      }
    });
  }

  public static void enqueueClientWork(IPayloadContext context, Consumer<LocalPlayer> work) {
    enqueueWork(context, () -> {
      if (context.player() instanceof LocalPlayer player) {
        work.accept(player);
      }
    });
  }
}
