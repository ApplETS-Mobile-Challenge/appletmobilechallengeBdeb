package ca.qc.bdeb.imobileapp.modele.objectModel;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Vincent on 12/02/2016.
 */
@XStreamAlias("QUESTION")
public class Question implements Serializable {

    @XStreamAlias("QUESTION_ID")
    private int questionId;

    @XStreamAlias("QUESTION_QUESTIONNAIRE_ID")
    private int questionnaireId;

    @XStreamAlias("QUESTION")
    private String question;

    @XStreamImplicit(itemFieldName = "ban")
    private HashMap<String, Boolean> answerChoices;

    public Question(int questionId, String question, int questionnaireId) {
        this.questionId = questionId;
        this.questionnaireId = questionnaireId;
        this.question = question;
        answerChoices = new HashMap<>();
    }

    public Question() {

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

    public void setAnswerChoices(HashMap<String, Boolean> answerChoices) {
        this.answerChoices = answerChoices;
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
