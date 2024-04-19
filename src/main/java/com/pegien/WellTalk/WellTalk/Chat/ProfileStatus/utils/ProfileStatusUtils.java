package com.pegien.WellTalk.WellTalk.Chat.ProfileStatus.utils;

import java.util.concurrent.TimeUnit;

public class ProfileStatusUtils {

    public static String millisToComment(Long millis){
        if(millis< TimeUnit.MINUTES.toMillis(1))
            return String.format("%d seconds ago",TimeUnit.MILLISECONDS.toSeconds(millis));
        else if(millis < TimeUnit.HOURS.toMillis(1))
            return String.format("%d minutes ago",TimeUnit.MILLISECONDS.toMinutes(millis));
        else
            return "";
    }

}
