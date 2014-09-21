package org.xmlcml.image.pixel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.xmlcml.euclid.Int2Range;

public class PixelNucleusList implements Iterable<PixelNucleus> {

	private final static Logger LOG = Logger.getLogger(PixelNucleusList.class);
	private List<PixelNucleus> list;

	public PixelNucleusList() {
		ensureList();
	}
	
	@Override
	public Iterator<PixelNucleus> iterator() {
		ensureList();
		return list.iterator();
	}
	
	public void add(PixelNucleus nucleus) {
		ensureList();
		list.add(nucleus);
	}

	private void ensureList() {
		if (list == null) {
			list = new ArrayList<PixelNucleus>();
		}
	}

	public void doTJunctionThinning(PixelIsland island) {
		for (PixelNucleus nucleus : this) {
			nucleus.doTJunctionThinning(island);
		}
	}

	public int size() {
		ensureList();
		return list.size();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (PixelNucleus nucleus : this) {
			sb.append(nucleus.toString());
		}
		sb.append("}");
		return sb.toString();
	}

	public PixelNucleus get(int i) {
		ensureList();
		return list.get(i);
	}

	public void addAll(PixelNucleusList l) {
		list.addAll(l.getList());
	}

	private List<PixelNucleus> getList() {
		return list;
	}

	/** merges any nuclei with touching pixels.
	 * 
	 * normally a tidy-up after generating nuclei in random order.
	 * may be obsoleted 
	 * 
	 * Not fully implemented
	 * 
	 */
	@Deprecated
	public void mergeTouchingNuclei() {
		PixelNucleus nucleusi = null;
		PixelNucleus nucleusj = null;
		PixelNucleus nucleusij = null;
		for (int i = 0; i < list.size() - 1; i++) {
			nucleusi = list.get(i);
			Int2Range bboxi = nucleusi.getBoundingBox();
			for (int j = i + 1; j < list.size(); j++) {
				nucleusj = list.get(j);
				Int2Range bboxj = nucleusi.getBoundingBox();
				if (bboxi.touches(bboxj)) {
					nucleusij = nucleusi.merge(nucleusj);
				}
			}
		}
	}


}