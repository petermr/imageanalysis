package org.xmlcml.image.colour;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.graphics.svg.util.ImageIOUtil;
import org.xmlcml.image.ImageAnalysisFixtures;
import org.xmlcml.image.ImageUtil;

import boofcv.io.image.UtilImageIO;

public class ColorAnalyzerIT {

	@Test
	public void testPosterizeMadagascar() throws IOException {
		ColorAnalyzerTest.testPosterize0("madagascar");
	}

	@Test
	public void testPosterizeSpect5() throws IOException {
		ColorAnalyzerTest.testPosterize0("spect5");
	}

	@Test
	public void testMoleculeGrayScale() {
		String fileRoot = "histogram";
	
		File moleculeFile = new File(ImageAnalysisFixtures.LINES_DIR, "IMG_20131119a.jpg");
		BufferedImage image = UtilImageIO.loadImage(moleculeFile.toString());
		ColorAnalyzer colorAnalyzer = new ColorAnalyzer(image);
		BufferedImage grayImage = colorAnalyzer.getGrayscaleImage();
		
		for (Integer nvalues : new Integer[]{4,8,16,32,64,128}) {
			BufferedImage imageOut = ImageUtil.flattenImage(grayImage, nvalues);
			colorAnalyzer.readImageDeepCopy(imageOut);
			SVGG g = colorAnalyzer.createColorFrequencyPlot();
			SVGSVG.wrapAndWriteAsSVG(g, new File("target/" + fileRoot + "/postermol"+nvalues+".svg"));
			ImageIOUtil.writeImageQuietly(imageOut, new File("target/" + fileRoot + "/postermol"+nvalues+".png"));
		}
		
	}

	@Test
		/** posterize blue/black line plot with antialising
		 * Problem is dithered colours
		 * 
		 * @param filename
		 * @throws IOException
		 */
		public void testPosterizeCochrane() throws IOException {
			ColorAnalyzer colorAnalyzer = new ColorAnalyzer();
			colorAnalyzer.readImage(new File(ImageAnalysisFixtures.PLOT_DIR,  "cochrane/xyplot2.png"));
			colorAnalyzer.setOutputDirectory(new File("target/"+"cochrane/xyplot2"));
			colorAnalyzer.setStartPlot(1);
			colorAnalyzer.setMaxPixelSize(1000000);
	//		colorAnalyzer.setIntervalCount(4);
			colorAnalyzer.setIntervalCount(2);
			colorAnalyzer.setEndPlot(15);
			colorAnalyzer.setMinPixelSize(3000);
	//		colorAnalyzer.setMinPixelSize(300);
			colorAnalyzer.flattenImage();
			colorAnalyzer.analyzeFlattenedColours();
		}

}
