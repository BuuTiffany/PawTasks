package com.example.pawtasks;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.User;

import java.util.Random;

public class GachaPage extends Fragment implements View.OnClickListener {

    private TextView tokenCountLabel;
    private Button addTokenButton;
    private Button pullButton;
    private GachaMachine gachaMachine = new GachaMachine();
    private PawTasksViewModel viewModel;
    private ImageView RightImage;
    private ImageView LeftImage;

    public GachaPage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inf = inflater.inflate(R.layout.fragment_gacha_page, container, false);

        // Get the tokenCount from the view model
        viewModel = new ViewModelProvider(requireActivity()).get(PawTasksViewModel.class);

        // For adding tokens
//        addTokenButton = (Button) inf.findViewById(R.id.addTokens_button);
//        addTokenButton.setOnClickListener(this);

        // For pulling dogs
        pullButton = (Button) inf.findViewById(R.id.gachaPull_button);
        pullButton.setOnClickListener(this);
        RightImage = inf.findViewById(R.id.RightImage);
        LeftImage = inf.findViewById(R.id.LeftImage);

        // Enable the Pull Button if tokenCount > 0
        if (viewModel.getTokenCount() > 0)
        {
            pullButton.setEnabled(true);
        } else {
            pullButton.setEnabled(false);
        }

        // Set TokenCount label
        tokenCountLabel = (TextView) inf.findViewById(R.id.gachaTokenCount_text);
        //tokenCountLabel.setText(String.valueOf(viewModel.getTokenCount()));

        fetchUserTokenCount();

        int randomNumber1 = new Random().nextInt(30) + 1;
        String avatarResourceName = "avatar_" + randomNumber1;
        int resourceId = getResources().getIdentifier(avatarResourceName, "drawable", getActivity().getPackageName());
        RightImage.setImageResource(resourceId);

        int randomNumber2 = new Random().nextInt(30) + 1;
        String avatarResourceName2 = "avatar_" + randomNumber2;
        int resourceId2 = getResources().getIdentifier(avatarResourceName2, "drawable", getActivity().getPackageName());
        LeftImage.setImageResource(resourceId2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.inAppStatusBar));
        }

        return inf;
    }

    private void fetchUserTokenCount() {
        Amplify.Auth.getCurrentUser(
                currentUser -> {
                    if (currentUser != null) {
                        String userId = currentUser.getUserId();
        Amplify.API.query(
                ModelQuery.get(User.class, userId),
                response -> {
                    if(response.getData() != null) {
                        // Assuming 'User' class has a 'getTokens()' method.
                        int tokenCount = response.getData().getTokens();
                        getActivity().runOnUiThread(() -> {
                            viewModel.setTokenCount(tokenCount); // You need to implement setTokenCount in your viewModel
                            tokenCountLabel.setText(String.valueOf(tokenCount));
                            if (tokenCount > 0) {
                                pullButton.setEnabled(true);
                            } else {
                                pullButton.setEnabled(false);
                            }
                        });
                    }
                },
                error -> Log.e("GachaPage", "Query failure", error)
        );
                    }
                },
                error -> Log.e("MyAmplifyApp", "Get current user failed", error)
        );
    }

    @Override
    public void onClick(View v)
    {
        // Add token button click activity
//        if (v.getId() == R.id.addTokens_button)
//        {
//            viewModel.incrementTokenCount();
//            tokenCountLabel.setText(String.valueOf(viewModel.getTokenCount()));
//
//            // Check if tokenCount exceeds 0
//            if (viewModel.getTokenCount() > 0)
//            {
//                pullButton.setEnabled(true);
//            }
//        }

        // Pull button click activity
        if (v.getId() == R.id.gachaPull_button)
        {
            // Pull from gacha machine if token count > 0
            if (viewModel.getTokenCount() > 0) {
                Pet adoptedPet = gachaMachine.pull();
                showPull(adoptedPet);
                viewModel.addPet(adoptedPet);
                viewModel.decrementTokenCount();
                tokenCountLabel.setText(String.valueOf(viewModel.getTokenCount()));
            }
            // Check if tokenCount becomes 0
            if (viewModel.getTokenCount() == 0)
            {
                pullButton.setEnabled(false);
            }

            Amplify.Auth.getCurrentUser(
                    currentUser -> {
                        if (currentUser != null) {
                            String userId = currentUser.getUserId();
                            Amplify.API.query(
                    ModelQuery.get(User.class, userId),
                    response -> {
                        if (response.getData() != null) {
                            User user = response.getData();
                            // Assuming you have decremented the local token count already
                            User updatedUser = user.copyOfBuilder()
                                    .tokens(viewModel.getTokenCount())
                                    .build();
                            Amplify.API.mutate(
                                    ModelMutation.update(updatedUser),
                                    response1 -> Log.i("GachaPage", "User tokens updated"),
                                    error -> Log.e("GachaPage", "Failed to update user tokens", error)
                            );
                        }
                    },
                    error -> Log.e("GachaPage", "Query failed", error)
            );
                        }
                    },
                    error -> Log.e("MyAmplifyApp", "Get current user failed", error)
            );
        }
    }

    private void showPull(Pet pet) {
        Context context = requireContext();
        final Dialog dialog = new Dialog(context);
        ImageView petImageView;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        Button okButton;

        // Determine which layout we need to look at
        switch (pet.getRarity())
        {
            case COMMON:
                dialog.setContentView(R.layout.common_pull);
                okButton = dialog.findViewById(R.id.CommonDialogButton);

                // Set the image in the popup
                petImageView = dialog.findViewById(R.id.DialogImage);
                petImageView.setImageResource(pet.getImageResourceId());
                break;

            case RARE:
                dialog.setContentView(R.layout.rare_pull);
                okButton = dialog.findViewById(R.id.RareDialogButton);

                // Set the image in the popup
                petImageView = dialog.findViewById(R.id.DialogImage);
                petImageView.setImageResource(pet.getImageResourceId());
                break;

            case EPIC:
                dialog.setContentView(R.layout.epic_pull);
                okButton = dialog.findViewById(R.id.EpicDialogButton);

                // Set the image in the popup
                petImageView = dialog.findViewById(R.id.DialogImage);
                petImageView.setImageResource(pet.getImageResourceId());
                break;

            case LEGENDARY:
                dialog.setContentView(R.layout.legendary_pull);
                okButton = dialog.findViewById(R.id.LegendaryDialogButton);

                // Set the image in the popup
                petImageView = dialog.findViewById(R.id.DialogImage);
                petImageView.setImageResource(pet.getImageResourceId());
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

}