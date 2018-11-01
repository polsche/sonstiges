package de.ba.bub.studisu.ort.model;

import org.junit.Assert;
import org.junit.Test;

import de.ba.bub.studisu.ort.model.OrtsucheValidator;

/**
 * Created by loutzenhj on 04.04.2017.
 */
public class OrtsucheValidatorTest {
    @Test
    public void testValidOrte(){
        String []orte ={"München","Neumarkt i.d.O","Frankfurt (Oder)","Ünterßaßendöf-Oberdoof","Trècon","Au"};
        for(String ort : orte) {
            OrtsucheValidator validator = new OrtsucheValidator(ort);
            Assert.assertEquals(OrtsucheValidator.VALID_NAME,validator.getResult());
        }
    }

    @Test
    public void testValidPlz(){
        String []plzte ={"90409","12345"};
        for(String plz : plzte) {
            OrtsucheValidator validator = new OrtsucheValidator(plz);
            Assert.assertEquals(OrtsucheValidator.VALID_PLZ,validator.getResult());
        }
    }

    @Test
    public void testInvalidOrte(){
        String []orte ={"München5","Neumarkt?=i.d.O","F"};
        for(String ort : orte) {
            OrtsucheValidator validator = new OrtsucheValidator(ort);
            Assert.assertEquals(OrtsucheValidator.INVALID,validator.getResult());
        }
    }

    @Test
    public void testInvalidPlz(){
        String []plzte ={"90-409","1245"};
        for(String plz : plzte) {
            OrtsucheValidator validator = new OrtsucheValidator(plz);
            Assert.assertEquals(OrtsucheValidator.INVALID,validator.getResult());
        }
    }

    @Test
    public void testOrtZuLang(){
        String ort = "BeratzHamburgBerlinMingaBieledoof - oberdoof BeratzHamburgBerlinMingaBieledoof - oberdoofBeratzHamburgBerlinMingaBieledoof - oberdoofBeratzHamburgBerlinMingaBieledoof " +
                "- oberdoofBeratzHamburgBerlinMingaBieledoof - oberdoof - oberdoofBeratzHamburgBerlinMingaBieledoof - oberdoof";
        OrtsucheValidator validator = new OrtsucheValidator(ort);
        Assert.assertEquals(OrtsucheValidator.INVALID_ORT_ZU_LANG,validator.getResult());
    }

    @Test
    public void testOrtFastZuLang(){
        //250 chars lang - noch ok
        String ort = "BeratzHamburgBerlinMingaBieledoofoberdoof BeratzHamburgBerlinMingaBieledoof - oberdoofBeratzHamburgBerlinMingaBieledoof - oberdoofBeratzHamburgBerlinMingaBieledoof oberdoof - oberdoofBeratzHamburgBerlinMingaBieledoof - oberdoof Oberweitendorf-Titting";
        OrtsucheValidator validator = new OrtsucheValidator(ort);
        Assert.assertEquals(OrtsucheValidator.VALID_NAME,validator.getResult());
    }

}
