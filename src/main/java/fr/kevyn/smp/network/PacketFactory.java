package fr.kevyn.smp.network;

import fr.kevyn.smp.SmpMod;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class PacketFactory {
  private PacketFactory() {}

  public static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> createType(
      String name) {
    return new CustomPacketPayload.Type<>(
        ResourceLocation.fromNamespaceAndPath(SmpMod.MODID, name));
  }
}
