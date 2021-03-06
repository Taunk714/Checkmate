package com.teamred.checkmate.ui.calendar;

/*import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import androidx.annotation.NonNull;*/
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence titles[];
    int numbOfTabs;

    public ViewPagerAdapter(FragmentManager fm, CharSequence titles[], int mNumbOfTabs) {
        super(fm);
        this.titles = titles;
        this.numbOfTabs = mNumbOfTabs;
    }

    public Fragment getItem(int position) {
        if (position == 0) {
            CompactCalendarTab compactCalendarTab = new CompactCalendarTab();
            return compactCalendarTab;
        } else {
            Tab2 tab2 = new Tab2();
            return tab2;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return numbOfTabs;
    }
}