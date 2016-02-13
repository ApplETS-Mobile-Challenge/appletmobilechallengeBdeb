package ca.qc.bdeb.imobileapp.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ca.qc.bdeb.imobileapp.R;
import ca.qc.bdeb.imobileapp.modele.objectModel.Question;

public class Create_Survey extends AppCompatActivity {

    public static final int RESULT_SUCCES = 1;
    private List<Question> questionList;
    private ListView listViewQuestion;
    private Survey_Question_Adapter adapterActivite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__survey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_question);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Create_Survey.this, Add_Modify_Question.class);
                startActivityForResult(intent, 0);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        questionList = new ArrayList<>();
        listViewQuestion = (ListView) findViewById(R.id.Create_Survey_List);
        adapterActivite = new Survey_Question_Adapter(this, R.layout.layout_list_question, questionList);
        listViewQuestion.setAdapter(adapterActivite);
        listViewQuestion.setEmptyView(findViewById(R.id.empty_survey));
        adapterActivite.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_delete, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_delete){
            //
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if (resultCode == RESULT_SUCCES){
            Question question = (Question)data.getSerializableExtra("question");
            adapterActivite.add(question);
            adapterActivite.notifyDataSetChanged();
        }
    }
}
