package com.example.scavengingscannables;

import static org.junit.Assert.assertEquals;

import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.scavengingscannables.ui.profile.CommentsActivity;
import com.example.scavengingscannables.ui.profile.QrCodesActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class CommentsActivityTest {

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
        solo.clickOnView(solo.getView(R.id.comment));
        solo.assertCurrentActivity("Wrong Activity", CommentsActivity.class);
    }
    @Test
    public void checkBack() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_profile));
        solo.clickOnView(solo.getView(R.id.ViewQrCodes));
        solo.clickOnView(solo.getView(R.id.comment));
        solo.assertCurrentActivity("Wrong Activity", CommentsActivity.class);
        solo.clickOnView(solo.getView(R.id.button_back));
        solo.assertCurrentActivity("Wrong Activity", QrCodesActivity.class);
    }
    @Test
    public void checkConfirmAdd() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_profile));
        solo.clickOnView(solo.getView(R.id.ViewQrCodes));
        solo.clickOnView(solo.getView(R.id.comment));
        solo.assertCurrentActivity("Wrong Activity", CommentsActivity.class);
        solo.enterText((EditText) solo.getView(R.id.input), "Good");
        solo.clickOnView(solo.getView(R.id.Confirm));
        solo.waitForText("Good", 1, 2000);
        solo.clickOnView(solo.getView(R.id.button_comments_back));
        solo.assertCurrentActivity("Wrong Activity", QrCodesActivity.class);
    }

    @After
    public void tearDown()throws Exception{
        solo.finishOpenedActivities();
    }
}
