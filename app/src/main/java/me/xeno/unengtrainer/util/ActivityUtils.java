package me.xeno.unengtrainer.util;

/**
 * Created by Administrator on 2017/5/15.
 */

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * This provides methods to help Activities load their UI.
 */
public class ActivityUtils {

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     *
     */
    public static void addFragmentToActivity (@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment, int frameId) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static void replaceFragment (@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment, int frameId) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.commit();
    }

//    public void setFragments(int index) {
//        for (int i = 0; i < mFragments.size(); i++) {
//            FragmentTransaction ft = mFragmentManager.beginTransaction();
//            Fragment fragment = mFragments.get(i);
//            if (i == index) {
//                ft.show(fragment);
//            } else {
//                ft.hide(fragment);
//            }
//            ft.commit();
//        }
//        mCurrentTab = index;
//    }
//
//    private void initFragments() {
//        for (Fragment fragment : mFragments) {
//            mFragmentManager.beginTransaction().add(mContainerViewId, fragment).hide(fragment).commit();
//        }
//
//        setFragments(0);
//    }

}