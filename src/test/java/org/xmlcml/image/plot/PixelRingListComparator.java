package org.xmlcml.image.plot;

import java.util.Comparator;

import org.xmlcml.image.pixel.PixelRingList;

public class PixelRingListComparator implements Comparator<PixelRingList> {

	public int compare(PixelRingList o1, PixelRingList o2) {
		return o1.get(0).size() - o2.get(0).size();
	}

}
