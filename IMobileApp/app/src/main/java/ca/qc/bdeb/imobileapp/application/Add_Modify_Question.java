package ca.qc.bdeb.imobileapp.application;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.qc.bdeb.imobileapp.R;
import ca.qc.bdeb.imobileapp.modele.objectModel.OptionAnswer;
import ca.qc.bdeb.imobileapp.modele.objectModel.Question;


public class Add_Modify_Question extends AppCompatActivity implements PopupResult{

    private EditText editText;
    private mehdi.sakout.fancybuttons.FancyButton fancyButton;
    private String reponsePopup;

    private List<OptionAnswer> listHashmapAnswers; // liste de reponses
    private ListView listViewAnswers;
    private Option_Answer_Adapter answer_adapter;
    private boolean caseChecked;
    private int positionCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__modify__question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        editText = (EditText)findViewById(R.id.Add_Modify_Question_Question_Text);
        fancyButton = (mehdi.sakout.fancybuttons.FancyButton)findViewById(R.id.home_page_btn_recevoir);
        caseChecked = false;
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(Add_Modify_Question.this);
                // Create TextView
                final EditText input = new EditText (Add_Modify_Question.this);
                alert.setView(input);
                alert.setTitle("Enter an answer choice");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        OptionAnswer optionAnswer = new OptionAnswer(input.getText().toString(), false);
                        listViewAnswers.setItemChecked(0, true);
                        answer_adapter.add(optionAnswer);
                        answer_adapter.notifyDataSetChanged();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        });

        listHashmapAnswers = new ArrayList<>();

        listViewAnswers = (ListView) findViewById(R.id.Add_Modify_Question_ListView);
        answer_adapter = new Option_Answer_Adapter(Add_Modify_Question.this, R.layout.layout_list_answer, listHashmapAnswers, this);
        listViewAnswers.setAdapter(answer_adapter);

        answer_adapter.notifyDataSetChanged();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        final Question question = new Question();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Question question = new Question(0, editText.getText().toString(), 0);

                for(int i = 0; i < listViewAnswers.getAdapter().getCount(); i++){
                    if(i != positionCheck) {
                        question.addAnswerChoices(answer_adapter.getItem(i).getAnswer()
                                , answer_adapter.getItem(i).getaBoolean());
                    }
                    else {
                        question.addAnswerChoices(answer_adapter.getItem(i).getAnswer()
                                , true);
                    }
                }
                Intent intent = new Intent();
                intent.putExtra("question", question);
                setResult(Create_Survey.RESULT_SUCCES, intent);
                finish();
            }
        });
        listHashmapAnswers = new ArrayList<>();

        listViewAnswers = (ListView) findViewById(R.id.Add_Modify_Question_ListView);
        answer_adapter = new Option_Answer_Adapter(Add_Modify_Question.this, R.layout.layout_list_answer, listHashmapAnswers, this);
        listViewAnswers.setAdapter(answer_adapter);
        answer_adapter.notifyDataSetChanged();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete_vrai, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_done){
            //
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onPopupResult(PopupResultChoices prc, String reponsePopup) {
        if (prc == PopupResultChoices.SUCESS){

            OptionAnswer optionAnswer = new OptionAnswer(reponsePopup, Boolean.FALSE);
            listViewAnswers.setItemChecked(0, true);
            answer_adapter.add(optionAnswer);
            answer_adapter.notifyDataSetChanged();
        }
    }

    public void deleteOptionAnswer(int position){
        listHashmapAnswers.remove(position);
        answer_adapter.notifyDataSetChanged();
    }
    public void checkCaseTrue(boolean isCheck, int position){
        caseChecked = isCheck;
        positionCheck = position;
    }

    public boolean isCheckCase() {
        return caseChecked;
    }
}