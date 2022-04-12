import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import prj.ClientThread;
import prj.Prj;
import prj.log.Logger;
import prj.net.ClientNetworkManager;
import prj.world.World;

import java.io.IOException;

public class NetTests {

    @Mock
    public ClientThread cth;
    public Logger logger;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        Prj.setupLogfile("Login Test");
        logger = new Logger("Test thread");
    }

    @Test
    public void loginTest() throws IOException, InterruptedException {
        logger.testinfo("begin login test");

        logger.testinfo("initialize world");
        World w = new World(0,10, cth);

        Mockito.when(cth.getWorld()).thenReturn(w);
        Mockito.when(cth.getListenerAddress()).thenReturn(w.getConnectionListener().getListenerAddress());

        logger.testinfo("initialize ClientNetworkManager");
        ClientNetworkManager cnm = new ClientNetworkManager(cth);

        Mockito.when(cth.getNetworkManager()).thenReturn(cnm);

        logger.testinfo("start server instance");
        cnm.startServerInstance();

        Thread.sleep(100);
        Assertions.assertEquals(1, cnm.getServerThread().getNetworkManager().getActiveClients());

        logger.testinfo("shutdown ClientNetworkManager");
        cnm.disconnect();

        logger.testinfo("shutdown world");
        w.getConnectionListener().shutdown();

        logger.testinfo("login test completed");
    }
}
