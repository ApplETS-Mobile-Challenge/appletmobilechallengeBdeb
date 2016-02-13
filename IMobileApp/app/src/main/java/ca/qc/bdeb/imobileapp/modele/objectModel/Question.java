package ca.qc.bdeb.imobileapp.modele.objectModel;

import java.util.HashMap;

/**
 * Created by Vincent on 12/02/2016.
 */
public class Question {

    private int questionId;
    private int questionnaireId;
    private String question;
    private HashMap<String, Boolean> answerChoices;

    public Question(int questionId, String question, int questionnaireId) {
        this.questionId = questionId;
        this.questionnaireId = questionnaireId;
        this.question = question;
        answerChoices = new HashMap<>();
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

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return this.question;
    }
}
