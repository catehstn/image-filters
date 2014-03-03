package com.catehuston.imagefilter.color;

import com.catehuston.imagefilter.model.HSBColor;

import processing.core.PApplet;
import processing.core.PImage;

public class ColorHelper {
	
	public boolean hueInRange(float hue, int hueRange, float lower, float upper) {
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
			return hue < upper || hue > lower;
		}
	}
	
	public HSBColor hsbColorFromImage(PApplet applet, PImage img, int hueRange) {
		img.loadPixels();
		int numberOfPixels = img.pixels.length;
		int[] hues = new int[hueRange];
		float[] saturations = new float[hueRange];
		float[] brightnesses = new float[hueRange];

		for (int i = 0; i < numberOfPixels; i++) {
			int pixel = img.pixels[i];
			int hue = Math.round(applet.hue(pixel));
			float saturation = applet.saturation(pixel);
			float brightness = applet.brightness(pixel);
			hues[hue]++;
			saturations[hue] += saturation;
			brightnesses[hue] += brightness;
		}

		// Find the most common hue.
		int hueCount = hues[0];
		int hue = 0;
		for (int i = 1; i < hues.length; i++) {
			if (hues[i] > hueCount) {
				hueCount = hues[i];
				hue = i;
			}
		}

		// Return the color to display.
		float s = saturations[hue] / hueCount;
		float b = brightnesses[hue] / hueCount;
		return new HSBColor(hue, s, b);
	}
	
	public void processImageForHue(PApplet applet, PImage img, int hueRange,
			int hueTolerance, boolean showHue) {
		applet.colorMode(PApplet.HSB, (hueRange - 1));
		img.loadPixels();
		int numberOfPixels = img.pixels.length;
		HSBColor dominantHue = hsbColorFromImage(applet, img, hueRange);
		// Manipulate photo, grayscale any pixel that isn't close to that hue.
		float lower = dominantHue.h - hueTolerance;
		float upper = dominantHue.h + hueTolerance;
		for (int i = 0; i < numberOfPixels; i++) {
			int pixel = img.pixels[i];
			float hue = applet.hue(pixel);
			if (hueInRange(hue, hueRange, lower, upper) == showHue) {
				float brightness = applet.brightness(pixel);
				img.pixels[i] = applet.color(brightness);
			}
		}
		img.updatePixels();
	}
	
	public void applyColorFilter(PApplet applet, PImage img, int minRed,
			int minBlue, int minGreen, int colorRange) {
		applet.colorMode(PApplet.RGB, colorRange);
		img.loadPixels();
		int numberOfPixels = img.pixels.length;
		for (int i = 0; i < numberOfPixels; i++) {
			int pixel = img.pixels[i];
			int alpha = Math.round(applet.alpha(pixel));
			int red = Math.round(applet.red(pixel));
			int green = Math.round(applet.green(pixel));
			int blue = Math.round(applet.blue(pixel));
			
			red = (red >= minRed) ? red : 0;
			green = (green >= minGreen) ? green : 0;
			blue = (blue >= minBlue) ? blue : 0;
			
			img.pixels[i] = applet.color((float) red, (float) green, (float) blue, (float) alpha);
		}
	}
}
