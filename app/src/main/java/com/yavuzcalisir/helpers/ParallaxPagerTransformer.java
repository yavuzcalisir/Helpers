package com.yavuzcalisir.helpers;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * This is parallax effect for viewPagers. Crete a new instance then set your ViewPager<br><br>
 *
 * Like this:
 * <code>mViewPager.setPageTransformer(false, transformer);</code>
 */
public class ParallaxPagerTransformer implements ViewPager.PageTransformer {

    private int id;
    private float speed = 0.6f;

    /**
     *
     * @param id is your parrallax view.
     */
    public ParallaxPagerTransformer(int id) {
        this.id = id;
    }

    @Override
    public void transformPage(View view, float position) {

        View parallaxView = view.findViewById(id);
        if (position > -1 && position < 1) {
            float width = parallaxView.getWidth();
            parallaxView.setTranslationX(-( position * width * speed ));
            parallaxView.setAlpha(1 - Math.abs(position));
        }
    }

}
