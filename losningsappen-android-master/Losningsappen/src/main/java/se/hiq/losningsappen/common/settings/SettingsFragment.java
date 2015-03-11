package se.hiq.losningsappen.common.settings;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import se.hiq.losningsappen.MainActivity;
import se.hiq.losningsappen.R;
import se.hiq.losningsappen.common.models.BackStackCapability;
import se.hiq.losningsappen.common.models.booksinfo.BookInfo;
import se.hiq.losningsappen.common.models.settings.SettingsContext;
import se.hiq.losningsappen.settings.BuyBookActivity;

/**
 * Created by Naknut on 03/08/14.
 */

public class SettingsFragment extends ListFragment implements ServiceConnection, BackStackCapability {

    public static final String ACTIONBAR_COLOR_KEY = "actionBarColor";
    public static final int API_VERSION = 3;
    public static final String SKU_TYPE = "inapp";
    public static final String RESPONSE_CODE_KEY = "RESPONSE_CODE";
    public static final String ITEM_ID_LIST_KEY = "ITEM_ID_LIST";
    public static final String DETAILS_LIST_KEY = "DETAILS_LIST";
    public static final String PRODUCT_ID_KEY = "productId";
    public static final String TITLE_KEY = "title";
    public static final String DESCRIPTION_KEY = "description";
    public static final String CLICKED_BOOK_KEY = "clickedBook";
    private SettingsArrayAdapter adapter;
    private IInAppBillingService connection;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        activity.bindService(new Intent("com.android.vending.billing.InAppBillingService.BIND"),
                this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDetach() {
        if (connection != null) getActivity().unbindService(this);
        super.onDetach();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getActivity(), BuyBookActivity.class);
        intent.putExtra(CLICKED_BOOK_KEY, position);
        startActivity(intent);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        new ObtainBooksStoreInfoTask(iBinder).execute();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        connection = null;
    }

    @Override
    public void onBackPressed() {
        ((MainActivity) getActivity()).setTab(2);
    }

    private class SettingsArrayAdapter extends ArrayAdapter<BookInfo> {

        Activity context;

        public SettingsArrayAdapter(Activity context, List<BookInfo> list) {
            super(context, R.layout.chapter, list);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = context.getLayoutInflater().inflate(android.R.layout.simple_list_item_2, null);
            TextView title = (TextView) convertView.findViewById(android.R.id.text1);
            TextView description = (TextView) convertView.findViewById(android.R.id.text2);
            title.setText(getItem(position).getTitle());
            description.setText(getItem(position).getDescription());
            return convertView;
        }
    }

    private class ObtainBooksStoreInfoTask extends AsyncTask<Void, Void, Void> {

        private final IBinder iBinder;

        private ObtainBooksStoreInfoTask(IBinder iBinder) {
            this.iBinder = iBinder;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            connection = IInAppBillingService.Stub.asInterface(iBinder);

            try {
                ArrayList<String> skuList = new ArrayList<String>();
                for (BookInfo bookInfo : SettingsContext.getInstance().getBookInfos()) {
                    skuList.add(bookInfo.getProductIdentifier());
                }
                skuList.add("matte5000_test0");

                Bundle querySkus = new Bundle();
                querySkus.putStringArrayList(ITEM_ID_LIST_KEY, skuList);

                Bundle skuDetails = connection.getSkuDetails(API_VERSION,
                        getActivity().getPackageName(), SKU_TYPE, querySkus);

                int response = skuDetails.getInt(RESPONSE_CODE_KEY);

                if (response == 0) {
                    ArrayList<String> responseList = skuDetails.getStringArrayList(DETAILS_LIST_KEY);

                    for (String thisResponse : responseList) {
                        JSONObject object = new JSONObject(thisResponse);
                        BookInfo bookInfo = SettingsContext.getInstance().getBookInfoWithId(object.getString(PRODUCT_ID_KEY));
                        if (bookInfo != null) {
                            bookInfo.setTitle(object.getString(TITLE_KEY));
                            bookInfo.setDescription(object.getString(DESCRIPTION_KEY));
                        }
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            adapter = new SettingsArrayAdapter(getActivity(), SettingsContext.getInstance().getBookInfos());
            SettingsFragment.this.setListAdapter(adapter);
        }
    }
}
