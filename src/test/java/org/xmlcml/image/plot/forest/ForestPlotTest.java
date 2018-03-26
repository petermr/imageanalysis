package org.xmlcml.image.plot.forest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.xmlcml.euclid.util.MultisetUtil;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.graphics.svg.util.ImageIOUtil;
import org.xmlcml.image.ImageAnalysisFixtures;
import org.xmlcml.image.ImageProcessor;
import org.xmlcml.image.ImageUtil;
import org.xmlcml.image.colour.ColorAnalyzer;
import org.xmlcml.image.colour.RGBColor;
import org.xmlcml.image.pixel.PixelIslandList;
import org.xmlcml.image.pixel.PixelList;
import org.xmlcml.image.pixel.PixelRingList;
import org.xmlcml.image.plot.PixelRingListComparator;
import org.xmlcml.image.plot.PlotTest;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;

import boofcv.io.image.UtilImageIO;
import junit.framework.Assert;

public class ForestPlotTest {
	private static final Logger LOG = Logger.getLogger(ForestPlotTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	@Test
	/** gray - will need some processing
	 * 
	 */
	public void testPMC2788519Medium() {
		String fileRoot = "PMC2788519";
		String plotType = "forest";
		File imageFile = new File(ImageAnalysisFixtures.DIAGRAMS_DIR, plotType+"/"+ fileRoot + ".png");
		Assert.assertTrue("file exists "+imageFile, imageFile.exists());
//		Assert.assertEquals("image size", 22, (int)FileUtils.sizeOf(imageFile));
		File targetDir = new File(ImageAnalysisFixtures.TARGET_DIR, plotType);
		BufferedImage image = UtilImageIO.loadImage(imageFile.toString());
		Assert.assertNotNull("image read", image);
		ImageProcessor imageProcessor = ImageProcessor.createDefaultProcessor();
		imageProcessor.setThinning(null);
		imageProcessor.setThreshold(180);
//		imageProcessor.setBinarize(false);
		imageProcessor.setBinarize(true);
		imageProcessor.processImage(image);
		BufferedImage image1 = imageProcessor.getBinarizedImage();
		ImageIOUtil.writeImageQuietly(image1, new File(targetDir, fileRoot + "/rawgray.png"));
	}
	
	@Test
	/** blue and black flattened to black
	 * 
	 */
	public void testBlueBlack() {
		String fileRoot = "blue";
		String plotType = "forest";
		File imageFile = new File(ImageAnalysisFixtures.DIAGRAMS_DIR, plotType + "/"+fileRoot+".png");
		Assert.assertTrue("file exists "+imageFile, imageFile.exists());
		File targetDir = new File(ImageAnalysisFixtures.TARGET_DIR, plotType);
		BufferedImage image = UtilImageIO.loadImage(imageFile.toString());
		Assert.assertNotNull("image read", image);
		ImageProcessor imageProcessor = ImageProcessor.createDefaultProcessor();
		imageProcessor.setThinning(null);
		imageProcessor.setBinarize(true);
		imageProcessor.processImage(image);
		BufferedImage image1 = imageProcessor.getBinarizedImage();
		ImageIOUtil.writeImageQuietly(image1, new File(targetDir, fileRoot+"/raw.png"));
		PixelIslandList pixelIslandList = imageProcessor.getOrCreatePixelIslandList();
		List<PixelRingList> pixelRingListList = pixelIslandList.createRingListList();
//		Assert.assertEquals("characters", 178, points.size());
		PlotTest.drawRings(pixelRingListList, new File(targetDir, fileRoot+"/points00.svg"));
		pixelRingListList.sort(new PixelRingListComparator());
		Collections.reverse(pixelRingListList);
		for (PixelRingList pixelRingList : pixelRingListList) {
			LOG.debug(pixelRingList.get(0).size());
		}
		PixelRingList pixelRingList = pixelRingListList.get(0);
		SVGG g = null;
		pixelRingList.plotRings(g, new String[] {"red", "cyan", "purple", "yellow", "blue", "pink", "green"});
		SVGSVG.wrapAndWriteAsSVG(g, new File(targetDir, fileRoot+"/allRings.svg"));
		for (int i = 0; i < pixelRingList.size(); i+=5) {
			PixelList pixelRing = pixelRingList.get(i);
			g = null;
			g = pixelRing.plotPixels(g, "red");
			SVGSVG.wrapAndWriteAsSVG(g, new File(targetDir, fileRoot+"/allRings"+i+".svg"));
			LOG.debug("i "+i);
		}
		PixelList pixelRing10 = pixelRingList.get(10);
		LOG.debug("PR "+pixelRing10);
		PixelIslandList pl;
//		PixelIslandList ringIslandList = PixelIslandList.;

	}
	
	
	@Test
	/** blue posterized
	 * 
	 */
	public void testBlue() {
		String fileRoot = "blue";
		String plotType = "forest";
		File imageFile = new File(ImageAnalysisFixtures.DIAGRAMS_DIR, plotType + "/"+fileRoot+".png");
		LOG.debug("image file "+imageFile);
		Assert.assertTrue("file exists "+imageFile, imageFile.exists());
		int nvalues = 4; // i.e. 16-bit color
		nvalues = 2;
		BufferedImage image = UtilImageIO.loadImage(imageFile.toString());
//		ImageUtil.flattenImage(image, nvalues);
		File file = new File("target/"+plotType+"/"+fileRoot+"/"+"posterize.png");
		LOG.debug("posterized file "+file);
		ImageIOUtil.writeImageQuietly(image, file);

		ColorAnalyzer colorAnalyzer = new ColorAnalyzer(image);
		Multiset<RGBColor> colorSet = colorAnalyzer.getOrCreateColorSet();
		List<RGBColor> colorList = new ArrayList<RGBColor>(colorSet);
		
		Multiset<Object> colorSet0 = HashMultiset.create();
		colorSet0.addAll(colorList);
		Iterable<Multiset.Entry<Object>> colorIterable = MultisetUtil.getObjectEntriesSortedByCount(colorSet0);
		for (Entry<Object> entry : colorIterable) {
			int count = entry.getCount(); 
			int rgb = ((RGBColor) entry.getElement()).getRGBInteger();
			int v = rgb % 0x010101;
			if (v == 0) {
//				LOG.debug("GRAY");
			} else {
				System.out.println(entry+"/"+count); 
			}
		}
		ImageIOUtil.writeImageQuietly(image, new File("target/posterize.png"));

	}

	@Test
	/** blue posterized
	 * 
	 */
	public void testBlue1() throws IOException {
		String fileRoot = "blue";
		String plotType = "forest";
		File imageFile = new File(ImageAnalysisFixtures.DIAGRAMS_DIR, plotType + "/"+fileRoot+".png");
		LOG.debug("image "+imageFile);
		Assert.assertTrue("file exists "+imageFile, imageFile.exists());
		
		ColorAnalyzer colorAnalyzer = new ColorAnalyzer();
		colorAnalyzer.readImage(imageFile);
		colorAnalyzer.setOutputDirectory(new File("target/"+fileRoot+"1"));
		LOG.debug("colorAnalyze "+imageFile);
		colorAnalyzer.defaultPosterize();

	}


}
