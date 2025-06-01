package fr.kevyn.artisanspath.network;

import fr.kevyn.artisanspath.ArtisansMod;
import fr.kevyn.artisanspath.network.client.UpdatePlayerAccountsPacket;
import fr.kevyn.artisanspath.network.server.ATMWithdrawPacket;
import fr.kevyn.artisanspath.network.server.ClearCardAccountPacket;
import fr.kevyn.artisanspath.network.server.CreateAccountPacket;
import fr.kevyn.artisanspath.network.server.DeleteAccountPacket;
import fr.kevyn.artisanspath.network.server.LeaveAccountPacket;
import fr.kevyn.artisanspath.network.server.MenuActionPacket;
import fr.kevyn.artisanspath.network.server.SetCardAccountPacket;
import fr.kevyn.artisanspath.network.server.UpdateAccountPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = ArtisansMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkHandler {
  private NetworkHandler() {}

  @SubscribeEvent
  public static void register(final RegisterPayloadHandlersEvent event) {
    final PayloadRegistrar registrar = event.registrar("1");
    registrar.playToClient(
        UpdatePlayerAccountsPacket.TYPE,
        UpdatePlayerAccountsPacket.STREAM_CODEC,
        UpdatePlayerAccountsPacket::handleOnClient);
    registrar.playToServer(
        ATMWithdrawPacket.TYPE, ATMWithdrawPacket.STREAM_CODEC, ATMWithdrawPacket::handleOnServer);
    registrar.playToServer(
        MenuActionPacket.TYPE, MenuActionPacket.STREAM_CODEC, MenuActionPacket::handleOnServer);
    registrar.playToServer(
        SetCardAccountPacket.TYPE,
        SetCardAccountPacket.STREAM_CODEC,
        SetCardAccountPacket::handleOnServer);
    registrar.playToServer(
        ClearCardAccountPacket.TYPE,
        ClearCardAccountPacket.STREAM_CODEC,
        ClearCardAccountPacket::handleOnServer);
    registrar.playToServer(
        CreateAccountPacket.TYPE,
        CreateAccountPacket.STREAM_CODEC,
        CreateAccountPacket::handleOnServer);
    registrar.playToServer(
        UpdateAccountPacket.TYPE,
        UpdateAccountPacket.STREAM_CODEC,
        UpdateAccountPacket::handleOnServer);
    registrar.playToServer(
        LeaveAccountPacket.TYPE,
        LeaveAccountPacket.STREAM_CODEC,
        LeaveAccountPacket::handleOnServer);
    registrar.playToServer(
        DeleteAccountPacket.TYPE,
        DeleteAccountPacket.STREAM_CODEC,
        DeleteAccountPacket::handleOnServer);
  }
}
