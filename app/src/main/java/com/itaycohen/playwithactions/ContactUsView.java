package com.itaycohen.playwithactions;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

class ContactUsView extends LinearLayout {

    private Button addToContactButton;
    private Button openDialerButton;
    private Button callButton;
    private Listener contactUsController;


    public ContactUsView(Context context) {
        super(context);
        initView();
    }

    public ContactUsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ContactUsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public ContactUsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }



    public void setContactUsListener(Listener contactUsListener) {
        this.contactUsController = contactUsListener;
        setClickListeners();
    }

    private void initView() {
        setOrientation(LinearLayout.VERTICAL);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setGravity(Gravity.CENTER);
        setLayoutParams(lp);
        int fiveDpInPx = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        setPadding(fiveDpInPx, fiveDpInPx, fiveDpInPx, fiveDpInPx);
        LayoutInflater.from(getContext()).inflate(R.layout.contact_us_dialog_content, this, true);
        addToContactButton = findViewById(R.id.buttonAddAsContact);
        openDialerButton = findViewById(R.id.buttonDial);
        callButton = findViewById(R.id.buttonCall);
    }

    private void setClickListeners() {
        addToContactButton.setOnClickListener(contactUsController == null ? null : contactUsController::onAddToContactClick);
        openDialerButton.setOnClickListener(contactUsController == null ? null : contactUsController::onGoToDialerClick);
        callButton.setOnClickListener(contactUsController == null ? null : contactUsController::onPerformCallClick);
    }

    public void setCallButtonStateEnabled() {
        callButton.setEnabled(true);
        callButton.setText(R.string.call);
    }

    public void setCallButtonStateBlocked(boolean wasRationalShown) {
        callButton.setEnabled(!wasRationalShown);
        callButton.setText(wasRationalShown ? R.string.no_call_permission : R.string.request_call_permission);
    }

    interface Listener {

        void onAddToContactClick(View view);
        void onGoToDialerClick(View view);
        void onPerformCallClick(View view);
    }

}
