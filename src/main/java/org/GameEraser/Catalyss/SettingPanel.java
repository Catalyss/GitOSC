package org.GameEraser.Catalyss;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;

public class SettingPanel implements Runnable {
    @Override
    public void run() {
        JSONObject Json = new JSONObject();
        try {
            FileReader fr = new FileReader("Settings.json");
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
        JTextField PASSWORD = new JTextField(5);

        frame.getContentPane().setPreferredSize(new Dimension(208, 129));
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
        IP.setText("127.0.0.1");
        PORT.setText("9000");

        if(Json.containsKey("IP")) IP.setText(Json.get("IP").toString());
        if(Json.containsKey("PORT")) PORT.setText(Json.get("PORT").toString());
        if(Json.containsKey("EMAIL")) LOGIN.setText(Json.get("EMAIL").toString());
        if(Json.containsKey("PASSWORD")) PASSWORD.setText(Json.get("PASSWORD").toString());

        jcomp1.setBounds(55, 100, 100, 25);
        IP.setBounds(5, 25, 100, 25);
        jcomp3.setBounds(5, 5, 100, 25);
        PORT.setBounds(5, 75, 100, 25);
        jcomp5.setBounds(5, 50, 100, 25);
        jcomp6.setBounds(105, 5, 100, 25);
        LOGIN.setBounds(105, 25, 100, 25);
        jcomp8.setBounds(105, 50, 105, 25);
        PASSWORD.setBounds(105, 75, 100, 25);
        jcomp1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    OnClose(IP.getText(), PORT.getText(), LOGIN.getText(), PASSWORD.getText());
                    frame.setVisible (false);
                } catch (IOException ex) {
                    ErrorPanel.Senderror(ex.toString());
                }
            }
        });

        frame.pack();
        frame.setVisible (true);
        frame.setLocationRelativeTo(null);
        frame.setBackground(Color.GRAY);
    }

    private void OnClose(String IP,String PORT,String EMAIL,String PASSWORD) throws IOException {
        JSONObject obj =new JSONObject();
        obj.put("IP",IP);
        obj.put("PORT",PORT);
        obj.put("EMAIL",EMAIL);
        obj.put("PASSWORD",PASSWORD);
        FileWriter fwr =new FileWriter("Settings.json");
        fwr.write(obj.toJSONString());
        fwr.close();
        OSCJframe.connect(IP, PORT);
    }
}
