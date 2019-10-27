package controller;


import java.util.ArrayList;

public interface CommunicateFragment {
     int sendTimeQuickData(ArrayList<Integer> listTimeInQuick);

     int sendHoursManualData(ArrayList<Integer> listHoursManual);

     int sendMinutesManualData(ArrayList<Integer> listMinutesManual);
}
