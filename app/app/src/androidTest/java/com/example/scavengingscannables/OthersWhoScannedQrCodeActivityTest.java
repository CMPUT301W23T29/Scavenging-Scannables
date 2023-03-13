package com.example.scavengingscannables;

import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.scavengingscannables.ui.profile.OthersWhoScannedQrCodeActivity;
import com.example.scavengingscannables.ui.profile.QrCodesActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class OthersWhoScannedQrCodeActivityTest {

    private Solo solo;
    private TextView username;
    @Rule
    public ActivityTestRule rule = new ActivityTestRule<>(MainActivity.class, true,true);
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    @Test
    public void checkSwitch(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_profile));
        solo.clickOnView(solo.getView(R.id.ViewQrCodes));
        solo.clickOnView(solo.getView(R.id.others));
        solo.assertCurrentActivity("Wrong Activity", OthersWhoScannedQrCodeActivity.class);
    }
    @Test
    public void checkBack() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_profile));
        solo.clickOnView(solo.getView(R.id.ViewQrCodes));
        solo.clickOnView(solo.getView(R.id.others));
        solo.assertCurrentActivity("Wrong Activity", OthersWhoScannedQrCodeActivity.class);
        solo.clickOnView(solo.getView(R.id.button_back));
        solo.assertCurrentActivity("Wrong Activity", QrCodesActivity.class);
    }

    @After
    public void tearDown()throws Exception{
        solo.finishOpenedActivities();
    }
}
