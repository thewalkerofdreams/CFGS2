package com.example.adventuremaps.Adapters;

import android.content.res.Resources;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.adventuremaps.Fragments.Tutorial.CommonFragmentSection01;
import com.example.adventuremaps.Fragments.Tutorial.CommonFragmentSection02;
import com.example.adventuremaps.Fragments.Tutorial.CommonFragmentSection03;
import com.example.adventuremaps.Fragments.Tutorial.OfflineMapsSectionFragment;
import com.example.adventuremaps.Fragments.Tutorial.RoutesSectionFragment;
import com.example.adventuremaps.R;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final Resources resources;
    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public ViewPagerAdapter(final Resources resources, FragmentManager fm) {
        super(fm);
        this.resources = resources;
    }

    @Override
    public Fragment getItem(int position) {
        final Fragment result;
        switch (position) {
            case 0:
                result = new CommonFragmentSection01();//Primera sección del tutorial
                break;
            case 1:
                result = new CommonFragmentSection02();//Segunda sección del tutorial
                break;
            case 2:
                result = new CommonFragmentSection03();//Tercera sección del tutorial
                break;
            case 3:
                result = new RoutesSectionFragment();//Cuarta sección del tutorial
                break;
            case 4:
                result = new OfflineMapsSectionFragment();//Quinta sección del tutorial
                break;
            default:
                result = null;
                break;
        }

        return result;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        switch (position) {
            case 0:
                return resources.getString(R.string.page_1);
            case 1:
                return resources.getString(R.string.page_2);
            case 2:
                return resources.getString(R.string.page_3);
            case 3:
                return resources.getString(R.string.page_4);
            case 4:
                return resources.getString(R.string.page_5);
            default:
                return null;
        }
    }

    /**
     * On each Fragment instantiation we are saving the reference of that Fragment in a Map
     * It will help us to retrieve the Fragment by position
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    /**
     * Remove the saved reference from our Map on the Fragment destroy
     *
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }


    /**
     * Get the Fragment by position
     *
     * @param position tab position of the fragment
     * @return
     */
    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}