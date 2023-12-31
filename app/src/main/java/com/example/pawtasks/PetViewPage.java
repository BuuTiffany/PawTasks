package com.example.pawtasks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class PetViewPage extends Fragment implements View.OnClickListener {

    private TextView tokenCountLabel;
    private Button walkButton;
    private Button feedButton;
    private Button danceButton;
    private Button sitStandButton;
    private PawTasksViewModel viewModel;
    private ImageView currentPetImage;
    private ImageView petdexButton;
    private Pet currentPet;

    private ImageView avatar;
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

        avatar = inf.findViewById(R.id.avatar);

        // Set TokenCount label
        tokenCountLabel = (TextView) inf.findViewById(R.id.petTokenCount_text);
        //tokenCountLabel.setText(getString(R.string.loading));

        fetchAndSetUserTokens();

        // Set image view
        currentPet = viewModel.getFirstPet();
        if (currentPet != null) {
            currentPetImage = inf.findViewById(R.id.currentPetImage);
            currentPetImage.setImageResource(currentPet.getImageResourceId());
        }

        // Set buttons
        walkButton = (Button) inf.findViewById(R.id.walk_button);
        walkButton.setOnClickListener(this);
        feedButton = (Button) inf.findViewById(R.id.feed_button);
        feedButton.setOnClickListener(this);
        danceButton = (Button) inf.findViewById(R.id.dance_button);
        danceButton.setOnClickListener(this);
        sitStandButton = (Button) inf.findViewById(R.id.sitstand_button);
        sitStandButton.setOnClickListener(this);
        petdexButton = (ImageView) inf.findViewById(R.id.petdex_button);
        petdexButton.setOnClickListener(this);


        // Buttons are only clickable if the user has tokens
        if (viewModel.getTokenCount() != 0 && currentPet != null)
        {
            walkButton.setEnabled(true);
            danceButton.setEnabled(true);
            feedButton.setEnabled(true);
            sitStandButton.setEnabled(true);
        }

        // Set TokenCount label
        tokenCountLabel = (TextView) inf.findViewById(R.id.petTokenCount_text);
        tokenCountLabel.setText(String.valueOf(viewModel.getTokenCount()));

        int randomNumber2 = new Random().nextInt(30) + 1;
        String avatarResourceName2 = "avatar_" + randomNumber2;
        int resourceId2 = getResources().getIdentifier(avatarResourceName2, "drawable", getActivity().getPackageName());
        avatar.setImageResource(resourceId2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.inAppStatusBar));
        }

        return inf;
    }

    private void fetchAndSetUserTokens() {

        Amplify.Auth.getCurrentUser(
                currentUser -> {
                    if (currentUser != null) {
                        String userId = currentUser.getUserId();
                        Amplify.API.query(
                                ModelQuery.get(User.class, userId),
                                response -> {
                                    if (response.getData() != null) {
                                        User user = response.getData();
                                        int tokens = user.getTokens() != null ? user.getTokens() : 0;

                                        // Since the query runs on a background thread, you need to run UI updates on the main thread
                                        getActivity().runOnUiThread(() -> {
                                            viewModel.setTokenCount(tokens); // Update your viewModel with the fetched token count
                                            tokenCountLabel.setText(String.valueOf(tokens)); // Update the label with the fetched token count

                                            // Enable or disable buttons based on the token count
                                            boolean hasTokens = tokens > 0;
                                            walkButton.setEnabled(hasTokens);
                                            danceButton.setEnabled(hasTokens);
                                            feedButton.setEnabled(hasTokens);
                                            sitStandButton.setEnabled(true);

                                        });
                                    }
                                },
                                error -> Log.e("PetViewPage", "Failed to fetch user tokens", error)
                        );
                    }

                },
                error -> Log.e("MyAmplifyApp", "Get current user failed", error)
        );
    }

    private void showPetdexModal() {
        Context context = requireContext();
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.petdex_view);

        // Make sure the ok button will close the popup
        Button okButton = (Button) dialog.findViewById(R.id.petdexOk_button);

        // Add all the image buttons
        ImageView neeko = (ImageView) dialog.findViewById(R.id.neeko);
        ImageView naafiri = (ImageView) dialog.findViewById(R.id.naafiri);
        ImageView doge = (ImageView) dialog.findViewById(R.id.doge);
        ImageView sophie = (ImageView) dialog.findViewById(R.id.sophie);
        ImageView checkers = (ImageView) dialog.findViewById(R.id.checkers);
        ImageView toffee = (ImageView) dialog.findViewById(R.id.toffee);
        ImageView shadow = (ImageView) dialog.findViewById(R.id.shadow);
        ImageView oakley = (ImageView) dialog.findViewById(R.id.oakley);
        ImageView liberty = (ImageView) dialog.findViewById(R.id.liberty);
        ImageView brutus = (ImageView) dialog.findViewById(R.id.brutus);

        // On click commands
        neeko.setOnClickListener((view -> {
            if (viewModel.getPet("Neeko") != null) {
                currentPet = viewModel.getPet("Neeko");
                currentPetImage.setImageResource(currentPet.getImageResourceId());
            }
            dialog.dismiss();
        }));
        naafiri.setOnClickListener((view -> {
            if (viewModel.getPet("Naafiri") != null) {
                currentPet = viewModel.getPet("Naafiri");
                currentPetImage.setImageResource(currentPet.getImageResourceId());
            }
            dialog.dismiss();
        }));
        doge.setOnClickListener((view -> {
            if (viewModel.getPet("Doge") != null) {
                currentPet = viewModel.getPet("Doge");
                currentPetImage.setImageResource(currentPet.getImageResourceId());
            }
            dialog.dismiss();
        }));
        sophie.setOnClickListener((view -> {
            if (viewModel.getPet("Sophie") != null) {
                currentPet = viewModel.getPet("Sophie");
                currentPetImage.setImageResource(currentPet.getImageResourceId());
            }
            dialog.dismiss();
        }));
        checkers.setOnClickListener((view -> {
            if (viewModel.getPet("Checkers") != null) {
                currentPet = viewModel.getPet("Checkers");
                currentPetImage.setImageResource(currentPet.getImageResourceId());
            }
            dialog.dismiss();
        }));
        toffee.setOnClickListener((view -> {
            if (viewModel.getPet("Toffee") != null) {
                currentPet = viewModel.getPet("Toffee");
                currentPetImage.setImageResource(currentPet.getImageResourceId());
            }
            dialog.dismiss();
        }));
        shadow.setOnClickListener((view -> {
            if (viewModel.getPet("Shadow") != null) {
                currentPet = viewModel.getPet("Shadow");
                currentPetImage.setImageResource(currentPet.getImageResourceId());
            }
            dialog.dismiss();
        }));
        oakley.setOnClickListener((view -> {
            if (viewModel.getPet("Oakley") != null) {
                currentPet = viewModel.getPet("Oakley");
                currentPetImage.setImageResource(currentPet.getImageResourceId());
            }
            dialog.dismiss();
        }));
        liberty.setOnClickListener((view -> {
            if (viewModel.getPet("Liberty") != null) {
                currentPet = viewModel.getPet("Liberty");
                currentPetImage.setImageResource(currentPet.getImageResourceId());
            }
            dialog.dismiss();
        }));
        brutus.setOnClickListener((view -> {
            if (viewModel.getPet("Brutus") != null) {
                currentPet = viewModel.getPet("Brutus");
                currentPetImage.setImageResource(currentPet.getImageResourceId());
            }
            dialog.dismiss();
        }));
        okButton.setOnClickListener((view -> {
            dialog.dismiss();
        }));

        // Show the popup
        dialog.show();
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

                Amplify.Auth.getCurrentUser(
                        currentUser -> {
                            if (currentUser != null) {
                                String userId = currentUser.getUserId();
                Amplify.API.query(
                        ModelQuery.get(User.class, userId),
                        response -> {
                            if (response.getData() != null) {
                                User user = response.getData();
                                User updatedUser = user.copyOfBuilder()
                                        .tokens(viewModel.getTokenCount())
                                        .build();
                                Amplify.API.mutate(
                                        ModelMutation.update(updatedUser),
                                        response1 -> Log.i("PetViewPage", "User tokens updated"),
                                        error -> Log.e("PetViewPage", "Failed to update user tokens", error)
                                );
                            }
                        },
                        error -> Log.e("PetViewPage", "Query failed", error)
                );
                            }
                        },
                        error -> Log.e("MyAmplifyApp", "Get current user failed", error)
                );
            }
        }

        // Walk button
        else if (v.getId() == R.id.walk_button)
        {
            if (!noTokens) {
                viewModel.decrementTokenCount();
                tokenCountLabel.setText(String.valueOf(viewModel.getTokenCount()));
                noTokens = (viewModel.getTokenCount() == 0);

                Amplify.Auth.getCurrentUser(
                        currentUser -> {
                            if (currentUser != null) {
                                String userId = currentUser.getUserId();
                                Amplify.API.query(
                                        ModelQuery.get(User.class, userId),
                                        response -> {
                                            if (response.getData() != null) {
                                                User user = response.getData();
                                                User updatedUser = user.copyOfBuilder()
                                                        .tokens(viewModel.getTokenCount())
                                                        .build();
                                                Amplify.API.mutate(
                                                        ModelMutation.update(updatedUser),
                                                        response1 -> Log.i("PetViewPage", "User tokens updated"),
                                                        error -> Log.e("PetViewPage", "Failed to update user tokens", error)
                                                );
                                            }
                                        },
                                        error -> Log.e("PetViewPage", "Query failed", error)
                                );
                            }

                        },
                        error -> Log.e("MyAmplifyApp", "Get current user failed", error)
                );
            }
        }

        // Dance button
        else if (v.getId() == R.id.dance_button)
        {
            if (!noTokens) {
                viewModel.decrementTokenCount();
                tokenCountLabel.setText(String.valueOf(viewModel.getTokenCount()));
                noTokens = (viewModel.getTokenCount() == 0);

                Amplify.Auth.getCurrentUser(
                        currentUser -> {
                            if (currentUser != null) {
                                String userId = currentUser.getUserId();
                Amplify.API.query(
                        ModelQuery.get(User.class, userId),
                        response -> {
                            if (response.getData() != null) {
                                User user = response.getData();
                                User updatedUser = user.copyOfBuilder()
                                        .tokens(viewModel.getTokenCount())
                                        .build();
                                Amplify.API.mutate(
                                        ModelMutation.update(updatedUser),
                                        response1 -> Log.i("PetViewPage", "User tokens updated"),
                                        error -> Log.e("PetViewPage", "Failed to update user tokens", error)
                                );
                            }
                        },
                        error -> Log.e("PetViewPage", "Query failed", error)
                );
                            }
                        },
                        error -> Log.e("MyAmplifyApp", "Get current user failed", error)
                );
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

        // Petdex Button
        else if (v.getId() == R.id.petdex_button)
        {
            showPetdexModal();
        }

        if (noTokens)
        {
            walkButton.setEnabled(false);
            danceButton.setEnabled(false);
            feedButton.setEnabled(false);
        }
    }
}