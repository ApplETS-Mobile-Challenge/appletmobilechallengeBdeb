package ca.qc.bdeb.imobileapp.modele.utilitaires;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import ca.qc.bdeb.imobileapp.modele.objectModel.Questionnaire;
import ca.qc.bdeb.imobileapp.modele.persistence.DbHelper;

/**
 * Created by Hippolyte Glaus on 12/02/16.
 */
public class XmlParser {

    public static String parseToXml(Questionnaire questionnaire) {
        questionnaire.convertBeforeSend();
        XStream xstream = new XStream(new DomDriver());
        return xstream.toXML(questionnaire);
    }

    public static Questionnaire readFromXml(String data) {
        XStream xstream = new XStream(new DomDriver());
        Questionnaire questionnaire = (Questionnaire) xstream.fromXML(data);
        questionnaire.rebuildAfterSend();
        return questionnaire;
    }

}
