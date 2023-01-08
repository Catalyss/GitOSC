package org.GameEraser.Catalyss;


import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCSerializeException;
import com.illposed.osc.transport.OSCPortOut;
import io.github.vrchatapi.ApiClient;
import io.github.vrchatapi.ApiException;
import io.github.vrchatapi.Configuration;
import io.github.vrchatapi.api.AuthenticationApi;
import io.github.vrchatapi.auth.HttpBasicAuth;
import io.github.vrchatapi.model.CurrentUser;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.parseInt;
import static java.lang.Thread.sleep;

//avatar/parameters/
public class Main {
    public static OSCPortOut oscPortOut;
    public static boolean connected;
    public static void Manual() throws IOException, OSCSerializeException {
        System.out.println("Is connected "+ connect("127.0.0.1","9000"));
        while (true){
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String name = reader.readLine();
            String[] str = name.split("ù%ù");
            if(str[2].replace("ù%ù","").equals("int")) oscPortOut.send(new OSCMessage("/avatar/parameters/"+str[0].replace("ù%ù",""), Collections.singletonList(parseInt(str[1].replace("ù%ù","")))));


        }
    }
    public static void VRCLogin() throws ApiException {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        AuthenticationApi authApi = new AuthenticationApi(defaultClient);

// Step 2. We begin with creating a Configuration
// This contains the username and password for authentication.
        HttpBasicAuth authHeader = (HttpBasicAuth) defaultClient.getAuthentication("authHeader");
        authHeader.setUsername("knee melter");
        authHeader.setPassword("recupliam@gmail.com");

// Step 3. Call getCurrentUser on Authentication API.
// This logs you in if the user isn't already logged in.
        CurrentUser result = authApi.getCurrentUser();
        System.out.println(result.getDisplayName());


    }

