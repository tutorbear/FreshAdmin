package com.example.freshadmin.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.freshadmin.R;
import com.google.android.material.chip.Chip;

public class AddChipViewHelper {

    public static void AddPhoneChip(LayoutInflater layoutInflater , ViewGroup viewGroup, String number, Activity activity) {
        Chip chip = (Chip) layoutInflater.inflate(R.layout.single_chip_layout, viewGroup, false);
        chip.setText(number);
        chip.setOnClickListener(v -> activity.startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + ((Chip) v).getText()))));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1.0f;
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;

        viewGroup.addView(chip, 0, layoutParams);
    }

}
