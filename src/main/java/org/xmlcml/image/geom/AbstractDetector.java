package org.xmlcml.image.geom;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.xmlcml.graphics.image.ImageIOUtil;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.graphics.svg.SVGUtil;

public abstract class AbstractDetector {

	protected File file;
	protected BufferedImage inputImage;
	protected BufferedImage outputImage;
	protected SVGSVG svg;

	public void readImageFile(File file) {
		this.file = file;
		inputImage = null;
		try {
			inputImage = ImageIO.read(file);
		} catch (Exception e) {
			throw new RuntimeException("Cannot read: "+file+" "+e);
		}
		process();
	}

	protected abstract void process();
	
	public void writeFile(File outfile) {
		ImageIOUtil.writeImageQuietly(outputImage, outfile);
	}

	public void writeSvg(File file) throws IOException {
		SVGUtil.debug(svg, new FileOutputStream(file), 1);
	}

}
