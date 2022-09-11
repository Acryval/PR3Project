package prj.gamestates;

import prj.Prj;
import prj.net.packet.Packet;
import prj.net.packet.gamestate.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class MenuState extends GameState{
    private final String[] options = {
            "Single Player",
            "Host Server",
            "Connect To Server",
            "Exit"
    };
    private final int numOptions = options.length;

    private Dimension scrRes;
    private int currOption;

    private JTextArea username;
    private JTextArea worldName;
    private JTextArea address;

    @Override
    public void processPackets(List<Packet> dataIn) {
        currOption = 0;
        username = new JTextArea("test user name");
        worldName = new JTextArea("test world name");
        address = new JTextArea("test address");

        GameStateManager.instance.add(username);
        GameStateManager.instance.add(worldName);
        GameStateManager.instance.add(address);
        Prj.instance.setContentPane(GameStateManager.instance);

        for(Packet p : dataIn){
            switch (p.getType()){
                case scrDimension -> scrRes = ((ScreenDimensionPacket)p).getScreenDimension();
                default -> {}
            }
        }
    }

    @Override
    public List<Packet> unload(List<Packet> endData) {
        List<Packet> out = new ArrayList<>();

        out.add(new SetUsernamePacket(username.getText()));
        out.add(new SetWorldNamePacket(worldName.getText()));

        switch (currOption){
            case 1 -> out.add(new StartServerPacket());
            case 2 -> {
                String[] s = address.getText().split(":");
                int port;
                if(s.length < 2){
                    port = 0;
                }else{
                    port = Integer.parseInt(s[1]);
                }
                out.add(new ConnectToServerPacket(new InetSocketAddress(s[0], port)));
            }
            default -> {}
        }

        out.add(new ScreenDimensionPacket(scrRes));

        GameStateManager.instance.remove(username);
        GameStateManager.instance.remove(worldName);
        GameStateManager.instance.remove(address);
        Prj.instance.setContentPane(GameStateManager.instance);

        return out;
    }

    @Override
    public void setActions(InputMap im, ActionMap am) {
        im.put(KeyStroke.getKeyStroke("pressed ESCAPE"), "exit");
        im.put(KeyStroke.getKeyStroke("pressed UP"), "pu");
        im.put(KeyStroke.getKeyStroke("pressed DOWN"), "pd");
        im.put(KeyStroke.getKeyStroke("pressed ENTER"), "select");

        am.put("exit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameStateManager.instance.stop();
            }
        });
        am.put("pu", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currOption--;
                if(currOption < 0){
                    currOption = numOptions-1;
                }
            }
        });
        am.put("pd", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currOption++;
                if(currOption >= numOptions){
                    currOption = 0;
                }
            }
        });
        am.put("select", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currOption == 3) GameStateManager.instance.stop();
                else GameStateManager.instance.setState("client");
            }
        });
    }

    @Override
    public void update(double dt) {

    }

    @Override
    public void draw(Graphics2D g, int width, int height) {

    }
}
