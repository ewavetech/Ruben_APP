package se.hiq.losningsappen.settings;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;

import se.hiq.losningsappen.R;
import se.hiq.losningsappen.common.models.NokActivity;
import se.hiq.losningsappen.common.models.booksinfo.BookInfo;
import se.hiq.losningsappen.common.models.settings.SettingsContext;

/**
 * Created by Naknut on 28/08/14.
 */
public class BuyBookActivity extends NokActivity implements ServiceConnection {

    private IInAppBillingService connection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bindService(new Intent("com.android.vending.billing.InAppBillingService.BIND"),
                this, Context.BIND_AUTO_CREATE);

        setContentView(R.layout.activity_buy_book);
        final int clickedBook = getIntent().getExtras().getInt("clickedBook");
        final BookInfo info = SettingsContext.getInstance().getBookInfos().get(clickedBook);

        findViewById(R.id.coverImage).setBackgroundColor(Color.LTGRAY);
        ((TextView) findViewById(R.id.title)).setText(info.getTitle());
        ((TextView) findViewById(R.id.description)).setText(info.getDescription());
        findViewById(R.id.activateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getApplicationContext(),clickedBook+"",Toast.LENGTH_LONG).show();
                SettingsContext settingsContext = SettingsContext.getInstance();
                settingsContext.setActiveBook(settingsContext.getBookInfos().get(clickedBook));
            }
        });
        findViewById(R.id.buyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (connection != null) {
                        Bundle buyIntentBundle = connection.getBuyIntent(3, getPackageName(),
                                info.getProductIdentifier(), "inapp", "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
                        PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
                        startIntentSenderForResult(pendingIntent.getIntentSender(),
                                1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                                Integer.valueOf(0));
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        connection = IInAppBillingService.Stub.asInterface(iBinder);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        connection = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (connection != null) {
            unbindService(this);
        }
    }
}
