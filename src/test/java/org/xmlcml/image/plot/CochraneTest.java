package org.xmlcml.image.plot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.graphics.svg.util.ImageIOUtil;
import org.xmlcml.image.ImageAnalysisFixtures;
import org.xmlcml.image.ImageProcessor;
import org.xmlcml.image.pixel.PixelIslandList;
import org.xmlcml.image.pixel.PixelList;
import org.xmlcml.image.pixel.PixelRingList;

import boofcv.io.image.UtilImageIO;

public class CochraneTest {

	private static final Logger LOG = Logger.getLogger(CochraneTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	@Test
	public void testExtract2LinePlot() {
		File imageFile = new File(ImageAnalysisFixtures.PLOT_DIR, "cochrane/xyplot2.png");
		File targetDir = ImageAnalysisFixtures.TARGET_PLOT_DIR;
		BufferedImage image = UtilImageIO.loadImage(imageFile.toString());
		ImageProcessor imageProcessor = ImageProcessor.createDefaultProcessor();
		imageProcessor.setBinarize(true);
		imageProcessor.processImage(image);
		BufferedImage image1 = imageProcessor.getBinarizedImage();
		ImageIOUtil.writeImageQuietly(image1, new File(targetDir, "cochrane/xyplot2.png"));

	}

	@Test
	public void testExtract2LinePlotNoThin() {
		File imageFile = new File(ImageAnalysisFixtures.PLOT_DIR, "cochrane/xyplot2.png");
		File targetDir = ImageAnalysisFixtures.TARGET_PLOT_DIR;
		BufferedImage image = UtilImageIO.loadImage(imageFile.toString());
		ImageProcessor imageProcessor = ImageProcessor.createDefaultProcessor();
		imageProcessor.setThinning(null);
		imageProcessor.setBinarize(true);
		imageProcessor.processImage(image);
		BufferedImage image1 = imageProcessor.getBinarizedImage();
		ImageIOUtil.writeImageQuietly(image1, new File(targetDir, "cochrane/nothin/xyplot2.png"));

	}

	@Test
	/** create points
	 * 
	 */
	public void testCreatePointsOld() {
		PixelIslandList points = ImageProcessor
				.createDefaultProcessorAndProcess(new File(ImageAnalysisFixtures.COMPOUND_DIR, "components/points.png"))
				.getOrCreatePixelIslandList();
		List<PixelRingList> pixelRingListList = points.createRingListList();
		Assert.assertEquals("characters", 4, points.size());
		PlotTest.drawRings(pixelRingListList, new File("target/plot/points0.svg"));
	}

	@Test
	/** create points
	 * 
	 */
	public void testCreatePoints() {
		ImageProcessor imageProcessor = ImageProcessor.createDefaultProcessor();
		imageProcessor.setThinning(null);
//		imageProcessor.setThreshold(140);
//		imageProcessor.setBinarize(true);
		File imageFile = new File(ImageAnalysisFixtures.PLOT_DIR, "cochrane/xyplot2.png");
		imageProcessor.readAndProcessFile(imageFile);
		PixelIslandList pixelIslandList = imageProcessor.getOrCreatePixelIslandList();
		List<PixelRingList> pixelRingListList = pixelIslandList.createRingListList();
//		Assert.assertEquals("characters", 178, points.size());
		PlotTest.drawRings(pixelRingListList, new File("target/plot/points00.svg"));
		pixelRingListList.sort(new PixelRingListComparator());
		Collections.reverse(pixelRingListList);
		for (PixelRingList pixelRingList : pixelRingListList) {
			LOG.debug(pixelRingList.get(0).size());
		}
		PixelRingList pixelRingList = pixelRingListList.get(0);
		SVGG g = null;
		pixelRingList.plotRings(g, new String[] {"red", "cyan", "purple", "yellow", "blue", "pink", "green"});
		SVGSVG.wrapAndWriteAsSVG(g, new File("target/plot/cochrane/rings", "allRings.svg"));
		int i = 0;
//		for (PixelList pixelRing : pixelRingList) {
//			g = null;
//			g = pixelRing.plotPixels(g, "red");
//			SVGSVG.wrapAndWriteAsSVG(g, new File("target/plot/cochrane/rings", "allRings"+i+".svg"));
//			LOG.debug("i "+i);
//			i++;
//		}
		PixelList pixelRing10 = pixelRingList.get(10);
		LOG.debug("PR "+pixelRing10);
		PixelIslandList pl;
//		PixelIslandList ringIslandList = PixelIslandList.;
	}

	


}
