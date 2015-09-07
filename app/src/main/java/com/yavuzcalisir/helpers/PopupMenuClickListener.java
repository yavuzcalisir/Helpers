package com.yavuzcalisir.helpers;

import android.content.Context;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.lang.reflect.Field;

/**
 * usage: view.setOnClickListener(new PopupMenuClickListener())
 *
 * Created by yavuz.calisir on 8/28/2015.
 */
public class PopupMenuClickListener implements View.OnClickListener {

    private static final String TAG = "PopupMenuClickListener";

    private Context mContext;
    private View anchorView;
    private int menuResId;
    private boolean forceIconShow;
    private OnMenuItemClikcListener onMenuItemClikcListener;

    public PopupMenuClickListener(View anchorView, int menuResId, OnMenuItemClikcListener onMenuItemClikcListener){
        this(anchorView, menuResId, false, onMenuItemClikcListener);
    }

    public PopupMenuClickListener(View anchorView, int menuResId, boolean forceToShowIcon, OnMenuItemClikcListener onMenuItemClikcListener) {
        this.anchorView = anchorView;
        this.forceIconShow = forceToShowIcon;
        this.mContext = anchorView.getContext();
        this.menuResId = menuResId;
        this.onMenuItemClikcListener = onMenuItemClikcListener;
    }

    @Override
    public void onClick(View view) {
        PopupMenu popupMenu = new PopupMenu(mContext, anchorView) {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                if(onMenuItemClikcListener != null){
                    onMenuItemClikcListener.onMenuItemClick(item.getItemId());
                }
                return true;
            }

        };

        if(forceIconShow){
            forceShowIcons(popupMenu);
        }

        popupMenu.inflate(menuResId);
        popupMenu.show();
    }

    private void forceShowIcons(PopupMenu menu){
        Object menuHelper;
        Class[] argTypes;
        try {
            Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
            fMenuHelper.setAccessible(true);
            menuHelper = fMenuHelper.get(menu);
            argTypes = new Class[]{boolean.class};
            menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
        } catch (Exception e) {
            Log.w(TAG, "error forcing menu icons to show", e);
        }
    }


    public interface OnMenuItemClikcListener{
        void onMenuItemClick(int menuItemId);
    }

}
