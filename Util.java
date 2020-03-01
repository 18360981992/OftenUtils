package com.pateo.cs.music.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.pateo.cs.music.R;
import com.pateo.cs.music.fragment.SmartFragmentFactory;
import com.pateo.music.logic.bean.Album;
import com.pateo.music.logic.bean.Artist;
import com.pateo.music.logic.bean.External;
import com.pateo.music.logic.bean.MusicEntry;
import com.pateo.music.logic.bean.Track;
import com.qinggan.system.MultiWindowManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import cn.kuwo.base.bean.Music;

public class Util {

    private static final String TAG = "Util----yi.hu";

    private static long exitTime;
    public static boolean enableClick(int CLICK_TIME) {
        long time = System.currentTimeMillis();
        if (Math.abs(time - exitTime) < CLICK_TIME) {
            return true;
        }
        exitTime = time;
        return false;
    }

    /**
     * convert List to String
     *
     * @param historyList
     * @return
     */
    public static String list2String(List<String> historyList) {
        String selectListString = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(historyList);
            selectListString = new String(Base64.encode(byteArrayOutputStream.toByteArray(),
                    Base64.DEFAULT));
            objectOutputStream.close();
        } catch (IOException e) {
            Dog.e(TAG, "list2String IOException : " + e);
        }
        return selectListString;
    }

    /**
     * convert String to List
     *
     * @param historyString
     * @return
     */
    @SuppressWarnings("unchecked")
    public static LinkedList<String> string2List(String historyString) {
        if (TextUtils.isEmpty(historyString)) {
            return null;
        }

        LinkedList<String> historyList = null;
        try {
            byte[] mobileBytes = Base64.decode(historyString.getBytes(), Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            historyList = (LinkedList<String>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            Dog.e(TAG, "string2List Exception : " + e);
        }
        return historyList;
    }

    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            Dog.e(TAG, "setIndicator NoSuchFieldException : " + e);
        }

        if (tabStrip == null) {
            Dog.i(TAG, "setIndicator....for null tabStrip");
            return;
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            Dog.e(TAG, "setIndicator IllegalAccessException : " + e);
        }

        if (llTab == null) {
            Dog.i(TAG, "setIndicator....for null llTab");
            return;
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }


    public static void showFragment(FragmentManager manager, int layoutId, int position, boolean isRestore, boolean isReplace) {
        FragmentTransaction ft = manager.beginTransaction();
        if(!isRestore){
            ft.setCustomAnimations(R.anim.scale_in, R.anim.scale_out);
        }
        Fragment current = SmartFragmentFactory.getFragment(position);
        boolean needShowCurrent = true;
        if (null == current) { // Added first.
            Dog.i(TAG, "current fragment is null, need to add fragment.....");
            current = SmartFragmentFactory.createFragment(position);
            ft.add(layoutId, current, SmartFragmentFactory.getTag(position));
            needShowCurrent = false; // Already done.
        }

        final ArrayMap<Integer, Fragment> fragments = SmartFragmentFactory.getAllFragments();
        final int N = fragments.size();
        for (int i = 0; i < N; i++) {
            Fragment fragment = fragments.get(i);
            if (fragment != null) {
                if ((fragments.size() > position && i == position)) {
                    if (needShowCurrent) {
                        if (isReplace) {
                            ft.replace(layoutId, fragment);
                        } else {
                            ft.show(fragment);
                        }
                    }
                } else {
                    ft.hide(fragment);
                }
            }
        }
        try {
            ft.commitNowAllowingStateLoss();
            manager.executePendingTransactions();
        }catch (Exception e){
            Dog.e(TAG,"万恶的 google...");
        }
    }

    public static MusicEntry convertNetToLocal(Music music, String albumUrl) {
        if (music == null) {
            Dog.i("Util", "convertNetToLocal failed...");
            return null;
        }

        MusicEntry musicEntry = new MusicEntry();
        Track track = new Track();
        track.setId((int) music.rid);
        track.setName(decode(music.name));
        if (TextUtils.isEmpty(music.filePath)) {
            //收藏中的entry比较会拿path，如果path无效，会出现异常，故此处放个ugly路径
            //这个路径是没有实际意义的
            track.setPath("kuwo/music/ugly/file/path");
        } else {
            track.setPath(music.filePath);
        }
        musicEntry.setTrack(track);

        Album album = new Album();
        album.setAlbumName(decode(music.album));
        album.setImageUrl(albumUrl);
        musicEntry.setAlbum(album);

        Artist artist = new Artist(-1, decode(music.artist));
        musicEntry.setArtist(artist);

        musicEntry.setExternal(External.ONLINEMUSIC.ordinal());

        return musicEntry;
    }

    public static String decode(String in) {
        if (TextUtils.isEmpty(in)) {
            return "";
        }
        return in.replace("&nbsp;", " ")
                .replace("&quot;", "\"")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&apos;", "\'");
    }

    public static String decodeLyricStr(String in) {
        if (TextUtils.isEmpty(in)) {
            return "";
        }

        //歌词中存在<4160,-4160>这种格式的字串,需要替换掉
        return in.replaceAll("<-?\\d+,-?\\d+>", "");
    }

    public static void launchMusic(Context context) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.pateo.borgward.music", "com.pateo.cs.music.activity.MusicPlayerActivity"));
        intent.putExtra("exApp", true);
        intent.putExtra("action", "home");
        intent.putExtra("isShowForIdentifyPlay", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        MultiWindowManager.getInstance(context).startActivityOnMainWindow(intent, false);
    }

    public static void launchSetting(Context context) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.pateo.borgward.settings", "com.pateo.borgward.settings.bluetooth.BluetoothActivity"));
        intent.putExtra("exApp", true);
        intent.putExtra("action", "home");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MultiWindowManager.getInstance(context).startActivityOnMainWindow(intent, false);
    }

    public static View getTabView(TabLayout tabLayout, int index) {
        View tabView = null;
        TabLayout.Tab tab = tabLayout.getTabAt(index);
        Field view = null;
        try {
            view = TabLayout.Tab.class.getDeclaredField("mView");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        view.setAccessible(true);
        try {
            tabView = (View) view.get(tab);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return tabView;
    }

    @SuppressLint("NewApi")
    public static void setProgressDrawable(@NonNull ProgressBar bar, @DrawableRes int resId) {
        Drawable layerDrawable = bar.getResources().getDrawable(resId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable d = getMethod("tileify", bar, new Object[] { layerDrawable, false });
            bar.setProgressDrawable(d);
        } else {
            bar.setProgressDrawableTiled(layerDrawable);
        }
    }

    private static Drawable getMethod(String methodName, Object o, Object[] paras) {
        Drawable newDrawable = null;
        try {
            Class<?> c[] = new Class[2];
            c[0] = Drawable.class;
            c[1] = boolean.class;
            Method method = ProgressBar.class.getDeclaredMethod(methodName, c);
            method.setAccessible(true);
            newDrawable = (Drawable) method.invoke(o, paras);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return newDrawable;
    }

    /**
     * 判断服务是否开启
     * @param mContext
     * @param className 这里是包名+类名 xxx.xxx.xxx.TestService
     * @return
     */
    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);

        if (!(serviceList.size()>0)) {
            return isRunning;
        }

        for (int i=0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }

        return isRunning;
    }
}
