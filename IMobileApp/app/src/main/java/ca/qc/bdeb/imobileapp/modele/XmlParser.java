package ca.qc.bdeb.imobileapp.modele;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import ca.qc.bdeb.imobileapp.modele.objectModel.Questionnaire;
import ca.qc.bdeb.imobileapp.modele.persistence.DbHelper;

/**
 * Created by Hippolyte Glaus on 12/02/16.
 */
public class XmlParser
{

    private DbHelper db;
    public XmlParser(DbHelper db){
        this.db = db;
    }

    public void ParseToXml(Questionnaire questionnaire){
        XStream xstream = new XStream(new DomDriver());
        String xml = xstream.toXML(questionnaire);
        ReadFromXml(xml);
    }

    public void ReadFromXml(String data){
        XStream xstream = new XStream(new DomDriver());
        Questionnaire newQuestionnaire = (Questionnaire)xstream.fromXML(data);
        db.insertNewQuestionnaire(newQuestionnaire);
    }


}
