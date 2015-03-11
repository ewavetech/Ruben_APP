package se.hiq.losningsappen.common.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.android.vending.billing.IInAppBillingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import se.hiq.losningsappen.R;

/**
 * Created by Naknut on 20/08/14.
 */
public class StoreServiceConnection implements ServiceConnection {

    IInAppBillingService service;

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder binder) {
        service = IInAppBillingService.Stub.asInterface(binder);
        ArrayList<String> skuList = new ArrayList<String>();
        skuList.add("premiumUpgrade");
        skuList.add("gas");
        Bundle querySkus = new Bundle();
        querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
        try {
            Bundle skuDetails = service.getSkuDetails(3,
                    "se.hiq.losningsappen", "inapp", querySkus);
            int response = skuDetails.getInt("RESPONSE_CODE");
            if (response == 0) {
                ArrayList<String> responseList
                        = skuDetails.getStringArrayList("DETAILS_LIST");

                for (String thisResponse : responseList) {
                    JSONObject object = new JSONObject(thisResponse);
                    String sku = object.getString("productId");
                    String price = object.getString("price");
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        service = null;
    }

    public Bundle getPurchases() {
        try {
            return service.getPurchases(3, "se.hiq.losningappen", "inapp", null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
}
