package ca.qc.bdeb.imobileapp.application;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ca.qc.bdeb.imobileapp.modele.objectModel.Question;

/**
 * Created by Olivier on 2016-02-12.
 */
public class Survey_List_Adapter extends ArrayAdapter<Question> {
    Context context;

    private List<Question> list;

    public Survey_List_Adapter(Context context, int resourceId, List<Question> items) {
        super(context, resourceId, items);
        this.list = items;
        this.context = context;
    }

    private class NoteHolder {
        TextView title;
        TextView body;
        TextView creation_Date;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NoteHolder holder = null;
        Question rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            holder = new NoteHolder();
           // convertView = mInflater.inflate(R.layout.list_note, null);

            convertView.setTag(holder);
        } else {
            holder = (NoteHolder) convertView.getTag();
        }

        return convertView;
    }

    public void update(int objectPosition, Question question){
        list.set(objectPosition, question);
        this.notifyDataSetChanged();
    }

    public void remove(int objectPosition)
    {
        list.remove(objectPosition);
        this.notifyDataSetChanged();
    }

    public void add(Question object){
        list.add(object);
        this.notifyDataSetChanged();
    }
}
