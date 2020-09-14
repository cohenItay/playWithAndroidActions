package com.itaycohen.playwithactions;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;

public class MainActivity extends AppCompatActivity {


    private CardView disputeCard;
    private CardView appointmentCard;
    private CardView contactUsCard;
    private CardView moreAboutUsCard;
    private ContactUsController contactUsController;
    private ActivityResultLauncher<String> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        setExitSharedElementCallback(new MaterialContainerTransformSharedElementCallback());
        getWindow().setSharedElementsUseOverlay(false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewsReferences();
        setClickListener();
    }

    private void initViewsReferences() {
        disputeCard = findViewById(R.id.cardViewDispute);
        appointmentCard = findViewById(R.id.cardViewMeeting);
        contactUsCard = findViewById(R.id.cardViewContactUs);
        moreAboutUsCard = findViewById(R.id.cardViewMoreOnWeb);
    }

    private void setClickListener() {

        disputeCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DisputeActivity.class);
            Bundle options = ActivityOptions.makeSceneTransitionAnimation(
                    MainActivity.this,
                    new Pair<>(v, getString(R.string.shared_element_dispute))
            ).toBundle();
            startActivity(intent, options);
        });

        appointmentCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AppointmentActivity.class);
            Bundle options = ActivityOptions.makeSceneTransitionAnimation(
                    MainActivity.this,
                    new Pair<>(v, getString(R.string.shared_element_appointment))
            ).toBundle();
            startActivity(intent, options);
        });

        contactUsCard.setOnClickListener(v -> {
            ContactUsView contactUsView = new ContactUsView(MainActivity.this);
            contactUsController = new ContactUsController(MainActivity.this, contactUsView);

            new MaterialAlertDialogBuilder(MainActivity.this)
                    .setView(contactUsView)
                    .show();
        });

        moreAboutUsCard.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, "Haters gonna hate");
            startActivity(intent);
        });
    }
}