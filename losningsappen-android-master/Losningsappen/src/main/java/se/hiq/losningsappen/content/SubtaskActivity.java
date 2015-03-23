package se.hiq.losningsappen.content;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import se.hiq.losningsappen.R;
import se.hiq.losningsappen.common.models.NokActivity;
import se.hiq.losningsappen.content.model.BookContext;
import se.hiq.losningsappen.content.model.Chapter;
import se.hiq.losningsappen.content.model.Subtask;
import se.hiq.losningsappen.content.model.Task;
import se.hiq.losningsappen.content.ui.HintView;
import se.hiq.losningsappen.history.HistoryContext;
import se.hiq.losningsappen.history.Historyable;

/**
 * Created by Naknut on 26/06/14.
 */
public class SubtaskActivity extends NokActivity implements HintView.HintViewListener {

    public final static String LABEL_HTML_BASE = "<h2 style=\"font-weight: normal\">?</h2>";
    private Subtask subtask;
    private HintView answerView;
    private HintView hintView;
    private HintView sollutionView;
    private int shownSollutions = 1;
    private List<String> subTaskSollutions;
    private String sollutionsConcat;
    private String subtaskName;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subtask_layout);

        final String[] taskDescription = new String[1];
        if (getIntent().getExtras() != null) {
            final int chapterPosition = getIntent().getExtras().getInt("chapterPosition");
            final int subChapterPosition = getIntent().getExtras().getInt("subChapterPosition");
            final int taskPosition = getIntent().getExtras().getInt("taskPosition");
            final int subtaskPosition = getIntent().getExtras().getInt("subtaskPosition");
            //subtaskName=getIntent().getExtras().get("subtaskName").toString();
            BookContext.getInstance().getChapters(new BookContext.GetChaptersCallback() {
                @Override
                public void onGetChapters(List<Chapter> chapters) {
                    Task task = chapters.get(chapterPosition).getSubChapter(subChapterPosition).getTask(taskPosition);
                    subtask = task.getSubtask(subtaskPosition);
                    subtask.setParentName(task.getName());
                    subtaskName=subtask.getName();
                    //Toast.makeText(getApplicationContext(),subtaskName.toString(),Toast.LENGTH_LONG).show();
                    taskDescription[0] = task.getName();
                }
            });
        } else {
            subtask = (Subtask) HistoryContext.getInstance().getCurrentHistoryable();
            taskDescription[0] = subtask.getName();
        }
        setTitle(getString(R.string.task_name_prefix) + " " + taskDescription[0]+subtaskName);

//        ArrayList<String> groups = new ArrayList<String>();
//        ArrayList<String> children = new ArrayList<String>();

        List<String> subTaskAnswers = subtask.getAnswers();
        List<String> subTaskHints = subtask.getHints();
        subTaskSollutions = subtask.getSolutions();

        answerView = (HintView) findViewById(R.id.answer_view);
        answerView.setHintViewListener(this);
        hintView = (HintView) findViewById(R.id.hint_view);
        hintView.setHintViewListener(this);
        sollutionView = (HintView) findViewById(R.id.sollution_view);
        sollutionView.setHintViewListener(this);
        sollutionView.setVerticalScrollBarEnabled(true);

        if (subTaskAnswers.size() > 0) {
            answerView.setVisibility(View.VISIBLE);
            String answers = LABEL_HTML_BASE.replace("?", getString(R.string.answer_label));
            for (String answerString : subTaskAnswers) {
                answers += answerString;
            }
            answerView.setHintContentText(webformat(answers));
        }
        if (subTaskHints.size() > 0) {
            hintView.setVisibility(View.VISIBLE);
            String hints = LABEL_HTML_BASE.replace("?", getString(R.string.hint_label));
            for (String hintString : subTaskHints) {
                hints += hintString;
            }
            hintView.setHintContentText(webformat(hints));
        }
        if (subTaskSollutions.size() > 0) {
            sollutionView.setVisibility(View.VISIBLE);
            if (subTaskSollutions.size() > 1) {
                final Button moreButton = (Button) sollutionView.findViewById(R.id.more_button);
                moreButton.setVisibility(View.VISIBLE);
                moreButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (shownSollutions < subTaskSollutions.size()) showMoreSollution();
                        if (shownSollutions == subTaskSollutions.size())
                            moreButton.setVisibility(View.GONE);
                        sollutionView.findViewById(R.id.overlay_box).invalidate();
                    }
                });
            }
            sollutionsConcat = LABEL_HTML_BASE.replace("?", getString(R.string.sollution_label));

            sollutionsConcat += subTaskSollutions.get(0);
            sollutionView.setHintContentText(webformat(sollutionsConcat));
        }

        findViewById(R.id.statusButtonsView).findViewById(R.id.notDoneButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSubtaskStatus(Subtask.Status.UNPASSED);
                finish();
            }
        });
        findViewById(R.id.statusButtonsView).findViewById(R.id.doneButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSubtaskStatus(Subtask.Status.PASSED);
                finish();
            }
        });
    }

    private String webformat(String hints) {

        return "<html>" +
                "<head>" +
                "<script type=\"text/javascript\" src=\"http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML\"></script>"+
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">" +
                "</head>" +
                "<body>" + hints + "</body>" +
                "</html>";
    }

    private void showMoreSollution() {
        sollutionsConcat += "<br />" + subTaskSollutions.get(shownSollutions);
        sollutionView.setHintContentText(webformat(sollutionsConcat));
        shownSollutions++;
    }

    private void setSubtaskStatus(Subtask.Status status) {
        boolean containsItem = false;
        for (Historyable item : HistoryContext.getInstance().getHistoryList()) {
            if (item.getClass().equals(Subtask.class) && item.equals(subtask.getName())) {
                containsItem = true;
                ((Subtask) item).setStatus(status);
                HistoryContext.getInstance().notifyDataSetChanged();
            }
        }
        if (!containsItem) {
            subtask.setStatus(status);
            HistoryContext.getInstance().addHistoryItemToList(subtask);
        }
    }

    @Override
    public void onHintViewClicked(HintView view) {
        if (!answerView.equals(view)) answerView.resetState();
        if (!hintView.equals(view)) hintView.resetState();
        if (!sollutionView.equals(view)) sollutionView.resetState();
    }
}