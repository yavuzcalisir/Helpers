package com.yavuzcalisir.helpers;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;

/**
 *
 * Created by yavuz.calisir on 8/13/2015.
 */
public class AlphaSatColorMatrixEvaluator implements TypeEvaluator {
    private ColorMatrix colorMatrix;
    private float[] elements = new float[20];

    public AlphaSatColorMatrixEvaluator() {
        colorMatrix = new ColorMatrix();
    }

    private ColorMatrix getColorMatrix() {
        return colorMatrix;
    }

    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        float phase = fraction * 3;

        float alpha = Math.min(phase, 2f) / 2f;
        elements[18] = alpha;

        final int MaxBlacker = 100;
        float blackening = (float) Math.round(( 1 - Math.min(phase, 2.5f) / 2.5f ) * MaxBlacker);
        elements[4] = elements[9] = elements[14] = -blackening;

        float invSat = 1 - Math.max(0.2f, fraction);
        float R = 0.213f * invSat;
        float G = 0.715f * invSat;
        float B = 0.072f * invSat;

        elements[0] = R + fraction;
        elements[1] = G;
        elements[2] = B;
        elements[5] = R;
        elements[6] = G + fraction;
        elements[7] = B;
        elements[10] = R;
        elements[11] = G;
        elements[12] = B + fraction;

        colorMatrix.set(elements);
        return colorMatrix;
    }

    public static void animate(final Drawable drawable){
        AlphaSatColorMatrixEvaluator evaluator = new AlphaSatColorMatrixEvaluator();
        final ColorMatrixColorFilter filter = new ColorMatrixColorFilter(evaluator.getColorMatrix());
        drawable.setColorFilter(filter);

        ObjectAnimator animator = ObjectAnimator.ofObject(filter, "colorMatrix", evaluator, evaluator.getColorMatrix());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                drawable.setColorFilter(filter);
            }
        });
        animator.setDuration(4000);
        animator.start();
    }
}
