package vn.edu.csc.babyapp;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.shawnlin.numberpicker.NumberPicker;

import java.util.ArrayList;

import controller.CommunicateFragment;


public class ManualTimeFragment extends Fragment {
    private static ManualTimeFragment instanceManual;

    ArrayList<Integer> fullTime;
    ArrayList<Integer> hourArray;
    ArrayList<Integer> minuteArray;

    SparseIntArray manualTimeData;

    CommunicateFragment communicateFragment;

    public ManualTimeFragment() {
        // Required empty public constructor
    }

    public static ManualTimeFragment getInstance() {
        if (instanceManual == null) {
            instanceManual = new ManualTimeFragment();
        }
        return instanceManual;
    }

    public static ManualTimeFragment getInstance(String param1, String param2) {
        ManualTimeFragment manualTimeFragment = new ManualTimeFragment();
        Bundle args = new Bundle();
        args.putString("KEY_ONE", param1);
        args.putString("KEY_TWO", param2);
        manualTimeFragment.setArguments(args);
        return manualTimeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.fragment_manual_time, container, false);

        fullTime = new ArrayList<>();
        hourArray = new ArrayList<>();
        minuteArray = new ArrayList<>();

        final com.shawnlin.numberpicker.NumberPicker hours = view.findViewById(R.id.number_picker);
        final com.shawnlin.numberpicker.NumberPicker minutes = view.findViewById(R.id.number_picker2);

        hours.setMaxValue(10);
        hours.setMinValue(0);

        minutes.setMaxValue(30);
        minutes.setMinValue(0);

        hours.setOnValueChangedListener(new com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(com.shawnlin.numberpicker.NumberPicker picker, int oldVal, int newVal) {
                hourArray.clear();
                hourArray.add(newVal);
                communicateFragment.sendHoursManualData(hourArray);
            }
        });

        minutes.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                minuteArray.clear();
                minuteArray.add(newVal);
                communicateFragment.sendMinutesManualData(minuteArray);
            }
        });

        hours.setValue(0);
        minutes.setValue(0);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof UpdateAcitivity) {
            communicateFragment = (CommunicateFragment) context;
        }
    }
}
