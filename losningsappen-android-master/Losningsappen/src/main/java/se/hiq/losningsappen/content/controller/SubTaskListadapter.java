package se.hiq.losningsappen.content.controller;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;

import java.util.List;

import se.hiq.losningsappen.R;
import se.hiq.losningsappen.common.models.settings.SettingsContext;

/**
 * Created by petterstenberg on 2014-08-18.
 */
public class SubTaskListadapter extends BaseExpandableListAdapter {

    private Activity activity;
    private List<String> groups;
    private List<String> children;
    private LayoutInflater inflater;

    public SubTaskListadapter(Activity activity, List<String> groups, List<String> children) {
        this.activity = activity;
        this.groups = groups;
        this.children = children;
        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return groups.get(i);
    }

    @Override
    public Object getChild(int i, int i2) {
        return children.get(i);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i2) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_group, viewGroup, false);
        }
        String group = (String) getGroup(i);
        ((CheckedTextView) convertView).setText(group);
        ((CheckedTextView) convertView).setChecked(b);
        return convertView;
    }

    @Override
    public View getChildView(int i, int i2, boolean b, View convertView, ViewGroup viewGroup) {
        WebView webView = new WebView(activity.getApplicationContext());
        String child = "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\"></head><body>" + getChild(i, i2) + "</body></html>";
        webView.loadDataWithBaseURL("file:///android_asset/" + SettingsContext.getInstance().getActiveBook(), child, "text/html", "UTF-8", null);
        return webView;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return false;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }
}
