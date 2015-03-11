package se.hiq.losningsappen.common.models;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import se.hiq.losningsappen.common.models.settings.SettingsContext;

/**
 * Created by petterstenberg on 2014-09-12.
 */
public abstract class NokActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        String color = SettingsContext.getInstance().getActiveBook().getColor();
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
