package org.GameEraser.Catalyss;

import io.github.vrchatapi.ApiClient;
import io.github.vrchatapi.ApiException;
import io.github.vrchatapi.Configuration;
import io.github.vrchatapi.api.AuthenticationApi;
import io.github.vrchatapi.auth.HttpBasicAuth;
import io.github.vrchatapi.model.CurrentUser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class VRCTrackerFormationg implements Runnable{
    @Override
    public void run() {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        AuthenticationApi authApi = new AuthenticationApi(defaultClient);

        HttpBasicAuth authHeader = (HttpBasicAuth) defaultClient.getAuthentication("authHeader");
        authHeader.setUsername("knee melter");
        authHeader.setPassword("recupliam@gmail.com");

        CurrentUser result = null;
        try {
            result = authApi.getCurrentUser();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

        System.out.println(result.getDisplayName());
        /* 2114 1728 1342 956 864
        {"selectedAvatars":
[{"authorId":"null",
"authorName":"null",
"created_at":"null",
"description":"null",
"id":"avtr_32c9cc16-f35b-4288-8676-9e72d543de403",
"imageUrl":"null",
"isMobileAccessible":true,
"isPCAccessible":true,
"name":"null",
"releaseStatus":"public",
"thumbnailImageUrl":"null",
"version":"null"},
         */
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
        JSONArray Avi = new JSONArray();
        int x = 0;
        int R = 0;
        for (String st : str.split(";")
        ) {
            URL url = null;
            if (!st.contains("avtr")) continue;
            try {
                x = x + 1;
                R = R + 1;
                boolean has = false;
                String strs="";
                try {
                    FileReader fr = new FileReader("VRCTracker_Cache_List.Json");
                    JSONParser parser = new JSONParser();
                    JSONObject Json = (JSONObject) parser.parse(fr);
                    JSONArray array = (JSONArray) Json.get("selectedAvatars");
                    strs = st.replace(";", "");
                    for (Object o : array) {
                        JSONObject jst = (JSONObject) o;
                        if (!jst.containsKey("id")) continue;
                        String strst = jst.get("id").toString();
                        if (strs.contains(strst)) {
                            has = true;
                        }
                    }
                } catch (Exception e) {
                }

                if (has) System.out.println("Contain ID - avtr_" + strs);
                if (has) continue;

                url = new URL("https://api.vrchat.cloud/api/1/avatars/" + st.replace(";", "").replace(System.getProperty("line.separator"), ""));
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setRequestMethod("GET");
                httpConn.setRequestProperty("Cookie", "apiKey=JlE5Jldo5Jibnk5O5hTx6XVqsJu4WJ26; auth=" + authApi.verifyAuthToken().getToken());
                InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                        ? httpConn.getInputStream()
                        : httpConn.getErrorStream();
                Scanner s = new Scanner(responseStream).useDelimiter("\\A");
                String response = s.hasNext() ? s.next() : "";

                System.out.println(str.split(";").length - x);
                Avi.add(Writer(response, st.replace(";", "").replace(System.getProperty("line.separator"), "")));
            } catch (Exception e) {
                break;
            }

        }
        try {
            try {
                FileReader fr = new FileReader("VRCTracker_Cache_List.Json");
                JSONParser parser = new JSONParser();
                JSONObject Json = (JSONObject) parser.parse(fr);
                JSONArray array = (JSONArray) Json.get("selectedAvatars");
                Avi.addAll(array);
            } catch (Exception ignored) {}
            JSONObject js = new JSONObject();
            js.put("selectedAvatars", Avi);
            FileWriter fwr = new FileWriter("VRCTracker_Cache_List.Json", false);
            fwr.append(js.toString());
            fwr.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public JSONObject Writer(String str,String Id) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject)parser.parse(str);
        JSONObject main= new JSONObject();
        if(!json.containsKey("id")) main.put("id",Id);
        if(!json.containsKey("id")) main.put("description",json.get("•◘-Invalid Avatar Id-◘•"));
        try {
            if(json.containsKey("id")) main.put("description", json.get("description"));
            if(json.containsKey("id")) main.put("id", json.get("id"));
            main.put("authorId", json.get("authorId"));
            main.put("authorName", json.get("authorName"));
            main.put("created_at", json.get("created_at"));
            main.put("imageUrl", json.get("imageUrl"));
            main.put("isMobileAccessible", true);
            main.put("isPCAccessible", true);
            main.put("name", json.get("name"));
            main.put("releaseStatus", json.get("releaseStatus"));
            main.put("thumbnailImageUrl", json.get("thumbnailImageUrl"));
            main.put("version", json.get("version"));
        }catch (Exception ignored){}
        return main;
    }
    public String Relog(AuthenticationApi authApiBase) throws ApiException, InterruptedException {
        System.out.println(authApiBase.logout().getSuccess());
        sleep(3000);
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        AuthenticationApi authApi = new AuthenticationApi(defaultClient);

        HttpBasicAuth authHeader = (HttpBasicAuth) defaultClient.getAuthentication("authHeader");
        authHeader.setUsername("knee melter");
        authHeader.setPassword("recupliam@gmail.com");

        CurrentUser result = null;
        try {
            result = authApi.getCurrentUser();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        return authApi.verifyAuthToken().getToken();
    }
    public String ReSend(URL url,AuthenticationApi authApi){
        try {
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");

            httpConn.setRequestProperty("Cookie", "apiKey=JlE5Jldo5Jibnk5O5hTx6XVqsJu4WJ26; auth=" + authApi.verifyAuthToken().getToken());

            InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                    ? httpConn.getInputStream()
                    : httpConn.getErrorStream();
            Scanner s = new Scanner(responseStream).useDelimiter("\\A");
            String response = s.hasNext() ? s.next() : "";
            //System.out.println(response);
            return response;
        }catch (Exception e){System.out.println(e);}
        return null;
    }
}
