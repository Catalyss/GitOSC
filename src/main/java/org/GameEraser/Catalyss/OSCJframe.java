package org.GameEraser.Catalyss;

import com.illposed.osc.*;
import com.illposed.osc.transport.OSCPortIn;
import com.illposed.osc.transport.OSCPortInBuilder;
import com.illposed.osc.transport.OSCPortOut;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

import static java.lang.Thread.sleep;

public class OSCJframe implements Runnable {
    private static boolean AFKMODE = false;
    public static OSCPortOut oscPortOut;
    public static boolean connected;
    public static boolean QuestModeB;
    public static String port = "9000";
    public static String adress = "127.0.0.1";
    public static String CurrentAvatarID;

    public static ArrayList<String> parmameterName = new ArrayList<String>();
    public static ArrayList<String> parmameterType = new ArrayList<String>();
    public static ArrayList<String> parmameterValue = new ArrayList<String>();
    public static JFrame window;
    public static JPanel pane;
    public static String[] CustomMessage = new String[]{"Socket connected", "Socket Heartbeat import || Rate 0/8 || Latency " + new Random().nextInt(5) + "ms || nearest = server.vrchat.eu || Connected on 82.34.54.120 || Debug true", "Socket Heartbeat output || Rate 4/8 || Latency  " + new Random().nextInt(5) + "ms || nearest = server.vrchat.eu || Connected on 82.34.54.120 || Debug true", "Socket Disconnected"};
    public static int Posy;
    public static int Posx;
    public static Boolean SocketMessage = false;
    public static Boolean CustomSocketMessage = false;
    public static LocalDateTime afkTime = null;
    public static LocalDateTime GameStarted = null;
    private static boolean AFKMessages = false;

    public static void Send() {

    }

    public static void ReadSettings() {
        SocketMessage = false;
        CustomSocketMessage = false;
        AFKMessages = false;

    }

