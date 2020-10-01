package com.itaycohen.playwithactions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;

public class DisputeActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageButton attachPhotoBtn;
    private TextInputEditText etName;
    private TextInputEditText etEmail;
    private TextInputEditText etSubject;
    private TextInputEditText etContent;
    private Button sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setSharedElementTransition();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispute);

        attachPhotoBtn = findViewById(R.id.ivImgAttachment);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etSubject = findViewById(R.id.etSubject);
        etContent = findViewById(R.id.etContent);
        sendBtn = findViewById(R.id.sendBtn);
        setClickListeners();
    }

    private void setClickListeners() {
        sendBtn.setOnClickListener(v -> {
            String error = isAllTextFieldFilled() ? null : getString(R.string.please_fill_info);
            sendBtn.requestFocus();
            sendBtn.setError(error);
            if (error == null) {
                sendBtn.setEnabled(false);
                sendBtn.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                Toast.makeText(this, R.string.sended, Toast.LENGTH_LONG).show();
            }
        });


        attachPhotoBtn.setEnabled(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY));
        attachPhotoBtn.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No Camera app exists in yout device", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isAllTextFieldFilled() {
        return !TextUtils.isEmpty(etName.getText()) &&
                !TextUtils.isEmpty(etEmail.getText()) &&
                !TextUtils.isEmpty(etSubject.getText()) &&
                !TextUtils.isEmpty(etContent.getText());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                attachPhotoBtn.setImageBitmap(imageBitmap);
            }
        }
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