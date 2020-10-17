package com.itaycohen.playwithactions;

import androidx.annotation.AttrRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;

import java.util.Calendar;

public class AppointmentActivity extends AppCompatActivity {

    private TextInputLayout mEventNameLayout;
    private TextInputLayout mEmailLayout;
    private TextInputLayout mMoreDetailsLayout;
    private TextInputEditText mEventName;
    private TextInputEditText mEmail;
    private TextInputEditText mMoreDetails;
    private Button mPickDateBtn;
    private Button mCorrelateBtn;
    private MeetingModel meetingModel = new MeetingModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setSharedElementTransition();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        setViewsRefs();
        mPickDateBtn.setOnClickListener(onPickDateClick);
        mCorrelateBtn.setOnClickListener(onCorrelateMettingClick);
    }

    private View.OnClickListener onPickDateClick = v -> {
        int dialogTheme = resolveOrThrow(v.getContext(), R.attr.materialCalendarTheme);
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
        builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
        builder.setTheme(dialogTheme);
        MaterialDatePicker<Long> dialog = builder.build();
        dialog.addOnPositiveButtonClickListener(selection -> {
            mPickDateBtn.setText(dialog.getHeaderText());
            meetingModel.selectedDateSinceEpoch = selection;
        });
        dialog.show(getSupportFragmentManager(), null);
    };

    private static int resolveOrThrow(Context context, @AttrRes int attributeResId) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(attributeResId, typedValue, true)) {
            return typedValue.data;
        }
        throw new IllegalArgumentException(context.getResources().getResourceName(attributeResId));
    }

    private void setViewsRefs() {
        mEventNameLayout = findViewById(R.id.nameLayout);
        mEmailLayout = findViewById(R.id.emailLayout);
        mMoreDetailsLayout = findViewById(R.id.contentLayout);
        mEventName = findViewById(R.id.etName);
        mEmail = findViewById(R.id.etEmail);
        mMoreDetails = findViewById(R.id.etContent);
        mPickDateBtn = findViewById(R.id.datePickedBtn);
        mCorrelateBtn = findViewById(R.id.correlateBtn);
    }

    private void setSharedElementTransition() {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        findViewById(android.R.id.content).setTransitionName(getString(R.string.shared_element_appointment));
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

    private View.OnClickListener onCorrelateMettingClick = v -> {
        if (!updateAndValidateInputs()) {
            Toast.makeText(v.getContext(), R.string.enter_all_of_the_detals, Toast.LENGTH_LONG).show();
            return;
        }

        Calendar endTime = Calendar.getInstance();
        endTime.setTimeInMillis(meetingModel.selectedDateSinceEpoch);
        endTime.add(Calendar.MINUTE, 30);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, meetingModel.selectedDateSinceEpoch)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
                .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
                .putExtra(CalendarContract.Events.TITLE, meetingModel.meetingTitle)
                .putExtra(CalendarContract.Events.DESCRIPTION, meetingModel.extraContent)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "Haters gonna Hate offices")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                .putExtra(Intent.EXTRA_EMAIL, meetingModel.email);
        startActivity(intent);
    };

    private boolean updateAndValidateInputs() {
        meetingModel.meetingTitle = mEventName.getText().toString();
        meetingModel.email = mEmail.getText().toString();
        meetingModel.extraContent = mMoreDetails.getText().toString();
        return !meetingModel.meetingTitle.isEmpty() &&
                !meetingModel.email.isEmpty() &&
                meetingModel.meetingTime > 0 &&
                meetingModel.selectedDateSinceEpoch > System.currentTimeMillis();
    }

    private class MeetingModel {
        String meetingTitle;
        long selectedDateSinceEpoch;
        int meetingTime = 30;
        String extraContent;
        String email;
    }
}