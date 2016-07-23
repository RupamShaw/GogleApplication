package com.jagdiv.android.gogleapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class BaseActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    //Need this to set the title of the app bar
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private static final String SELECTED_ITEM_ID = "selected_item_id";
    private static final String FIRST_TIME = "first_time";
    private Toolbar mToolbar;
    private NavigationView mDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mSelectedId;
    private boolean mUserSawDrawer = false;
    public static String EXTRA_TYPE="EXTRA_TYPE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
void toolBar(Bundle savedInstanceState,String toolbarTitle){
    mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
    //Notice how the title is set on the Collapsing Toolbar Layout instead of the Toolbar
    mCollapsingToolbarLayout.setTitle(toolbarTitle);
    mToolbar = (Toolbar) findViewById(R.id.app_bar);
    setSupportActionBar(mToolbar);
    mDrawer = (NavigationView) findViewById(R.id.navigation_drawer);
    mDrawer.setNavigationItemSelectedListener(this);
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerToggle = new ActionBarDrawerToggle(this,
            mDrawerLayout,
            mToolbar,
            R.string.drawer_open,
            R.string.drawer_close);
    mDrawerLayout.setDrawerListener(mDrawerToggle);
    mDrawerToggle.syncState();
    if (!didUserSeeDrawer()) {
        showDrawer();
        markDrawerSeen();
    } else {
        hideDrawer();
    }
    mSelectedId = savedInstanceState == null ? R.id.navigation_item_1 : savedInstanceState.getInt(SELECTED_ITEM_ID);
}
    private boolean didUserSeeDrawer() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserSawDrawer = sharedPreferences.getBoolean(FIRST_TIME, false);
        return mUserSawDrawer;
    }

    private void markDrawerSeen() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserSawDrawer = true;
        sharedPreferences.edit().putBoolean(FIRST_TIME, mUserSawDrawer).apply();
    }

    private void showDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    private void hideDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private void navigate(int mSelectedId) {
        Intent intent = null;
        if (mSelectedId == R.id.navigation_item_1) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
          //  intent = new Intent(this, CalendarActivity.class);
         //   intent.putExtra(PDFActivity.EXTRA_TYPE,R.string.title_activity_home);
            //startActivity(intent);
        }
        if (mSelectedId == R.id.navigation_item_2) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this, CalendarActivity.class);
            startActivity(intent);
        }
        if (mSelectedId == R.id.navigation_item_3 ){
            mDrawerLayout.closeDrawer(GravityCompat.START);
            //intent = new Intent(this, Photos.class);
          //  intent.putExtra(PDFActivity.EXTRA_TYPE,R.string.title_activity_photos);
            //startActivity(intent);
        }
        if (mSelectedId == R.id.navigation_item_4) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this, PDFActivity.class);

            intent.putExtra(PDFActivity.EXTRA_TYPE, getResources().getString(R.string.title_activity_notifications));
            startActivity(intent);
        }
        if (mSelectedId == R.id.navigation_item_5) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
           intent = new Intent(this, PDFActivity.class);
//            intent = new Intent(this, MyPdfViewActivity.class);
            intent.putExtra(PDFActivity.EXTRA_TYPE, getResources().getString(R.string.title_activity_news_letter));
            startActivity(intent);
        }
        if (mSelectedId == R.id.navigation_item_6) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this, PDFActivity.class);
            intent.putExtra(PDFActivity.EXTRA_TYPE, getResources().getString(R.string.title_activity_documents));
            startActivity(intent);
        }

        if (mSelectedId == R.id.navigation_item_7) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this, PDFActivity.class);
            intent.putExtra(PDFActivity.EXTRA_TYPE, getResources().getString(R.string.title_activity_notes));
            startActivity(intent);
        }
        if (mSelectedId == R.id.navigation_item_8) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this, PDFActivity.class);
            intent.putExtra(PDFActivity.EXTRA_TYPE, getResources().getString(R.string.title_activity_timetables));
            startActivity(intent);
        }
        if (mSelectedId == R.id.navigation_item_9) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this, PDFActivity.class);
            intent.putExtra(PDFActivity.EXTRA_TYPE, getResources().getString(R.string.title_activity_pandc));
            startActivity(intent);
        }
        if (mSelectedId == R.id.navigation_item_10) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this, PDFActivity.class);
            intent.putExtra(PDFActivity.EXTRA_TYPE, getResources().getString(R.string.title_activity_canteens));
            startActivity(intent);
        }
        if (mSelectedId == R.id.navigation_item_11) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
      //      intent = new Intent(this, PDFActivity.class);
    //        intent.putExtra(PDFActivity.EXTRA_TYPE, getResources().getString(R.string.title_activity_videos));
        //    startActivity(intent);
        }
        if (mSelectedId == R.id.navigation_item_12) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this, PDFActivity.class);
            intent.putExtra(PDFActivity.EXTRA_TYPE, getResources().getString(R.string.title_activity_links));
            startActivity(intent);
        }
        if (mSelectedId == R.id.navigation_item_13) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this, FormsActivity.class);
           // intent.putExtra(FormsActivity.EXTRA_TYPE, getResources().getString(R.string.title_activity_forms));
            startActivity(intent);
        }

        if (mSelectedId == R.id.navigation_item_14) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
         //   intent = new Intent(this, PDFActivity.class);
         //   intent.putExtra(PDFActivity.EXTRA_TYPE, getResources().getString(R.string.title_activity_settings));
         //   startActivity(intent);
        }
        if (mSelectedId == R.id.navigation_item_15) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
          //  intent = new Intent(this, PDFActivity.class);
            //intent.putExtra(PDFActivity.EXTRA_TYPE, getResources().getString(R.string.title_activity_feedback));
          //  startActivity(intent);
        }
        if (mSelectedId == R.id.navigation_item_16) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            //  intent = new Intent(this, PDFActivity.class);
            //intent.putExtra(PDFActivity.EXTRA_TYPE, getResources().getString(R.string.title_activity_Signout));
            //  startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        menuItem.setChecked(true);
        mSelectedId = menuItem.getItemId();

        navigate(mSelectedId);
        return true;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_ITEM_ID, mSelectedId);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
