package com.itaycohen.playwithactions;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;

import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;

public class DisputeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setSharedElementTransition();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispute);
    }

    private void setSharedElementTransition() {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        findViewById(android.R.id.content).setTransitionName(getString(R.string.shared_element_dispute));
        setEnterSharedElementCallback(new MaterialContainerTransformSharedElementCallback());

        MaterialContainerTransform enterTransform = new MaterialContainerTransform();
        enterTransform.addTarget(android.R.id.content);
        enterTransform.setDuration(300L);
        getWindow().setSharedElementEnterTransition(enterTransform);

        MaterialContainerTransform returnTransform = new MaterialContainerTransform();
        returnTransform.addTarget(android.R.id.content);
        returnTransform.setDuration(250L);
        getWindow().setSharedElementEnterTransition(enterTransform);
    }


}