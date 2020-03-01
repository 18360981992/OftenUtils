package com.pateo.cs.music.fragment;

import android.support.v4.app.Fragment;
import android.util.ArrayMap;

import com.pateo.cs.music.bean.FragmentIndex;

public class SmartFragmentFactory {

    public static final String TAG_DISCOVERY = "discovery";
    public static final String TAG_LOCAL = "local";
    public static final String TAG_MINE = "mine";
    public static final String TAG_SEARCH = "search";
    public static final String TAG_PLAYER = "player";
    public static final String TAG_PLAYLIST = "playlist";
    public static final String TAG_AUX = "aux";

    private static final int FRAGMENT_SIZE = 7;

    // tab
    private ArrayMap<Integer, Fragment> mFragments;
    private static SmartFragmentFactory sFactory;

    static {
        sFactory = new SmartFragmentFactory();
    }

    private SmartFragmentFactory() {
        mFragments = new ArrayMap<>(FRAGMENT_SIZE);
        reset();
    }

    private void reset() {
        mFragments.clear();
        mFragments.put(FragmentIndex.DISCOVERY_FRAGMENT, null);
        mFragments.put(FragmentIndex.LOCAL_FRAGMENT, null);
        mFragments.put(FragmentIndex.MINE_FRAGMENT, null);
        mFragments.put(FragmentIndex.SEARCH_FRAGMENT, null);
        mFragments.put(FragmentIndex.PLAYER_FRAGMENT, null);
        mFragments.put(FragmentIndex.PLAYLIST_FRAGMENT, null);
        mFragments.put(FragmentIndex.AUX_FRAGMENT, null);
    }

    public static ArrayMap getAllFragments() {
        return sFactory.getAllFragmentsInternal();
    }

    private ArrayMap getAllFragmentsInternal() {
        return mFragments;
    }

    public static Fragment getFragment(int position) {
        return sFactory.getFragmentInternal(position);
    }

    private Fragment getFragmentInternal(int position) {
        return mFragments.get(position);
    }

    public static Fragment createFragment(int position) {
        return sFactory.createFragmentInternal(position);
    }

    private Fragment createFragmentInternal(int position) {
        Fragment fragment = mFragments.get(position);

        if (null == fragment) {
            switch (position) {
                case FragmentIndex.DISCOVERY_FRAGMENT:
                    fragment = new MenuDiscoveryFragment();
                    break;
                case FragmentIndex.LOCAL_FRAGMENT:
                    fragment = new MenuLocalFragment();
                    break;
                case FragmentIndex.MINE_FRAGMENT:
                    fragment = new MenuMineFragment();
                    break;
                case FragmentIndex.SEARCH_FRAGMENT:
                    fragment = new MenuSearchFragment();
                    break;
                case FragmentIndex.PLAYER_FRAGMENT:
                    fragment = new MusicPlayerFragment();
                    break;
                case FragmentIndex.PLAYLIST_FRAGMENT:
                    fragment = new MusicPlaylistFragment();
                    break;
                case FragmentIndex.AUX_FRAGMENT:
                    fragment = new MusicAuxFragment();
                    break;
            }
            mFragments.put(position, fragment);
        }
        return fragment;
    }

    public static void replaceFragment(Fragment fragment, int position) {
        sFactory.replaceFragmentInternal(fragment, position);
    }

    private void replaceFragmentInternal(Fragment fragment, int position) {
        mFragments.put(position, fragment);
    }

    public static void destroyFragments() {
        sFactory.destroyFragmentsInternal();
    }

    private void destroyFragmentsInternal() {
        reset();
    }

    public static String getTag(int position) {
        switch (position) {
            case FragmentIndex.DISCOVERY_FRAGMENT:
                return TAG_DISCOVERY;
            case FragmentIndex.LOCAL_FRAGMENT:
                return TAG_LOCAL;
            case FragmentIndex.MINE_FRAGMENT:
                return TAG_MINE;
            case FragmentIndex.SEARCH_FRAGMENT:
                return TAG_SEARCH;
            case FragmentIndex.PLAYER_FRAGMENT:
                return TAG_PLAYER;
            case FragmentIndex.PLAYLIST_FRAGMENT:
                return TAG_PLAYLIST;
            case FragmentIndex.AUX_FRAGMENT:
                return TAG_AUX;
        }
        return null;
    }

    public static int getId(String tag) {
        switch (tag) {
            case TAG_DISCOVERY:
                return FragmentIndex.DISCOVERY_FRAGMENT;
            case TAG_LOCAL:
                return FragmentIndex.LOCAL_FRAGMENT;
            case TAG_MINE:
                return FragmentIndex.MINE_FRAGMENT;
            case TAG_SEARCH:
                return FragmentIndex.SEARCH_FRAGMENT;
            case TAG_PLAYER:
                return FragmentIndex.PLAYER_FRAGMENT;
            case TAG_PLAYLIST:
                return FragmentIndex.PLAYLIST_FRAGMENT;
            case TAG_AUX:
                return FragmentIndex.AUX_FRAGMENT;
        }
        return -1;
    }
}
