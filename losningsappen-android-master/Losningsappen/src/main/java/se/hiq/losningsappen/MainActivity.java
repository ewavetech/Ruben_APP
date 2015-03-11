package se.hiq.losningsappen;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import se.hiq.losningsappen.chaptertest.ChapterTestFragment;
import se.hiq.losningsappen.common.controllers.ParseBookInfoJsonTask;
import se.hiq.losningsappen.common.models.BackStackCapability;
import se.hiq.losningsappen.common.models.booksinfo.BookInfo;
import se.hiq.losningsappen.common.models.settings.SettingsContext;
import se.hiq.losningsappen.common.search.SearchAdapter;
import se.hiq.losningsappen.common.search.SearchTask;
import se.hiq.losningsappen.common.settings.SettingsFragment;
import se.hiq.losningsappen.common.util.LoadHistoryTask;
import se.hiq.losningsappen.common.util.SaveHistoryTask;
import se.hiq.losningsappen.content.BookContainerFragment;
import se.hiq.losningsappen.content.model.ContentContext;
import se.hiq.losningsappen.content.model.Task;
import se.hiq.losningsappen.history.HistoryContext;
import se.hiq.losningsappen.history.HistoryFragment;

/**
 * Created by Naknut on 17/06/14.
 * <p/>
 * Main class for app
 */
public class MainActivity extends FragmentActivity implements SearchTask.OnSearchResultListener,
        ContentContext.ContentListener, HistoryContext.HistoryContentListener, SettingsContext.SettingsListener {

    public static final int BOOK_CONTAINER_FRAGMENT_POSITION = 0;
    public static final int SPLASHSCREEN_DELAY_MILLIS = 4000;
    private static final String HOCKEYAPP_APP_ID = "b54ef70416f6a1cfd95d217b7ddd7d97";
    private static final String HISTORY_DATA = "history_data";
    private static final String TAG = "MAIN_ACTIVITY";
    private static final boolean DEVELOPER_MODE = false;
    private AppSectionsPagerAdapter adapter;
    private ViewPager viewPager;
    private SearchAdapter searchAdapter;
    private ListView searchResultListView;
    private SearchTask searchTask;
    private int currentTab = 0;
    private List<Fragment> fragmentList;
    private String currentChapterTitle = "";
    private Dialog mSplashDialog;
    private String bookTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        if (DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .detectAll()
                    .penaltyDialog()
                    .penaltyLog()
                    .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }

    showSplashScreen();
    setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        new LoadHistoryTask().execute();
        HistoryContext.getInstance().addHistoryContentListener(this);
        SettingsContext.getInstance().addSettingsListener(this);

// TODO Fix issue with leaking receiver
//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
//                final NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
//                if (activeNetwork != null && activeNetwork.isConnected()) {
//                    Toast.makeText(MainActivity.this, "Have connection", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
//                }
//            }
//        }, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        new ParseBookInfoJsonTask().execute(this);

        setContentView(R.layout.activity_main);

        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(Fragment.instantiate(this, BookContainerFragment.class.getName()));
        fragmentList.add(Fragment.instantiate(this, HistoryFragment.class.getName()));
        fragmentList.add(Fragment.instantiate(this, ChapterTestFragment.class.getName()));
        fragmentList.add(Fragment.instantiate(this, SettingsFragment.class.getName()));

        adapter = new AppSectionsPagerAdapter(getSupportFragmentManager(), fragmentList);

        initSearch();

        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        addTabsToActionbar();

        viewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {

                        getActionBar().setSelectedNavigationItem(position);
                    }
                });

        ContentContext.getInstance().addListener(this);
        checkForUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForCrashes();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HistoryContext.getInstance().removeHistoryContentListener(this);
        ContentContext.getInstance().removeListener(this);
        SettingsContext.getInstance().removeSettingsListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) searchResultListView.setVisibility(View.GONE);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
