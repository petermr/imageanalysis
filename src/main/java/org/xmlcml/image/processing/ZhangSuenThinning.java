package org.xmlcml.image.processing;

	 
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.xmlcml.image.ImageUtil;
 
/**
 *
 * @author nayef
 */
public class ZhangSuenThinning extends Thinning {
 
    public ZhangSuenThinning(BufferedImage image) {
    	super(image);
    }


	public ZhangSuenThinning() {
		super();
	}

	@Override
	public int[][] doThinning() {
        int a, b;
 
        List<Point> pointsToChange = new LinkedList<Point>();
        do {
 
            hasChange = false;
            for (int y = 1; y + 1 < binaryImage.length; y++) {
                for (int x = 1; x + 1 < binaryImage[y].length; x++) {
                    a = getA(binaryImage, y, x);
                    b = getB(binaryImage, y, x);
                    if ( binaryImage[y][x]==1 && 2 <= b && b <= 6 && a == 1
                            && (binaryImage[y - 1][x] * binaryImage[y][x + 1] * binaryImage[y + 1][x] == 0)
                            && (binaryImage[y][x + 1] * binaryImage[y + 1][x] * binaryImage[y][x - 1] == 0)) {
                        pointsToChange.add(new Point(x, y));
                        //binaryImage[y][x] = 0;
                        hasChange = true;
                    }
                }
            }
 
            for (Point point : pointsToChange) {
                binaryImage[point.getY()][point.getX()] = 0;
            }
 
            pointsToChange.clear();
 
            for (int y = 1; y + 1 < binaryImage.length; y++) {
                for (int x = 1; x + 1 < binaryImage[y].length; x++) {
                    a = getA(binaryImage, y, x);
                    b = getB(binaryImage, y, x);
                    if ( binaryImage[y][x]==1 && 2 <= b && b <= 6 && a == 1
                            && (binaryImage[y - 1][x] * binaryImage[y][x + 1] * binaryImage[y][x - 1] == 0)
                            && (binaryImage[y - 1][x] * binaryImage[y + 1][x] * binaryImage[y][x - 1] == 0)) {
                        pointsToChange.add(new Point(x, y));
 
                        hasChange = true;
                    }
                }
            }
 
            for (Point point : pointsToChange) {
                binaryImage[point.getY()][point.getX()] = 0;
            }
 
            pointsToChange.clear();
 
        } while (hasChange);
 
        return binaryImage;
    }
 
    private class Point {
 
        private int x, y;
 
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
 
        public int getX() {
            return x;
        }
 
        public void setX(int x) {
            this.x = x;
        }
 
        public int getY() {
            return y;
        }
 
        public void setY(int y) {
            this.y = y;
        }
    };
 
    private int getA(int[][] binaryImage, int y, int x) {
 
        int count = 0;
        //p2 p3
        if (binaryImage[y - 1][x] == 0 && binaryImage[y - 1][x + 1] == 1) {
            count++;
        }
        //p3 p4
        if (binaryImage[y - 1][x + 1] == 0 && binaryImage[y][x + 1] == 1) {
            count++;
        }
        //p4 p5
        if (binaryImage[y][x + 1] == 0 && binaryImage[y + 1][x + 1] == 1) {
            count++;
        }
        //p5 p6
        if (binaryImage[y + 1][x + 1] == 0 && binaryImage[y + 1][x] == 1) {
            count++;
        }
        //p6 p7
        if (binaryImage[y + 1][x] == 0 && binaryImage[y + 1][x - 1] == 1) {
            count++;
        }
        //p7 p8
        if (binaryImage[y + 1][x - 1] == 0 && binaryImage[y][x - 1] == 1) {
            count++;
        }
        //p8 p9
        if (binaryImage[y][x - 1] == 0 && binaryImage[y - 1][x - 1] == 1) {
            count++;
        }
        //p9 p2
        if (binaryImage[y - 1][x - 1] == 0 && binaryImage[y - 1][x] == 1) {
            count++;
        }
 
        return count;
    }
 
    private int getB(int[][] binaryImage, int y, int x) {
 
        return binaryImage[y - 1][x] + binaryImage[y - 1][x + 1] + binaryImage[y][x + 1]
                + binaryImage[y + 1][x + 1] + binaryImage[y + 1][x] + binaryImage[y + 1][x - 1]
                + binaryImage[y][x - 1] + binaryImage[y - 1][x - 1];
    }
    

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) throws IOException {
//    
//       BufferedImage image = ImageIO.read(new File("/home/nayef/Desktop/bw.jpg"));
//
//       int[][] imageData = copyImageToBinary(image);
//       Thinning thinningService = new ZhangSuenThinning();
//       thinningService.doThinning(/*imageData*/);
//       copyBinaryToImage(image, imageData);
//
//       ImageUtil.writeImageQuietly(image, "/home/nayef/Desktop/bwThin.jpg");

   }

}