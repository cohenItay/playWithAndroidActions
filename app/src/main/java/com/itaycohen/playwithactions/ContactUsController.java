package com.itaycohen.playwithactions;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ContactUsController implements ContactUsView.Listener {

    private static final String EMAIL = "haterGonna@Hate.com";
    private static final String NAME = "Hatters gonna hate";
    private static final String PHONE = "+972547547554";

    private final ContactUsView contactUsView;
    private final AppCompatActivity activity;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    public ContactUsController(
            @NonNull AppCompatActivity activity,
            ContactUsView contactUsView
    ) {
        this.activity = activity;
        this.contactUsView = contactUsView;
        requestPermissionLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallbackImpl() // Alpha library - android OS bug, it doesn't call this callback at all.
        );
        contactUsView.setContactUsListener(this);
    }

    @Override
    public void onAddToContactClick(View view) {
        Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
        intent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
        intent.putExtra(
                ContactsContract.Intents.Insert.NAME,
                NAME
        ).putExtra(
                ContactsContract.Intents.Insert.EMAIL,
                EMAIL
        ).putExtra(
                ContactsContract.Intents.Insert.EMAIL_TYPE,
                ContactsContract.CommonDataKinds.Email.TYPE_WORK
        ).putExtra(
                ContactsContract.Intents.Insert.PHONE,
                PHONE
        ).putExtra(
                ContactsContract.Intents.Insert.PHONE_TYPE,
                ContactsContract.CommonDataKinds.Phone.TYPE_WORK
        );
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    @Override
    public void onGoToDialerClick(View view) {
        useTelephone(false);
    }

    @Override
    public void onPerformCallClick(View view) {
        int permissionState = ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);

        if (permissionState == PackageManager.PERMISSION_GRANTED)
            useTelephone(true);
        else if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)) {
            showRationalDialog();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE); // show OS dialog
        }
    }


    private void showRationalDialog() {
        new MaterialAlertDialogBuilder(activity)
                .setTitle(activity.getString(R.string.permission_request))
                .setMessage(R.string.call_permission_rational_message)
                .setPositiveButton(
                        R.string.yes_i_approve,
                        (dialog, which) -> requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                )
                .setNegativeButton(
                        R.string.deny,
                        (dialog, which) ->  {
                            dialog.dismiss();
                            contactUsView.setCallButtonStateBlocked(true);
                        }
                )
                .show();
    }


    private void useTelephone(boolean invokeCall) {
        Intent intent = new Intent(invokeCall ? Intent.ACTION_CALL : Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + PHONE));
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    private class ActivityResultCallbackImpl implements ActivityResultCallback<Boolean> {
        @Override
        public void onActivityResult(Boolean isCallPermissionGranted) {
            if (isCallPermissionGranted) {
                useTelephone(true);
                contactUsView.setCallButtonStateEnabled();
            } else {
                boolean wasRationalShown = !ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE);
                contactUsView.setCallButtonStateBlocked(wasRationalShown);
            }
        }
    }
}
