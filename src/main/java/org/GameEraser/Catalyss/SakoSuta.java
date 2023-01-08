package org.GameEraser.Catalyss;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import javax.swing.*;
public class SakoSuta {
    private static String Username=null;
    private static JPanel Names() {
        JPanel pane = new JPanel();
        //construct components
        JButton jcomp1 = new JButton("Confirm");
        JTextField jcomp2 = new JTextField(5);
        JLabel jcomp3 = new JLabel("What's your name ?");

        //adjust size and set layout
        pane.setPreferredSize(new Dimension(248, 115));
        pane.setLayout(null);

        //add components
        pane.add(jcomp1);
        pane.add(jcomp2);
        pane.add(jcomp3);

        //set component bounds (only needed by Absolute Positioning)
        jcomp1.setBounds(70, 80, 100, 25);
        jcomp2.setBounds(30, 45, 180, 25);
        jcomp3.setBounds(65, 15, 125, 25);

        jcomp1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Username= jcomp2.getText();
                System.out.println(Username);
                frame.getContentPane().add(Age());
            }
        });
        return pane;
    }
    private static JPanel Age() {

        JPanel panel =new JPanel();
        //construct preComponents
        String[] MONTHItems = {"January", "Febuary", "March", "April", "May ", "June", "July", "August", "September", "October", "November", "December"};
        String[] DAYItems = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
        List ls = new List();
        for(int i = LocalDateTime.now().getYear();i > LocalDateTime.now().getYear()-100;i--){
            ls.add(String.valueOf(i));
        }

        //construct components
        JButton jcomp1 = new JButton ("Confirm");
        JLabel jcomp2 = new JLabel ("What's your Birthday ?");
        JComboBox MONTH = new JComboBox (MONTHItems);
        JComboBox DAY = new JComboBox (DAYItems);
        JComboBox YEAR = new JComboBox (ls.getItems());

        //adjust size and set layout
        panel.setPreferredSize (new Dimension (248, 115));
        panel.setLayout (null);

        //add components
        panel.add (jcomp1);
        panel.add (jcomp2);
        panel.add (MONTH);
        panel.add (DAY);
        panel.add (YEAR);

        //set component bounds (only needed by Absolute Positioning)
        jcomp1.setBounds (70, 80, 100, 25);
        jcomp2.setBounds (60, 15, 130, 25);
        MONTH.setBounds (5, 50, 100, 25);
        DAY.setBounds (105, 50, 60, 25);
        YEAR.setBounds (165, 50, 80, 25);

        jcomp1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date date1;
                try {
                    date1=new SimpleDateFormat("dd/MM/yyyy").parse(DAY.getSelectedIndex()+"/"+MONTH.getSelectedIndex()+"/"+ (Arrays.toString(YEAR.getSelectedObjects())).replace("[","").replace("]",""));
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
                LocalDate localDate = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int Years = LocalDateTime.now().getYear()-localDate.getYear();
                int Month = localDate.getMonthValue()-LocalDateTime.now().getMonthValue();
                int Day = localDate.getDayOfYear()-LocalDateTime.now().getDayOfYear();
                frame.getContentPane().add(Finakl(Username+" is "+Years+"y "+Month+"m "+Day+"d old"));
                Username="null;";
            }
        });

        return panel;
    }
    private static JPanel Finakl(String age) {
        JPanel panel = new JPanel();
        //construct components
        JButton jcomp1 = new JButton ("Close");
        JLabel jcomp2 = new JLabel (age);

        //adjust size and set layout
        panel.setPreferredSize (new Dimension (248, 115));
        panel.setLayout (null);

        //add components
        panel.add (jcomp1);
        panel.add (jcomp2);

        //set component bounds (only needed by Absolute Positioning)
        jcomp1.setBounds (70, 80, 100, 25);
        jcomp2.setBounds (30, 5, 180, 65);
        jcomp1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        return panel;
    }
    private static JFrame frame;
    public static void main(String[] args) {
        frame = new JFrame("MyPanel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(Names());
        frame.pack();
        frame.setVisible(true);
        // New timer which works!
        int delay = 100; //milliseconds
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String date = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date(System.currentTimeMillis()));
                if(Username!=null)frame.getContentPane().getComponent(0).setVisible(false);
                if(Username=="null;")frame.getContentPane().getComponent(1).setVisible(false);
            }
        };
        new Timer(delay, taskPerformer).start();
    }
}
