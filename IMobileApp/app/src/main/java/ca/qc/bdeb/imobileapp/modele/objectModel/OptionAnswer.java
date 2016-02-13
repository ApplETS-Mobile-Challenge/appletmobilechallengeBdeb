package ca.qc.bdeb.imobileapp.modele.objectModel;

import java.io.Serializable;

/**
 * Created by Hippolyte Glaus on 13/02/16.
 */
public class OptionAnswer implements Serializable  {
    private String answer;
    private Boolean aBoolean;

    public OptionAnswer(String answer, Boolean aBoolean) {
        this.answer = answer;
        this.aBoolean = aBoolean;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean getaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(Boolean aBoolean) {
        this.aBoolean = aBoolean;
    }
}
