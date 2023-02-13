package org.GameEraser.Catalyss;

import io.github.vrchatapi.ApiClient;
import io.github.vrchatapi.ApiException;
import io.github.vrchatapi.Configuration;
import io.github.vrchatapi.api.AuthenticationApi;
import io.github.vrchatapi.auth.HttpBasicAuth;
import io.github.vrchatapi.model.CurrentUser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class AvatarReader implements Runnable {
    private CurrentUser result;
    private AuthenticationApi authApi;
    private void Login(){
        JSONObject Json = new JSONObject();
        try {
            FileReader fr = new FileReader("src/resources/Settings.json");
            JSONParser parser = new JSONParser();
            Json = (JSONObject) parser.parse(fr);
        } catch (Exception ignored) {

        }
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        authApi = new AuthenticationApi(defaultClient);

        HttpBasicAuth authHeader = (HttpBasicAuth) defaultClient.getAuthentication("authHeader");
        if(Json.containsKey("EMAIL")&&Json.containsKey("PASSWORD")) {
            authHeader.setUsername(Json.get("EMAIL").toString());
            authHeader.setPassword(Json.get("PASSWORD").toString());
            result = null;
            try {
                result = authApi.getCurrentUser();
            } catch (ApiException e) {
                ErrorPanel.Senderror(e.toString());
            }
            System.out.println(result.getDisplayName());
        }else {
            ErrorPanel.Senderror("No authentication go to settings");
        }
    }
    @Override
    public void run() {
        Login();
        JFrame frame = new JFrame("Avatar Reader");
        //construct preComponents

        StringBuilder otherstringBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("IDS.txt"));
            String line = null;
            String ls = System.getProperty("line.separator");
            while ((line = reader.readLine()) != null) {
                otherstringBuilder.append(line);
                otherstringBuilder.append(ls);
            }
            reader.close();
        }catch (Exception ev) {
            ErrorPanel.Senderror(ev.toString());
        }
        String[] IDLISTItems = otherstringBuilder.toString().replace(";avtr_","").split("\\n");
        //construct components
        JList list= new JList(IDLISTItems);
        JScrollPane IDLIST = new JScrollPane(list);
        JLabel CREATOR = new JLabel("CreatorName");
        JLabel AVATARNAME = new JLabel("AvatarName");
        JButton AVILOADERBUTTON = new JButton("Load Selected");
        JButton DOWNLOAD = new JButton("Download Selected");
        JTextArea AVATARDESCRIPTION = new JTextArea("AvatarDescription");
        JLabel VERSION = new JLabel("Version : 0 ");
        JLabel PUBLICORPRIVATE = new JLabel("PUBLIC / PRIVATE");
        JLabel CREATED = new JLabel("Creation Date");
        JTextArea AUTHORID = new JTextArea(1, 1);
        JTextArea AVATARID = new JTextArea(1, 1);
        JLabel PICTURE = new JLabel();

        //set components properties
        AUTHORID.setToolTipText("Author ID");
        AVATARID.setToolTipText("Avatar ID");
        PICTURE.setToolTipText("Avatar Picture");

        //adjust size and set layout
        frame.getContentPane().setPreferredSize(new Dimension(944, 569));
        frame.getContentPane().setLayout(null);

        //add components
        frame.getContentPane().add(IDLIST);
        frame.getContentPane().add(CREATOR);
        frame.getContentPane().add(AVATARNAME);
        frame.getContentPane().add(AVILOADERBUTTON);
        frame.getContentPane().add(DOWNLOAD);
        frame.getContentPane().add(AVATARDESCRIPTION);
        frame.getContentPane().add(VERSION);
        frame.getContentPane().add(PUBLICORPRIVATE);
        frame.getContentPane().add(CREATED);
        frame.getContentPane().add(AUTHORID);
        frame.getContentPane().add(AVATARID);
        frame.getContentPane().add(PICTURE);

        //set component bounds (only needed by Absolute Positioning)
        IDLIST.setBounds(5, 5, 270, 560);
        CREATOR.setBounds(725, 10, 200, 30);
        AVATARNAME.setBounds(725, 40, 200, 30);
        AVILOADERBUTTON.setBounds(290, 530, 280, 30);
        DOWNLOAD.setBounds(290, 500, 280, 30);
        AVATARDESCRIPTION.setBounds(725, 75, 195, 115);
        VERSION.setBounds(725, 190, 200, 30);
        PUBLICORPRIVATE.setBounds(725, 220, 200, 30);
        CREATED.setBounds(725, 250, 200, 30);
        AUTHORID.setBounds(725, 280, 200, 30);
        AVATARID.setBounds(725, 310, 200, 30);
        PICTURE.setBounds(280, 5, 440, 440);

        AVATARDESCRIPTION.setEditable(false);
        AVATARDESCRIPTION.setLineWrap(true);
        AVATARDESCRIPTION.setWrapStyleWord(true);

        frame.pack();
        frame.setVisible (true);
        frame.setLocationRelativeTo(null);
        frame.setBackground(Color.GRAY);

        AVILOADERBUTTON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(result==null){ErrorPanel.Senderror("Please login in setting");return;};
                if(list.isSelectionEmpty()){ErrorPanel.Senderror("Select something");return;}
                try {
                    URL url = new URL("https://api.vrchat.cloud/api/1/avatars/avtr_" + list.getSelectedValue().toString());
                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                    httpConn.setRequestMethod("GET");
                    httpConn.setRequestProperty("Cookie", "apiKey=JlE5Jldo5Jibnk5O5hTx6XVqsJu4WJ26; auth=" + authApi.verifyAuthToken().getToken());
                    InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                            ? httpConn.getInputStream()
                            : httpConn.getErrorStream();
                    Scanner s = new Scanner(responseStream).useDelimiter("\\A");
                    String response = s.hasNext() ? s.next() : "";
                    JSONParser parser = new JSONParser();
                    JSONObject json = (JSONObject) parser.parse(response);

                    CREATOR.setText(json.get("authorName").toString());
                    AVATARNAME.setText(json.get("name").toString());
                    AVATARDESCRIPTION.setText(json.get("description").toString());
                    VERSION.setText("Version : "+json.get("version").toString());
                    PUBLICORPRIVATE.setText(json.get("releaseStatus").toString());
                    CREATED.setText(json.get("created_at").toString());
                    AUTHORID.setText(json.get("authorId").toString());
                    AVATARID.setText(json.get("id").toString());

                    ImageIcon imageIcon = new ImageIcon(new URL(json.get("imageUrl").toString())); // load the image to a imageIcon
                    Image image = imageIcon.getImage(); // transform it
                    Image newimg = image.getScaledInstance(440, 440,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                    imageIcon = new ImageIcon(newimg);  // transform it back
                    PICTURE.setIcon(imageIcon);

                }catch (Exception ev){ErrorPanel.Senderror(ev.toString());}
            }
        });
        DOWNLOAD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(list.isSelectionEmpty()){ErrorPanel.Senderror("Select something");return;}
                URL url = null;
                String str = "avtr_"+list.getSelectedValue().toString();
                    System.out.println("https://api.ripper.store/api/v1/id-downloader?id="+str);
                try {
                    url = new URL("https://api.ripper.store/api/v1/id-downloader?id="+str);
                } catch (MalformedURLException ex) {
                    ErrorPanel.Senderror(ex.toString());
                    return;
                }
                try {
                    FileOutputStream fos=new FileOutputStream(new File(str.trim()+".vrca"));
                    url.openStream().transferTo(fos);
                    fos.close();
                } catch (IOException ex) {
                    ErrorPanel.Senderror(ex.toString());
                }

            }
        });

    }
}
