package se.hiq.losningsappen.content;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

import se.hiq.losningsappen.MainActivity;
import se.hiq.losningsappen.R;
import se.hiq.losningsappen.common.models.settings.SettingsContext;
import se.hiq.losningsappen.content.model.ContentContext;
import se.hiq.losningsappen.common.models.BackStackCapability;

/**
 * Created by petterstenberg on 2014-08-18.
 */

public class BookContainerFragment extends Fragment implements BackStackCapability, ContentContext.ContentListener {

    public static final String TAG = "BOOK_CONTAINER_FRAGMENT";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ContentContext.getInstance().addListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_container, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        initFirstFragment();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ContentContext.getInstance().removeListener(this);
    }

    public void initFirstFragment() {
        addFragment(BookFragment.instantiate(getActivity(), BookFragment.class.getName()));
    }

    public void addFragment(Fragment fragment) {
        try {
            getChildFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        if (getChildFragmentManager().getBackStackEntryCount() == 2)
            getActivity().setTitle(SettingsContext.getInstance().getActiveBook().getTitle());
        getChildFragmentManager().popBackStack();
        if (getChildFragmentManager().getBackStackEntryCount() == 1 && getActivity() != null)
            ((MainActivity) getActivity()).onSuperBackPressed();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onContentStateChanged(ContentContext.ContentState contentState) {
        switch (contentState) {
            case ROOT:
                break;
            case CHAPTER:
                addFragment(ChapterFragment.instantiate(getActivity(), ChapterFragment.class.getName()));
                break;
            case SUBCHAPTER:
                addFragment(SubChapterFragment.instantiate(getActivity(), SubChapterFragment.class.getName()));
                break;
            case TASK:
                addFragment(TaskFragment.instantiate(getActivity(), TaskFragment.class.getName()));
                break;
            case SUBTASK:
                addFragment(SubtaskFragment.instantiate(getActivity(), SubtaskFragment.class.getName()));
                break;
        }
    }
}
