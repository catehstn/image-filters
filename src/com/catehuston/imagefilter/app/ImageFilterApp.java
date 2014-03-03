package com.catehuston.imagefilter.app;

import java.io.File;

import processing.core.PApplet;
import processing.core.PImage;

import com.catehuston.imagefilter.color.ColorHelper;


@SuppressWarnings("serial")
public class ImageFilterApp extends PApplet {
	
	PImage img;
	String filePath;
	
	static final String instructions = "Press r to\nincrease red\nfilter, e to\nreduce it.\n"
			+ "g to increase\ngreen filter, f\nto reduce it.\nb to increase\nblue filter,\nv to reduce it.\n"
			+ "h hides the\ndominant hue\nand s shows the\ndominant hue.\nPress c to\nchoose a new\nfile. Press"
			+ " space\nto reset";
	static final int filterHeight = 2;
	static final int filterIncrement = 5;
	static final int hueRange = 320; 
	static final int hueTolerance = 10;
	static final int imageMax = 640;
	static final int rgbColorRange = 100;
	static final int sideBarPadding = 10;
	static final int sideBarWidth = rgbColorRange + 2 * sideBarPadding;
	
	boolean dominentHueHidden = false;
	boolean dominantHueShowing = false;
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

		y += 4 * sideBarPadding;
		text(instructions, x, y);

		// Draw image.
		if (img != null) {
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
			setUpImage();
			redraw();
		}
	}
	
	private void drawImage() {
		imageMode(CENTER);
		if (dominantHueShowing) {
			ColorHelper.processImageForHue(this, img, hueRange, hueTolerance, true);
		} else if (dominentHueHidden) {
			ColorHelper.processImageForHue(this, img, hueRange, hueTolerance, false);
		}
		ColorHelper.applyColorFilter(this, img, redFilter, blueFilter, greenFilter, rgbColorRange);
		img.updatePixels();
		image(img, 320, 320, img.width, img.height);
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
			 dominentHueHidden = true;
			 dominantHueShowing = false;
			 break;
		 case 's':
			 dominentHueHidden = false;
			 dominantHueShowing = true;
			 break;
		 case ' ':
			 resetImage();
		 }
		 redraw();
	}
	
	private void chooseFile() {
		// Choose the file.
		selectInput("Select a file to process:", "fileSelected");
	}
	
	private void setUpImage() {
		img = null;
		img = loadImage(filePath);
		// Fix the size.
		if (img.width > imageMax || img.height > imageMax) {
			int imgWidth = imageMax;
			int imgHeight = imageMax;
			if (img.width > img.height) {
				imgHeight = (imgHeight *  img.height) / img.width;
			} else {
				imgWidth = (imgWidth * img.width) / img.height;
			}
			img.resize(imgWidth, imgHeight);
		}
	}
	
	private void resetImage() {
		redFilter = 0;
		greenFilter = 0;
		blueFilter = 0;
		dominantHueShowing = false;
		dominentHueHidden = false;
		setUpImage();
	}
}
