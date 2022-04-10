package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class LoginForm {
    DatabaseFunctions databaseFunctions = new DatabaseFunctions();
    private JFrame frame = new JFrame("Login");;
    private JTextField textFieldLogin;
    private JTextField textFieldPassword;
    private JPanel panel = new JPanel();
    private JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER,30,40));
    private JLabel labelLogin;
    private JLabel labelPassword;
    private final int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    private final int height = Toolkit.getDefaultToolkit().getScreenSize().height;
    private JButton buttonLogin;
    private JButton buttonRegister;

    public LoginForm(){
        initFrame();
        buttonLogin.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    if(checkIfUserExistAndLogin()) {
                        GameFrame frame = new GameFrame();
                    }
                    else {
                        frame.dispose();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttonRegister.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    addUserToDatabase();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                finally{
                    frame.dispose();
                    GameFrame frame = new GameFrame();
                }
            }
        });
    }

    private void initFrame(){
        frame.setResizable(false);
        int frameWidth = frame.getSize().width;
        int frameHeight = frame.getSize().height;
        frame.setBounds(width/3, height/3, width/4, height/4 + 20);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container c = frame.getContentPane();
        panel.setLayout(new GridLayout(3,2,30,45));

        textFieldLogin= new JTextField();
        textFieldLogin.setColumns(10);

        textFieldPassword = new JTextField();
        textFieldPassword.setColumns(10);

        labelLogin = new JLabel("Login:");
        labelPassword = new JLabel("Password:");

        buttonLogin = new JButton("Login");
        buttonRegister = new JButton("Register");
        frame.setVisible(true);
        panel.add(labelLogin);
        panel.add(textFieldLogin);
        panel.add(labelPassword);
        panel.add(textFieldPassword);
        panel.add(buttonLogin);
        panel.add(buttonRegister);
        panel.setVisible(true);
        panel1.add(panel);
        panel1.setVisible(true);
        c.add(panel1);
    }

    public void addUserToDatabase() throws SQLException {
        String login = textFieldLogin.getText();
        String password = textFieldPassword.getText();
        User user = new User(login, password);
        if(!checkIfUserExistsInDatabase(user)){
            databaseFunctions.addUser(user);
        }
        else{
            System.out.println("nazwa zajeta");
        }
    }

    private boolean checkIfUserExistsInDatabase(User user) throws SQLException {
        return databaseFunctions.checkIfUserExistsInDatabase(user);
    }

    public boolean checkIfUserExistAndLogin() throws SQLException {
        String login = textFieldLogin.getText();
        String password = textFieldPassword.getText();
        User user = new User(login, password);
        return checkIfUserExistsInDatabase(user);
    }

}
