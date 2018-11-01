package de.ba.bub.studisu.studienfach.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.ba.bub.studisu.studienangebote.model.requestparams.Studienfaecher;

/**
 * Tests für {@link StudienfachSucheValidator}.
 * 
 * @author StraubP
 *
 */
public class StudienfachSucheValidatorTest {

	/**
	 * Testet die Validierung in verschiedenen Varianten.
	 */
	@Test
	public void test() {
		check(null, null, StudienfachSucheValidator.INVALID);
		check("", null, StudienfachSucheValidator.INVALID);
		check(null, new Studienfaecher(null), StudienfachSucheValidator.INVALID);
		check(null, new Studienfaecher(""), StudienfachSucheValidator.INVALID);
		check("", new Studienfaecher(null), StudienfachSucheValidator.INVALID);
		check("", new Studienfaecher(""), StudienfachSucheValidator.INVALID);
		check(" ", new Studienfaecher(""), StudienfachSucheValidator.INVALID);
		check("a", null, StudienfachSucheValidator.INVALID);
		check("aaa", null, StudienfachSucheValidator.VALID_SUCHBEGRIFF);
		check("aaa1", null, StudienfachSucheValidator.INVALID);
		check(null, new Studienfaecher("1"), StudienfachSucheValidator.VALID_IDS);
		check(null, new Studienfaecher("1;2"), StudienfachSucheValidator.VALID_IDS);
		String zulang = "Informatik-Biologe-wirtschafts-Philosoph-lehramt-chemikerInformatik-Biologe-wirtschafts-Philosoph-lehramt-chemikerInformatik-Biologe-wirtschafts-Philosoph-lehramt-chemikerInformatik-Biologe-wirtschafts-Philosoph-lehramt-chemikerInformatik-Biologe-wirt";
		check(zulang, null, StudienfachSucheValidator.INVALID_SUCHBEGRIFF_ZU_LANG);
		String fastzulang= "InformatikBiologe-wirtschafts-Philosoph-lehramt-chemikerInformatik-Biologe-wirtschafts-Philosoph-lehramt-chemikerInformatik-Biologe-wirtschafts-Philosoph-lehramt-chemikerInformatik-Biologe-wirtschafts-Philosoph-lehramt-chemikerInformatik-Biologe-wirt";
		check(fastzulang, null, StudienfachSucheValidator.VALID_SUCHBEGRIFF);
	}

	/**
	 * Methode, die eine Validierung ausführt.
	 * 
	 * @param suchbegriff
	 *            Suchbegriff
	 * @param studienfaecher
	 *            Studienfächer
	 * @param expected
	 *            erwartetes Validierungsergebnis
	 */
	private void check(String suchbegriff, Studienfaecher studienfaecher, int expected) {
		StudienfachSucheValidator val = new StudienfachSucheValidator(suchbegriff, studienfaecher);
		assertEquals(expected, val.getResult());
	}

}
