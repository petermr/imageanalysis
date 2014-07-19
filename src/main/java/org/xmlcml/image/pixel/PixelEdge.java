package org.xmlcml.image.pixel;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.xmlcml.euclid.Real2;
import org.xmlcml.euclid.Real2Array;
import org.xmlcml.graphics.svg.SVGCircle;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGLine;
import org.xmlcml.graphics.svg.SVGPolyline;
import org.xmlcml.graphics.svg.SVGRect;
import org.xmlcml.image.geom.DouglasPeucker;

public class PixelEdge {

	private final static Logger LOG = Logger.getLogger(PixelEdge.class);
	
	private List<PixelNode> nodes;
	private PixelList pixelList; // pixels in order
	private PixelIsland island;
	private SVGPolyline polyline;

	public PixelEdge(PixelIsland island) {
		this.island = island;
		this.pixelList = new PixelList();
		this.nodes = new ArrayList<PixelNode>();
	}

	/** adds node and pixel contained within it.
	 * 
	 * @param node
	 * @param pos 0 or 1
	 */
	public void addNode(PixelNode node, int pos) {
		ensureNodes();
		if (this.nodes.size() != pos) {
			LOG.trace("Cannot add node");
		} else if (node == null) {
			LOG.trace("Cannot add null node");
		} else {
			nodes.add(node);
			node.addEdge(this);
			LOG.trace("size "+nodes.size());
		}
	}
	
	private void ensureNodes() {
		if (nodes == null) {
			nodes = new ArrayList<PixelNode>();
		}
	}

	public void addPixel(Pixel pixel) {
		pixelList.add(pixel);
	}
	
	public void addPixelList(PixelList pixelList) {
		this.pixelList.addAll(pixelList);
	}
	
	public PixelList getPixelList() {
		return pixelList;
	}
	
	/** gets pixelNodes at end of edge.
	 * 
	 * normally 2; but for single cycles there are no nodes.
	 * 
	 * @return
	 */
	public List<PixelNode> getPixelNodes() {
		return nodes;
	}
	
	/** gets pixel from list.
	 * 
	 * @param i
	 * @return null if no list or i is outside range
	 */
	public Pixel get(int i) {
		return (pixelList == null || size() == 0 || i < 0 || i >= size()) ? null : pixelList.get(i);
	}
	
	public Pixel getFirst() {
		return get(0);
	}

	public Pixel getLast() {
		return get(size() - 1);
	}
	
	public int size() {
		return (pixelList == null) ? 0 : pixelList.size();
	}

	public PixelNode getPixelNode(int i) {
		return (nodes == null || i < 0 || i >= nodes.size()) ? null : nodes.get(i);
	}
	
	public void removeNodes() {
		while (nodes != null && nodes.size() > 0) {
			nodes.remove(0);
		}
	}
	
	public String toString() {
		String s = ""+pixelList+"/"+nodes;
		return s;
	}

	public boolean equalsIgnoreOrder(String listString) {
		boolean equals = pixelList.toString().equals(listString);
		if (!equals) {
			PixelList newList = new PixelList(pixelList);
			newList.reverse();
			equals = newList.toString().equals(listString);
		}
		return equals;
	}

	public SVGPolyline getOrCreateSegmentedPolyline() {
		if (polyline == null) {
			DouglasPeucker douglasPeucker = new DouglasPeucker(2.0);
			Real2Array points = pixelList.getReal2Array();
			Real2Array pointArray = douglasPeucker.reduceToArray(points);
			polyline = new SVGPolyline(pointArray);
		}
		return polyline;
	}

	public PixelNode getOtherNode(PixelNode pixelNode) {
		if (nodes.size() != 2) {
			return null;
		} else if (nodes.get(0).equals(pixelNode)) {
			return nodes.get(1);
		} else if (nodes.get(1).equals(pixelNode)) {
			return nodes.get(0);
		} else {
			return null;
		}
	}

	public Pixel getNearestPixelToMidPoint(Real2 midPoint) {
		Pixel midPixel = null;
		Real2 midPixelXY = null;
		double distMin = Double.MAX_VALUE;
		for (Pixel pixel :pixelList) {
			if (midPixel == null) {
				midPixel = pixel;
				midPixelXY = new Real2(midPixel.getInt2());
			} else {
				Real2 xy = new Real2(pixel.getInt2());
				double dist = midPixelXY.getDistance(xy);
				if (dist < distMin) {
					midPixelXY = xy;
					distMin = dist;
					midPixel = pixel;
				}
			}
		}
		return midPixel;
	}

	public SVGG createPixelSVG(String colour) {
		SVGG g = new SVGG();
		for (Pixel pixel : pixelList) {
			SVGRect rect = pixel.getSVGRect(1, colour);
			g.appendChild(rect);
		}
		return g;
	}

	/** some edges are zero or one pixels and return to same node.
	 * 
	 * (Algorithm needs mending)
	 * 
	 * examples:
	 * {(19,48)}/[(19,48), (19,48)]
	 * {(22,36)}/[(23,36), (23,36)]
	 * {(29,30)}/[(29,31), (29,31)]
	 * {(29,29)}/[(29,31), (29,31)]
	 * 
	 * 
	 * @return
	 */
	public boolean isZeroCircular() {
		boolean circular = false;
		if (nodes.size() == 0) {
			circular = pixelList.size() <= 1;
		} else if (nodes.size() == 2) {
			circular = pixelList.size() <= 1;
		}
		return circular;
	}

	public SVGG createLineSVG() {
		SVGG g = new SVGG();
		if (getFirst() != null && getLast() != null) {
			Real2 firstXY = new Real2(getFirst().getInt2()).plus(new Real2(0.5, 0.5));
			Real2 lastXY = new Real2(getLast().getInt2()).plus(new Real2(0.5, 0.5));
			SVGLine line = new SVGLine(firstXY, lastXY);
			line.setWidth(0.5);
			g.appendChild(line);
		}
		return g;
	}

}
