package com.example.freshadmin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.Arrays;
import java.util.Calendar;

public class SMSReceiver extends BroadcastReceiver {
    public static final String pdu_type = "pdus";

    @Override
    public void onReceive(final Context context, Intent intent) {
        // Get the SMS message.
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";
        String format = bundle.getString("format");
        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null) {
            // Check the Android version.
            // Fill the msgs array.
            msgs = new SmsMessage[pdus.length];
            int count = 0;
            String fullSms = "";
            boolean from = false;
            for (int i = 0; i < msgs.length; i++) {
                count++;
                // Check Android version and use appropriate createFromPdu.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // If Android version M or newer:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    // If Android version L or older:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                // Build the message to show.
                if(msgs[i].getOriginatingAddress().equals("+12017629970"))
                    from = true;
                fullSms += msgs[i].getMessageBody();
            }
            if(from){
                String[] split = fullSms.split("\\s+");
                Log.d("StrArray", Arrays.toString(split));
                int start = 6;
                if(split[start+1].equals("Cash") && split[start+2].equals("In") && split[start+3].equals("Tk")){
                    //From agent | save object
                    String trxId = "";
                    String dateStr = "";
                    String amount = split[start+4];
                    for (int j = 0; j < split.length; j++) {
                        if (split[j].equals("TrxID")) {
                            trxId = split[j+1];
                            dateStr = split[j+3];
                            break;
                        }
                    }

                    Calendar cal = Calendar.getInstance();
                    String[] date = dateStr.split("/");
                    cal.set(Calendar.DAY_OF_MONTH,Integer.parseInt(date[0]));
                    cal.set(Calendar.MONTH,Integer.parseInt(date[1])-1);
                    cal.set(Calendar.YEAR,Integer.parseInt(date[2]));

                    // Saves the new object.
                    ParseObject bKashObj = new ParseObject("Bkash");
                    bKashObj.put("trxId", trxId);
                    bKashObj.put("paidAmount", Double.parseDouble(amount));
                    bKashObj.put("paidDate", cal.getTime());
                    bKashObj.saveEventually(new SaveCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            if(e==null){
                                Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(context, "Coming to else", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(context, "Not bkash", Toast.LENGTH_SHORT).show();
            }
        }
    }
}




//                String[] splitMsg = msgs[i].getMessageBody().split("\\s+");
//Traversing the splitted array to find trxid