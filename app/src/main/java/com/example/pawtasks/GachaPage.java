package com.example.pawtasks;

import com.example.pawtasks.GachaMachine;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

public class GachaPage extends Fragment implements View.OnClickListener {

    public GachaPage() {
        // Required empty public constructor
    }

    public GachaPage(String savedTokenCount) {
        tokenCount = Integer.valueOf(savedTokenCount);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inf = inflater.inflate(R.layout.fragment_gacha_page, container, false);

        // For adding tokens
        addTokenButton = (Button) inf.findViewById(R.id.addTokens_button);
        addTokenButton.setOnClickListener(this);

        // For pulling dogs
        pullButton = (Button) inf.findViewById(R.id.gachaPull_button);
        pullButton.setOnClickListener(this);

        // Enable the Pull Button if tokenCount > 0
        if (tokenCount > 0)
        {
            pullButton.setEnabled(true);
        } else {
            pullButton.setEnabled(false);
        }

        // Set TokenCount label
        tokenCountLabel = (TextView) inf.findViewById(R.id.gachaTokenCount_text);
        tokenCountLabel.setText(String.valueOf(tokenCount));

        return inf;
    }

    @Override
    public void onClick(View v)
    {
        // Add token button click activity
        if (v.getId() == R.id.addTokens_button)
        {
            tokenCountLabel.setText(String.valueOf(++tokenCount));

            // Check if tokenCount exceeds 0
            if (tokenCount > 0)
            {
                pullButton.setEnabled(true);
            }
        }

        // Pull button click activity
        else if (v.getId() == R.id.gachaPull_button)
        {
            // Pull from gacha machine if token count > 0
            if (tokenCount > 0) {
                tokenCountLabel.setText(String.valueOf(tokenCount - 1));
                gachaMachine.pull();
                tokenCount--;
            }
            // Check if tokenCount becomes 0
            if (tokenCount == 0)
            {
                pullButton.setEnabled(false);
            }
        }
    }

    int tokenCount = 9;
    private TextView tokenCountLabel;
    private Button addTokenButton;
    private Button pullButton;
    private GachaMachine gachaMachine;

}