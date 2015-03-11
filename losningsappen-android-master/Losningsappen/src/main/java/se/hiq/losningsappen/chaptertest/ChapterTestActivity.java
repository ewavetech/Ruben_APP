package se.hiq.losningsappen.chaptertest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import se.hiq.losningsappen.R;
import se.hiq.losningsappen.chaptertest.models.ChapterTest;
import se.hiq.losningsappen.chaptertest.models.ChapterTestContext;
import se.hiq.losningsappen.chaptertest.models.ChapterTestResult;
import se.hiq.losningsappen.chaptertest.models.Question;
import se.hiq.losningsappen.common.models.NokActivity;
import se.hiq.losningsappen.common.models.settings.SettingsContext;
import se.hiq.losningsappen.common.util.Utils;
import se.hiq.losningsappen.history.HistoryContext;
import se.hiq.losningsappen.history.Historyable;

/**
 * Created by Naknut on 05/08/14.
 */
public class ChapterTestActivity extends NokActivity implements View.OnClickListener {

    private ChapterTestArrayAdapter adapter;
    private String imagePath;
    private int testPosition;
    private TextView testResultTextView;
    private View testResultBar;
    private ListView questionList;
    private ChapterTest chapterTest;
    private boolean shouldCorrectAnswers;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.chapter_test);

        if (getIntent().getExtras() != null) {
            testPosition = getIntent().getExtras().getInt("testPosition");
            chapterTest = ChapterTestContext.getInstance().getChapterTests().get(testPosition);
            if (getIntent().getExtras().getBoolean(ChapterTestFragment.SHOULD_CLEAR_KEY) == true)
                chapterTest.resetAnswers();
        } else {
            ChapterTestResult chapterTestResult = (ChapterTestResult) HistoryContext.getInstance().getCurrentHistoryable();
            chapterTest = chapterTestResult.getTest();
            shouldCorrectAnswers = true;
        }
        List<Question> questions = chapterTest.getQuestions();

        adapter = new ChapterTestArrayAdapter(this, questions);

        imagePath = SettingsContext.getInstance().getActiveBook().getBookPath();
        boolean shouldValidateNumbers = (getIntent().getExtras() == null) ? true : false;
        adapter.setShouldValidateAnswers(shouldValidateNumbers);
        questionList = (ListView) findViewById(R.id.questionsListView);

        testResultBar = findViewById(R.id.test_result_bar);
        testResultTextView = (TextView) findViewById(R.id.test_result_text);
        ImageButton retryButton = (ImageButton) findViewById(R.id.retry_button);
        retryButton.setOnClickListener(this);

        initCorrectButton();

        questionList.setAdapter(adapter);
        if (shouldCorrectAnswers) correctTest(questions);


        setTitle(chapterTest.getTitle());
    }

    @Override
    protected void onResume() {
        super.onResume();

        ChapterTestArrayAdapter.configImageLoader(getApplicationContext());
    }

    private void initCorrectButton() {
        Button correctButton = (Button) findViewById(R.id.correctButton);
        correctButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correctAndStoreTestResult();
                adapter.setShouldValidateAnswers(true);
            }
        });
    }

    private void correctAndStoreTestResult() {
        double rightPercentage = correctTest(chapterTest.getQuestions());

        ChapterTest chapterTest = ChapterTestContext.getInstance().getChapterTests().get(testPosition);
        boolean containsItem = false;
        for (Historyable item : HistoryContext.getInstance().getHistoryList()) {
            if (item.equals(chapterTest.getTitle())) {
                containsItem = true;
                ((ChapterTestResult) item).setTest(chapterTest);
                ((ChapterTestResult) item).setResultRatio(rightPercentage);
                HistoryContext.getInstance().notifyDataSetChanged();
            }
        }

        if (!containsItem) {
            ChapterTest chapterTestCopy = null;
            try {
                chapterTestCopy = (ChapterTest) Utils.deepCopy(chapterTest);
            } catch (Exception e) {
                e.printStackTrace();
            }
            HistoryContext.getInstance()
                    .addHistoryItemToList(new ChapterTestResult(chapterTestCopy, rightPercentage));
        }
    }

    private double correctTest(List<Question> questions) {
        int score = 0;
        for (Question question : questions) {
            if (question.getTypeOfQuestion().isCorrectAnswer(question)) score++;
        }
        String testResultText = getString(R.string.test_result_base).replace("[#rightAnswers]",
                String.valueOf(score)).replace("[#totalAnswers]", String.valueOf(questions.size()));
        testResultTextView.setText(testResultText);
        testResultBar.setVisibility(View.VISIBLE);

        return (double) score / (double) chapterTest.getQuestions().size();
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public void onClick(View view) {
        testResultBar.setVisibility(View.GONE);
        adapter.setShouldValidateAnswers(false);
        chapterTest.resetAnswers();
        questionList.setAdapter(adapter);
    }

}
