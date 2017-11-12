package search.music.avita.trainme;

import android.widget.CheckBox;

/**
 * Created by avita on 11/4/2017.
 */

public class Workout {
    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getActivityDuration() {
        return activityDuration;
    }

    public void setActivityDuration(int activityDuration) {
        this.activityDuration = activityDuration;
    }

    public String activityName;
    public int activityDuration;



    public CheckBox checkBox;
    public Workout(String activityName, int activityDuration)

    {
        this.activityDuration=activityDuration;
        this.activityName = activityName;
    }

}
