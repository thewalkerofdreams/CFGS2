package es.iesnervion.yeray.pocketcharacters.Activities;

import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import es.iesnervion.yeray.pocketcharacters.Activities.ui.main.SectionsPagerAdapter;
import es.iesnervion.yeray.pocketcharacters.Fragments.FragmentTutorial01;
import es.iesnervion.yeray.pocketcharacters.Fragments.FragmentTutorial02;
import es.iesnervion.yeray.pocketcharacters.Fragments.FragmentTutorial03;
import es.iesnervion.yeray.pocketcharacters.Fragments.FragmentTutorial04;
import es.iesnervion.yeray.pocketcharacters.R;

public class TutorialActivity extends AppCompatActivity implements FragmentTutorial01.OnFragmentInteractionListener, FragmentTutorial02.OnFragmentInteractionListener,
        FragmentTutorial03.OnFragmentInteractionListener, FragmentTutorial04.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}