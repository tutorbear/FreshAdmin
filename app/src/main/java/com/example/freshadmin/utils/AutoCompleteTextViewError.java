package com.example.freshadmin.utils;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ScrollView;

import com.google.android.material.textfield.TextInputLayout;

public class AutoCompleteTextViewError {
    public AutoCompleteTextViewError(TextInputLayout textInputLayout, ScrollView scrollView){
        scrollToView(scrollView, textInputLayout);

        textInputLayout.requestFocus();

        ValueAnimator valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), ColorEx.RED, ColorEx.WHITE,
                ColorEx.RED, ColorEx.WHITE);

        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(animation -> {
            textInputLayout.setBoxStrokeColor((int)animation.getAnimatedValue());
        });
        valueAnimator.start();
    }

    public static void create(TextInputLayout textInputLayout, ScrollView scrollView){
        new AutoCompleteTextViewError(textInputLayout,scrollView);
    }

    /**
     * Used to scroll to the given view.
     *
     * @param scrollViewParent Parent ScrollView
     * @param view View to which we need to scroll.
     */

    private void scrollToView(final ScrollView scrollViewParent, final View view) {
        // Get deepChild Offset
        Point childOffset = new Point();
        getDeepChildOffset(scrollViewParent, view.getParent(), view, childOffset);
        // Scroll to child.
        scrollViewParent.smoothScrollTo(0, childOffset.y);
    }

    /**
     * Used to get deep child offset.
     * <p/>
     * 1. We need to scroll to child in scrollview, but the child may not the direct child to scrollview.
     * 2. So to get correct child position to scroll, we need to iterate through all of its parent views till the main parent.
     *
     * @param mainParent        Main Top parent.
     * @param parent            Parent.
     * @param child             Child.
     * @param accumulatedOffset Accumulated Offset.
     */

    private void getDeepChildOffset(final ViewGroup mainParent, final ViewParent parent, final View child, final Point accumulatedOffset) {
        ViewGroup parentGroup = (ViewGroup) parent;
        accumulatedOffset.x += child.getLeft();
        accumulatedOffset.y += child.getTop();
        if (parentGroup.equals(mainParent)) {
            return;
        }
        getDeepChildOffset(mainParent, parentGroup.getParent(), parentGroup, accumulatedOffset);
    }
}
