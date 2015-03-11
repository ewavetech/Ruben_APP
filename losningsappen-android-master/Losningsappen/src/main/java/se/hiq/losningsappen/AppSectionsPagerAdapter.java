package se.hiq.losningsappen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by petterstenberg on 2014-08-18.
 * <p/>
 * This adapter manages the main tab fragments in the {@link se.hiq.losningsappen.MainActivity} class.
 */

public class AppSectionsPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    public AppSectionsPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);

        this.fragmentList = fragmentList;
//        Bundle chapterArgs = new Bundle();
//        chapterArgs.putString("text", "Kapiteltest");
//        chapterFragment.setArguments(chapterArgs);
    }

    @Override
    public Fragment getItem(int i) {

        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public String getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Innehåll";
            case 1:
                return "Historik";
            case 2:
                return "Kapiteltest";
            case 3:
                return "Bokshop";
            default:
                return null;
        }
    }

    public int getItemLayout(int i) {
        switch (i) {
            case 0:
                return R.layout.content_tab_item;
            case 1:
                return R.layout.history_tab_item;
            case 2:
                return R.layout.chaptertest_tab_item;
            case 3:
                return R.layout.bookstore_tab_item;
            default:
                return 0;
        }
    }
}
