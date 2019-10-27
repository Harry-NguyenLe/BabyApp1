package controller;

import java.util.ArrayList;

public class ConverterTimer {

    public static ArrayList<String> convertTime(long timer) {
        //Convert timer to string
        int secs = (int) (timer / 1000);
        int mins = (int) ((timer / 1000) / 60);
        int hrs = (int) (((timer / 1000) / 60) / 60);

        /* Convert the seconds to String
         * and format to ensure it has
         * a leading zero when required
         */

        secs = secs % 60;
        String seconds = String.valueOf(secs);
        if (secs == 0) {
            seconds = "00";
        }
        if (secs < 10 && secs > 0) {
            seconds = "0" + seconds;
        }

        /* Convert the minutes to String and format the String */

        mins = mins % 60;
        String minutes = String.valueOf(mins);
        if (mins == 0) {
            minutes = "00";
        }
        if (mins < 10 && mins > 0) {
            minutes = "0" + minutes;
        }

        /* Convert the hours to String and format the String */

        String hours = String.valueOf(hrs);
        if (hrs == 0) {
            hours = "00";
        }
        if (hrs < 10 && hrs > 0) {
            hours = "0" + hours;
        }

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(0, hours);
        arrayList.add(1, minutes);
        arrayList.add(2, seconds);

        return arrayList;
    }
}
