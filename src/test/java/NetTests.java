import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import prj.ClientThread;
import prj.Prj;
import prj.net.ClientNetworkManager;
import prj.world.World;

import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

public class NetTests {

    @Mock
    public ClientThread cth;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        Prj.setupLogfile("Login Test");
    }

    @Test
    public void loginTest() throws IOException, InterruptedException {
        World w = new World(0,10, cth);

        Mockito.when(cth.getWorld()).thenReturn(w);
        Mockito.when(cth.getListenerAddress()).thenReturn(w.getConnectionListener().getListenerAddress());

        ClientNetworkManager cnm = new ClientNetworkManager(cth);

        Mockito.when(cth.getNetworkManager()).thenReturn(cnm);

        cnm.startServerInstance();

        Thread.sleep(100);
        Assertions.assertEquals(1, cnm.getServerThread().getNetworkManager().getActiveClients());

        cnm.shutdown();
        w.getConnectionListener().shutdown();
    }
}
