package com.example.freshadmin.utils;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import static com.parse.ParseException.INVALID_SESSION_TOKEN;

public class ParseErrorHandler {
    public static void handleParseError(ParseException e, Activity c, String methodName) {
        switch (e.getCode()) {
            case INVALID_SESSION_TOKEN:
                new AlertDialog.Builder(c)
                        .setTitle("Dear User")
                        .setMessage("Your current session has expired. Please log out and log in again.").
                        setPositiveButton("OK", (dialogInterface, i) -> {
                        }).create().show();
                break;
            default:
                new AlertDialog.Builder(c)
                        .setTitle("Dear User")
                        .setMessage("Something Went Wrong. Please try again later").
                        setPositiveButton("OK", (dialogInterface, i) -> {
                        }).create().show();
                ParseUser user = ParseUser.getCurrentUser();
                ParseObject entity = new ParseObject("ErrorReport");
                entity.put("message", e.getMessage()+"");
                entity.put("code", e.getCode());
                entity.put("activity",c.getClass().getSimpleName());
                entity.put("username", user != null ?user.getUsername()+" "+user.getString("type"):"");
                entity.put("methodName",methodName);
                // Saves the new object.
                entity.saveEventually();

                break;
        }
    }
}
