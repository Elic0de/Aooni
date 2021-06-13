package com.elic0de.aooni.util.format;

public class TimeFormat {

    public static String secondsToTimeString(int seconds) {
        String returnFormat = "simplified";
        int secs;
        int mins = 0;

        if (seconds < 60) {
            secs = seconds;
        } else {
            mins = seconds / 60;
            secs = seconds % 60;
        }

        if (returnFormat.equals("simplified")) {
            if (mins > 0) {
                return mins + ":" + (secs < 10 ? "0" + secs : secs);
            }
            else {
                return secs + "";
            }
        }
        if (returnFormat.equals("simplified-zeros")) {
            if (mins > 0) {
                return (mins < 10 ? "0" + mins : mins) + ":" + (secs < 10 ? "0" + secs : secs);
            }
            else {
                return secs + "";
            }
        }

        returnFormat = returnFormat.replaceAll("(?<!\\\\)mm", mins < 10 ? "0" + mins : mins +"");
        returnFormat = returnFormat.replaceAll("(?<!\\\\)m", mins + "");
        returnFormat = returnFormat.replaceAll("(?<!\\\\)ss", secs < 10 ? "0" + secs : secs +"");
        returnFormat = returnFormat.replaceAll("(?<!\\\\)s", secs + "");

        return returnFormat;
    }
}
