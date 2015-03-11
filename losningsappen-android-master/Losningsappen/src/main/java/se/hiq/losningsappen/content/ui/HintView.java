package se.hiq.losningsappen.content.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import se.hiq.losningsappen.R;
import se.hiq.losningsappen.common.models.settings.SettingsContext;

/**
 * Created by petterstenberg on 2014-09-04.
 * <p/>
 * This widget shows a hint if user push it.
 */

public class HintView extends FrameLayout implements View.OnClickListener {

    private int defaultHeight;
    private float minHeight;
    private TextView overlayBox;
    private WebView infoBox;
    private HintViewListener listener;

    public HintView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.hint_view, this, true);

        overlayBox = (TextView) findViewById(R.id.overlay_box);
        overlayBox.setOnClickListener(this);
        infoBox = (WebView) findViewById(R.id.info_box);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.HintView, 0, 0);
        String hintViewLabel = attributes.getString(R.styleable.HintView_hintview_label);
        overlayBox.setText(hintViewLabel);
        attributes.recycle();
    }

    @Override
    public void onClick(View view) {
        overlayBox.setVisibility(GONE);
        listener.onHintViewClicked(this);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        setLayoutParams(params);
        invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        defaultHeight = getLayoutParams().height;
    }

    public void setHintViewListener(HintViewListener listener) {
        this.listener = listener;
    }

    public void setHintContentText(String contentText) {
        infoBox.loadDataWithBaseURL("file:///android_asset/" + SettingsContext.getInstance().getActiveBook(), contentText, "text/html", "UTF-8", null);
    }

    public void resetState() {
        overlayBox.setVisibility(VISIBLE);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
        params.height = defaultHeight;
        setLayoutParams(params);
        invalidate();
    }

    public interface HintViewListener {
        void onHintViewClicked(HintView view);
    }
}