    public static void main(String[] args) throws IOException, InterruptedException, OSCSerializeException, ApiException {
        //Manual();
        //Thread t =new Thread(new IDCREATOR());
        //Thread t =new Thread(new VRCTrackerFormationg());

        OSCJframe.main(args);
        if(1==1) return;
        Thread t =new Thread(new IDListMerger());
        t.run();


        System.out.println("Debug true or false");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String name = reader.readLine();

        if(!name.contains("t"))System.out.println("Is connected "+ connect("127.0.0.1","9000"));
        else System.out.println("Is connected "+ connect("127.0.0.1","5234"));
/*
        float SWB;
        float RGBs;
        OSCMessage message;
        while(connected) {
            for (int PX = 1; PX < 3; PX++) {
                for (float R = 0; R < 1.01; R = (float) (R + 0.01)) {
                    for (float s = 0; s < 1.01; s = (float) (s + 0.01)) {
                            ArrayList<OSCMessage> oscmessages = new ArrayList<>();
                            message = new OSCMessage("/avatar/parameters/W&B", Collections.singletonList((float) s));
                            oscmessages.add(message);
                            message = new OSCMessage("/avatar/parameters/RGB", Collections.singletonList((float)R));
                            oscmessages.add(message);
                            message = new OSCMessage("/avatar/parameters/S", Collections.singletonList((float) s));
                            oscmessages.add(message);
                            message = new OSCMessage("/avatar/parameters/X", Collections.singletonList((int) PX));
                            message = new OSCMessage("/avatar/parameters/Y", Collections.singletonList((int) PX));
                            oscmessages.add(message);
                            for (OSCMessage m : oscmessages) {
                                forwardMessage(m);
                            }
                        }
                    }
                }
            }
             */
        List<Webcam> w = Webcam.getWebcams();
        Webcam webcam = w.get(0);
        webcam.setViewSize(new Dimension(176,144));

        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setFPSDisplayed(true);
        panel.setDisplayDebugInfo(true);
        panel.setImageSizeDisplayed(true);
        panel.setMirrored(true);

        BufferedImage img = new BufferedImage(176, 144, BufferedImage.TYPE_INT_RGB);;
        JFrame window = new JFrame("Test webcam panel");
        JPanel pane = new JPanel();
        pane.setSize(176,144);
        window.add(panel).setBounds(176,0,176,144);
        window.setSize(176*2,144);
        window.add(pane);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        while(true) {
            Raster image = resizeImage(webcam.getImage(),176/2,144/2).getData();
            //Raster image = resizeImage(webcam.getImage(),10,10).getData();
            img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    float[] hsv = new float[3];
                    float[] RGB = new float[3];
                    image.getPixel(x, y, RGB);
                    Color.RGBtoHSB((int) RGB[0] * 1, (int) RGB[1] * 1, (int) RGB[2] * 1, hsv);
                    //System.out.println(hsv[0] + "  " + hsv[1] + "  " + hsv[2] + "  \n" + x + " " + y);
                    img.setRGB(x, y, Color.HSBtoRGB(hsv[0], hsv[1], hsv[2]));
                    sendPixel(x,y,hsv[0], hsv[1], hsv[2]);
                }
            }
            pane.getGraphics().drawImage(img, 0, 0, new ImageObserver() {
                @Override
                public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                    return true;
                }
            });
        }

        /*

        int value= 0;
        while(true) {

            message = new OSCMessage("/avatar/parameters/TGHair", Collections.singletonList(value));
            oscmessages.add(message);
            message = new OSCMessage("/avatar/parameters/TGTop", Collections.singletonList(value));
            oscmessages.add(message);
            message = new OSCMessage("/avatar/parameters/TGPant", Collections.singletonList(value));
            oscmessages.add(message);


            if (value == 2) value = 0;
            else value++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }
         */
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    public static String sendPixel(int X, int Y,float Hue, float Saturation,float Brightness){
        try {
            oscPortOut.send(new OSCMessage("/avatar/parameters/SWB", Collections.singletonList((float) Brightness)));
            oscPortOut.send(new OSCMessage("/avatar/parameters/RGBs", Collections.singletonList((float) Hue)));
            oscPortOut.send(new OSCMessage("/avatar/parameters/TGS", Collections.singletonList((float) Saturation)));
            oscPortOut.send(new OSCMessage("/avatar/parameters/TGY", Collections.singletonList((int) Y)));
            oscPortOut.send(new OSCMessage("/avatar/parameters/TGX", Collections.singletonList((int) X)));
            oscPortOut.send(new OSCMessage("/avatar/parameters/OSC", Collections.singletonList(true)));
            sleep(100);
            return "Succes";
        } catch (Exception exception){
            return "Fail";
        }
    }
    public static boolean connect(String address, String port)
    {
        InetAddress addr;
        try
        {
            addr = InetAddress.getByName(address);
        } catch (UnknownHostException e1)
        {
            e1.printStackTrace();
            return false;
        }
        // InetAddress addr = InetAddress.getByName("192.168.100.155");
        // OSCPortOut oscPortOut = new OSCPortOut();
        try
        {
            oscPortOut = new OSCPortOut(addr, Integer.valueOf(port));
            connected = true;
        } catch (NumberFormatException | IOException e1)
        {
            e1.printStackTrace();
            connected = false;
            return false;
        }
        int i = 1;
        Object[] args = new Object[1];
        return true;
    }

    public static void forwardMessage(OSCMessage message)
    {
        try
        {
            oscPortOut.send(message);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void AnimationMaker() throws IOException, InterruptedException {
        for (int x = 0; x < 89; x++) {
            for (int y = 0; y < 73; y++) {
                String name ="SW";
                String P1_R = Files.readString(Path.of("P1 " + name + ".anim"));
                FileWriter fwR = new FileWriter("Pixel_" + x + "_" + y + " " + name + ".anim");
                fwR.write(P1_R.replace("Pixel_0_0", "Thing/Pixel_" + x + "_" + y).replace("P1 " + name, "Pixel_" + x + "_" + y + " " + name));
                fwR.close();
            }
        }
    }

    public static void AnimatorMaker() throws IOException, InterruptedException {
        //
        Path path1 = Path.of("C:/Users/liamd/Documents/Unity Project/WebcamToAvi/Assets/PixelAnimation/PixelControl_Edit.controller");
        FileWriter fwR = new FileWriter(path1.toFile());
        for (int x = 0; x < 1; x++) {
            for (int y = 0; y < 1; y++) {
                Path path = Path.of("C:/Users/liamd/Documents/Unity Project/WebcamToAvi/Assets/PixelAnimation/");
                String R_Guild = Files.readString(Path.of(path+"/R/Pixel_"+y+"_"+x+" R.anim.meta")).replace("fileFormatVersion: 2\n","").replace("NativeFormatImporter:\n" + "  externalObjects: {}\n" + "  mainObjectFileID: 0\n" + "  userData: \n" + "  assetBundleName: \n" + "  assetBundleVariant:","");
                String G_Guild = Files.readString(Path.of(path+"/G/Pixel_"+y+"_"+x+" G.anim.meta")).replace("fileFormatVersion: 2\n","").replace("NativeFormatImporter:\n" + "  externalObjects: {}\n" + "  mainObjectFileID: 0\n" + "  userData: \n" + "  assetBundleName: \n" + "  assetBundleVariant:","");
                String B_Guild = Files.readString(Path.of(path+"/B/Pixel_"+y+"_"+x+" B.anim.meta")).replace("fileFormatVersion: 2\n","").replace("NativeFormatImporter:\n" + "  externalObjects: {}\n" + "  mainObjectFileID: 0\n" + "  userData: \n" + "  assetBundleName: \n" + "  assetBundleVariant:","");
                String SW_Guild = Files.readString(Path.of(path+"/SW/Pixel_"+y+"_"+x+" SW.anim.meta")).replace("fileFormatVersion: 2\n","").replace("NativeFormatImporter:\n" + "  externalObjects: {}\n" + "  mainObjectFileID: 0\n" + "  userData: \n" + "  assetBundleName: \n" + "  assetBundleVariant:","");
                String SB_Guild = Files.readString(Path.of(path+"/SB/Pixel_"+y+"_"+x+" SB.anim.meta")).replace("fileFormatVersion: 2\n","").replace("NativeFormatImporter:\n" + "  externalObjects: {}\n" + "  mainObjectFileID: 0\n" + "  userData: \n" + "  assetBundleName: \n" + "  assetBundleVariant:","");
                String ShitToAppend = Files.readString(Path.of("ShitToAppend.txt"));
                String str=null;
                String[]IDS=new String[]{"147258369"+x+y,"147257369"+x+y,"1857258369"+x+y,"14778958369"+x+y,"147188369"+x+y,"1412758369"+x+y,"1472753369"+x+y,"14727523369"+x+y,"1472754533369"+x+y,"14727538673369"+x+y,"7374047945955651066","1474226753369"+x+y,"147422757853369"+x+y,"1625447422753369"+x+y,"14746543922753369"+x+y,"14421187422753369"+x+y};


                if(x==0&&y==0){

                    fwR.write("--- !u!91 &9100000\n" +
                            "AnimatorController:\n" +
                            "  m_ObjectHideFlags: 0\n" +
                            "  m_CorrespondingSourceObject: {fileID: 0}\n" +
                            "  m_PrefabInstance: {fileID: 0}\n" +
                            "  m_PrefabAsset: {fileID: 0}\n" +
                            "  m_Name: PixelControl\n" +
                            "  serializedVersion: 5\n" +
                            "  m_AnimatorParameters:\n" +
                            "  - m_Name: S\n" +
                            "    m_Type: 1\n" +
                            "    m_DefaultFloat: 0\n" +
                            "    m_DefaultInt: 0\n" +
                            "    m_DefaultBool: 0\n" +
                            "    m_Controller: {fileID: 9100000}\n" +
                            "  - m_Name: RGBs\n" +
                            "    m_Type: 1\n" +
                            "    m_DefaultFloat: 0\n" +
                            "    m_DefaultInt: 0\n" +
                            "    m_DefaultBool: 0\n" +
                            "    m_Controller: {fileID: 9100000}\n" +
                            "  - m_Name: SWB\n" +
                            "    m_Type: 1\n" +
                            "    m_DefaultFloat: 0\n" +
                            "    m_DefaultInt: 0\n" +
                            "    m_DefaultBool: 0\n" +
                            "    m_Controller: {fileID: 9100000}\n" +
                            "  - m_Name: X\n" +
                            "    m_Type: 3\n" +
                            "    m_DefaultFloat: 0\n" +
                            "    m_DefaultInt: 0\n" +
                            "    m_DefaultBool: 0\n" +
                            "    m_Controller: {fileID: 9100000}\n" +
                            "  - m_Name: Y\n" +
                            "    m_Type: 3\n" +
                            "    m_DefaultFloat: 0\n" +
                            "    m_DefaultInt: 0\n" +
                            "    m_DefaultBool: 0\n" +
                            "    m_Controller: {fileID: 9100000}\n" +
                            "  m_AnimatorLayers:\n" +
                            "  - serializedVersion: 5\n" +
                            "    m_Name: Base\n" +
                            "    m_StateMachine: {fileID: "+IDS[6]+"}\n" +
                            "    m_Mask: {fileID: 0}\n" +
                            "    m_Motions: []\n" +
                            "    m_Behaviours: []\n" +
                            "    m_BlendingMode: 0\n" +
                            "    m_SyncedLayerIndex: -1\n" +
                            "    m_DefaultWeight: 0\n" +
                            "    m_IKPass: 0\n" +
                            "    m_SyncedLayerAffectsTiming: 0\n" +
                            "    m_Controller: {fileID: 9100000}\n" +
                            "\n" +
                            "--- !u!1102 &7374047945955651066\n" +
                            "AnimatorState:\n" +
                            "  serializedVersion: 6\n" +
                            "  m_ObjectHideFlags: 1\n" +
                            "  m_CorrespondingSourceObject: {fileID: 0}\n" +
                            "  m_PrefabInstance: {fileID: 0}\n" +
                            "  m_PrefabAsset: {fileID: 0}\n" +
                            "  m_Name: Default\n" +
                            "  m_Speed: 1\n" +
                            "  m_CycleOffset: 0\n" +
                            "  m_Transitions: []\n" +
                            "  m_StateMachineBehaviours: []\n" +
                            "  m_Position: {x: 50, y: 50, z: 0}\n" +
                            "  m_IKOnFeet: 0\n" +
                            "  m_WriteDefaultValues: 0\n" +
                            "  m_Mirror: 0\n" +
                            "  m_SpeedParameterActive: 0\n" +
                            "  m_MirrorParameterActive: 0\n" +
                            "  m_CycleOffsetParameterActive: 0\n" +
                            "  m_TimeParameterActive: 0\n" +
                            "  m_Motion: {fileID: 0}\n" +
                            "  m_Tag: \n" +
                            "  m_SpeedParameter: \n" +
                            "  m_MirrorParameter: \n" +
                            "  m_CycleOffsetParameter: \n" +
                            "  m_TimeParameter: ");
                    fwR.flush();
                }




                try {
                    str=("\n\n\n\n--- !u!206 &-"+IDS[1]+"\n" +
                                    "BlendTree:\n" +
                                    "  m_ObjectHideFlags: 1\n" +
                                    "  m_CorrespondingSourceObject: {fileID: 0}\n" +
                                    "  m_PrefabInstance: {fileID: 0}\n" +
                                    "  m_PrefabAsset: {fileID: 0}\n" +
                                    "  m_Name: BlendTree\n" +
                                    "  m_Childs:\n" +
                                    "  - serializedVersion: 2\n" +
                                    "    m_Motion: {fileID: 7400000, guid: "+SB_Guild+", type: 2}\n" +
                                    "    m_Threshold: 0\n" +
                                    "    m_Position: {x: 0, y: 0}\n" +
                                    "    m_TimeScale: 1\n" +
                                    "    m_CycleOffset: 0\n" +
                                    "    m_DirectBlendParameter: SWB\n" +
                                    "    m_Mirror: 0\n" +
                                    "  - serializedVersion: 2\n" +
                                    "    m_Motion: {fileID: 7400000, guid: "+G_Guild+", type: 2}\n" +
                                    "    m_Threshold: 1\n" +
                                    "    m_Position: {x: 0, y: 0}\n" +
                                    "    m_TimeScale: 1\n" +
                                    "    m_CycleOffset: 0\n" +
                                    "    m_DirectBlendParameter: SWB\n" +
                                    "    m_Mirror: 0\n" +
                                    "  m_BlendParameter: S\n" +
                                    "  m_BlendParameterY: Blend\n" +
                                    "  m_MinThreshold: 0\n" +
                                    "  m_MaxThreshold: 1\n" +
                                    "  m_UseAutomaticThresholds: 1\n" +
                                    "  m_NormalizedBlendValues: 0\n" +
                                    "  m_BlendType: 0\n" +
                                    "--- !u!206 &-"+IDS[3]+"\n" +
                                    "BlendTree:\n" +
                                    "  m_ObjectHideFlags: 1\n" +
                                    "  m_CorrespondingSourceObject: {fileID: 0}\n" +
                                    "  m_PrefabInstance: {fileID: 0}\n" +
                                    "  m_PrefabAsset: {fileID: 0}\n" +
                                    "  m_Name: BlendTree\n" +
                                    "  m_Childs:\n" +
                                    "  - serializedVersion: 2\n" +
                                    "    m_Motion: {fileID: 7400000, guid: "+SB_Guild+", type: 2}\n" +
                                    "    m_Threshold: 0\n" +
                                    "    m_Position: {x: 0, y: 0}\n" +
                                    "    m_TimeScale: 1\n" +
                                    "    m_CycleOffset: 0\n" +
                                    "    m_DirectBlendParameter: SWB\n" +
                                    "    m_Mirror: 0\n" +
                                    "  - serializedVersion: 2\n" +
                                    "    m_Motion: {fileID: 7400000, guid: "+R_Guild+", type: 2}\n" +
                                    "    m_Threshold: 0.5\n" +
                                    "    m_Position: {x: 0, y: 0}\n" +
                                    "    m_TimeScale: 1\n" +
                                    "    m_CycleOffset: 0\n" +
                                    "    m_DirectBlendParameter: SWB\n" +
                                    "    m_Mirror: 0\n" +
                                    "  m_BlendParameter: S\n" +
                                    "  m_BlendParameterY: Blend\n" +
                                    "  m_MinThreshold: 0\n" +
                                    "  m_MaxThreshold: 0.5\n" +
                                    "  m_UseAutomaticThresholds: 1\n" +
                                    "  m_NormalizedBlendValues: 0\n" +
                                    "  m_BlendType: 0\n" +
                                    "  \n" +
                                    "  --- !u!206 &"+IDS[2]+"\n" +
                                    "BlendTree:\n" +
                                    "  m_ObjectHideFlags: 1\n" +
                                    "  m_CorrespondingSourceObject: {fileID: 0}\n" +
                                    "  m_PrefabInstance: {fileID: 0}\n" +
                                    "  m_PrefabAsset: {fileID: 0}\n" +
                                    "  m_Name: BlendTree\n" +
                                    "  m_Childs:\n" +
                                    "  - serializedVersion: 2\n" +
                                    "    m_Motion: {fileID: 7400000, guid: "+SB_Guild+", type: 2}\n" +
                                    "    m_Threshold: 0\n" +
                                    "    m_Position: {x: 0, y: 0}\n" +
                                    "    m_TimeScale: 1\n" +
                                    "    m_CycleOffset: 0\n" +
                                    "    m_DirectBlendParameter: SWB\n" +
                                    "    m_Mirror: 0\n" +
                                    "  - serializedVersion: 2\n" +
                                    "    m_Motion: {fileID: 7400000, guid: "+R_Guild+", type: 2}\n" +
                                    "    m_Threshold: 1\n" +
                                    "    m_Position: {x: 0, y: 0}\n" +
                                    "    m_TimeScale: 1\n" +
                                    "    m_CycleOffset: 0\n" +
                                    "    m_DirectBlendParameter: SWB\n" +
                                    "    m_Mirror: 0\n" +
                                    "  m_BlendParameter: S\n" +
                                    "  m_BlendParameterY: Blend\n" +
                                    "  m_MinThreshold: 0\n" +
                                    "  m_MaxThreshold: 1\n" +
                                    "  m_UseAutomaticThresholds: 1\n" +
                                    "  m_NormalizedBlendValues: 0\n" +
                                    "  m_BlendType: 0\n" +
                                    "--- !u!206 &"+IDS[5]+"\n" +
                                    "BlendTree:\n" +
                                    "  m_ObjectHideFlags: 1\n" +
                                    "  m_CorrespondingSourceObject: {fileID: 0}\n" +
                                    "  m_PrefabInstance: {fileID: 0}\n" +
                                    "  m_PrefabAsset: {fileID: 0}\n" +
                                    "  m_Name: Blend Tree\n" +
                                    "  m_Childs:\n" +
                                    "  - serializedVersion: 2\n" +
                                    "    m_Motion: {fileID: 7400000, guid: "+SW_Guild+", type: 2}\n" +
                                    "    m_Threshold: 0\n" +
                                    "    m_Position: {x: 0, y: 0}\n" +
                                    "    m_TimeScale: 1\n" +
                                    "    m_CycleOffset: 0\n" +
                                    "    m_DirectBlendParameter: RGB\n" +
                                    "    m_Mirror: 0\n" +
                                    "  - serializedVersion: 2\n" +
                                    "    m_Motion: {fileID: 7400000, guid: "+SW_Guild+", type: 2}\n" +
                                    "    m_Threshold: 0\n" +
                                    "    m_Position: {x: 0, y: 0.33333}\n" +
                                    "    m_TimeScale: 1\n" +
                                    "    m_CycleOffset: 0\n" +
                                    "    m_DirectBlendParameter: RGB\n" +
                                    "    m_Mirror: 0\n" +
                                    "  - serializedVersion: 2\n" +
                                    "    m_Motion: {fileID: 7400000, guid: "+SW_Guild+", type: 2}\n" +
                                    "    m_Threshold: 0\n" +
                                    "    m_Position: {x: 0, y: 0.66666}\n" +
                                    "    m_TimeScale: 1\n" +
                                    "    m_CycleOffset: 0\n" +
                                    "    m_DirectBlendParameter: RGB\n" +
                                    "    m_Mirror: 0\n" +
                                    "  - serializedVersion: 2\n" +
                                    "    m_Motion: {fileID: 7400000, guid: "+SW_Guild+", type: 2}\n" +
                                    "    m_Threshold: 0\n" +
                                    "    m_Position: {x: 0, y: 1}\n" +
                                    "    m_TimeScale: 1\n" +
                                    "    m_CycleOffset: 0\n" +
                                    "    m_DirectBlendParameter: RGB\n" +
                                    "    m_Mirror: 0\n" +
                                    "  - serializedVersion: 2\n" +
                                    "    m_Motion: {fileID: 7400000, guid: "+SB_Guild+", type: 2}\n" +
                                    "    m_Threshold: 0\n" +
                                    "    m_Position: {x: 1, y: 0}\n" +
                                    "    m_TimeScale: 1\n" +
                                    "    m_CycleOffset: 0\n" +
                                    "    m_DirectBlendParameter: RGB\n" +
                                    "    m_Mirror: 0\n" +
                                    "  - serializedVersion: 2\n" +
                                    "    m_Motion: {fileID: 7400000, guid: "+SB_Guild+", type: 2}\n" +
                                    "    m_Threshold: 0\n" +
                                    "    m_Position: {x: 1, y: 0.3333}\n" +
                                    "    m_TimeScale: 1\n" +
                                    "    m_CycleOffset: 0\n" +
                                    "    m_DirectBlendParameter: RGB\n" +
                                    "    m_Mirror: 0\n" +
                                    "  - serializedVersion: 2\n" +
                                    "    m_Motion: {fileID: 7400000, guid: "+SB_Guild+", type: 2}\n" +
                                    "    m_Threshold: 0\n" +
                                    "    m_Position: {x: 1, y: 0.66666}\n" +
                                    "    m_TimeScale: 1\n" +
                                    "    m_CycleOffset: 0\n" +
                                    "    m_DirectBlendParameter: RGB\n" +
                                    "    m_Mirror: 0\n" +
                                    "  - serializedVersion: 2\n" +
                                    "    m_Motion: {fileID: 7400000, guid: "+SB_Guild+", type: 2}\n" +
                                    "    m_Threshold: 0\n" +
                                    "    m_Position: {x: 1, y: 1}\n" +
                                    "    m_TimeScale: 1\n" +
                                    "    m_CycleOffset: 0\n" +
                                    "    m_DirectBlendParameter: RGB\n" +
                                    "    m_Mirror: 0\n" +
                                    "  - serializedVersion: 2\n" +
                                    "    m_Motion: {fileID: "+IDS[2]+"}\n" +
                                    "    m_Threshold: 0\n" +
                                    "    m_Position: {x: 0.5, y: 0}\n" +
                                    "    m_TimeScale: 1\n" +
                                    "    m_CycleOffset: 0\n" +
                                    "    m_DirectBlendParameter: SWB\n" +
                                    "    m_Mirror: 0\n" +
                                    "  - serializedVersion: 2\n" +
                                    "    m_Motion: {fileID: -"+IDS[1]+"}\n" +
                                    "    m_Threshold: 0\n" +
                                    "    m_Position: {x: 0.5, y: 0.33333}\n" +
                                    "    m_TimeScale: 1\n" +
                                    "    m_CycleOffset: 0\n" +
                                    "    m_DirectBlendParameter: SWB\n" +
                                    "    m_Mirror: 0\n" +
                                    "  - serializedVersion: 2\n" +
                                    "    m_Motion: {fileID: "+IDS[8]+"}\n" +
                                    "    m_Threshold: 0\n" +
                                    "    m_Position: {x: 0.5, y: 0.66666}\n" +
                                    "    m_TimeScale: 1\n" +
                                    "    m_CycleOffset: 0\n" +
                                    "    m_DirectBlendParameter: SWB\n" +
                                    "    m_Mirror: 0\n" +
                                    "  - serializedVersion: 2\n" +
                                    "    m_Motion: {fileID: -"+IDS[3]+"}\n" +
                                    "    m_Threshold: 0\n" +
                                    "    m_Position: {x: 0.5, y: 1}\n" +
                                    "    m_TimeScale: 1\n" +
                                    "    m_CycleOffset: 0\n" +
                                    "    m_DirectBlendParameter: SWB\n" +
                                    "    m_Mirror: 0\n" +
                                    "  m_BlendParameter: SWB\n" +
                                    "  m_BlendParameterY: RGBs\n" +
                                    "  m_MinThreshold: 0\n" +
                                    "  m_MaxThreshold: 0\n" +
                                    "  m_UseAutomaticThresholds: 1\n" +
                                    "  m_NormalizedBlendValues: 0\n" +
                                    "  m_BlendType: 3\n" +
                                    "--- !u!1107 &"+IDS[6]+"\n" +
                                    "AnimatorStateMachine:\n" +
                                    "  serializedVersion: 6\n" +
                                    "  m_ObjectHideFlags: 1\n" +
                                    "  m_CorrespondingSourceObject: {fileID: 0}\n" +
                                    "  m_PrefabInstance: {fileID: 0}\n" +
                                    "  m_PrefabAsset: {fileID: 0}\n" +
                                    "  m_Name: Base\n" +
                                    "  m_ChildStates:\n" +
                                    "  - serializedVersion: 1\n" +
                                    "    m_State: {fileID: "+IDS[10]+"}\n" +
                                    "    m_Position: {x: 250, y: 110, z: 0}\n" +
                                    "  - serializedVersion: 1\n" +
                                    "    m_State: {fileID: "+IDS[9]+"}\n" +
                                    "    m_Position: {x: 30, y: -40, z: 0}\n" +
                                    "  m_ChildStateMachines: []\n" +
                                    "  m_AnyStateTransitions:\n" +
                                    "  - {fileID: "+IDS[7]+"}\n" +
                                    "  m_EntryTransitions: []\n" +
                                    "  m_StateMachineTransitions: {}\n" +
                                    "  m_StateMachineBehaviours: []\n" +
                                    "  m_AnyStatePosition: {x: 50, y: 20, z: 0}\n" +
                                    "  m_EntryPosition: {x: 50, y: 120, z: 0}\n" +
                                    "  m_ExitPosition: {x: 800, y: 120, z: 0}\n" +
                                    "  m_ParentStateMachinePosition: {x: 800, y: 20, z: 0}\n" +
                                    "  m_DefaultState: {fileID: "+IDS[10]+"}\n" +
                                    "--- !u!1101 &"+IDS[7]+"\n" +
                                    "AnimatorStateTransition:\n" +
                                    "  m_ObjectHideFlags: 1\n" +
                                    "  m_CorrespondingSourceObject: {fileID: 0}\n" +
                                    "  m_PrefabInstance: {fileID: 0}\n" +
                                    "  m_PrefabAsset: {fileID: 0}\n" +
                                    "  m_Name: \n" +
                                    "  m_Conditions:\n" +
                                    "  - m_ConditionMode: 6\n" +
                                    "    m_ConditionEvent: X\n" +
                                    "    m_EventTreshold: "+x+"\n" +
                                    "  - m_ConditionMode: 6\n" +
                                    "    m_ConditionEvent: Y\n" +
                                    "    m_EventTreshold: "+y+"\n" +
                                    "  m_DstStateMachine: {fileID: 0}\n" +
                                    "  m_DstState: {fileID: "+IDS[9]+"}\n" +
                                    "  m_Solo: 0\n" +
                                    "  m_Mute: 0\n" +
                                    "  m_IsExit: 0\n" +
                                    "  serializedVersion: 3\n" +
                                    "  m_TransitionDuration: 0\n" +
                                    "  m_TransitionOffset: 0\n" +
                                    "  m_ExitTime: 0\n" +
                                    "  m_HasExitTime: 0\n" +
                                    "  m_HasFixedDuration: 1\n" +
                                    "  m_InterruptionSource: 0\n" +
                                    "  m_OrderedInterruption: 1\n" +
                                    "  m_CanTransitionToSelf: 1\n" +
                                    "  \n" +
                                    "  --- !u!206 &"+IDS[8]+"\n" +
                                    "BlendTree:\n" +
                                    "  m_ObjectHideFlags: 1\n" +
                                    "  m_CorrespondingSourceObject: {fileID: 0}\n" +
                                    "  m_PrefabInstance: {fileID: 0}\n" +
                                    "  m_PrefabAsset: {fileID: 0}\n" +
                                    "  m_Name: BlendTree\n" +
                                    "  m_Childs:\n" +
                                    "  - serializedVersion: 2\n" +
                                    "    m_Motion: {fileID: 7400000, guid: "+SB_Guild+", type: 2}\n" +
                                    "    m_Threshold: 0\n" +
                                    "    m_Position: {x: 0, y: 0}\n" +
                                    "    m_TimeScale: 1\n" +
                                    "    m_CycleOffset: 0\n" +
                                    "    m_DirectBlendParameter: SWB\n" +
                                    "    m_Mirror: 0\n" +
                                    "  - serializedVersion: 2\n" +
                                    "    m_Motion: {fileID: 7400000, guid: "+B_Guild+", type: 2}\n" +
                                    "    m_Threshold: 1\n" +
                                    "    m_Position: {x: 0, y: 0}\n" +
                                    "    m_TimeScale: 1\n" +
                                    "    m_CycleOffset: 0\n" +
                                    "    m_DirectBlendParameter: SWB\n" +
                                    "    m_Mirror: 0\n" +
                                    "  m_BlendParameter: S\n" +
                                    "  m_BlendParameterY: Blend\n" +
                                    "  m_MinThreshold: 0\n" +
                                    "  m_MaxThreshold: 1\n" +
                                    "  m_UseAutomaticThresholds: 1\n" +
                                    "  m_NormalizedBlendValues: 0\n" +
                                    "  m_BlendType: 0\n" +
                                    "--- !u!1102 &"+IDS[9]+"\n" +
                                    "AnimatorState:\n" +
                                    "  serializedVersion: 6\n" +
                                    "  m_ObjectHideFlags: 1\n" +
                                    "  m_CorrespondingSourceObject: {fileID: 0}\n" +
                                    "  m_PrefabInstance: {fileID: 0}\n" +
                                    "  m_PrefabAsset: {fileID: 0}\n" +
                                    "  m_Name: Pixel_"+x+"_"+y+"\n" +
                                    "  m_Speed: 1\n" +
                                    "  m_CycleOffset: 0\n" +
                                    "  m_Transitions: []\n" +
                                    "  m_StateMachineBehaviours: []\n" +
                                    "  m_Position: {x: 50, y: 50, z: 0}\n" +
                                    "  m_IKOnFeet: 0\n" +
                                    "  m_WriteDefaultValues: 0\n" +
                                    "  m_Mirror: 0\n" +
                                    "  m_SpeedParameterActive: 0\n" +
                                    "  m_MirrorParameterActive: 0\n" +
                                    "  m_CycleOffsetParameterActive: 0\n" +
                                    "  m_TimeParameterActive: 0\n" +
                                    "  m_Motion: {fileID: "+IDS[4]+"}\n" +
                                    "  m_Tag: \n" +
                                    "  m_SpeedParameter: \n" +
                                    "  m_MirrorParameter: \n" +
                                    "  m_CycleOffsetParameter: \n" +
                                    "  m_TimeParameter: \n"
                    );
                }catch(Exception ignored){}


                fwR.append(str);
                fwR.flush();
            }
        }
    }

}