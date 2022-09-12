package prj.gamestates;

import prj.Prj;
import prj.net.packet.Packet;
import prj.net.packet.PacketType;
import prj.net.packet.gamestate.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class MenuState extends GameState{

    private Dimension scrRes;
    private int currOption;

    private JTextArea username;
    private JTextArea worldName;
    private JTextArea address;

    private JLabel usernameLabel;
    private JLabel worldNameLabel;
    private JLabel addressLabel;

    private JButton spButton;
    private JButton hostButton;
    private JButton mpButton;
    private JButton exitButton;

    @Override
    public void processPackets(List<Packet> dataIn) {
        currOption = 0;

        for(Packet p : dataIn){
            if (p.getType() == PacketType.scrDimension) {
                scrRes = ((ScreenDimensionPacket) p).getScreenDimension();
            }
        }

        username = new JTextArea("Username");
        worldName = new JTextArea("New World");
        address = new JTextArea("127.0.0.1:50060");

        usernameLabel = new JLabel("Username:");
        worldNameLabel = new JLabel("World Name:");
        addressLabel = new JLabel("Server Address:");

        spButton = new JButton("Single Player");
        hostButton = new JButton("Host Server");
        mpButton = new JButton("Connect To Server");
        exitButton = new JButton("Exit");

        spButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currOption = 0;
                GameStateManager.instance.setState("client");
            }
        });
        hostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currOption = 1;
                GameStateManager.instance.setState("client");
            }
        });
        mpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currOption = 2;
                GameStateManager.instance.setState("client");
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameStateManager.instance.stop();
            }
        });

        SpringLayout layout = new SpringLayout();

        GameStateManager.instance.setLayout(layout);
        GameStateManager.instance.add(usernameLabel);
        GameStateManager.instance.add(worldNameLabel);
        GameStateManager.instance.add(addressLabel);
        GameStateManager.instance.add(username);
        GameStateManager.instance.add(worldName);
        GameStateManager.instance.add(address);
        GameStateManager.instance.add(spButton);
        GameStateManager.instance.add(hostButton);
        GameStateManager.instance.add(mpButton);
        GameStateManager.instance.add(exitButton);

        layout.putConstraint(SpringLayout.NORTH, username, 300, SpringLayout.NORTH, GameStateManager.instance);
        layout.putConstraint(SpringLayout.WEST, username, scrRes.width/2 + 5, SpringLayout.WEST, GameStateManager.instance);
        layout.putConstraint(SpringLayout.NORTH, usernameLabel, 300, SpringLayout.NORTH, GameStateManager.instance);
        layout.putConstraint(SpringLayout.EAST, usernameLabel, scrRes.width/2 - 5, SpringLayout.WEST, GameStateManager.instance);

        layout.putConstraint(SpringLayout.NORTH, worldName, 320, SpringLayout.NORTH, GameStateManager.instance);
        layout.putConstraint(SpringLayout.WEST, worldName, scrRes.width/2 + 5, SpringLayout.WEST, GameStateManager.instance);
        layout.putConstraint(SpringLayout.NORTH, worldNameLabel, 320, SpringLayout.NORTH, GameStateManager.instance);
        layout.putConstraint(SpringLayout.EAST, worldNameLabel, scrRes.width/2 - 5, SpringLayout.WEST, GameStateManager.instance);

        layout.putConstraint(SpringLayout.NORTH, address, 340, SpringLayout.NORTH, GameStateManager.instance);
        layout.putConstraint(SpringLayout.WEST, address, scrRes.width/2 + 5, SpringLayout.WEST, GameStateManager.instance);
        layout.putConstraint(SpringLayout.NORTH, addressLabel, 340, SpringLayout.NORTH, GameStateManager.instance);
        layout.putConstraint(SpringLayout.EAST, addressLabel, scrRes.width/2 - 5, SpringLayout.WEST, GameStateManager.instance);

        layout.putConstraint(SpringLayout.NORTH, spButton, 400, SpringLayout.NORTH, GameStateManager.instance);
        layout.putConstraint(SpringLayout.NORTH, hostButton, 430, SpringLayout.NORTH, GameStateManager.instance);
        layout.putConstraint(SpringLayout.NORTH, mpButton, 460, SpringLayout.NORTH, GameStateManager.instance);
        layout.putConstraint(SpringLayout.NORTH, exitButton, 490, SpringLayout.NORTH, GameStateManager.instance);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, spButton, scrRes.width/2, SpringLayout.WEST, GameStateManager.instance);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, hostButton, scrRes.width/2, SpringLayout.WEST, GameStateManager.instance);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, mpButton, scrRes.width/2, SpringLayout.WEST, GameStateManager.instance);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, exitButton, scrRes.width/2, SpringLayout.WEST, GameStateManager.instance);

        Prj.instance.setContentPane(GameStateManager.instance);
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
        GameStateManager.instance.remove(usernameLabel);
        GameStateManager.instance.remove(worldNameLabel);
        GameStateManager.instance.remove(addressLabel);
        GameStateManager.instance.remove(spButton);
        GameStateManager.instance.remove(hostButton);
        GameStateManager.instance.remove(mpButton);
        GameStateManager.instance.remove(exitButton);
        Prj.instance.setContentPane(GameStateManager.instance);

        return out;
    }

    @Override
    public void setActions(InputMap im, ActionMap am) {
        im.put(KeyStroke.getKeyStroke("pressed ESCAPE"), "exit");
        am.put("exit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameStateManager.instance.stop();
            }
        });
    }

    @Override
    public void update(double dt) {}

    @Override
    public void draw(Graphics2D g, int width, int height) {}
}
