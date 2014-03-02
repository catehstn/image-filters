package com.catehuston.imagefilter.app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import processing.core.PApplet;
import processing.core.PImage;

import com.catehuston.imagefilter.color.ColorHelper;
import com.catehuston.imagefilter.model.HSBColor;


@SuppressWarnings("serial")
public class ImageFilterApp extends PApplet {
	
	PImage img;
	static final int hueRange = 320; 
	static final int hueTolerance = 10;

	public void setup() {
		noLoop();
		
		// Set up the view.
		size(640, 640);
		background(0);

		//Choose the file.
		selectInput("Select a file to process:");
	}
	
	public void draw() {
		if (img != null) {
			image(img, 0, 0, 640, 640);
		}
	}
	
	// Callback for selectInput(), has to be public to be found.
	public void fileSelected(File file) {
		if (file == null) {
			println("User hit cancel.");
		} else {
			img = loadImage(file.getAbsolutePath());
			chooseFilter();
		}
	}
	
	private void chooseFilter() {
		JPopupMenu popupMenu = new JPopupMenu("Filters");
		JMenuItem menuItem1 = new JMenuItem("Show Dominent Hue Filter");
		menuItem1.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				dominantHueFilter();
				redraw();
			}
		});
		popupMenu.add(menuItem1);
		
		JMenuItem menuItem2 = new JMenuItem("Hide Dominent Hue Filter");
		menuItem2.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				hideDominentHueFilter();
				redraw();
			}
		});
		popupMenu.add(menuItem2);
		popupMenu.show(this, 0, 0);
	}
	
	private void dominantHueFilter() {
		processImageForHue(false);
	}
	
	private void hideDominentHueFilter() {
		processImageForHue(true);
	}
	
	private void processImageForHue(boolean hideHue) {
		img.loadPixels();
		int numberOfPixels = img.pixels.length;
		HSBColor dominantHue = ColorHelper.hsbColorFromImage(img, this, hueRange);
		// Manipulate photo, grayscale any pixel that isn't close to that hue.
		float lower = dominantHue.h - hueTolerance;
		float upper = dominantHue.h + hueTolerance;
		for (int i = 0; i < numberOfPixels; i++) {
			int pixel = img.pixels[i];
			float hue = hue(pixel);
			if (hueInRange(hue, lower, upper) == hideHue) {
				float brightness = brightness(pixel);
				img.pixels[i] = color(brightness);
			}
		}
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
}
