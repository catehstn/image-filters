package com.catehuston.imagefilter.model;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import processing.core.PApplet;
import processing.core.PImage;

import com.catehuston.imagefilter.color.ColorHelper;

@RunWith(MockitoJUnitRunner.class)
public class ImageStateTest {
	
	@Mock PApplet applet;
	@Mock ColorHelper colorHelper;
	@Mock PImage image;
	private ImageState imageState;

	@Before public void setUp() throws Exception {
	//	applet = mock(PApplet.class);
		//colorHelper = mock(ColorHelper.class);
		//image = mock(PImage.class);
		imageState = new ImageState(colorHelper);
	}

	@After public void tearDown() throws Exception {
	}

	@Test public void testUpdateImage() {
		// Dominant hue hidden.
		imageState.set(image, true, false, 5, 10, 15);
		imageState.updateImage(applet, 100, 10, 100);
		verify(colorHelper).processImageForHue(applet, null, 100, 10, false);
		verify(image).updatePixels();
		
		// Dominant hue showing.
		
		// Neither set.
	}

}
