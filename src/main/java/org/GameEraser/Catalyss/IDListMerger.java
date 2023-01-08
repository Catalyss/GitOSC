package org.GameEraser.Catalyss;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class IDListMerger implements Runnable{
    @Override
    public void run() {
        JFrame frame = new JFrame ("Cache Merger Tool");
        JMenu fileMenu = new JMenu ("File");
        JMenuItem printItem = new JMenuItem ("Print");
        fileMenu.add (printItem);
        JMenuItem exitItem = new JMenuItem ("Exit");
        fileMenu.add (exitItem);
        JMenu helpMenu = new JMenu ("Help");
        JMenuItem contentsItem = new JMenuItem ("Contents");
        helpMenu.add (contentsItem);
        JMenuItem aboutItem = new JMenuItem ("About");
        helpMenu.add (aboutItem);

        JButton jcomp1 = new JButton("Load File");
        JButton jcomp2 = new JButton("Merge on new file");
        JButton jcomp3 = new JButton("Merge on current");
        JTextArea base = new JTextArea(5, 5);
        JScrollPane sp = new JScrollPane(base);
        JTextArea merged = new JTextArea(5, 5);
        JScrollPane sp2 = new JScrollPane(merged);
        JTextField jcomp6 = new JTextField(5);
        JLabel jcomp7 = new JLabel("New file name");
        JLabel jcomp8 = new JLabel("Current list");
        JLabel jcomp9 = new JLabel("List to merge");
        JMenuBar jcomp10 = new JMenuBar();
        jcomp10.add (fileMenu);
        jcomp10.add (helpMenu);

        jcomp1.setToolTipText ("Will try to load IDS.txt if exist");
        jcomp3.setToolTipText ("Will save the file as IDS.txt");

        frame.getContentPane().setPreferredSize (new Dimension (944, 569));
        frame.getContentPane().setLayout (null);

        frame.getContentPane().add (jcomp1);
        frame.getContentPane().add (jcomp2);
        frame.getContentPane().add (jcomp3);
        frame.getContentPane().add (sp);
        frame.getContentPane().add (sp2);
        frame.getContentPane().add (jcomp6);
        frame.getContentPane().add (jcomp7);
        frame.getContentPane().add (jcomp8);
        frame.getContentPane().add (jcomp9);
        frame.getContentPane().add (jcomp10);

        jcomp1.setBounds (92, 10, 100, 25);
        jcomp2.setBounds (395, 85, 151, 20);
        jcomp3.setBounds (400, 60, 140, 20);
        sp.setBounds (10, 40, 295, 495);
        sp2.setBounds (640, 40, 295, 495);
        jcomp6.setBounds (375, 135, 190, 25);
        jcomp7.setBounds (430, 110, 100, 25);
        jcomp8.setBounds (90, 540, 100, 25);
        jcomp9.setBounds (765, 540, 115, 25);
        jcomp10.setBounds (375, 330, 0, 25);

        frame.pack();
        frame.setVisible (true);
        frame.setLocationRelativeTo(null);
        frame.setBackground(Color.GRAY);


        jcomp1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    StringBuilder otherstringBuilder = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new FileReader("IDS.txt"));
                    String line = null;
                    String ls = System.getProperty("line.separator");
                    while ((line = reader.readLine()) != null) {
                        otherstringBuilder.append(line);
                        otherstringBuilder.append(ls);
                    }
                    reader.close();
                    String Base = String.valueOf(otherstringBuilder);
                    base.setText(Base);
                }catch (Exception ev) {
                    ErrorPanel.Senderror(ev.toString());
                }
            }
        });
        jcomp3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder otherstringBuilder = new StringBuilder();
                otherstringBuilder.append(base.getText());
                String ls = System.getProperty("line.separator");

                otherstringBuilder.append(ls);
                otherstringBuilder.append(ls);
                otherstringBuilder.append(ls);

                for (String st:merged.getText().split("\\n")) {
                    if(otherstringBuilder.toString().contains(st)||!st.contains("avtr")){
                        System.out.println(otherstringBuilder.toString().contains(st) +" or "+st.contains("avtr"));
                    }else {
                        System.out.println(st+"\n");
                        otherstringBuilder.append(ls);
                        otherstringBuilder.append(st);
                    }
                }
                FileWriter fwr = null;
                try {
                    fwr = new FileWriter("IDS.txt", false);
                    fwr.append(otherstringBuilder.toString());
                    fwr.close();
                } catch (Exception ev) {
                    ErrorPanel.Senderror(ev.toString());
                }
                merged.setText("");
                try {
                    otherstringBuilder = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new FileReader("IDS.txt"));
                    String line = null;
                    ls = System.getProperty("line.separator");
                    while ((line = reader.readLine()) != null) {
                        otherstringBuilder.append(line);
                        otherstringBuilder.append(ls);
                    }
                    reader.close();
                    String Base = String.valueOf(otherstringBuilder);
                    base.setText(Base);
                }catch (Exception ev) {
                    ErrorPanel.Senderror(ev.toString());
                }
            }
        });
        jcomp2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder otherstringBuilder = new StringBuilder();
                otherstringBuilder.append(base.getText());

                String ls = System.getProperty("line.separator");


                otherstringBuilder.append(ls);
                otherstringBuilder.append(ls);
                otherstringBuilder.append(ls);
                for (String st:merged.getText().split("\\n")) {
                    if(otherstringBuilder.toString().contains(st)||!st.contains("avtr")){
                        System.out.println(otherstringBuilder.toString().contains(st) +" or "+st.contains("avtr"));
                    }else {
                        System.out.println(st+"\n");
                        otherstringBuilder.append(ls);
                        otherstringBuilder.append(st);
                    }
                }
                FileWriter fwr = null;
                String Files = "Saved_on_new_file";
                if(jcomp6.getText()=="" || jcomp6.getText()==null) Files=jcomp6.getText();
                try {
                    fwr = new FileWriter(Files+".txt", false);
                    fwr.append(otherstringBuilder.toString());
                    fwr.close();
                } catch (Exception ev) {
                    ErrorPanel.Senderror(ev.toString());
                }
                merged.setText("");
            }
        });
    }
}
