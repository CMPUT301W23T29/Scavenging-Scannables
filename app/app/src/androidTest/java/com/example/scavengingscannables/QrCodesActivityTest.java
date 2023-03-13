package com.example.scavengingscannables;

import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.scavengingscannables.ui.profile.QrCodesActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class QrCodesActivityTest {
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
        solo.assertCurrentActivity("Wrong Activity", QrCodesActivity.class);
    }
    @Test
    public void checkBack() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_profile));
        solo.clickOnView(solo.getView(R.id.ViewQrCodes));
        solo.assertCurrentActivity("Wrong Activity", QrCodesActivity.class);
        solo.clickOnView(solo.getView(R.id.button_back));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @Test
    public void checkDelete() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_profile));
        solo.clickOnView(solo.getView(R.id.ViewQrCodes));
        solo.clickOnView(solo.getView(R.id.button_delete));
        //ListView  myList = (ListView) solo.getView(R.id.qrcode_list,1);
        //View v= (View) myList.getChildAt(1);
        solo.clickOnView(solo.getView(R.id.codeName));
        solo.clickOnButton("no");
        //solo.clickInList(1);
        //solo.clickOnButton("yes");
        solo.clickOnView(solo.getView(R.id.button_delete));
        solo.clickOnView(solo.getView(R.id.button_back));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }


    @After
    public void tearDown()throws Exception{
        solo.finishOpenedActivities();
    }
}
