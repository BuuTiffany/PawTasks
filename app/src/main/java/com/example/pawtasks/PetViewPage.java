package com.example.pawtasks;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Button;


public class PetViewPage extends Fragment implements View.OnClickListener {

    private TextView tokenCountLabel;
    private Button walkButton;
    private Button feedButton;
    private Button danceButton;
    private Button sitStandButton;
    private PawTasksViewModel viewModel;

    public PetViewPage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inf = inflater.inflate(R.layout.fragment_pet_view_page, container, false);

        // Get the token count
        viewModel = new ViewModelProvider(requireActivity()).get(PawTasksViewModel.class);

        // Set TokenCount label
        tokenCountLabel = (TextView) inf.findViewById(R.id.petTokenCount_text);
        tokenCountLabel.setText(String.valueOf(viewModel.getTokenCount()));

        // Set buttons
        walkButton = (Button) inf.findViewById(R.id.walk_button);
        walkButton.setOnClickListener(this);
        feedButton = (Button) inf.findViewById(R.id.feed_button);
        feedButton.setOnClickListener(this);
        danceButton = (Button) inf.findViewById(R.id.dance_button);
        danceButton.setOnClickListener(this);
        sitStandButton = (Button) inf.findViewById(R.id.sitstand_button);
        sitStandButton.setOnClickListener(this);

        return inf;
    }

    @Override
    public void onClick(View v)
    {
        // Feed button
        if(v.getId() == R.id.feed_button)
        {
            if (viewModel.getTokenCount() > 0) {
                viewModel.decrementTokenCount();
            }
        }

        // Walk button
        else if (v.getId() == R.id.walk_button)
        {
            if (viewModel.getTokenCount() > 0) {
                viewModel.decrementTokenCount();
            }
        }

        // Dance button
        else if (v.getId() == R.id.dance_button)
        {
            if (viewModel.getTokenCount() > 0) {
                viewModel.decrementTokenCount();
            }
        }

        // Sit/Stand button
        else if (v.getId() == R.id.sitstand_button)
        {

        }
    }
}