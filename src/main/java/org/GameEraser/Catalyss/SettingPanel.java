package org.GameEraser.Catalyss;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URISyntaxException;
import javax.swing.*;

public class SettingPanel implements Runnable {
    @Override
    public void run() {
        JSONObject Json = new JSONObject();
        try {
            FileReader fr = new FileReader("src/resources/Settings.json");
            JSONParser parser = new JSONParser();
            Json = (JSONObject) parser.parse(fr);
        } catch (Exception ignored) {

        }
        JFrame frame = new JFrame("Settings");

        JButton jcomp1 = new JButton("Confirm");
        JTextField IP = new JTextField(5);
        JLabel jcomp3 = new JLabel("OSC IP");
        JTextField PORT = new JTextField(5);
        JLabel jcomp5 = new JLabel("OSC PORT");
        JLabel jcomp6 = new JLabel("VRChat Login");
        JTextField LOGIN = new JTextField(5);
        JLabel jcomp8 = new JLabel("VRChat Password");
        JLabel Other = new JLabel("VRChat Password");
        JPasswordField PASSWORD = new JPasswordField(5);

        JLabel OtherLabel = new JLabel("Other Settings");
        JLabel Minesweeper = new JLabel("Mine                BoardX              BoardY");
        JTextField Mine = new JTextField(5);
        JTextField BoardX = new JTextField(5);
        JTextField BoardY = new JTextField(5);

        frame.getContentPane().setPreferredSize(new Dimension(208, 204));
        frame.getContentPane().setLayout(null);

        frame.getContentPane().add(jcomp1);
        frame.getContentPane().add(IP);
        frame.getContentPane().add(jcomp3);
        frame.getContentPane().add(PORT);
        frame.getContentPane().add(jcomp5);
        frame.getContentPane().add(jcomp6);
        frame.getContentPane().add(LOGIN);
        frame.getContentPane().add(jcomp8);
        frame.getContentPane().add(PASSWORD);
        frame.getContentPane().add(OtherLabel);
        frame.getContentPane().add(Mine);
        frame.getContentPane().add(BoardY);
        frame.getContentPane().add(BoardX);
        frame.getContentPane().add(Minesweeper);
        IP.setText("127.0.0.1");
        PORT.setText("9000");
        try {
            if (Json.containsKey("IP")) IP.setText(Json.get("IP").toString());
            if (Json.containsKey("PORT")) PORT.setText(Json.get("PORT").toString());
            if (Json.containsKey("EMAIL")) LOGIN.setText(Json.get("EMAIL").toString());
            if (Json.containsKey("PASSWORD")) PASSWORD.setText(Json.get("PASSWORD").toString());
            if (Json.containsKey("MINE")) Mine.setText(Json.get("MINE").toString());
            if (Json.containsKey("BOARDX")) BoardX.setText(Json.get("BOARDX").toString());
            if (Json.containsKey("BOARDY")) BoardY.setText(Json.get("BOARDY").toString());
        }catch (Exception ignored){}

        jcomp1.setBounds(55, 175, 100, 25);
        IP.setBounds(5, 25, 100, 25);
        jcomp3.setBounds(5, 5, 100, 25);
        PORT.setBounds(5, 75, 100, 25);
        jcomp5.setBounds(5, 50, 100, 25);
        jcomp6.setBounds(105, 5, 100, 25);
        LOGIN.setBounds(105, 25, 100, 25);
        jcomp8.setBounds(105, 50, 105, 25);
        PASSWORD.setBounds(105, 75, 100, 25);

        OtherLabel.setBounds(55, 100, 100, 25);
        Minesweeper.setBounds(5, 125, 208, 25);
        Mine.setBounds(5, 150, 35, 25);
        BoardX.setBounds(5+82, 150, 35, 25);
        BoardY.setBounds(5+82+82, 150, 35, 25);
        jcomp1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    OnClose(IP.getText(), PORT.getText(), LOGIN.getText(), PASSWORD.getText(),BoardY.getText(), BoardX.getText(),Mine.getText());
                    frame.setVisible (false);
                } catch (Exception ex) {
                    ErrorPanel.Senderror(ex.toString());
                }
            }
        });

        frame.pack();
        frame.setVisible (true);
        frame.setLocationRelativeTo(null);
        frame.setBackground(Color.GRAY);
    }

    private void OnClose(String IP,String PORT,String EMAIL,String PASSWORD,String BoardY,String BoardX,String Mine) throws IOException, URISyntaxException {
        JSONObject obj =new JSONObject();
        obj.put("IP",IP);
        obj.put("PORT",PORT);
        obj.put("EMAIL",EMAIL);
        obj.put("PASSWORD",PASSWORD);
        obj.put("BOARDY",BoardY);
        obj.put("BOARDX",BoardX);
        obj.put("MINE",Mine);
        FileWriter fwr =new FileWriter("src/resources/Settings.json");
        fwr.write(obj.toJSONString());
        fwr.close();
        OSCJframe.connect(IP, PORT);
    }
}
