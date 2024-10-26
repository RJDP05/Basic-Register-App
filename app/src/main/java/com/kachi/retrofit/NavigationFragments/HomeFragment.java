package com.kachi.retrofit.NavigationFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kachi.retrofit.R;
import com.kachi.retrofit.SharedPreference;


public class HomeFragment extends Fragment {

    TextView txt_name,txt_email,txt_gender;
    SharedPreference sharedPreference;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        txt_name = view.findViewById(R.id.txt_name);
        txt_email = view.findViewById(R.id.txt_email);
        txt_gender = view.findViewById(R.id.txt_gender);

        sharedPreference = new SharedPreference(getActivity());

        txt_name.setText(sharedPreference.getUser().getName());
        txt_email.setText(sharedPreference.getUser().getEmail());
        txt_gender.setText(sharedPreference.getUser().getGender());

        return view;
    }
}