//                processSearchQuery(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() > 0) {
                    processSearchQuery(s);
                    searchResultListView.setVisibility(View.VISIBLE);
                } else {
                    searchAdapter.clear();
                    searchResultListView.setVisibility(View.GONE);
                }
                return false;
            }

            private void processSearchQuery(String query) {

                searchAdapter.clear();
                if (searchTask != null) searchTask.cancel(true);

                if (query != null || query.length() > 0) {
                    searchTask = new SearchTask();
                    searchTask.setDelegate(MainActivity.this);
                    searchTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, query);
                }
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {
            Bundle extra = data.getExtras();
            String color = extra.getString(SettingsFragment.ACTIONBAR_COLOR_KEY).trim();
            setActionBarColor(color);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (viewPager.getCurrentItem() == BOOK_CONTAINER_FRAGMENT_POSITION)
            currentChapterTitle = title.toString();
    }

    @Override
    public void onSearchResponse(Task response) {
        searchAdapter.add(response);
        searchAdapter.sort(new Comparator<Task>() {
            @Override
            public int compare(Task lhs, Task rhs) {
                return lhs.toString().compareTo(rhs.toString());
            }
        });
        searchAdapter.notifyDataSetChanged();
        searchResultListView.invalidate();
    }

    protected void showSplashScreen() {
        mSplashDialog = new Dialog(this, R.style.Theme_Base);
        mSplashDialog.setContentView(R.layout.splash_screen);
        mSplashDialog.setCancelable(false);
        mSplashDialog.show();

        // Set Runnable to remove splash screen just in case
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSplashDialog != null) {
                    mSplashDialog.dismiss();
                    mSplashDialog = null;
                    getActionBar().show();
                }
            }
        }, SPLASHSCREEN_DELAY_MILLIS);
    }

    private void addTabsToActionbar() {

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
                viewPager.setCurrentItem(tab.getPosition());
                currentTab = tab.getPosition();

                if (viewPager.getCurrentItem() == BOOK_CONTAINER_FRAGMENT_POSITION) {
                    setTitle(currentChapterTitle);
                } else {
                    setTitle(bookTitle);
                }
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {

            }
        };

        for (int i = 0; i < adapter.getCount(); i++) {
            getActionBar().addTab(
                    getActionBar().newTab()
                            .setCustomView(adapter.getItemLayout(i))
                            .setTabListener(tabListener));
        }
    }

    private void initSearch() {
        List<Task> searchResultsList = new ArrayList<Task>();

        int abContainerViewID = getResources().getIdentifier("action_bar_container", "id", "android");
        FrameLayout actionBarContainer = (FrameLayout) findViewById(abContainerViewID).getParent().getParent();
        LinearLayout customView = (LinearLayout) getLayoutInflater().inflate(R.layout.search_result, null);

        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        customView.setY(statusBarHeight);
        actionBarContainer.addView(customView);

        searchResultListView = (ListView) customView.findViewById(R.id.search_result_list);

        searchAdapter = new SearchAdapter(this, android.R.layout.simple_list_item_1,
                searchResultsList);
        searchResultListView.setAdapter(searchAdapter);
    }

    public void setActionBarColor(String color) {
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
        this.invalidateOptionsMenu();
    }

    private void checkForCrashes() {
        CrashManager.register(this, HOCKEYAPP_APP_ID);
    }

    private void checkForUpdates() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                UpdateManager.register(MainActivity.this, HOCKEYAPP_APP_ID);
                return null;
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        ((BackStackCapability) adapter.getItem(currentTab)).onBackPressed();
    }

    public void onSuperBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onContentStateChanged(ContentContext.ContentState contentState) {
        setTitle(ContentContext.getInstance().getActionBarDescription());
    }

    public void setTab(int tab) {
        this.viewPager.setCurrentItem(tab);
    }

    @Override
    public void onHistoryContentChanged() {
        new SaveHistoryTask().execute();
    }

    @Override
    public void onBookInfosChanged(List<BookInfo> bookInfos) {
    }

    @Override
    public void onActiveBookInfoChanged(BookInfo activeBook) {
        setActionBarColor(activeBook.getColor());
        setTitle(activeBook.getTitle());
        bookTitle = activeBook.getTitle();
    }
}
