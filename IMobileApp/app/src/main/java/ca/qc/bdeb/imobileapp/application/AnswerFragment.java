package ca.qc.bdeb.imobileapp.application;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import ca.qc.bdeb.imobileapp.R;
import ca.qc.bdeb.imobileapp.modele.objectModel.Question;
import ca.qc.bdeb.imobileapp.modele.objectModel.Questionnaire;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AnswerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AnswerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnswerFragment extends Fragment {
    private static final String QUESTION = "question";
    private static final String QUESTIONNAIRE_NAME = "questionnaire_name";
    private static final String QUESTION_NUMBER = "questionNumber";

    private Question question;
    private String questionnaireName;
    private OnFragmentInteractionListener mListener;

    private FancyButton fancyButton;
    private ListView listView;
    private TextView textView;
    private String choosenAnswer = null;

    private ArrayAdapterAnswerQuestion adpater;
    public AnswerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AnswerFragment newInstance(Question question, String questionnaireName) {
        AnswerFragment fragment = new AnswerFragment();
        Bundle args = new Bundle();
        args.putSerializable(QUESTION, question);
        args.putSerializable(QUESTIONNAIRE_NAME, questionnaireName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            questionnaireName = getArguments().getString(QUESTIONNAIRE_NAME);
            question = (Question) getArguments().getSerializable(QUESTION);
        }
        if(question != null) {
            adpater = new ArrayAdapterAnswerQuestion(this, getActivity().getApplicationContext(), R.layout.layout_list_answer_question, getAnswerList());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        if(questionnaireName != null) {
            rootView = inflater.inflate(R.layout.fragment_answer_start_up, container, false);
            fancyButton = (FancyButton) rootView.findViewById(R.id.answer_activity_button_start_questionnaire);
            fancyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onFragmentInteraction();
                }
            });
            textView = (TextView) rootView.findViewById(R.id.answer_activity_questionnaire_name_txt_view);
            textView.setText(questionnaireName);
        }
        else {
            rootView = inflater.inflate(R.layout.fragment_answer, container, false);
            fancyButton = (FancyButton) rootView.findViewById(R.id.answer_activity_button_next_questionnaire);
            fancyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(choosenAnswer != null) {
                        if(question.getAnswerChoices().get(choosenAnswer)) {
                            final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle("Create a questionnaire");
                            alert.setMessage("Enter a title");
                            // Create TextView
                            final TextView input = new TextView (getActivity());
                            input.setText("Nice job !!");
                            alert.setView(input);
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    mListener.onFragmentInteraction();
                                }
                            });
                            alert.show();
                        }
                        else {
                            String goodAnswer = null;
                            for(Map.Entry<String, Boolean> ent : question.getAnswerChoices().entrySet()) {
                                if(ent.getValue()) {
                                    goodAnswer = ent.getKey();
                                    break;
                                }
                            }
                            final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle("Create a questionnaire");
                            alert.setMessage("Enter a title");
                            // Create TextView
                            final TextView input = new TextView (getActivity());
                            input.setText("Wrong answer !! The good one was " + goodAnswer);
                            alert.setView(input);
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    mListener.onFragmentInteraction();
                                }
                            });
                            alert.show();
                        }
                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Yoo need to choose an answer !", Toast.LENGTH_LONG).show();
                    }
                }
            });
            listView = (ListView) rootView.findViewById(R.id.list_view_answer_question);
            listView.setAdapter(adpater);
            textView = (TextView) rootView.findViewById(R.id.answer_activity_question_txt_view);
            textView.setText(question.getQuestion());
        }
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(boolean goodAnswer) {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();

    }

    private ArrayList<String> getAnswerList() {
        ArrayList<String> answerList = new ArrayList<>();
        for(Map.Entry<String, Boolean> entry : question.getAnswerChoices().entrySet()) {
            answerList.add(entry.getKey());
        }
        return answerList;
    }

    public void checkBoxClicked(String  key) {
        choosenAnswer = key;
    }
}
