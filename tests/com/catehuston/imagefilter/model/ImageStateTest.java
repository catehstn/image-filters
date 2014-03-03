package com.catehuston.imagefilter.model;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import processing.core.PApplet;
import processing.core.PImage;

import com.catehuston.imagefilter.color.ColorHelper;

public class ImageStateTest {
	
	PApplet applet;
	ColorHelper colorHelper;
	PImage image;
	private ImageState imageState;

	@Before public void setUp() throws Exception {
		applet = createMock(PApplet.class);
		colorHelper = createMock(ColorHelper.class);
		image = createMock(PImage.class);
		imageState = new ImageState(colorHelper);
	}

	@After public void tearDown() throws Exception {
	}

	@Test public void testUpdateImage() {
		// Dominant hue hidden.
		imageState.set(image, true, false, 5, 10, 15);

		colorHelper.processImageForHue(applet, image, 100, 10, false);
		colorHelper.applyColorFilter(applet, image, 15, 5, 10, 100);
		image.updatePixels();
		replay(colorHelper);
		replay(image);
		imageState.updateImage(applet, 100, 10, 100);
		
//		verify(colorHelper).processImageForHue(applet, null, 100, 10, false);
	//	verify(image).updatePixels();
		
		// Dominant hue showing.
		
		// Neither set.
	}

}
