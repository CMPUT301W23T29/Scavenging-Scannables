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
import com.example.scavengingscannables.ui.notifications.QrCodesActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class FindUserTest {
    private Solo solo;
    private TextView username;
    @Rule
    public ActivityTestRule rule = new ActivityTestRule<>(MainActivity.class, true,true);
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    @Test
    public void check_imagebutton(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_home));
        solo.clickOnView(solo.getView(R.id.search_button1));
        solo.assertCurrentActivity("Wrong Activity",FindUserActivity.class);
    }
    @Test
    public void check_edittest_and_searchbutoon() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_home));
        solo.clickOnView(solo.getView(R.id.search_button1));
        solo.assertCurrentActivity("Wrong Activity", FindUserActivity.class);
        solo.enterText((EditText) solo.getView(R.id.search_box_input), "er");
        solo.clickOnButton("Search");
        solo.waitForText("er", 1, 2000);
    }
    @Test
    public void check_Switching_From_FindUserActivity_to_DisplaySearchActivity() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_home));
        solo.clickOnView(solo.getView(R.id.search_button1));
        solo.assertCurrentActivity("Wrong Activity", FindUserActivity.class);
        solo.enterText((EditText) solo.getView(R.id.search_box_input), "er");
        solo.clickOnButton("Search");
        solo.waitForText("er", 1, 2000);
        solo.clickInList(0);
        solo.clickOnView(solo.getView(R.id.display_user_name));
        solo.assertCurrentActivity("Wrong Activity", DisplaySearch.class);
    }
    @Test
    public void check_Switching_From_DisplaySearch_to_OtherQRCodes() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_home));
        solo.clickOnView(solo.getView(R.id.search_button1));
        solo.assertCurrentActivity("Wrong Activity", FindUserActivity.class);
        solo.enterText((EditText) solo.getView(R.id.search_box_input), "er");
        solo.clickOnButton("Search");
        solo.waitForText("er", 1, 2000);
        solo.clickInList(0);
        solo.clickOnView(solo.getView(R.id.display_user_name));
        solo.assertCurrentActivity("Wrong Activity", DisplaySearch.class);
        solo.clickOnView(solo.getView(R.id.view_qrCodes));
        solo.assertCurrentActivity("Wrong Activity", OthersQrCodesActivity.class);
    }
    @Test
    public void check_backbutton_of_OtherQrCodes() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_home));
        solo.clickOnView(solo.getView(R.id.search_button1));
        solo.assertCurrentActivity("Wrong Activity", FindUserActivity.class);
        solo.enterText((EditText) solo.getView(R.id.search_box_input), "er");
        solo.clickOnButton("Search");
        solo.waitForText("er", 1, 2000);
        solo.clickInList(0);
        solo.clickOnView(solo.getView(R.id.display_user_name));
        solo.assertCurrentActivity("Wrong Activity", DisplaySearch.class);
        solo.clickOnView(solo.getView(R.id.view_qrCodes));
        solo.assertCurrentActivity("Wrong Activity", OthersQrCodesActivity.class);
        solo.clickOnView(solo.getView(R.id.other_back_button));
        solo.assertCurrentActivity("Wrong Activity", DisplaySearch.class);
    }
    @Test
    public void check_backbutton_of_DisplaySearch() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_home));
        solo.clickOnView(solo.getView(R.id.search_button1));
        solo.assertCurrentActivity("Wrong Activity", FindUserActivity.class);
        solo.enterText((EditText) solo.getView(R.id.search_box_input), "er");
        solo.clickOnButton("Search");
        solo.waitForText("er", 1, 2000);
        solo.clickInList(0);
        solo.clickOnView(solo.getView(R.id.display_user_name));
        solo.assertCurrentActivity("Wrong Activity", DisplaySearch.class);
        solo.clickOnView(solo.getView(R.id.view_qrCodes));
        solo.assertCurrentActivity("Wrong Activity", OthersQrCodesActivity.class);
        solo.clickOnView(solo.getView(R.id.other_back_button));
        solo.assertCurrentActivity("Wrong Activity", DisplaySearch.class);
        solo.clickOnView(solo.getView(R.id.display_back));
    }
    @Test
    public void check_button_of_FindUser(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_home));
        solo.clickOnView(solo.getView(R.id.search_button1));
        solo.assertCurrentActivity("Wrong Activity",FindUserActivity.class);
        solo.enterText((EditText) solo.getView(R.id.search_box_input),"er");
        solo.clickOnButton("Search");
        solo.waitForText("er", 1, 2000);
        solo.clickInList(0);
        solo.clickOnView(solo.getView(R.id.display_user_name));
        solo.assertCurrentActivity("Wrong Activity", DisplaySearch.class);
        solo.clickOnView(solo.getView(R.id.view_qrCodes));
        solo.assertCurrentActivity("Wrong Activity", OthersQrCodesActivity.class);
        solo.clickOnView(solo.getView(R.id.other_back_button));
        solo.assertCurrentActivity("Wrong Activity", DisplaySearch.class);
        solo.clickOnView(solo.getView(R.id.display_back));
        solo.assertCurrentActivity("Wrong Activity",FindUserActivity.class);
        solo.clickOnView(solo.getView(R.id.search_back_button));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }
    @After
    public void tearDown()throws Exception{
        solo.finishOpenedActivities();
    }
}
