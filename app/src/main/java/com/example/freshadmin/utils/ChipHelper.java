package com.example.freshadmin.utils;

import android.app.Activity;
import android.content.Context;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class ChipHelper {

    public static ChipHelper create() {
        return new ChipHelper();
    }

    public String getCheckedChipTextFromGroup(ChipGroup chipGroup, Context context) {
        return ((Chip)((Activity) context).findViewById(chipGroup.getCheckedChipId())).getText().toString();
    }

    public void setChipFromString(ChipGroup chipGroup, String setChipText) {
        for (int i = 0; i < chipGroup.getChildCount(); i++)
            if (chipGroup.getChildAt(i) instanceof Chip) {
                if (((Chip) chipGroup.getChildAt(i)).getText().equals(setChipText)) {
                    ((Chip) chipGroup.getChildAt(i)).setChecked(true);
                }
            } else break;
    }

}
