package org.GameEraser.Catalyss;

import javax.sound.midi.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MidiSender {
    public static JFrame frame =new JFrame("Midi Control Panel");
    public static JButton old=new JButton("FUCK~");
    public static int State=3;
    public static int V=0;
    private static boolean pressed = false;
    private static boolean PlaySound = false;

    public static void main(String[] args) throws Exception {
        old.setName("String.valueOf(x*(10)+ y)");
        ShortMessage myMsg = new ShortMessage();
        ShortMessage myMsgs = new ShortMessage();
        // Start playing the note Middle C (60),
        // moderately loud (velocity = 93).
        myMsg.setMessage(ShortMessage.NOTE_ON, 1, 1, 93);
        //76 83 81 76 79 79 81
        long timeStamp = -1;
        Receiver rcvr = MidiSystem.getMidiDevice(MidiSystem.getMidiDeviceInfo()[4]).getReceiver();
        MidiSystem.getMidiDevice(MidiSystem.getMidiDeviceInfo()[4]).open();
        for(int x =0; x<13;x++) {
            for (int y = 0; y < 11; y++) {
                JButton btn = new JButton(String.valueOf(x*(10)+ y));
                btn.setName(String.valueOf(x*(10)+ y));
                int finalX = x;
                int finalY = y;
                btn.getModel().addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        ButtonModel btns = (ButtonModel) e.getSource();
                        if(State==3&&btns.isPressed() != pressed){
                            pressed=btns.isPressed();
                            if(pressed) {
                                try {
                                    rcvr.send(new ShortMessage(ShortMessage.NOTE_ON, 0, (finalX*(10)+ finalY), V),timeStamp);
                                    if(PlaySound)MidiSystem.getReceiver().send(new ShortMessage(ShortMessage.NOTE_ON, 0, (finalX*(10)+ finalY), V),timeStamp);
                                } catch (InvalidMidiDataException ex) {
                                    throw new RuntimeException(ex);
                                } catch (MidiUnavailableException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                            if(!pressed) {
                                try {
                                    rcvr.send(new ShortMessage(ShortMessage.NOTE_OFF, 0, (finalX*(10)+ finalY), V),timeStamp);
                                    if(PlaySound)MidiSystem.getReceiver().send(new ShortMessage(ShortMessage.NOTE_OFF, 0, (finalX*(10)+ finalY), V),timeStamp);
                                } catch (InvalidMidiDataException ex) {
                                    throw new RuntimeException(ex);
                                } catch (MidiUnavailableException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }

                        }
                    }});
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            if(State==0)rcvr.send(new ShortMessage(ShortMessage.NOTE_OFF, 0, (finalX*(10)+ finalY), V),timeStamp);
                            if(State==1)rcvr.send(new ShortMessage(ShortMessage.NOTE_ON, 0, (finalX*(10)+ finalY), V),timeStamp);
                            if(State==2)rcvr.send(new ShortMessage(ShortMessage.CONTROL_CHANGE, 0, (finalX*(10)+ finalY), V),timeStamp);
                        } catch (InvalidMidiDataException ex) {
                            throw new RuntimeException(ex);
                        };
                    }
                });
                btn.setBounds((x*60),(y*60),60,60);

                if(btn.getName().equals(old.getName()))continue;
                if((x*(10)+ y)>127)continue;
                old=btn;
                frame.add(btn);
                frame.setLayout(null);
                frame.setSize(12*60+77,11*60+40);
            }
        }

        JSlider jSlider =new JSlider(SwingConstants.HORIZONTAL,0,127,0);
        JLabel jLabel = new JLabel("Volume : 0");
        JRadioButton State_Off =new JRadioButton("State_Off");
        JRadioButton State_On =new JRadioButton("State_On");
        JRadioButton State_CC =new JRadioButton("State_CC");
        JRadioButton State_Click =new JRadioButton("State_Pressed");
        JRadioButton PlaySounds =new JRadioButton("PlaySound");
        State_Off.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlaySounds.setSelected(false);
                if(State==0)State_Off.setSelected(true);
                State_On.setSelected(false);
                State_CC.setSelected(false);
                State_Click.setSelected(false);
                State=0;
            }
        });
        State_On.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlaySounds.setSelected(false);
                if(State==1)State_On.setSelected(true);
                State_Off.setSelected(false);
                State_CC.setSelected(false);
                State_Click.setSelected(false);
                State=1;
            }
        });
        State_CC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlaySounds.setSelected(false);
                if(State==2)State_CC.setSelected(true);
                State_On.setSelected(false);
                State_Off.setSelected(false);
                State_Click.setSelected(false);
                State=2;
            }
        });
        State_Click.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlaySounds.setSelected(PlaySound);
                if(State==3)State_Click.setSelected(true);
                State_On.setSelected(false);
                State_Off.setSelected(false);
                State_CC.setSelected(false);
                State=3;
            }
        });
        PlaySounds.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(State!=3)PlaySounds.setSelected(false);
                PlaySound=PlaySounds.isSelected();
            }
        });
        jSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                jLabel.setText(String.valueOf("Volume : "+jSlider.getValue()));
                V= jSlider.getValue();
            }
        });

        jSlider.setBounds(60+35,30,11*60,30);
        jLabel.setBounds(60*10,0,11*60,35);
        State_Off.setBounds(60*1,0,80,35);
        State_On.setBounds((int) (60*2.5),0,80,35);
        State_CC.setBounds(60*4,0,80,35);
        State_Click.setBounds((int) (60*5.5),0,110,35);
        PlaySounds.setBounds((int) (60*+7.5),0,110,35);

        State_Click.setSelected(true);
        frame.add(jSlider);
        frame.add(State_Off);
        frame.add(PlaySounds);
        frame.add(State_Click);
        frame.add(State_On);
        frame.add(State_CC);
        frame.add(jLabel);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
