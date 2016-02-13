package ca.qc.bdeb.imobileapp.modele.objectModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Vincent on 12/02/2016.
 */
public class Question implements Serializable {

    private int questionId;
    private int questionnaireId;
    private String question;
    private HashMap<String, Boolean> answerChoices;
    private String[] tabQuestionChoices;
    private boolean[] tabQuestionChoicesResponse;


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

    public String[] getTabQuestionChoices() {
        return tabQuestionChoices;
    }

    public void setTabQuestionChoices(String[] tabQuestionChoices) {
        this.tabQuestionChoices = tabQuestionChoices;
    }

    public boolean[] getTabQuestionChoicesResponse() {
        return tabQuestionChoicesResponse;
    }

    public void setTabQuestionChoicesResponse(boolean[] tabQuestionChoicesResponse) {
        this.tabQuestionChoicesResponse = tabQuestionChoicesResponse;
    }


    public void convertBeforeSend() {
        if (!answerChoices.isEmpty()) {
            tabQuestionChoices = new String[answerChoices.size()];
            tabQuestionChoicesResponse = new boolean[answerChoices.size()];
            int i = 0;
            Iterator it = answerChoices.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                tabQuestionChoices[i] = pair.getKey().toString();
                tabQuestionChoicesResponse[i] = (boolean) pair.getValue();
                ++i;
                it.remove();
            }
            answerChoices.clear();
        }
    }

    public void rebuildAfterSend() {
        if (answerChoices.isEmpty()) {
            for (int i = 0; i < tabQuestionChoices.length; i++) {
                answerChoices.put(tabQuestionChoices[i], tabQuestionChoicesResponse[i]);
            }
            tabQuestionChoices = new String[0];
            tabQuestionChoicesResponse = new boolean[0];
        }
    }

}
