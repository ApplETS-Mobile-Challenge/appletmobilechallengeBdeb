package ca.qc.bdeb.imobileapp.application;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ca.qc.bdeb.imobileapp.R;
import ca.qc.bdeb.imobileapp.modele.objectModel.OptionAnswer;
import ca.qc.bdeb.imobileapp.modele.objectModel.Question;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Hippolyte Glaus on 13/02/16.
 */
public class Option_Answer_Adapter extends ArrayAdapter<OptionAnswer> {
    private Context context;
    private List<OptionAnswer> optionAnswerList;
    private Activity activity;

    public Option_Answer_Adapter(Context context, int resource, List<OptionAnswer> list, Activity activite) {
        super(context, resource, list);
        this.context = context;
        this.optionAnswerList = list;
        this.activity = activite;
    }

    private class answerOption {
        TextView optionAnswer;
        CheckBox isAnswer;
        FancyButton deleteButon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        answerOption holder = null;

       OptionAnswer rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            holder = new answerOption();
            convertView = mInflater.inflate(R.layout.layout_list_answer, null);
            holder.optionAnswer = (TextView) convertView.findViewById(R.id.answer_list_answer_text);
            holder.isAnswer = (CheckBox) convertView.findViewById(R.id.answer_list_checkbox);
            holder.deleteButon = (FancyButton) convertView.findViewById(R.id.home_page_btn_creer);
            convertView.setTag(holder);

            holder.deleteButon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FancyButton fb = (FancyButton) v;
                    ((Add_Modify_Question) activity).deleteOptionAnswer((Integer) fb.getTag());
                }
            });

            holder.isAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((Add_Modify_Question) activity).caseChecked){
//                        holder.isAnswer
                    }else {
                        ((Add_Modify_Question) activity).checkCaseTrue();
                    }
                }
            });

        } else {
            holder = (answerOption) convertView.getTag();
        }

        holder.optionAnswer.setText(rowItem.getAnswer());
        holder.isAnswer.setChecked(rowItem.getaBoolean());
        holder.deleteButon.setTag(position);
        holder.isAnswer.setTag(position);


        return convertView;
    }

    public void add(OptionAnswer object){
        optionAnswerList.add(object);
        this.notifyDataSetChanged();
    }

}
