package online.westbay.trackingapp.mainpage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by ajayg on 11/11/2016.
 */

public class HomeFragmentViewPagerAdapter extends FragmentStatePagerAdapter {

    private static int NUM_ITEMS = 4;

    public HomeFragmentViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return DashboardFragment.newInstance();
            case 1:
                return MapFragment.newInstance("", "");
            case 2:
                return VehicleFragment.newInstance();
            case 3:
                return ProfileFragment.newInstance();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
