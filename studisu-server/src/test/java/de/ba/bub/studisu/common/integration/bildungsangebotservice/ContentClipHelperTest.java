package de.ba.bub.studisu.common.integration.bildungsangebotservice;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Testklasse des Helpers zum Kürzen von HTML-Texten aus dem Bildungsangebotsservice.
 * 
 * @author StraubP
 * @author SchneideK084
 */
public class ContentClipHelperTest {

	private static int TIMES = 1; // für Performance-Tests, z.B. 100000

	@Test
	public void testClipContentGekuerztNachLaenge() {
		ContentClipHelper clipHelper = new ContentClipHelper();
		long start = System.currentTimeMillis();
		String inhalt = "<h4>aaa</h4><p>bbb<br/>ccc<br/></p><ul><li>ddd</li><li>eee</li></ul><ol><li>fff</li><li>ggg</li></ol>";
		for (int i = 0; i < TIMES; i++) {
			assertEquals(inhalt, clipHelper.clipContent(inhalt, 400, 100));
			assertEquals(inhalt, clipHelper.clipContent(inhalt, 21, 100));
			assertEquals(
					"<h4>aaa</h4><p>bbb<br/>ccc<br/></p><ul><li>ddd</li><li>eee</li></ul><ol><li>fff</li><li>gg&hellip;</li></ol>",
					clipHelper.clipContent(inhalt, 20, 100));
			assertEquals(
					"<h4>aaa</h4><p>bbb<br/>ccc<br/></p><ul><li>ddd</li><li>eee</li></ul><ol><li>fff</li><li>&hellip;</li></ol>",
					clipHelper.clipContent(inhalt, 18, 100));
			assertEquals(
					"<h4>aaa</h4><p>bbb<br/>ccc<br/></p><ul><li>ddd</li><li>eee</li></ul><ol><li>ff&hellip;</li></ol>",
					clipHelper.clipContent(inhalt, 17, 100));
			assertEquals(
					"<h4>aaa</h4><p>bbb<br/>ccc<br/></p><ul><li>ddd</li><li>eee</li></ul><ol><li>&hellip;</li></ol>",
					clipHelper.clipContent(inhalt, 15, 100));
			assertEquals("<h4>aaa</h4><p>bbb<br/>ccc<br/></p><ul><li>ddd</li><li>&hellip;</li></ul>",
					clipHelper.clipContent(inhalt, 12, 100));
			assertEquals("<h4>aaa</h4><p>bbb<br/>ccc<br/></p><ul><li>dd&hellip;</li></ul>",
					clipHelper.clipContent(inhalt, 11, 100));
			assertEquals("<h4>aaa</h4><p>bbb<br/>ccc<br/></p><ul><li>&hellip;</li></ul>",
					clipHelper.clipContent(inhalt, 9, 100));
			assertEquals("<h4>aaa</h4><p>bbb<br/>cc&hellip;</p>", clipHelper.clipContent(inhalt, 8, 100));
			assertEquals("<h4>aaa</h4><p>bbb<br/>&hellip;</p>", clipHelper.clipContent(inhalt, 6, 100));
			assertEquals("<h4>aaa</h4><p>bb&hellip;</p>", clipHelper.clipContent(inhalt, 5, 100));
			assertEquals("<h4>aaa</h4><p>&hellip;</p>", clipHelper.clipContent(inhalt, 3, 100));
			assertEquals("<h4>aa&hellip;</h4>", clipHelper.clipContent(inhalt, 2, 100));
		}
		System.out.println("Nach Zeichen: " + (System.currentTimeMillis() - start));
	}

	@Test
	public void testSimpleTextKlaus() {
		ContentClipHelper clipHelper = new ContentClipHelper();
		long start = System.currentTimeMillis();
		String inhalt = "<h4>Schwerpunkte:</h4><p>Grundlagen der Informatik der Systeme (Softwaretechnik, Betriebssysteme, Computernetze, Hardware-Software-Systeme, Dantenbanksysteme), Grundlagen und Vertiefungsgebiet der Computerwissenschaft, Grundlagen und Vertiefungsgebiet der Mathematik, Nebenfach, Schlüsselqualifikationen</p>";
		for (int i = 0; i < TIMES; i++) {
			assertEquals(inhalt, clipHelper.clipContent(inhalt, 400, 2));
			assertEquals("<h4>Schwerpunkte:</h4><p>Grundlagen der In&hellip;</p>", clipHelper.clipContent(inhalt, 30, 2));
			assertEquals("<h4>Schwer&hellip;</h4>", clipHelper.clipContent(inhalt, 6, 2));
			assertEquals("<h4>Schwerpunkte:</h4><p>&hellip;</p>", clipHelper.clipContent(inhalt, 200, 1));
		}
		System.out.println("Simple Text: " + (System.currentTimeMillis() - start));
	}

	@Test
	public void testGetInhaltGekuerztNachZeilen() {
		ContentClipHelper clipHelper = new ContentClipHelper();
		long start = System.currentTimeMillis();
		for (int i = 0; i < TIMES; i++) {
			String inhalt = "<h4>aaa</h4><p>bbb<br/>ccc<br/></p><ul><li>ddd</li><li>eee</li></ul><ol><li>fff</li><li>ggg</li></ol>";
			assertEquals(inhalt, clipHelper.clipContent(inhalt, 100, 8));
			assertEquals(
					"<h4>aaa</h4><p>bbb<br/>ccc<br/></p><ul><li>ddd</li><li>eee</li></ul><ol><li>fff</li><li>&hellip;</li></ol>",
					clipHelper.clipContent(inhalt, 100, 7));
			assertEquals(
					"<h4>aaa</h4><p>bbb<br/>ccc<br/></p><ul><li>ddd</li><li>eee</li></ul><ol><li>&hellip;</li></ol>",
					clipHelper.clipContent(inhalt, 100, 6));
			assertEquals("<h4>aaa</h4><p>bbb<br/>ccc<br/></p><ul><li>ddd</li><li>&hellip;</li></ul>",
					clipHelper.clipContent(inhalt, 100, 5));
			assertEquals("<h4>aaa</h4><p>bbb<br/>ccc<br/></p><ul><li>&hellip;</li></ul>",
					clipHelper.clipContent(inhalt, 100, 4));
			assertEquals("<h4>aaa</h4><p>bbb<br/>ccc<br/>&hellip;</p>", clipHelper.clipContent(inhalt, 100, 3));
			assertEquals("<h4>aaa</h4><p>bbb<br/>&hellip;</p>", clipHelper.clipContent(inhalt, 100, 2));
			assertEquals("<h4>aaa</h4><p>&hellip;</p>", clipHelper.clipContent(inhalt, 100, 1));
		}
		System.out.println("Nach Zeilen: " + (System.currentTimeMillis() - start));
	}
}
