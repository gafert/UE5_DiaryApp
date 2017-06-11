package fhtw.bsa2.gafert_steiner.ue5_diaryapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;
import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;
import es.dmoral.toasty.Toasty;

public class TabbedActivity extends AppCompatActivity {

    // Tab Titles
    public final static String[] TITLE = {"Emotion Statistics", "Add Entry", "Search"};

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.viewPagerContainer);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        setTitle(TITLE[0]);     // Set title to first fragment title

        // Load Entries from file
        try {
            FileIO IO = FileIO.getFileIOInstance();
            FileIO.setContext(this.getBaseContext());
            String emotionJson = IO.readEmotions();

            if (emotionJson != null) {
                Gson gson = new Gson();

                EmotionEntries entries = EmotionEntries.getInstance();
                ArrayList<EmotionEntry> entryList = gson.fromJson(emotionJson, GlobalVariables.listType);
                entries.setEntryList(entryList);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Change the title on fragment change
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setTitle(TITLE[position]);  // Set AppBar title to active fragment title
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // Add custom Navigation Bar
        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.navigationTabBar);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(this, R.drawable.ic_history_white_48dp),  // Set the Icon
                        ContextCompat.getColor(this, R.color.navBar1)                   // Set the Text
                ).title(TITLE[0])
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(this, R.drawable.ic_add_white_48dp),
                        ContextCompat.getColor(this, R.color.navBar2)
                ).title(TITLE[1])
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(this, R.drawable.ic_search_white_48dp),
                        ContextCompat.getColor(this, R.color.navBar3)
                ).title(TITLE[2])
                        .build()
        );

        navigationTabBar.setModels(models);     // Add all tabs
        navigationTabBar.setIsTitled(false);    // Hide Titles from navBar
        navigationTabBar.setViewPager(mViewPager, 0);   // Connect to viewPager


        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toasty.error(this, "I said leave me alone Bastard!", Toast.LENGTH_SHORT).show();
            this.finish();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            Toasty.success(this, "Deleted all entries", Toast.LENGTH_SHORT).show();
            EmotionEntries emotionEntries = EmotionEntries.getInstance();
            emotionEntries.deleteAll();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ChartFragment();
                case 1:
                    return new AddFragment();
                case 2:
                    return new SearchFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
