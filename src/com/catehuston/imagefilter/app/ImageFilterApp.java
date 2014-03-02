package com.catehuston.imagefilter.app;

import java.io.File;

import processing.core.PApplet;
import processing.core.PImage;

import com.catehuston.imagefilter.color.ColorHelper;
import com.catehuston.imagefilter.model.HSBColor;


@SuppressWarnings("serial")
public class ImageFilterApp extends PApplet {
	
	PImage img;
	String filePath;
	
	static final int rgbColorRange = 100;
	static final int filterHeight = 2;
	static final int filterIncrement = 5;
	static final int hueRange = 320; 
	static final int hueTolerance = 10;
	static final int imageMax = 640;
	static final int sideBarPadding = 10;
	static final int sideBarWidth = rgbColorRange + 2 * sideBarPadding;
	
	int redFilter = 0;
	int blueFilter = 0;
	int greenFilter = 0;

	public void setup() {
		noLoop();
		
		// Set up the view.
		size(imageMax + sideBarWidth, imageMax);
		background(0);

		chooseFile();
	}
	
	public void draw() {
		background(0);
		
		colorMode(RGB, rgbColorRange);
		stroke(rgbColorRange);
		line(imageMax, 0, imageMax, imageMax);
		
		// Draw red line
		int x = imageMax + sideBarPadding;
		int y = 2 * sideBarPadding;
		stroke(rgbColorRange, 0, 0);
		line(x, y, x + rgbColorRange, y);
		line(x + redFilter, y - filterHeight, x + redFilter, y + filterHeight);
		
		// Draw green line
		y += 2 * sideBarPadding;
		stroke(0, rgbColorRange, 0);
		line(x, y, x + rgbColorRange, y);
		line(x + greenFilter, y - filterHeight, x + greenFilter, y + filterHeight);
		
		// Draw blue line
		y += 2 * sideBarPadding;
		stroke(0, 0, rgbColorRange);
		line(x, y, x + rgbColorRange, y);
		line(x + blueFilter, y - filterHeight, x + blueFilter, y + filterHeight);
		
		// Draw image.
		if (img != null) {
			print("draw image\n");
			drawImage();
		}
		updatePixels();
	}
	
	// Callback for selectInput(), has to be public to be found.
	public void fileSelected(File file) {
		if (file == null) {
			println("User hit cancel.");
		} else {
			this.filePath = file.getAbsolutePath();
			img = loadImage(filePath);
			redraw();
		}
	}
	
	private void dominantHueFilter() {
		processImageForHue(true);
	}
	
	private void hideDominentHueFilter() {
		processImageForHue(false);
	}
	
	private void processImageForHue(boolean showHue) {
		img = loadImage(filePath);
		colorMode(HSB, (hueRange - 1));
		img.loadPixels();
		int numberOfPixels = img.pixels.length;
		HSBColor dominantHue = ColorHelper.hsbColorFromImage(img, this, hueRange);
		// Manipulate photo, grayscale any pixel that isn't close to that hue.
		float lower = dominantHue.h - hueTolerance;
		float upper = dominantHue.h + hueTolerance;
		for (int i = 0; i < numberOfPixels; i++) {
			int pixel = img.pixels[i];
			float hue = hue(pixel);
			if (hueInRange(hue, lower, upper) == showHue) {
				float brightness = brightness(pixel);
				img.pixels[i] = color(brightness);
			}
		}
		img.updatePixels();
	}
	
	private static boolean hueInRange(float hue, float lower, float upper) {
		// Need to compensate for it being circular - can go around.
		if (lower < 0) {
			lower += hueRange;
		}
		if (upper > hueRange) {
			upper -= hueRange;
		}
		if (lower < upper) {
			return hue < upper && hue > lower;
		} else {
			return hue > upper || hue < lower;
		}
	}
	
	private void drawImage() {
		colorFilter(redFilter, blueFilter, greenFilter);
		int imgWidth = imageMax;
		int imgHeight = imageMax;
		if (img.width > img.height) {
			imgHeight = (imgHeight *  img.height) / img.width;
		} else {
			imgWidth = (imgWidth * img.width) / img.height;
		}
		imageMode(CENTER);
		image(img, 320, 320, imgWidth, imgHeight);
	}

	private void colorFilter(int minRed, int minBlue, int minGreen) {
		colorMode(RGB, rgbColorRange);
		img.loadPixels();
		int numberOfPixels = img.pixels.length;
		for (int i = 0; i < numberOfPixels; i++) {
			int pixel = img.pixels[i];
			int alpha = Math.round(alpha(pixel));
			int red = Math.round(red(pixel));
			int green = Math.round(green(pixel));
			int blue = Math.round(blue(pixel));
			
			red = (red >= minRed) ? red : 0;
			green = (green >= minGreen) ? green : 0;
			blue = (blue >= minBlue) ? blue : 0;
			
			img.pixels[i] = color((float) red, (float) green, (float) blue, (float) alpha);
		}
	}
	
	public void keyPressed() {
		 switch (key) {
		 case 'r':
			 redFilter+=filterIncrement;
			 redFilter = min(redFilter, rgbColorRange);
			 break;
		 case 'e':
			 redFilter-=filterIncrement;
			 redFilter = max(redFilter, 0);
			 break;
		 case 'g':
			 greenFilter+=filterIncrement;
			 greenFilter = min(greenFilter, rgbColorRange);
			 break;
		 case 'f':
			 greenFilter-=filterIncrement;
			 greenFilter = max(greenFilter, 0);
			 break;
		 case 'b':
			 blueFilter+=filterIncrement;
			 blueFilter = min(blueFilter, rgbColorRange);
			 break;
		 case 'v':
			 blueFilter-=filterIncrement;
			 blueFilter = max(blueFilter, 0);
			 break;
		 case 'c':
			 chooseFile();
			 break;
		 case 'h':
			 hideDominentHueFilter();
			 break;
		 case 's':
			 dominantHueFilter();
			 break;
		 }
		 redraw();
	}
	
	private void chooseFile() {
		//Choose the file.
		selectInput("Select a file to process:", "fileSelected");
	}
}
