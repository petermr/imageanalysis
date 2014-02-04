package org.xmlcml.image.text;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.xmlcml.image.Fixtures;
import org.xmlcml.image.processing.OtsuBinarize;
import org.xmlcml.image.processing.ThinningService;

public class TextTest {

	@Test
	public void testThin() throws Exception {
    	OtsuBinarize otsuBinarize = new OtsuBinarize();
        otsuBinarize.read(Fixtures.NRRL_PNG);
        otsuBinarize.toGray();
        otsuBinarize.binarize();
        otsuBinarize.writeImage(new File("target/NRRL.binarize.png"));
        BufferedImage image = otsuBinarize.getBinarizedImage();
        ThinningService thinningService = new ThinningService(image);
        thinningService.doThinning();
        image = thinningService.getThinnedImage();
        ImageIO.write(image, "png", new File("target/NRRL.thin.png"));
		
	}
	
	@Test
	public void testThinNO2() throws Exception {
		binarizeThin(Fixtures.NO2, "NO2");
	}
	
	@Test
	public void testThinCyclopiazonic() throws Exception {
		binarizeThin("Cyclopiazonic", true);
	}
	
	@Test
	public void testThinDithiobicycle() throws Exception {
		binarizeThin("dithiobicycle", false);
	}
	
	@Test
	public void testThinNumbers() throws Exception {
		binarizeThin("numbers", false);
	}
	
	@Test
	public void testThinSugar() throws Exception {
		binarizeThin("sugar", false);
	}

	@Test
	public void testThinYaxis() throws Exception {
		binarizeThin("yaxis", false);
	}

	@Test
	public void testNaringin() throws Exception {
		binarizeThin("naringin", false);
	}

	

	@Test
	public void testSubPixel() throws Exception {
		BufferedImage bImage = ImageIO.read(new File(Fixtures.TEXT_DIR, "mini.png"));
		AntiColour[] anti = new AntiColour[24];
		for (int j = 12; j < Math.min(26, bImage.getHeight()); j++) {
			for (int i = 18; i < bImage.getWidth(); i++) {
				anti[i] = new AntiColour(bImage, i, j);
				System.out.print(i+"/"+j+"/"+anti[i].ared+"/"+anti[i].agreen+"/"+anti[i].ablue+"   ");
			}
			System.out.println();
			AntiColour[] newAnti = new AntiColour[24];
			for (int i = 19; i < bImage.getWidth()-1; i++) {
				AntiColour left = anti[i].getLeft();
				AntiColour right = anti[i].getRight();
				System.out.print(i+"/"+j+" / ");
//				System.out.print(left.ared+"/"+left.agreen+"/"+left.ablue+"   ");
				System.out.print(anti[i].ared+"/"+anti[i].agreen+"/"+anti[i].ablue+"   ");
//				System.out.print(right.ared+"/"+right.agreen+"/"+right.ablue+"   ");
				System.out.println();
			}
		}
	}
	
	@Test
	public void testSubPixelC() throws Exception {
		BufferedImage bImage = ImageIO.read(new File(Fixtures.TEXT_DIR, "C.png"));
		AntiColour[] anti = new AntiColour[24];
		for (int j = 4; j < Math.min(18, bImage.getHeight()); j++) {
			for (int i = 0; i < bImage.getWidth(); i++) {
				anti[i] = new AntiColour(bImage, i, j);
				System.out.print(i+"/"+j+" "+anti[i].ared+"/"+anti[i].agreen+"/"+anti[i].ablue+"   ");
			}
			System.out.println();
		}
	}
	
	// =============================================

	private void binarizeThin(File imageFile, String abbrev) throws IOException {
		OtsuBinarize otsuBinarize = new OtsuBinarize();
        otsuBinarize.read(imageFile);
        otsuBinarize.toGray();
        otsuBinarize.binarize();
        otsuBinarize.writeImage(new File("target/"+abbrev+".binarize.png"));
        BufferedImage image = otsuBinarize.getBinarizedImage();
        ThinningService thinningService = new ThinningService(image);
        thinningService.doThinning();
        image = thinningService.getThinnedImage();
        ImageIO.write(image, "png", new File("target/"+abbrev+".thin.png"));
	}
	
	private void binarizeThin(String abbrev, boolean sharp) throws IOException {
		OtsuBinarize otsuBinarize = new OtsuBinarize();
        otsuBinarize.read(new File(Fixtures.TEXT_DIR, abbrev+".png"));
        otsuBinarize.toGray();
        otsuBinarize.writeImage(new File("target/"+abbrev+".gray.png"));
        if (sharp) {
	        otsuBinarize.sharpenGray();
	        otsuBinarize.writeImage(new File("target/"+abbrev+".sharp.png"));
        }
        otsuBinarize.binarize();
        otsuBinarize.writeImage(new File("target/"+abbrev+".binarize.png"));
        BufferedImage image = otsuBinarize.getBinarizedImage();
        ThinningService thinningService = new ThinningService(image);
        thinningService.doThinning();
        image = thinningService.getThinnedImage();
        ImageIO.write(image, "png", new File("target/"+abbrev+".thin.png"));
	}
	
	
}