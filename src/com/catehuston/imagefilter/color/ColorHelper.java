package com.catehuston.imagefilter.color;

import com.catehuston.imagefilter.model.HSBColor;

import processing.core.PApplet;
import processing.core.PImage;

public class ColorHelper {
	
	public static HSBColor hsbColorFromImage(PImage img, PApplet applet, int hueRange) {
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
}
