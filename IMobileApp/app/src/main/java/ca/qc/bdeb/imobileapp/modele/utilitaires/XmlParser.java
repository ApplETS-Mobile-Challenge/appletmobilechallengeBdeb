package ca.qc.bdeb.imobileapp.modele.utilitaires;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import ca.qc.bdeb.imobileapp.modele.objectModel.OptionAnswer;
import ca.qc.bdeb.imobileapp.modele.objectModel.Question;
import ca.qc.bdeb.imobileapp.modele.objectModel.Questionnaire;
import ca.qc.bdeb.imobileapp.modele.persistence.DbHelper;

/**
 * Created by Hippolyte Glaus on 12/02/16.
 */
public class XmlParser {

    public static String parseToXml(Questionnaire questionnaire) {
        XStream xstream = new XStream();
        xstream.processAnnotations(Questionnaire.class);
        xstream.processAnnotations(Question.class);
        xstream.processAnnotations(OptionAnswer.class);

        return xstream.toXML(questionnaire);
    }

    public static Questionnaire readFromXml(String data) {
        Questionnaire questionnaire = null;
        try {
            File file = new File(data);
            FileReader fileReader = new FileReader(file);

            XStream xstream = new XStream();
            xstream.processAnnotations(Questionnaire.class);
            xstream.processAnnotations(Question.class);
            xstream.processAnnotations(OptionAnswer.class);

            questionnaire = (Questionnaire) xstream.fromXML(fileReader);
        } catch (FileNotFoundException e) {
        }

        return questionnaire;
    }

}