    public static void main(String[] args) throws IOException, InterruptedException, OSCSerializeException {
        GameStarted = LocalDateTime.now();
        ReadSettings();

        Thread thread = new Thread(new OSCJframe());
        thread.start();
        System.out.println("Is connected " + connect("127.0.0.1", "9000"));
        oscPortOut.send(new OSCMessage("/avatar/parameters/_locked", Collections.singletonList(false)));

        OSCPortInBuilder port1 = new OSCPortInBuilder();
        port1.setPort(Integer.parseInt(port) + 1);
        OSCPortIn oscPortIn = port1.build();
        OSCMessageListener messageListener = new OSCMessageListener() {
            @Override
            public void acceptMessage(OSCMessageEvent oscMessageEvent) {
                try {
                    MessageRecived(oscMessageEvent.getMessage().getAddress(), oscMessageEvent.getMessage().getArguments(), oscMessageEvent.getMessage().getInfo());
                } catch (OSCSerializeException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        };
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

        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JButton JButton = new JButton("Midi");
        JButton menuSetting = new JButton("Settings");
        JMenuItem menuItemExit = new JMenuItem("Load Current");
        JMenu menuIDTOOL = new JMenu("Cache Tool");
        JMenuItem menuItemCacheCleaner = new JMenuItem("Cache cleaner");
        JMenuItem menuItemIDListMerger = new JMenuItem("ID Merger");
        JMenuItem menuItemInfoReader = new JMenuItem("ID Reader");
        menuFile.add(menuItemExit);
        MenuScroller.setScrollerFor(menuFile);
        menuItemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (CurrentAvatarID != null) AviChange(null, null);
                } catch (OSCSerializeException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        menuFile.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                menuFile.removeAll();
                menuFile.add(menuItemExit);
                File folder = new File(System.getProperty("user.home") + "\\AppData\\LocalLow\\VRChat\\VRChat\\OSC");
                File[] listOfFiles = folder.listFiles();
                File OSCFile = null;
                for (int i = 0; i < listOfFiles.length; i++) {
                    if (listOfFiles[i].isFile()) {
                    } else if (listOfFiles[i].isDirectory()) {
                        File folders = new File(System.getProperty("user.home") + "\\AppData\\LocalLow\\VRChat\\VRChat\\OSC\\" + listOfFiles[i].getName() + "\\Avatars");
                        File[] listOfFiless = folders.listFiles();

                        for (int is = 0; is < listOfFiless.length; is++) {
                            if (listOfFiless[is].isFile()) {

                                BufferedReader reader = null;
                                try {
                                    reader = new BufferedReader(new FileReader(listOfFiless[is]));
                                } catch (FileNotFoundException ex) {
                                    throw new RuntimeException(ex);
                                }
                                StringBuilder stringBuilder = new StringBuilder();
                                String line = null;
                                String ls = System.getProperty("line.separator");
                                while (true) {
                                    try {
                                        if (!((line = reader.readLine()) != null)) break;
                                    } catch (IOException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                    stringBuilder.append(line);
                                    stringBuilder.append(ls);
                                }
                                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                                try {
                                    reader.close();
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                                String jsonString = stringBuilder.toString();
                                int iss = jsonString.indexOf("{");
                                jsonString = jsonString.substring(iss);
                                JSONObject obj = new JSONObject(jsonString.trim());

                                String id = obj.getString("id");
                                String name = obj.getString("name");

                                JMenuItem menueavi = new JMenuItem(name);
                                menuFile.add(menueavi);
                                menueavi.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        CurrentAvatarID = id;
                                        try {
                                            if (CurrentAvatarID != null) AviChange(null, null);
                                        } catch (OSCSerializeException ex) {
                                            throw new RuntimeException(ex);
                                        } catch (IOException ex) {
                                            throw new RuntimeException(ex);
                                        } catch (ParseException ex) {
                                            throw new RuntimeException(ex);
                                        }
                                    }
                                });
                            } else if (listOfFiless[i].isDirectory()) {
                            }
                        }
                    }
                }
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });
        JButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MidiSender.main(args);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        //JButton menuChat = new JButton("ChatBox");
        /*menuChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ///chatbox/input
                JFrame ChatBox = new JFrame("ChatBox");
                ChatBox.setSize(485 * 2, 485);
                ChatBox.add(new JTextField());
                JPanel panel = new JPanel();
                //construct preComponents
                String[] jcomp3Items = {"Item 3", "Item 33333333333", "Item 3"};

                //construct components
                JTextField jcomp1 = new JTextField(5);
                JButton jcomp2 = new JButton("newButton");
                JList jcomp3 = new JList(jcomp3Items);

                //adjust size and set layout
                panel.setPreferredSize(new Dimension(944, 569));
                BorderLayout layout = new BorderLayout(0, 0);
                panel.setLayout(layout);

                //add components
                panel.add(jcomp1, BorderLayout.CENTER);
                panel.add(jcomp2, BorderLayout.SOUTH);
                panel.add(jcomp3, BorderLayout.WEST);
                panel.setBackground(Color.DARK_GRAY);
                ChatBox.setBackground(Color.DARK_GRAY);

                jcomp3.setBackground(Color.DARK_GRAY);
                jcomp2.setBackground(Color.DARK_GRAY);
                jcomp1.setBackground(Color.DARK_GRAY);

                panel.setForeground(Color.LIGHT_GRAY);
                ChatBox.setForeground(Color.LIGHT_GRAY);
                jcomp3.setForeground(Color.LIGHT_GRAY);
                jcomp2.setForeground(Color.LIGHT_GRAY);
                jcomp1.setForeground(Color.LIGHT_GRAY);
                ChatBox.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                ChatBox.getContentPane().add(panel);
                ChatBox.pack();
                ChatBox.setVisible(true);
                ChatBox.setLocationRelativeTo(null);

            }
        });

         */
        menuIDTOOL.add(menuItemCacheCleaner);
        menuIDTOOL.add(menuItemIDListMerger);
        menuIDTOOL.add(menuItemInfoReader);
        menuItemCacheCleaner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread T3 = new Thread(new Extractor());
                T3.start();
            }
        });
        menuItemIDListMerger.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread t =new Thread(new IDListMerger());
                t.run();
            }
        });
        menuItemInfoReader.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ErrorPanel.Senderror("Not Implemented");
            }
        });

        menuSetting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JPanel panel = new JPanel();
                JRadioButton QuestMode = new JRadioButton("Quest Mode");
                JLabel IpLab = new JLabel("Ip");
                JTextField Ip = new JTextField("127.0.0.1");
                JLabel portoutLAB = new JLabel("Port");
                JTextField portout = new JTextField("9000");
                JButton reload = new JButton("Reconnect");
                QuestMode.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent ev) {
                        if (ev.getStateChange() == ItemEvent.SELECTED) {
                            QuestModeB = true;
                        } else if (ev.getStateChange() == ItemEvent.DESELECTED) {
                            QuestModeB = false;
                        }
                    }
                });
                JButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            oscPortOut.disconnect();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        Main.connect(Ip.getText(), portout.getText());
                    }
                });
                panel.add(QuestMode);
                panel.add(IpLab);
                panel.add(Ip);
                panel.add(portoutLAB);
                panel.add(portout);
                panel.add(reload);

                //adjust size and set layout
                panel.setPreferredSize(new Dimension(944, 569));
                BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
                panel.setLayout(layout);

                JFrame frame = new JFrame("Settings");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.add(panel);
                frame.pack();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }
        });


        menuBar.add(menuFile);
        menuBar.add(menuIDTOOL);
        //menuBar.add(menuChat);
        menuBar.add(menuSetting);
        menuBar.add(JButton);
        window = new JFrame("VRChat tool by Catalyss");
        pane = new JPanel();
        pane.setOpaque(true);
        pane.setBackground(Color.GRAY);
        window.add(pane).setBounds(0, 0, 200, 800);
        window.setJMenuBar(menuBar);
        window.setSize(430, 485);
        window.setResizable(true);
        window.setDefaultCloseOperation(OnClose());
        window.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    if (SocketMessage)
                        oscPortOut.send(new OSCMessage("/chatbox/input", Collections.singletonList(getSocketMessage(3))));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (OSCSerializeException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        window.setBackground(Color.GRAY);
    }

    public static int OnClose() throws OSCSerializeException, IOException {
        if (SocketMessage)
            oscPortOut.send(new OSCMessage("/chatbox/input", Collections.singletonList(getSocketMessage(0))));
        return JFrame.EXIT_ON_CLOSE;
    }

    public static String getSocketMessage(int I) {
        String[] DefaultSocketMessages = new String[]{"Socket connected", "Socket Heartbeat import || Rate 0/8 || Latency " + new Random().nextInt(5) + "ms || nearest = server.vrchat.eu || Connected on 82.34.54.120 || Debug true", "Socket Heartbeat output || Rate 4/8 || Latency  " + new Random().nextInt(5) + "ms || nearest = server.vrchat.eu || Connected on 82.34.54.120 || Debug true", "Socket Disconnected"};
        if (!CustomSocketMessage && (!AFKMODE)) {
            afkTime = null;
            return DefaultSocketMessages[I];
        }
        if (AFKMODE && AFKMessages) {
            if (afkTime == null) afkTime = LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm:ss");
            Duration diff = Duration.between(afkTime, LocalDateTime.now());
            String str = String.format("%d:%02d:%02d",
                    diff.toHours(),
                    diff.toMinutesPart(),
                    diff.toSecondsPart());
            String[] CustomMessage = new String[]{"Script Loaded", " Currently AFK since " + "|" + dtf.format(afkTime) + " CEST| ", "AFK Time " + str, "Script Unloaded"};
            return CustomMessage[I];
        }
        if (CustomMessage[I] == "" || CustomMessage[I] == null) {
            afkTime = null;
            return DefaultSocketMessages[I];
        }
        return CustomMessage[I];
    }

    public static void MessageRecived(String Address, List<Object> Value, OSCMessageInfo info) throws OSCSerializeException, IOException, ParseException {
        /*boolean IsDefault = false;
        String[] DefaultParm =new String[]{"Viseme","Voice","GestureLeft","GestureLeftWeight","GestureRight","GestureRightWeight","TrackingType","VRMode","MuteSelf","Earmuffs","Grounded","AngularY","Upright","AFK","Seated","InStation","VelocityX","VelocityY","VelocityZ","IsLocal","AvatarVersion","VRCEmote","VRCFaceBlendH","VRCFaceBlendV"};
        String[] TypeList =new String[]{"i","T","F","f","s"};
        for(String str:DefaultParm){
            if (str.equals(Address.replace("/avatar/parameters/", ""))) {
                IsDefault = true;
                break;
            }
        }
        if(IsDefault)return;
        if(!parmameterName.contains(adress))parmameterName.add(Address);
        parmameterValue.add(String.valueOf(Value.get(0)));
        parmameterType.add((String) info.getArgumentTypeTags());
        System.out.println(Address+" "+ Value+" "+info.getArgumentTypeTags());
         */
        if (Address.equals("/avatar/parameters/AFK")) {
            if (Value.get(0).toString().equals("true")) AFKMODE = true;
            else AFKMODE = false;
        }
        if (Address.equals("/avatar/change")) {
            CurrentAvatarID = (String) Value.get(0);
            System.out.println(CurrentAvatarID);
            AviChange(Address, info);
        }
    }

    public static void AviChange(String Address, OSCMessageInfo info) throws OSCSerializeException, IOException, ParseException {
        Posy = 0;
        Posx = 0;
        pane.removeAll();
        pane.updateUI();

        pane.setBackground(Color.GRAY);

        oscPortOut.send(new OSCMessage("/avatar/parameters/_locked", Collections.singletonList(false)));
        if (QuestModeB) {
        /*for(int i =0;i<parmameterType.toArray().length;i++){
            if(parmameterType.get(i).equals("T")) {
                JRadioButton radioButton=new JRadioButton(parmameterName.get(i));
                radioButton.setEnabled(true);
                pane.add(radioButton).setBounds(20,(20*(i+1))+40,500,20);
            }
            if(parmameterType.get(i).equals("F")) {
                JRadioButton radioButton=new JRadioButton(parmameterName.get(i));
                radioButton.setEnabled(false);
                pane.add(radioButton).setBounds(20,(20*(i+1))+40,500,20);
            }
            if(parmameterType.get(i).equals("i")||parmameterType.get(i).equals("s")) {
                JLabel label =new JLabel(parmameterName.get(i));
                JTextField textField =new JTextField();
                textField.setText(parmameterValue.get(i));
                pane.add(textField).setBounds(20,(20*(i+1))+40,50,20);
                pane.add(label).setBounds(20+55,(20*(i+1))+40,500,20);
            }
            if(parmameterType.get(i).equals("f")) {
                JSlider slider = new JSlider();
                slider.setOrientation(SwingConstants.HORIZONTAL);
                JLabel label =new JLabel(parmameterName.get(i));
                slider.setMaximum(100);
                slider.setMinimum(0);
                pane.add(slider).setBounds(20,(20*(i+1))+40,250,20);
                pane.add(label).setBounds(20+255,(20*(i+1))+40,245,20);
            }
        }
         */
            JSONObject Parm = new JSONObject();
            JSONObject Input = new JSONObject();
            Input.append("address", Address);
            Input.append("type", info.getArgumentTypeTags());
            Parm.append("Input", Input);
            System.out.println(Address + "  " + info.getArgumentTypeTags());
            ParameterWindow(Parm);
        }
        if (!QuestModeB) {

            File folder = new File(System.getProperty("user.home") + "\\AppData\\LocalLow\\VRChat\\VRChat\\OSC");
            File[] listOfFiles = folder.listFiles();
            File OSCFile = null;
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                } else if (listOfFiles[i].isDirectory()) {

                    File folders = new File(System.getProperty("user.home") + "\\AppData\\LocalLow\\VRChat\\VRChat\\OSC\\" + listOfFiles[i].getName() + "\\Avatars");
                    File[] listOfFiless = folders.listFiles();

                    for (int is = 0; is < listOfFiless.length; is++) {
                        if (listOfFiless[is].isFile()) {
                            if (listOfFiless[is].getName().contains(CurrentAvatarID)) OSCFile = listOfFiless[is];
                        } else if (listOfFiless[is].isDirectory()) {
                        }
                    }
                }
            }

            assert OSCFile != null;
            if (OSCFile == null || !OSCFile.exists()) return;
            BufferedReader reader = new BufferedReader(new FileReader(OSCFile));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            String ls = System.getProperty("line.separator");
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            reader.close();
            String jsonString = stringBuilder.toString();
            int i = jsonString.indexOf("{");
            jsonString = jsonString.substring(i);
            JSONObject obj = new JSONObject(jsonString.trim());

            String id = obj.getString("id");
            String name = obj.getString("name");
            JSONArray parameters = obj.getJSONArray("parameters");
            System.out.println(id + "  " + name);
            for (Object parameter : parameters) {
                ParameterWindow((JSONObject) parameter);
            }
        }
    }

    //"C:/Users/liamd/AppData/LocalLow/VRChat/VRChat/OSC/usr_0f13f3c9-e6d2-4f4b-99ba-74b870ceac44/Avatars/avtr_f2ffa90b-b73a-4d0a-9ce4-668b20e6f015.json"

    public static void ParameterWindow(JSONObject check) throws OSCSerializeException, IOException {

        oscPortOut.send(new OSCMessage("/avatar/parameters/_locked", Collections.singletonList(false)));
        if (!check.has("input")) return;
        JPanel Box = new JPanel();
        Box.setBackground(Color.DARK_GRAY);
        pane.setBackground(Color.GRAY);
        pane.setLayout(null);
        Box.setLayout(null);
        Border blackline = BorderFactory.createLineBorder(Color.black);
        Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border raisedbevel = BorderFactory.createRaisedBevelBorder();
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();
        Border empty = BorderFactory.createEmptyBorder();

        Border compound;

//This creates a nice frame.
        compound = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
        Box.setBorder(compound);
        JSONObject input = (JSONObject) check.get("input");
        String type = input.getString("type");

        if (type.equals("Bool")) {
            JRadioButton jButton = new JRadioButton(String.valueOf(check.get("name")).replace(" ", "_"));
            jButton.setBackground(Color.DARK_GRAY);
            jButton.setForeground(Color.LIGHT_GRAY);
            jButton.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent ev) {
                    if (ev.getStateChange() == ItemEvent.SELECTED) {
                        try {
                            oscPortOut.send(new OSCMessage("/avatar/parameters/" + String.valueOf(check.get("name")).replace(" ", "_"), Collections.singletonList(true)));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (OSCSerializeException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (ev.getStateChange() == ItemEvent.DESELECTED) {
                        try {
                            oscPortOut.send(new OSCMessage("/avatar/parameters/" + String.valueOf(check.get("name")).replace(" ", "_"), Collections.singletonList(false)));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (OSCSerializeException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }
            });

            pane.add(jButton).setBounds(Posx, Posy, 205, 30);
            Posy = Posy + 30;
            if (Posy >= 400) {
                Posy = 0;
                Posx = Posx + 205;
            }
        }
        if (type.equals("Float")) {
            JSlider jSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 0);
            JLabel jLabel = new JLabel(String.valueOf(check.get("name")).replace(" ", "_") + " : 0");
            jSlider.setName(String.valueOf(check.get("name")).replace(" ", "_"));
            Box.add(jSlider).setBounds(5, 5, 195, 20);
            Box.add(jLabel).setBounds(25, 20, 195, 20);
            jSlider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    jLabel.setText(String.valueOf(check.get("name")).replace(" ", "_") + " : " + jSlider.getValue());
                    try {
                        oscPortOut.send(new OSCMessage("/avatar/parameters/" + String.valueOf(check.get("name")).replace(" ", "_"), Collections.singletonList(((float) (jSlider.getValue())) / 100f)));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (OSCSerializeException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            jLabel.setForeground(Color.LIGHT_GRAY);
            jSlider.setBackground(Color.DARK_GRAY);
            jSlider.setForeground(Color.LIGHT_GRAY);
            jLabel.setBackground(Color.DARK_GRAY);

            pane.add(Box).setBounds(Posx, Posy, 205, 50);
            Posy = Posy + 50;
            if (Posy >= 400) {
                Posy = 0;
                Posx = Posx + 205;
            }
        }
        if (type.equals("Int")) {
            JSlider jSlider = new JSlider(SwingConstants.HORIZONTAL, 0 - Integer.MAX_VALUE, 0 + Integer.MAX_VALUE, 0);
            jSlider.setEnabled(false);
            JLabel jLabel = new JLabel(String.valueOf(check.get("name")).replace(" ", "_") + " : 0");
            JButton moar = new JButton("+");
            JButton less = new JButton("-");
            moar.setSize(20, 20);
            less.setSize(20, 20);
            jSlider.setName(String.valueOf(check.get("name")).replace(" ", "_"));
            Box.add(jLabel).setBounds(35, 5, 180, 20);
            Box.add(less).setBounds(5, 5, 20, 20);
            Box.add(moar).setBounds(180, 5, 20, 20);
            less.setBackground(Color.DARK_GRAY);
            less.setForeground(Color.LIGHT_GRAY);
            moar.setBackground(Color.DARK_GRAY);
            moar.setForeground(Color.LIGHT_GRAY);
            jLabel.setBackground(Color.DARK_GRAY);
            jLabel.setForeground(Color.LIGHT_GRAY);

            jSlider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    jLabel.setText(String.valueOf(check.get("name")).replace(" ", "_") + " : " + jSlider.getValue());
                    try {
                        oscPortOut.send(new OSCMessage("/avatar/parameters/" + String.valueOf(check.get("name")).replace(" ", "_"), Collections.singletonList((int) (jSlider.getValue()))));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (OSCSerializeException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            less.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    jSlider.setValue(jSlider.getValue() - 1);
                }
            });
            moar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    jSlider.setValue(jSlider.getValue() + 1);
                }
            });

            pane.add(Box).setBounds(Posx, Posy, 205, 30);
            Posy = Posy + 30;
            if (Posy >= 400) {
                Posy = 0;
                Posx = Posx + 205;
            }
        }

    }

    public static boolean connect(String address, String port) {
        InetAddress addr;
        try {
            addr = InetAddress.getByName(address);
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
            return false;
        }
        // InetAddress addr = InetAddress.getByName("192.168.100.155");
        // OSCPortOut oscPortOut = new OSCPortOut();
        try {
            oscPortOut = new OSCPortOut(addr, Integer.parseInt(port));
            connected = true;
        } catch (NumberFormatException | IOException e1) {
            e1.printStackTrace();
            connected = false;
            return false;
        }
        int i = 1;
        Object[] args = new Object[1];
        return true;
    }

    public void run() {
        System.out.println("Is heartbeat " + connect(adress, port));
        int onefourth = 0;
        while (true) {
            try {
                if (onefourth == 4 && ((SocketMessage) || (AFKMODE && AFKMessages)))
                    oscPortOut.send(new OSCMessage("/chatbox/input", Collections.singletonList(getSocketMessage(1))));
                if (onefourth == 0 && ((SocketMessage) || (AFKMODE && AFKMessages)))
                    oscPortOut.send(new OSCMessage("/chatbox/input", Collections.singletonList(getSocketMessage(2))));
                sleep(800);
                oscPortOut.send(new OSCMessage("/Heartbeat", Collections.singletonList(0)));
                sleep(200);
                oscPortOut.send(new OSCMessage("/Heartbeat", Collections.singletonList(1)));
                onefourth = onefourth + 1;
                if (onefourth == 8) onefourth = 0;
            } catch (InterruptedException | OSCSerializeException | IOException e) {
                Thread thread = new Thread(new OSCJframe());
                thread.start();
            }

        }
    }
}
