package com.yavuzcalisir.helpers;

import android.os.Build;

/**
 * basic sdk_int checker.
 * 
 * Created by yavuz.calisir on 8/13/2015.
 */
public class OsVersion {

    /**
     * @return true when the caller API version is at least Cupcake 3
     */
    public static boolean cupcake() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE;
    }

    /**
     * @return true when the caller API version is at least Donut 4
     */
    public static boolean donut() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT;
    }

    /**
     * @return true when the caller API version is at least Eclair 5
     */
    public static boolean eclair() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR;
    }

    /**
     * @return true when the caller API version is at least Froyo 8
     */
    public static boolean froyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    /**
     * @return true when the caller API version is at least GingerBread 9
     */
    public static boolean gingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    /**
     * @return true when the caller API version is at least Honeycomb 11
     */
    public static boolean honeycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * @return true when the caller API version is at least Honeycomb 3.2, 13
     */
    public static boolean honeycombMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2;
    }

    /**
     * @return true when the caller API version is at least ICS 14
     */
    public static boolean iceCreamSandwich() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    /**
     * @return true when the caller API version is at least JellyBean 16
     */
    public static boolean jellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * @return true when the caller API version is at least JellyBean MR1 17
     */
    public static boolean jellyBeanMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    /**
     * @return true when the caller API version is at least JellyBean MR2 18
     */
    public static boolean jellyBeanMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    /**
     * @return true when the caller API version is at least Kitkat 19
     */
    public static boolean kitkat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }


    /**
     * @return true when the caller API version is at least Lollipop 21
     */
    public static boolean lollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }


    /**
     * @param apiLevel minimum API level version that has to support the device
     * @return true when the caller API version is at least apiLevel
     */
    public static boolean isAtLeastAPI(int apiLevel) {
        return Build.VERSION.SDK_INT >= apiLevel;
    }

}
