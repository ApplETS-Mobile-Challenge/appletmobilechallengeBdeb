package ca.qc.bdeb.imobileapp.application;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import ca.qc.bdeb.imobileapp.R;
import ca.qc.bdeb.imobileapp.modele.objectModel.Question;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Olivier on 2016-02-12.
 */
public class Survey_Question_Adapter extends ArrayAdapter<Question> {
    Context context;

    private List<Question> list;

    public Survey_Question_Adapter(Context context, int resourceId, List<Question> items) {
        super(context, resourceId, items);
        this.list = items;
        this.context = context;
    }

    private class QuestionHolder {
        TextView title;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuestionHolder holder = null;
        Question rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            holder = new QuestionHolder();
            convertView = mInflater.inflate(R.layout.layout_list_question, null);
            holder.title = (TextView) convertView.findViewById(R.id.question_list_layout);
            convertView.setTag(holder);
        } else {
            holder = (QuestionHolder) convertView.getTag();
        }

        holder.title.setText(rowItem.getQuestion());

        return convertView;
    }

    public void add(Question object){
        list.add(object);
        this.notifyDataSetChanged();
    }
}
