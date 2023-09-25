package com.example.pawtasks;

//import com.example.pawtasks.GachaMachine;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Button;

public class GachaPage extends Fragment implements View.OnClickListener {

    public GachaPage() {
        // Required empty public constructor
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
                showPull(gachaMachine.pull());
                tokenCount--;
            }
            // Check if tokenCount becomes 0
            if (tokenCount == 0)
            {
                pullButton.setEnabled(false);
            }
        }
    }

    private void showPull(Rarity rarity) {
        Context context = requireContext();
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        Button okButton;

        // Determine which layout we need to look at
        switch (rarity)
        {
            case COMMON:
                dialog.setContentView(R.layout.common_pull);
                okButton = dialog.findViewById(R.id.CommonDialogButton);
                break;

            case RARE:
                dialog.setContentView(R.layout.rare_pull);
                okButton = dialog.findViewById(R.id.RareDialogButton);
                break;

            case EPIC:
                dialog.setContentView(R.layout.epic_pull);
                okButton = dialog.findViewById(R.id.EpicDialogButton);
                break;

            case LEGENDARY:
                dialog.setContentView(R.layout.legendary_pull);
                okButton = dialog.findViewById(R.id.LegendaryDialogButton);
                break;

            default:
                dialog.setContentView(R.layout.common_pull);
                okButton = dialog.findViewById(R.id.CommonDialogButton);
                break;
        }

        // Make sure the ok button will close the popup
        okButton.setOnClickListener((view -> {
            dialog.dismiss();
        }));

        // Show the popup
        dialog.show();
    }

    int tokenCount = 9;
    private TextView tokenCountLabel;
    private Button addTokenButton;
    private Button pullButton;
    private GachaMachine gachaMachine = new GachaMachine();

}