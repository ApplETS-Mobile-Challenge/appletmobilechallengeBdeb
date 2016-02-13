package ca.qc.bdeb.imobileapp.application;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import ca.qc.bdeb.imobileapp.R;
import ca.qc.bdeb.imobileapp.modele.objectModel.Questionnaire;

/**
 * Created by Hippolyte Glaus on 13/02/16.
 */
public class Survey_Adapter extends ArrayAdapter<Questionnaire> {
    private Context context;
    List<Questionnaire> items;

    public Survey_Adapter(Context context, int resource, List<Questionnaire> items) {
        super(context, resource);
        this.context = context;
        this.items = items;
    }

    private class Survey {
        TextView title;
        TextView nbr_question;
        TextView creation_Date;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Survey holder = null;
        Questionnaire rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            holder = new Survey();
            convertView = mInflater.inflate(R.layout.layout_list_survey, null);
            holder.title = (TextView) convertView.findViewById(R.id.survey_List_nom);
            holder.nbr_question = (TextView) convertView.findViewById(R.id.survey_list_nbrQuestion);
            convertView.setTag(holder);
        } else {
            holder = (Survey) convertView.getTag();
        }

        holder.title.setText(rowItem.getQuestionnaireName());
        holder.nbr_question.setText(Integer.toString(rowItem.getQuestionList().size()));

        return convertView;
    }

}
