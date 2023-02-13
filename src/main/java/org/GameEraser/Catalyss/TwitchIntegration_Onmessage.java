package org.GameEraser.Catalyss;

import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;

public interface TwitchIntegration_Onmessage {
    void onMessage(User user, Channel channel, String message);
}
