package usa.cactuspuppy.nccustom.command.subcmd;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import usa.cactuspuppy.nccustom.Main;

import java.net.InetAddress;

import static org.junit.Assert.*;

public class JoinLeaveTest {

    @Test
    public void onPlayerJoin() {
        JoinLeave.setAllowMessages(false);
        Player mockPlayer = Mockito.mock(Player.class);
        PlayerJoinEvent event = new PlayerJoinEvent(mockPlayer, "mockPlayer has joined the game");

        JoinLeave.onPlayerJoin(event);
        assertNotSame("mockPlayer has joined the game", event.getJoinMessage());
        assertEquals("", event.getJoinMessage());
    }

    @Test
    public void onPlayerQuit() {
        JoinLeave.setAllowMessages(false);
        Player mockPlayer = Mockito.mock(Player.class);
        PlayerQuitEvent event = new PlayerQuitEvent(mockPlayer, "mockPlayer has left the game");

        JoinLeave.onPlayerQuit(event);
        assertNotSame("mockPlayer has left the game", event.getQuitMessage());
        assertEquals("", event.getQuitMessage());
    }
}