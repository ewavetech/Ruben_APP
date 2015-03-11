//package se.hiq.losningsappen.common.search;
//
//import android.os.AsyncTask;
//import android.view.View;
//import android.widget.ListView;
//import android.widget.SearchView;
//
//import se.hiq.losningsappen.MainActivity;
//
///**
// * Created by petterstenberg on 2014-09-02.
// * <p/>
// * This class blablabla //TODO
// */
//public class SearchTextListener implements SearchView.OnQueryTextListener {
//
//    private final ListView searchResultListView;
//
//    public SearchTextListener(ListView searchResultListView) {
//        this.searchResultListView = searchResultListView;
//        SearchAdapter searchAdapter = (SearchAdapter) searchResultListView.getAdapter();
//    }
//
//    @Override
//    public boolean onQueryTextSubmit(String s) {
////                processSearchQuery(s);
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String s) {
//        if (s.length() > 0) {
//            processSearchQuery(s);
//            searchResultListView.setVisibility(View.VISIBLE);
//        } else {
//            searchAdapter.clear();
//            searchResultListView.setVisibility(View.GONE);
//        }
//        return false;
//    }
//
//    private void processSearchQuery(String query) {
//
//        searchAdapter.clear();
//        if (searchTask != null) searchTask.cancel(true);
//
//        if (query != null || query.length() > 0) {
//            searchTask = new SearchTask();
//            searchTask.setDelegate(MainActivity.this);
//            searchTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, query);
//        }
//    }
//}
