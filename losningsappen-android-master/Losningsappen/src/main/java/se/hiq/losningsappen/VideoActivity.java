package se.hiq.losningsappen;

import android.app.Activity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by Naknut on 24/06/14.
 */
public class VideoActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Bundle params = getIntent().getExtras();
        String videoLink = params.getString("videoLink");
        VideoView vimeoView = (VideoView)this.findViewById(R.id.vimeoView);
        MediaController controller = new MediaController(this);
        controller.setAnchorView(vimeoView);
        controller.setMediaPlayer(vimeoView);
        vimeoView.setMediaController(controller);
        vimeoView.setVideoPath(videoLink);
        vimeoView.start();
    }
}
