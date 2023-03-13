package com.example.scavengingscannables;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.scavengingscannables.ui.home.DisplaySearch;
import com.example.scavengingscannables.ui.home.FindUserActivity;
import com.example.scavengingscannables.ui.home.HomeFragment;
import com.example.scavengingscannables.ui.home.HomeViewModel;
import com.example.scavengingscannables.ui.home.OthersQrCodesActivity;
import com.example.scavengingscannables.ui.notifications.Comments;
import com.example.scavengingscannables.ui.notifications.Others;
import com.example.scavengingscannables.ui.notifications.QrCodesActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

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
        solo.clickOnView(solo.getView(R.id.navigation_notifications));
        solo.clickOnView(solo.getView(R.id.ViewQrCodes));
        solo.assertCurrentActivity("Wrong Activity", QrCodesActivity.class);
    }
    @Test
    public void checkBack() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_notifications));
        solo.clickOnView(solo.getView(R.id.ViewQrCodes));
        solo.assertCurrentActivity("Wrong Activity", QrCodesActivity.class);
        solo.clickOnView(solo.getView(R.id.button_back));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @Test
    public void checkDelete() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_notifications));
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
