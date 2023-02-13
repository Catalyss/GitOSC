package org.GameEraser.Catalyss;


import com.illposed.osc.*;
import com.illposed.osc.transport.OSCPortIn;
import com.illposed.osc.transport.OSCPortInBuilder;
import com.illposed.osc.transport.OSCPortOut;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;

public class Main {
    private static OSCPortOut oscPortOut;
    private static boolean connected;

    public static void main(String[] args) throws IOException, InterruptedException, OSCSerializeException {
        OSCJframe.main(args);
        if(1==1) return;
        JFrame window = new JFrame("A");
        window.setSize(500, 500);
        window.setResizable(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        final int[] r = {0};
        final int[] g = {0};
        final int[] b = {0};
        OSCMessageListener messageListener = new OSCMessageListener() {
            @Override
            public void acceptMessage(OSCMessageEvent event) {
                System.out.println(event.getMessage().getAddress()+"  "+event.getMessage().getArguments());
                if(event.getMessage().getAddress().contains("OBS")){
                    if(event.getMessage().getAddress().contains("r")) r[0] =(int) event.getMessage().getArguments().get(0);
                    if(event.getMessage().getAddress().contains("g")) g[0] =(int) event.getMessage().getArguments().get(0);
                    if(event.getMessage().getAddress().contains("b")) b[0] =(int) event.getMessage().getArguments().get(0);
                    if(event.getMessage().getAddress().contains("s")) b[0] =(int) event.getMessage().getArguments().get(0);
                    System.out.println(r[0]);
                    System.out.println(g[0]);
                    System.out.println(b[0]);
                    Color bg= new Color(r[0], g[0], b[0]);
                    window.getContentPane().setBackground(bg);
                    window.getContentPane().setForeground(bg);
                }else{
                   connect("9000");
                    try {
                        oscPortOut.send(new OSCMessage(event.getMessage().getAddress(),event.getMessage().getArguments()));
                    } catch (Exception ignored) {}
                }

            }
        };
        OSCPortInBuilder port1 = new OSCPortInBuilder();
        String port = "2004";
        port1.setPort(Integer.parseInt(port));
        OSCPortIn oscPortIn = port1.build();
        MessageSelector messageSelector = new MessageSelector() {
            @Override
            public boolean isInfoRequired() {
                return false;
            }

            @Override
            public boolean matches(OSCMessageEvent oscMessageEvent) {
                return true;
            }
        };
        port1.addMessageListener(messageSelector, messageListener);
        oscPortIn.startListening();


    }
    private static boolean connect(String port) {
        InetAddress addr;
        try {
            addr = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e1) {
            ErrorPanel.Senderror("using default err:" + e1.toString());
            System.out.println(connect( "9000"));
            return false;
        }
        // InetAddress addr = InetAddress.getByName("192.168.100.155");
        // OSCPortOut oscPortOut = new OSCPortOut();
        try {
            oscPortOut = new OSCPortOut(addr, Integer.parseInt(port));
            connected = true;
        } catch (NumberFormatException | IOException e1) {
            ErrorPanel.Senderror("using default err:" + e1.toString());
            System.out.println(connect( "9000"));
            connected = false;
            return false;
        }
        int i = 1;
        Object[] args = new Object[1];
        return true;
    }
}