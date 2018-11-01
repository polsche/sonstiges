package de.ba.bub.studisu.studienangebote.model.requestparams;

/**
 * Pager.
 * 
 * Sorting findet nur über den Abstand zwischen Wunschort(en) und Studienort
 * statt und ist "fest verdrahtet".
 * 
 * Stand heute gibt es keine andere fachlich sinnvolle Sortierung, so dass hier
 * nur noch Paging benötigt wird...
 * 
 * KSC 2017-10-16
 *
 * @author loutzenhj on 06.04.2017.
 *
 */
public class Paging {

	public static final int COUNT_DEFAULT = 20;

	private int offset = 0;
	private int count = COUNT_DEFAULT;
	
	@Override
	public String toString() {
		return "Paging Offset:"+getOffset()+" Count:"+getCount();
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
