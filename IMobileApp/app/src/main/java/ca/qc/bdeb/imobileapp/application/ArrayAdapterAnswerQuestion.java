package ca.qc.bdeb.imobileapp.application;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import ca.qc.bdeb.imobileapp.R;

/**
 * Created by Vincent on 13/02/2016.
 */

public class ArrayAdapterAnswerQuestion extends ArrayAdapter<String> {

    private Context context;
    public ArrayAdapterAnswerQuestion(Context context, int resource, ArrayList<String> items) {
        super(context, resource, items);
        this.context = context;
    }

    private class Holder {
        CheckBox checkBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Holder holder = null;

        String answerChoice = getItem(position);

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_list_answer_question, null);
            holder = new Holder();
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox_answer_question);
            convertView.setTag(holder);
        }
        else {
            holder = (Holder) convertView.getTag();
            holder.checkBox.setText("");
        }
        holder.checkBox.setText(answerChoice);
        return convertView;
    }
}
