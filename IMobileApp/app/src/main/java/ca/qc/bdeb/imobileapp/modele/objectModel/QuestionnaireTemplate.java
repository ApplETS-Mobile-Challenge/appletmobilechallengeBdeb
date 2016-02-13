package ca.qc.bdeb.imobileapp.modele.objectModel;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Vincent on 13/02/2016.
 */
public class QuestionnaireTemplate implements Serializable {

    public String questionnaireName;
    public Date editDate;
    public int numberOfAnwer;
    public int questionnaireId;

}
