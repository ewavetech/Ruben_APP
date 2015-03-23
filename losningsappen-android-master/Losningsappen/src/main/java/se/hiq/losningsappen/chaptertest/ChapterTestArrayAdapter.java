package se.hiq.losningsappen.chaptertest;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import se.hiq.losningsappen.R;
import se.hiq.losningsappen.chaptertest.models.Choice;
import se.hiq.losningsappen.chaptertest.models.Question;
import se.hiq.losningsappen.common.models.settings.SettingsContext;

/**
 * Created by petterstenberg on 2014-08-27.
 * <p/>
 * ArrayAdapter that writes out the information in every chapter test.
 */

public class ChapterTestArrayAdapter extends BaseAdapter {

    public final static String HTML_BASE = "<html>" +
            "<head>" +
            "<script type=\"text/javascript\"\n" +
            "  src=\"https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML\">\n" +
            "</script>"+
            "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">" +
            "</head>" +
            "<body>" +
            "?" +
            "</body>" +
            "</html>";
    private static ImageLoaderConfiguration configuration;
    private Context context;
    private List<Question> list;
    private boolean shouldValidateAnswers;
    private boolean hasCorrectedAnswers;

    public ChapterTestArrayAdapter(Context context, List<Question> list) {
        this.context = context;
        this.list = list;

        if (configuration == null) configImageLoader(context);
    }
//    private boolean shouldResetCheckButtons;

    public static void configImageLoader(Context context) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.loading_text_whole)
                .resetViewBeforeLoading(true)
                .build();
        configuration = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(options)
                .imageDownloader(new SVGLoader(context))
//                    .writeDebugLogs()
                .build();
        ImageLoader.getInstance().destroy();
        ImageLoader.getInstance().init(configuration);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Question getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Question question = list.get(position);

        if (convertView == null) {
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                    question.getTypeOfQuestion().getLayoutResource(), parent, false);
        }

        final ImageView questionImageView = (ImageView) convertView.findViewById(R.id.question_image);
        questionImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

