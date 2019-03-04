package com.android.personalbest;

import android.widget.EditText;

import com.android.personalbest.UIdisplay.LoginUI;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class CheckInvalidTest {

    // Need a temporary activity & EditText to be able to produce Editable objects
    private LoginUI mockActivity;
    private EditText mockEditable;

    @Before
    public void setup() {
        mockActivity = Robolectric.setupActivity(LoginUI.class);
        mockEditable = new EditText(mockActivity);
    }

    @Test
    public void testCheckforName() {
        mockEditable.setText("");
        assertFalse(CheckInvalid.checkForName(mockEditable.getText()));

        mockEditable.setText("Joe Bob");
        assertEquals(true, CheckInvalid.checkForName(mockEditable.getText()));

        mockEditable.setText("Alex-!C~hris1!4J5on[]!Kac`ad45t!Lola!");
        assertEquals(true, CheckInvalid.checkForName(mockEditable.getText()));
    }


    @Test
    public void testCheckForHeightFt() {
        mockEditable.setText("Joe Bob");
        assertEquals(-1, CheckInvalid.checkForHeightft(mockEditable.getText()));

        mockEditable.setText("-10");
        assertEquals(-1, CheckInvalid.checkForHeightft(mockEditable.getText()));

        mockEditable.setText("10");
        assertEquals(-1, CheckInvalid.checkForHeightft(mockEditable.getText()));

        mockEditable.setText("5");
        assertEquals(5, CheckInvalid.checkForHeightft(mockEditable.getText()));
    }


    @Test
    public void testCheckForHeightIn() {
        mockEditable.setText("Joe Bob");
        assertEquals(-1, CheckInvalid.checkForHeightin(mockEditable.getText()));

        mockEditable.setText("-10");
        assertEquals(-1, CheckInvalid.checkForHeightin(mockEditable.getText()));

        mockEditable.setText("12");
        assertEquals(-1, CheckInvalid.checkForHeightin(mockEditable.getText()));

        mockEditable.setText("0");
        assertEquals(0, CheckInvalid.checkForHeightin(mockEditable.getText()));

        mockEditable.setText("8");
        assertEquals(8, CheckInvalid.checkForHeightin(mockEditable.getText()));
    }


    @Test
    public void testCheckGoal() {
        mockEditable.setText("Joe Bob");
        assertEquals(-1, CheckInvalid.checkForGoal(mockEditable.getText()));

        mockEditable.setText("-12345");
        assertEquals(-1, CheckInvalid.checkForGoal(mockEditable.getText()));

        mockEditable.setText("1");
        assertEquals(1, CheckInvalid.checkForGoal(mockEditable.getText()));

        mockEditable.setText("15000");
        assertEquals(15000, CheckInvalid.checkForGoal(mockEditable.getText()));
    }

}
