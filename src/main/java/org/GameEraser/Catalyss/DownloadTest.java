package org.GameEraser.Catalyss;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadTest implements Runnable{
    @Override
    public void run() {
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
        String str = String.valueOf(stringBuilder);

        for (String st:str.split(";")
             ) {
            URL url = null;
            if(!st.contains("avtr"))continue;
            try {
                url = new URL("https://api.ripper.store/api/v1/id-downloader?id="+st.replace(";","").replace(System.getProperty("line.separator"),""));
            } catch (MalformedURLException e) {
            }
            try {
                url.openStream().transferTo(new FileOutputStream(new File("Avatar/"+st.replace(";","").replace(System.getProperty("line.separator"),"")+".vrca")));
            } catch (IOException e) {
            }
        }

    }
}
