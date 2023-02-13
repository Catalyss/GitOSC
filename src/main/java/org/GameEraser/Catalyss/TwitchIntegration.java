package org.GameEraser.Catalyss;

import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;
import com.cavariux.twitchirc.Core.TwitchBot;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class TwitchIntegration extends TwitchBot implements Runnable, TwitchIntegration_Onmessage {
    //oauth:ofrobybr21orgs030jsbsn24ngcgbvS
    //m7l69psy09o2ano6ckyn9spom8dv5x
    @Override
    public void run() {
        JSONObject Json = new JSONObject();
        try {
            FileReader fr = new FileReader("src/resources/Settings.json");
            JSONParser parser = new JSONParser();
            Json = (JSONObject) parser.parse(fr);
        } catch (Exception ignored) {
        }

        if (Json.get("TWITCH_TG").toString().equals("false")) ErrorPanel.Senderror("Twitch Integration is Off");
        if (Json.get("TWITCH_Username").toString().equals("")) ErrorPanel.Senderror("Fill Username in settings");
        if (Json.get("TWITCH_Oauth_Key").toString().equals("")) ErrorPanel.Senderror("Fill Oauth_Key in settings");
        if (Json.get("TWITCH_ClientID").toString().equals("")) ErrorPanel.Senderror("Fill ClientID in settings");

        this.setUsername(Json.get("TWITCH_Username").toString()); //this.setUsername("CavsBot");
        this.setOauth_Key(Json.get("TWITCH_Oauth_Key").toString()); //this.setOauth_Key("oauth:9kvd020oj3wgcrsyafoaofrt3uv7bi");
        this.setClientID(Json.get("TWITCH_ClientID").toString()); //this.setClientID("axjhfp777tflhy0yjb5sftsil");
        this.connect();
        Channel ch = this.joinChannel("#channel");
        this.sendMessage("A", ch);
        this.start();
        TwitchIntegration_Onmessage bot = this;

    }

    @Override
    public void onMessage(User user, Channel channel, String message) {
        if (message.equalsIgnoreCase("hello"))
            if (channel.isMod(user))
                this.sendMessage("Hi there Mod " + user, channel);
            else
                this.sendMessage("Hi there " + user, channel);
    }
}
