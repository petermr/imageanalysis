package org.xmlcml.image.processing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.xmlcml.euclid.Int2;
import org.xmlcml.euclid.Real2;
import org.xmlcml.euclid.Real2Array;


/** a group of 3- or more- connected pixels.
 * 
 * @author pm286
 *
 */
public class Nucleus {
	
	private final static Logger LOG = Logger.getLogger(Nucleus.class);

	private Set<Pixel> pixelSet;
	private Set<Pixel> spikeSet;
	private PixelIsland island;
	private Set<Pixel> thinnedSet;
	private List<Pixel> centrePixelList;
	private Real2 centre;
	private List<Shell> shellList;
	
	public Nucleus(PixelIsland island) {
		this.island = island;
		pixelSet = new HashSet<Pixel>();
	}

	public void add(Pixel pixel) {
		pixelSet.add(pixel);
	}

	public int size() {
		return pixelSet.size();
	}
	
	public boolean contains(Pixel pixel) {
		return pixelSet.contains(pixel);
	}
	
	/** set of "spikes" from nucleus.
	 * 
	 * these are pixels with with exactly 2 neighbours which are attached to nucleus.
	 * They are likely to be the start of the edges between nuclei (nodes)
	 * @return
	 */
	public Set<Pixel> getSpikeSet() {
		spikeSet = new HashSet<Pixel>();
		Iterator<Pixel> iterator = pixelSet.iterator();
		while (iterator.hasNext()) {
			Pixel pixel = iterator.next();
			List<Pixel> neighbourList = pixel.getNeighbours(island);
			for (Pixel neighbour : neighbourList) {
				if (neighbour.getNeighbours(island).size() == 2) {
					spikeSet.add(neighbour);
				}
			}
		}
		return spikeSet;
	}
	
	/** returns iteration required to create overlapping shells.
	 * 
	 * @param maxIterations
	 * @return
	 */
	int ensureFlattened(int maxIterations) {
		int iteration = 0;
		getSpikeSet();
		if (spikeSet.size() != pixelSet.size() && spikeSet.size() > 0) {
			shellList = createShellListFromSpikeSet();
			boolean anded = false;
			for (; iteration < maxIterations; iteration++) {
				for (Shell shell : shellList) {
					shell.expandOnePixelFromCurrent();
				}
				
				thinnedSet = new HashSet<Pixel>(shellList.get(0).getExpandedSet());
				// remove shells 1... from 0
				for (int j = 1; j < shellList.size(); j++) {
					Shell shell = shellList.get(j);
					thinnedSet.retainAll(shell.getExpandedSet());
					if (thinnedSet.size() != 0) {
						LOG.debug("all shells overlap on iteration "+iteration+ "; size "+thinnedSet.size()+" spikes "+spikeSet.size());
						getCentre(thinnedSet);
						anded = true;
						break;
					}
				}
				if (anded) break;
			}
			if (!anded) {
				LOG.error("failed to AND all spikesets");
			}
		}
		return iteration;
	}

	public Real2 getCentre(Set<Pixel> pixels) {
		if (centre == null) {
			Real2Array coords = new Real2Array();
			for (Pixel pixel : pixels) {
				coords.add(new Real2(pixel.getInt2()));
			}
			centre = coords.getMean();
		}
		return centre;
	}

	boolean areNeighbours(int i, int j) {
		return centrePixelList.get(i).getNeighbours(island).contains(centrePixelList.get(j));
	}

	private List<Shell> createShellListFromSpikeSet() {
		List<Shell> shellList = new ArrayList<Shell>(); 
		for (Pixel pixel : spikeSet) {
			Shell shell = new Shell(pixel, island);
			shellList.add(shell);
		}
		return shellList;
	}

	public Real2 getCentre() {
//		LOG.debug(pixelSet);
		return getCentre(pixelSet);
	}

}