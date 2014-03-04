package com.catehuston.imagefilter.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
		imageState = new ImageState(colorHelper);
	}

	@Test public void testUpdateImageDominantHueHidden() {
		imageState.set(image, true, false, 5, 10, 15);

		imageState.updateImage(applet, 100, 10, 100);
		
		verify(colorHelper).processImageForHue(applet, image, 100, 10, false);
		verify(colorHelper).applyColorFilter(applet, image, 15, 5, 10, 100);
		verify(image).updatePixels();
	}
	
	@Test public void testUpdateDominantHueShowing() {
		imageState.set(image, false, true, 5, 10, 15);

		imageState.updateImage(applet, 100, 10, 100);
		
		verify(colorHelper).processImageForHue(applet, image, 100, 10, true);
		verify(colorHelper).applyColorFilter(applet, image, 15, 5, 10, 100);
		verify(image).updatePixels();
	}
	
	@Test public void testUpdateRGBOnly() {
		imageState.set(image, false, false, 5, 10, 15);

		imageState.updateImage(applet, 100, 10, 100);
		
		verify(colorHelper, never()).processImageForHue(any(PApplet.class), any(PImage.class),
				anyInt(), anyInt(), anyBoolean());
		verify(colorHelper).applyColorFilter(applet, image, 15, 5, 10, 100);
		verify(image).updatePixels();
	}
	
	@Test public void testKeyPress() {
		imageState.processKeyPress('r', 5, 100);
		assertState(false, false, 5, 0, 0);
		
		imageState.processKeyPress('e', 5, 100);
		assertState(false, false, 0, 0, 0);
		
		imageState.processKeyPress('g', 5, 100);
		assertState(false, false, 0, 5, 0);
		
		imageState.processKeyPress('f', 5, 100);
		assertState(false, false, 0, 0, 0);
		
		imageState.processKeyPress('b', 5, 100);
		assertState(false, false, 0, 0, 5);
		
		imageState.processKeyPress('v', 5, 100);
		assertState(false, false, 0, 0, 0);
		
		imageState.processKeyPress('h', 5, 100);
		assertState(true, false, 0, 0, 0);
		
		imageState.processKeyPress('s', 5, 100);
		assertState(false, true, 0, 0, 0);
	}
	
	private void assertState(boolean dominantHueHidden, boolean dominantHueShowing,
			int redFilter, int greenFilter, int blueFilter) {
		assertEquals(dominantHueHidden, imageState.dominantHueHidden());
		assertEquals(dominantHueShowing, imageState.dominantHueShowing());
		assertEquals(redFilter, imageState.redFilter());
		assertEquals(greenFilter, imageState.greenFilter());
		assertEquals(blueFilter, imageState.blueFilter());
	}
	
	@Test public void testSetupImage() {
		when(applet.loadImage(null)).thenReturn(image);
		imageState.setUpImage(applet, 10);
		// TODO: If want to test this, have to figure out how to mock a field.
		// May require wrapping PImage. 
	}
	
	@Test public void testResetImage() {
		when(applet.loadImage(null)).thenReturn(image);
		imageState.set(image, true, true, 5, 10, 15);
		imageState.resetImage(applet, 10);
		assertState(false, false, 0, 0, 0);
	}
}
