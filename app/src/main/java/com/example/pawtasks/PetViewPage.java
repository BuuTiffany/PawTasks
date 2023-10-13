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

        // Buttons are only clickable if the user has tokens
        if (viewModel.getTokenCount() != 0)
        {
            walkButton.setEnabled(true);
            danceButton.setEnabled(true);
            feedButton.setEnabled(true);
        }

        // Set TokenCount label
        tokenCountLabel = (TextView) inf.findViewById(R.id.petTokenCount_text);
        tokenCountLabel.setText(String.valueOf(viewModel.getTokenCount()));

        return inf;
    }

    @Override
    public void onClick(View v)
    {
        boolean noTokens = (viewModel.getTokenCount() == 0);

        // Feed button
        if(v.getId() == R.id.feed_button)
        {
            if (!noTokens) {
                viewModel.decrementTokenCount();
                tokenCountLabel.setText(String.valueOf(viewModel.getTokenCount()));
                noTokens = (viewModel.getTokenCount() == 0);
            }
        }

        // Walk button
        else if (v.getId() == R.id.walk_button)
        {
            if (!noTokens) {
                viewModel.decrementTokenCount();
                tokenCountLabel.setText(String.valueOf(viewModel.getTokenCount()));
                noTokens = (viewModel.getTokenCount() == 0);
            }
        }

        // Dance button
        else if (v.getId() == R.id.dance_button)
        {
            if (!noTokens) {
                viewModel.decrementTokenCount();
                tokenCountLabel.setText(String.valueOf(viewModel.getTokenCount()));
                noTokens = (viewModel.getTokenCount() == 0);
            }
        }

        // Sit/Stand button
        else if (v.getId() == R.id.sitstand_button)
        {
            if (sitStandButton.getText() == getString(R.string.sitButtonText))
            {
                sitStandButton.setText(R.string.standButtonText);
            } else {
                sitStandButton.setText(R.string.sitButtonText);
            }
        }

        if (noTokens)
        {
            walkButton.setEnabled(false);
            danceButton.setEnabled(false);
            feedButton.setEnabled(false);
        }
    }
}