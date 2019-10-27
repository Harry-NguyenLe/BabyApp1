package vn.edu.csc.babyapp;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import controller.CommunicateFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuickTimeFragment extends Fragment {

    ArrayList<Integer> listRadioMinute;
    CommunicateFragment communicateFragment;
    public static int REQ_CODE_COUNT_DOWN_TIME = 1003;
    private static QuickTimeFragment instanceQuickTime;

    public QuickTimeFragment() {
        // Required empty public constructor
    }

    public static QuickTimeFragment getInstance(String param1, String param2) {
        QuickTimeFragment quickTimeFragment = new QuickTimeFragment();
        Bundle args = new Bundle();
        args.putString("KEY_ONE", param1);
        args.putString("KEY_TWO", param2);
        quickTimeFragment.setArguments(args);
        return quickTimeFragment;
    }

    public static QuickTimeFragment getInstance() {
        if (instanceQuickTime == null) {
            instanceQuickTime = new QuickTimeFragment();
        }
        return instanceQuickTime;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View quickTimeFragmentView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_quick_time, container, false);

        RadioButton radio5, radio15, radio20, radio25, radio45, radio95, radio120, radio180;

        radio5 = quickTimeFragmentView.findViewById(R.id.radio5);
        radio15 = quickTimeFragmentView.findViewById(R.id.radio15);
        radio20 = quickTimeFragmentView.findViewById(R.id.radio20);
        radio25 = quickTimeFragmentView.findViewById(R.id.radio25);
        radio45 = quickTimeFragmentView.findViewById(R.id.radio45);
        radio95 = quickTimeFragmentView.findViewById(R.id.radio95);

        listRadioMinute = new ArrayList<>();

        radio5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listRadioMinute.clear();
                listRadioMinute.add(5);
                communicateFragment.sendTimeQuickData(listRadioMinute);
            }
        });

        radio15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listRadioMinute.clear();
                listRadioMinute.add(15);
                communicateFragment.sendTimeQuickData(listRadioMinute);
            }
        });

        radio20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listRadioMinute.clear();
                listRadioMinute.add(20);
                communicateFragment.sendTimeQuickData(listRadioMinute);
            }
        });

        radio25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listRadioMinute.clear();
                listRadioMinute.add(25);
                communicateFragment.sendTimeQuickData(listRadioMinute);
            }
        });

        radio45.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listRadioMinute.clear();
                listRadioMinute.add(45);
                communicateFragment.sendTimeQuickData(listRadioMinute);
            }
        });

        radio95.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listRadioMinute.clear();
                listRadioMinute.add(95);
                communicateFragment.sendTimeQuickData(listRadioMinute);
            }
        });


        return quickTimeFragmentView;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof UpdateAcitivity) {
            communicateFragment = (CommunicateFragment) context;
        }
    }


}





