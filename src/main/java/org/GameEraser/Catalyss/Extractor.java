package org.GameEraser.Catalyss;

import com.illposed.osc.OSCSerializeException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Extractor implements Runnable{
    public boolean locked =false;
    @Override
    public void run() {
        File folder = new File(System.getProperty("user.home")+"\\AppData\\LocalLow\\VRChat\\VRChat\\Cache-WindowsPlayer\\");
        File[] listOfFiles = folder.listFiles();
        JFrame frame = new JFrame("Cache Tool");
        JLabel label = new JLabel("Cleaning 0/"+listOfFiles.length+" Files");
        label.setText("Cleaning 0/"+listOfFiles.length+" Files");
        JSlider slider= new JSlider();
        JPanel panel = new JPanel();
        slider.setValue(0);
        slider.setEnabled(false);
        slider.setMinimum(0);
        slider.setMaximum(listOfFiles.length);
        label.setFont(new Font("Arial", Font.PLAIN, 30));
        slider.setForeground(Color.GREEN);
        panel.add(label);
        panel.add(slider);
        panel.setPreferredSize (new Dimension(400, 70));
        BoxLayout layout = new BoxLayout (panel, BoxLayout.Y_AXIS);
        panel.setLayout (layout);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        StringBuilder str = new StringBuilder();
        System.out.println(listOfFiles.length);
        for (int i = 0; i < listOfFiles.length; i++) {
            locked=false;
            System.out.println(i);
            if(listOfFiles[i].isDirectory()){
                File[] files = listOfFiles[i].listFiles();
                for (int is = 0; is < files.length; is++) {
                    if(files[is].isDirectory()){
                        File[] files2 = files[is].listFiles();
                        for (int iss = 0; iss < files2.length; iss++) {
                            if(files2[iss].isFile()){
                                if(files2[iss].getName().equals("__lock")){
                                    locked=true;
                                }
                                if(files2[iss].getName().equals("__data")){
                                    // System.out.println(listOfFilesss[ids].getAbsolutePath());
                                    Path filePath = Path.of(files2[iss].getAbsolutePath());
                                    String fileContent = "";
                                    String a = "";
                                    try
                                    {
                                        byte[] bytes = Files.readAllBytes(Paths.get(filePath.toUri()));
                                        fileContent = new String (bytes);
                                        String requiredString = fileContent.substring(fileContent.indexOf("prefab-id-v1_avtr_"),fileContent.indexOf("prefab-id-v1_avtr_")+65-11 );
                                        String replace = requiredString.replace("prefab-id-v1_", "\n;").replace("�", "").replace(".", "");
                                        byte[] bytes2 = Files.readAllBytes(Path.of("IDS.txt"));
                                        a = new String (bytes2);
                                        if(a.contains(replace)) {
                                            System.out.println("Already Exist");
                                            continue;
                                        }
                                        str.append(replace);
                                    }
                                    catch (Exception e)
                                    {
                                        //e.printStackTrace();
                                    }

                                }
                            }
                        }
                    }
                }
                if(!locked){
                    System.out.println(listOfFiles[i].delete());
                    listOfFiles[i].renameTo(new File("dele"+i));
                    File f =new File(Path.of("dele"+i).toUri());
                    try {
                        delete(f);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                slider.setValue(i);
                label.setText("Cleaning "+i+"/"+listOfFiles.length+" Files");
            }


        }

        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("IDS.txt"));
            String line = null;
            String ls = System.getProperty("line.separator");
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            reader.close();

        } catch (IOException e) {
        }
        FileWriter fwr = null;
        try {
            fwr = new FileWriter("IDS.txt");
        } catch (IOException e) {
        }
        try {
            fwr.write(stringBuilder+str.toString());
        } catch (IOException e) {
        }
        try {
            fwr.flush();
        } catch (IOException e) {
        }
        frame.setVisible(true);
        try {
            Thread.sleep(80);
        } catch (InterruptedException e) {
        }
        frame.dispose();
    }
    public static void delete(File file)
            throws IOException{

        if(file.isDirectory()){

            //directory is empty, then delete it
            if(file.list().length==0){

                file.delete();
                System.out.println("Directory is deleted : "
                        + file.getAbsolutePath());

            }else{

                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    delete(fileDelete);
                }

                //check the directory again, if empty then delete it
                if(file.list().length==0){
                    file.delete();
                    System.out.println("Directory is deleted : "
                            + file.getAbsolutePath());
                }
            }

        }else{
            //if file, then delete it
            file.delete();
            System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }
}
