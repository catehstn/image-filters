package com.catehuston.imagefilter.app;

import java.io.File;

import processing.core.PApplet;

import com.catehuston.imagefilter.color.ColorHelper;
import com.catehuston.imagefilter.model.ImageState;


@SuppressWarnings("serial")
public class ImageFilterApp extends PApplet {
	
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
	
	private ImageState imageState;

	public void setup() {
		noLoop();
		imageState = new ImageState(new ColorHelper());
		
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
		line(x + imageState.redFilter(), y - filterHeight,
				x + imageState.redFilter(), y + filterHeight);

		// Draw green line
		y += 2 * sideBarPadding;
		stroke(0, rgbColorRange, 0);
		line(x, y, x + rgbColorRange, y);
		line(x + imageState.greenFilter(), y - filterHeight,
				x + imageState.greenFilter(), y + filterHeight);

		// Draw blue line
		y += 2 * sideBarPadding;
		stroke(0, 0, rgbColorRange);
		line(x, y, x + rgbColorRange, y);
		line(x + imageState.blueFilter(), y - filterHeight,
				x + imageState.blueFilter(), y + filterHeight);

		y += 4 * sideBarPadding;
		text(instructions, x, y);

		// Draw image.
		if (imageState.image() != null) {
			drawImage();
		}
		updatePixels();
	}
	
	// Callback for selectInput(), has to be public to be found.
	public void fileSelected(File file) {
		System.out.println("fileSelected");
		if (file == null) {
			println("User hit cancel.");
		} else {
			System.out.println("file selected");
			imageState.setFilepath(file.getAbsolutePath());
			System.out.println("set up image fileSelected");
			imageState.setUpImage(this, imageMax);
			System.out.println("redraw");
			redraw();
		}
	}
	
	private void drawImage() {
		imageMode(CENTER);
		imageState.updateImage(this, hueRange, hueTolerance, rgbColorRange);;
		image(imageState.image().image(), 320, 320, imageState.image().getWidth(), imageState.image().getHeight());
	}
	
	public void keyPressed() {
		if (key == 'c') {
			chooseFile();
		} else if (key == ' ') {
			imageState.resetImage(this, imageMax);
		}
		 imageState.processKeyPress(key, filterIncrement, rgbColorRange);
		 redraw();
	}
	
	private void chooseFile() {
		// Choose the file.
		selectInput("Select a file to process:", "fileSelected");
	}
}
