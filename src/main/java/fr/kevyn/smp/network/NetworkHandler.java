package fr.kevyn.smp.network;

import fr.kevyn.smp.SmpMod;
import fr.kevyn.smp.network.client.UpdatePlayerAccountsNet;
import fr.kevyn.smp.network.server.ATMWithdrawNet;
import fr.kevyn.smp.network.server.ClearCardAccountPacket;
import fr.kevyn.smp.network.server.MenuActionNet;
import fr.kevyn.smp.network.server.SetCardAccountPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = SmpMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkHandler {
  @SubscribeEvent
  public static void register(final RegisterPayloadHandlersEvent event) {
    final PayloadRegistrar registrar = event.registrar("1");
    registrar.playToClient(
        UpdatePlayerAccountsNet.TYPE,
        UpdatePlayerAccountsNet.STREAM_CODEC,
        UpdatePlayerAccountsNet::handleOnClient);
    registrar.playToServer(
        ATMWithdrawNet.TYPE, ATMWithdrawNet.STREAM_CODEC, ATMWithdrawNet::handleOnServer);
    registrar.playToServer(
        MenuActionNet.TYPE, MenuActionNet.STREAM_CODEC, MenuActionNet::handleOnServer);
    registrar.playToServer(
        SetCardAccountPacket.TYPE,
        SetCardAccountPacket.STREAM_CODEC,
        SetCardAccountPacket::handleOnServer);
    registrar.playToServer(
        ClearCardAccountPacket.TYPE,
        ClearCardAccountPacket.STREAM_CODEC,
        ClearCardAccountPacket::handleOnServer);
  }
}
