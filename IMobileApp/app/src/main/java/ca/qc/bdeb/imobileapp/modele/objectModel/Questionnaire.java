package ca.qc.bdeb.imobileapp.modele.objectModel;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Vincent on 12/02/2016.
 */
public class Questionnaire {

    private int questionnaireId;
    private String questionnaireName;
    private Date creationDate;
    private Date editDate;
    private ArrayList<Question> questionList;

    public Questionnaire(int questionnaireId, String questionnaireName, Date creationDate, Date editDate, ArrayList<Question> questionList) {
        this.questionnaireId = questionnaireId;
        this.questionnaireName = questionnaireName;
        this.creationDate = creationDate;
        this.editDate = editDate;
        this.questionList = questionList;
    }

    public int getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(int questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public String getQuestionnaireName() {
        return questionnaireName;
    }

    public void setQuestionnaireName(String questionnaireName) {
        this.questionnaireName = questionnaireName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public ArrayList<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(ArrayList<Question> questionList) {
        this.questionList = questionList;
    }
}
