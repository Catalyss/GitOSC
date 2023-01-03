package org.GameEraser.Catalyss;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ErrorPanel {

    public static void Senderror(String Error) {
        JFrame frame = new JFrame("Error");
        JButton jcomp1 = new JButton("Ok");
        JLabel jcomp2 = new JLabel(Error, SwingConstants.CENTER);

        frame.getContentPane().setPreferredSize(new Dimension(322, 84));
        frame.getContentPane().setLayout(null);
        frame.getContentPane().add(jcomp1);
        frame.getContentPane().add(jcomp2);

        jcomp1.setBounds(110, 60, 100, 20);
        jcomp2.setBounds(0, 20, 322, 30);
        jcomp1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        });

        frame.pack();
        frame.setBackground(Color.GRAY);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