//        String imagePath = ((ChapterTestActivity) context).getImagePath()+ "/" + question.getImgPath();
        String mockImagePath = ((ChapterTestActivity) context).getImagePath() +
                "/chapterTest_img/1bcVux Test kap 1-6 Sant eller falskt HH140525/2test.svg";

        String imagePath = mockImagePath;

        ImageLoader.getInstance().displayImage(imagePath, questionImageView);

        switch (question.getTypeOfQuestion()) {
            case QUESTIONTYPE_TRUEORFALSE:
            case QUESTIONTYPE_1X2:
                mapSingleChoice(convertView, position);
                break;
            case QUESTIONTYPE_MULTIPLE_CHOICE:
                mapMultiChoice(convertView, question);
                break;
        }

        return convertView;
    }

    private void mapSingleChoice(View convertView, int position) {

        Question question = getItem(position);
        WebView questionText = (WebView) convertView.findViewById(R.id.questionText);
        final RadioGroup group = (RadioGroup) convertView.findViewById(R.id.radio_buttons);

        loadHtml(questionText, question);

        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) group.getChildAt(i);
            radioButton.setText(question.getChoices().get(i).getText());
            if (getItem(position).getChoice(i).isChecked()) group.check(radioButton.getId());
        }

        group.setOnCheckedChangeListener(new RadioGroupListener(position));

        if (shouldValidateAnswers) {
            correctQuestion(question, convertView, false);
            setRadioGroupEnabled(group, false);
        }
    }

    private void setRadioGroupEnabled(RadioGroup radioGroup, boolean enabled) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(enabled);
        }
    }

    private void mapMultiChoice(View convertView, final Question question) {

        MultipleChoiceViewHolder viewHolder;

        if (convertView.getTag() == null) {
            viewHolder = new MultipleChoiceViewHolder();
            viewHolder.checkBoxContainer = (LinearLayout) convertView.findViewById(R.id.check_boxes);
            viewHolder.questionText = (WebView) convertView.findViewById(R.id.questionText);
            viewHolder.questionText.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            for (Choice ignored : question.getChoices()) {
                ToggleButton tempCheckbox = new ToggleButton(context);
                tempCheckbox.setBackgroundResource(R.drawable.radiobutton_state_drawable);
                viewHolder.checkBoxContainer.addView(tempCheckbox, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                viewHolder.checkBoxes.add(tempCheckbox);
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MultipleChoiceViewHolder) convertView.getTag();
        }

        loadHtml(viewHolder.questionText, question);

        for (int i = 0; i < question.getChoices().size(); i++) {
            final Choice choice = question.getChoice(i);
            String text = choice.getNumber() + " " + choice.getText();
            viewHolder.checkBoxes.get(i).setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            viewHolder.checkBoxes.get(i).setTextOn(text);
            viewHolder.checkBoxes.get(i).setTextOff(text);
            viewHolder.checkBoxes.get(i).setChecked(choice.isChecked());
            viewHolder.checkBoxes.get(i).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    choice.setChecked(b);
                }
            });
        }

        if (shouldValidateAnswers) {
            correctQuestion(question, convertView, true);
        }
    }

    private void loadHtml(WebView webView, Question question) {
        String htmlText = HTML_BASE.replace("?", question.getText().trim());
        webView.loadUrl("about:blank");
        webView.loadDataWithBaseURL("file:///android_asset/" + SettingsContext.getInstance().getActiveBook(), htmlText, "text/html", "UTF-8", null);
    }

    private void correctQuestion(Question question, View convertView, boolean isMultipleChoice) {

        View correctIndicator = convertView.findViewById(R.id.correct_indicator);
        if (!isMultipleChoice) {
            for (Choice choice : question.getChoices()) {
                if (choice.isChecked() && choice.isCorrectAnswer()) {
                    correctIndicator.setBackgroundResource(R.color.green);
                } else if (choice.isChecked() && !choice.isCorrectAnswer()) {
                    correctIndicator.setBackgroundResource(R.color.red);
                    for (int i = 0; i < question.getChoices().size(); i++) {
                        if (question.getChoice(i).isCorrectAnswer())
                            ((RadioGroup) convertView.findViewById(R.id.radio_buttons)).getChildAt(i).setBackgroundResource(R.color.green);
                    }
                }
            }
        } else {
            boolean isCorrectAnswer = true;

            for (Choice choice : question.getChoices()) {
                if (choice.isChecked() && !choice.isCorrectAnswer()) {
                    isCorrectAnswer = false;
                } else if (!choice.isChecked() && choice.isCorrectAnswer()) {
                    isCorrectAnswer = false;
                }
            }

            if (isCorrectAnswer) {
                correctIndicator.setBackgroundResource(R.color.green);
                ;
            } else {
                correctIndicator.setBackgroundResource(R.color.red);
            }
        }
        hasCorrectedAnswers = true;
    }

    public void setShouldValidateAnswers(boolean shouldValidateAnswers) {
        this.shouldValidateAnswers = shouldValidateAnswers;
        notifyDataSetChanged();
    }

    private enum SingleChoiceMode {TRUE_FALSE, ONE_X_TWO}

    private static class MultipleChoiceViewHolder {
        public List<ToggleButton> checkBoxes = new ArrayList<ToggleButton>();
        public LinearLayout checkBoxContainer;
        public WebView questionText;
        public FrameLayout questionTextContainer;
    }

    private class RadioGroupListener implements RadioGroup.OnCheckedChangeListener {

        private int position;

        private RadioGroupListener(int position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int id) {

            RadioButton checkedRadioButton = (RadioButton) radioGroup.findViewById(id);
            int radioButtonIndex = radioGroup.indexOfChild(checkedRadioButton);
            if (radioButtonIndex != -1)
                getItem(position).getChoices().get(radioButtonIndex).setChecked(true);

            clearOthers(checkedRadioButton, radioGroup);

//            Log.d("CTAA", "Checked: " + position + ": " + getItem(position).getChoice(0).isChecked()
//                    + " " + getItem(position).getChoice(1).isChecked() + " " + getItem(position).getChoice(2).isChecked());
        }

        private void clearOthers(RadioButton radioButton, RadioGroup radioGroup) {
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                RadioButton radioButtonItem = (RadioButton) radioGroup.getChildAt(i);
                if (radioButtonItem != radioButton)
                    getItem(position).getChoice(i).setChecked(false);
            }
        }
    }
}
