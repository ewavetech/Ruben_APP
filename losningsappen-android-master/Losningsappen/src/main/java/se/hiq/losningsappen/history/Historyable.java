package se.hiq.losningsappen.history;

/**
 * Created by petterstenberg on 2014-08-26.
 * <p/>
 * This interface describes classes that are to be contained in the history list view
 */

public interface Historyable {
    String name = null;
    float completedStatus = -1;

    public String getName();
}
