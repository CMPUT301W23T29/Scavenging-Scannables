package com.example.scavengingscannables;

import android.app.Activity;
import android.app.Instrumentation;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.ActivityTestRule$$ExternalSyntheticLambda0;

import com.example.scavengingscannables.ui.home.DisplaySearch;
import com.example.scavengingscannables.ui.home.FindUserActivity;
import com.example.scavengingscannables.ui.home.HomeFragment;
import com.example.scavengingscannables.ui.home.HomeViewModel;
import com.example.scavengingscannables.ui.home.OthersQrCodesActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class FindUserTest {
    private Solo solo;
    private TextView username;
    @Rule
    public ActivityTestRule rule = new ActivityTestRule<>(FindUserActivity.class, true,true);
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    @Test
    public void check(){
        solo.assertCurrentActivity("Wrong Activity", FindUserActivity.class);
        solo.enterText((EditText) solo.getView(R.id.search_box_input),"er");
        solo.clickOnButton("Search");
        solo.waitForText("er", 1, 2000);
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity", DisplaySearch.class);
    }
    @After
    public void tearDown()throws Exception{
        solo.finishOpenedActivities();
    }
}
