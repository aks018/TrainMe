package search.music.avita.trainme;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by avita on 11/5/2017.
 */

public class WorkoutAdapter extends BaseAdapter
{
    private ArrayList<Workout> workout;
    public static boolean[] isCheckedArray;
    private static LayoutInflater inflater = null;
    Context context;

    public WorkoutAdapter(Context mainActivity, ArrayList<Workout> workout, boolean[] isChecked) {
        this.workout = workout;
        this.isCheckedArray = isChecked;
        context = mainActivity;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return workout.size();
    }

    @Override
    public Object getItem(int position) {
        return workout.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView activity;
        TextView duration;
        CheckBox performActivity;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.workout_table, parent, false);
        holder.activity = (TextView) rowView.findViewById(R.id.textViewActivity);
        holder.duration = (TextView) rowView.findViewById(R.id.textViewDuration);
        holder.performActivity = (CheckBox) rowView.findViewById(R.id.checkBoxPerformActivity);

        holder.activity.setText(workout.get(position).getActivityName());
        holder.duration.setText(Integer.toString(workout.get(position).getActivityDuration()));
        holder.duration.setTag(Integer.valueOf(position));
        holder.performActivity.setTag(Integer.valueOf(position));

        holder.performActivity.setChecked(isCheckedArray[position]); // set the status as we stored it
        holder.performActivity.setOnCheckedChangeListener(mListener); // set the listener

        /*holder.duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("NewWorkoutTag", "CLICKED!");
                Log.d("NewWorkoutTag" , v.getTag().toString());
            }
        });*/

        return rowView;
    }



    CompoundButton.OnCheckedChangeListener mListener = new CompoundButton.OnCheckedChangeListener() {

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.d("NewWorkoutTag", isCheckedArray.toString());
            isCheckedArray[(Integer)buttonView.getTag()] = isChecked; // get the tag so we know the row and store the status
        }
    };

    public boolean doesActivityExist()
    {
        for(int i=0; i<isCheckedArray.length; i++)
        {
            if(isCheckedArray[i]==true)
            {
                return true;
            }
        }
        return false;
    }
}
