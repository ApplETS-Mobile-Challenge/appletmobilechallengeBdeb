package ca.qc.bdeb.imobileapp.modele.objectModel;

import java.util.HashMap;

/**
 * Created by Vincent on 12/02/2016.
 */
public class Question {

    private int questionId;
    private int questionnaireId;
    private HashMap<String, Boolean> answerChoices;

    public Question(int questionId, int questionnaireId) {
        this.questionId = questionId;
        this.questionnaireId = questionnaireId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public int getQuestionnaireId() {
        return questionnaireId;
    }

    public HashMap<String, Boolean> getAnswerChoices() {
        return answerChoices;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public void setQuestionnaireId(int questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public void addAnswerChoices(String answersChoiceName, boolean isTrue) {
        answerChoices.put(answersChoiceName, isTrue);
    }

    public void deletAnswerChoice(String answerChoiceName) {
        answerChoices.remove(answerChoiceName);
    }
}
