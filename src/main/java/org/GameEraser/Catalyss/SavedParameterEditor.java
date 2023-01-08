package org.GameEraser.Catalyss;

import com.illposed.osc.OSCSerializeException;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.*;

public class SavedParameterEditor implements Runnable{
    private int Posy;
    private int Posx;
    private JTextArea pane;
    private int AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA;
    @Override
    public void run() {
        StringBuilder otherstringBuilder = new StringBuilder();
        JSONObject IDFINDER = new JSONObject();
        List ListOrder = new List();
        File folder = new File(System.getProperty("user.home") + "\\AppData\\LocalLow\\VRChat\\VRChat\\OSC");
        File[] listOfFiles = folder.listFiles();

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
                        if(!IDFINDER.has(id))IDFINDER.append(id,name);
                    } else if (listOfFiless[i].isDirectory()) {
                    }
                }
            }
        }

        File folderDATA = new File(System.getProperty("user.home") + "\\AppData\\LocalLow\\VRChat\\VRChat\\LocalAvatarData");
        File[] listOfFilesDATA = folderDATA.listFiles();
        for (int i = 0; i < listOfFilesDATA.length; i++) {
            if (listOfFiles[i].isFile()) {
            } else if (listOfFiles[i].isDirectory()) {
                File folders = new File(System.getProperty("user.home") + "\\AppData\\LocalLow\\VRChat\\VRChat\\LocalAvatarData\\" + listOfFiles[i].getName() );
                File[] listOfFiless = folders.listFiles();
                for (int is = 0; is < listOfFiless.length; is++) {
                    if (listOfFiless[is].isFile()) {
                        if(IDFINDER.has(listOfFiless[is].getName())){
                            otherstringBuilder.append(IDFINDER.get(listOfFiless[is].getName()).toString());
                            otherstringBuilder.append(System.getProperty("line.separator"));
                        }else{
                            otherstringBuilder.append(listOfFiless[is].getName());
                            otherstringBuilder.append(System.getProperty("line.separator"));
                        }
                        ListOrder.add(String.valueOf(listOfFiless[is]));
                    } else if (listOfFiless[i].isDirectory()) {
                    }
                }
            }
        }

        JFrame frame = new JFrame("Saved Data Editor");

        //construct preComponents
        String[] LISTItems = otherstringBuilder.toString().split("\\n");

        //construct components
        JList LIST = new JList(LISTItems);
        pane = new JTextArea();
        pane.setBounds(0,0,1000,560);
        pane.setEditable(false);
        pane.setBackground(Color.GRAY);
        pane.setForeground(Color.GRAY);
        pane.setLayout(null);
        JScrollPane JScrollPaneLIST = new JScrollPane(LIST);
        JScrollPane JScrollPanechanger = new JScrollPane(pane);
        JScrollPanechanger.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        //adjust size and set layout
        frame.getContentPane().setPreferredSize(new Dimension(944, 569));
        frame.getContentPane().setLayout(null);

        //add components
        frame.getContentPane().add(JScrollPaneLIST);
        frame.getContentPane().add(JScrollPanechanger);

        //set component bounds (only needed by Absolute Positioning)
        JScrollPaneLIST.setBounds(5, 5, 225, 560);
        JScrollPanechanger.setBounds(230, 5, 710, 560);

        LIST.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Posy =0;
                Posx =0;
                pane.setText("...................................................................)");
                AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=0;
                System.out.println(ListOrder.getItem(LIST.getSelectedIndex())+".json");
                try{pane.removeAll();}catch(Exception ignored){}
                pane.updateUI();
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(ListOrder.getItem(LIST.getSelectedIndex())));
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
                    JSONArray parameters = obj.getJSONArray("animationParameters");
                    for (Object parameter : parameters) {
                        ParameterWindow((JSONObject) parameter,ListOrder.getItem(LIST.getSelectedIndex()));
                    }
                }catch (Exception ex){
                    ErrorPanel.Senderror(ex.toString());
                }
            }
        });

        frame.pack();
        frame.setVisible (true);
        frame.setLocationRelativeTo(null);
        frame.setBackground(Color.GRAY);

    }
    private void ParameterWindow(JSONObject check, String item) throws OSCSerializeException, IOException {
        if(String.valueOf(check.get("name")).equals(""))return;
        JPanel Box = new JPanel();
        Box.setBackground(Color.DARK_GRAY);
        pane.setBackground(Color.GRAY);
        pane.setForeground(Color.GRAY);
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

        JTextPane  txt = new JTextPane();
        boolean Gottem=false;
        for(Component cp:Box.getComponents()){
            if(cp.getName().equals(String.valueOf(check.get("name")).replace(" ", "_"))&& cp instanceof JTextPane){
                Gottem=true;
                break;
            }
        }
        JLabel jLabel;
        if(!Gottem) {
            jLabel = new JLabel(String.valueOf(check.get("name")).replace(" ", "_"), SwingConstants.CENTER);
            txt.setName(String.valueOf(check.get("name")).replace(" ", "_"));
        }else{
            jLabel = new JLabel(String.valueOf(check.get("name")).replace(" ", "_"), SwingConstants.CENTER);
            txt.setName(String.valueOf(check.get("name")).replace(" ", "_")+"___"+AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA);
            AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA++;
        }
        Box.add(txt).setBounds(5, 5, 195, 20);
        Box.add(jLabel).setBounds(5, 23, 195, 20);
        txt.setText(String.valueOf(check.get("value")));
        StyledDocument doc = txt.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        jLabel.setForeground(Color.LIGHT_GRAY);
        txt.setBackground(Color.DARK_GRAY);
        txt.setForeground(Color.LIGHT_GRAY);
        jLabel.setBackground(Color.DARK_GRAY);
        txt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try{TEXTINPUT(txt.getDocument().getText(0,txt.getDocument().getLength()),txt.getName(),item);}catch(Exception ex){ErrorPanel.Senderror(ex.toString());}
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try{TEXTINPUT(txt.getDocument().getText(0,txt.getDocument().getLength()),txt.getName(),item);}catch(Exception ex){ErrorPanel.Senderror(ex.toString());}
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try{TEXTINPUT(txt.getDocument().getText(0,txt.getDocument().getLength()),txt.getName(),item);}catch(Exception ex){ErrorPanel.Senderror(ex.toString());}
            }
        });

        pane.add(Box).setBounds(Posx, Posy, 205, 49);
        Posy = Posy + 49;
        pane.setBounds(0,0,1000,560);
        if (Posy >= 510) {
            Posy = 0;
            Posx = Posx + 205;
            pane.append("...................................................................)");
        }

    }
    private void TEXTINPUT(String input,String Name,String File){
        if(input.equals(""))return;

        File BaseFile= new File(File);
        try {
            if (BaseFile == null || !BaseFile.exists()) return;
            BufferedReader reader = new BufferedReader(new FileReader(BaseFile));
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
            if(Name.contains("__")){
               ErrorPanel.Senderror("hum what? Lazy dev");
               return;
            }else{
                JSONArray arr= (JSONArray) obj.get("animationParameters");
                for(Object jb: arr){
                    JSONObject jsonObject = (JSONObject) jb;
                    if(jsonObject.get("name").toString().equals(Name)){
                        jsonObject.remove("value");
                        double db =Double.parseDouble(input);
                        jsonObject.put("value",db);
                    }
                }
            }
            FileWriter frw = new FileWriter(File);
            frw.write(obj.toString());
            frw.close();

        }catch (Exception ex){
            ErrorPanel.Senderror(ex.toString());
        }
    }
}
