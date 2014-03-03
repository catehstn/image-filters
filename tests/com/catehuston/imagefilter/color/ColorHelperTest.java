package com.catehuston.imagefilter.color;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import processing.core.PApplet;
import processing.core.PImage;

import com.catehuston.imagefilter.model.HSBColor;

public class ColorHelperTest {
	
	@Mock PApplet applet;
	PImage image;
	
	private static final int px1 = -3887210;
	private static final int px2 = -4216178;
	private static final int px3 = -4083818;
	private static final int px4 = -4216690;
	private static final int px5 = -399164;

	@Before public void setUp() throws Exception {
		//applet = mock(PApplet.class);
		MockitoAnnotations.initMocks(this);
		
		image = new PImage();
		int[] pixels = { px1, px2, px3, px4, px5 };
		image.pixels = pixels;
	}

	@After public void tearDown() throws Exception {
		reset(applet);
	}

	@Test public void testHueInRange() {
		// In range.
		assertTrue(ColorHelper.hueInRange(10, 20, 5, 15));
		// Lowest end of range.
		assertTrue(ColorHelper.hueInRange(6, 20, 5, 15));
		// Highest end of range.
		assertTrue(ColorHelper.hueInRange(14, 20, 5, 15));
		// In range, looping over.
		assertTrue(ColorHelper.hueInRange(4, 20, 10, 25));
		// In range, looping under.
		assertTrue(ColorHelper.hueInRange(4, 20, -5, 5));
		// Out of range - below.
		assertFalse(ColorHelper.hueInRange(4, 20, 5, 15));
		// Out of range - above.
		assertFalse(ColorHelper.hueInRange(16, 20, 5, 15));
		// Out of range, looping over.
		assertFalse(ColorHelper.hueInRange(10, 20, 15, 25));
		// Out of range, looping under.
		assertFalse(ColorHelper.hueInRange(6, 20, -5, 5));
	}

	@Test public void testHsbColorFromImage() {
	/*	when(applet.hue(-3887210)).thenReturn(3F);
		when(applet.saturation(-3887210)).thenReturn(5F);
		when(applet.brightness(-3887210)).thenReturn(10F);
		
		when(applet.hue(-4216178)).thenReturn(2F);
		when(applet.saturation(-4216178)).thenReturn(6F);
		when(applet.brightness(-4216178)).thenReturn(11F);
		
		when(applet.hue(-4083818)).thenReturn(3F);
		when(applet.saturation(-4083818)).thenReturn(7F);
		when(applet.brightness(-4083818)).thenReturn(12F);
		
		when(applet.hue(-4216690)).thenReturn(5F);
		when(applet.saturation(-4216690)).thenReturn(8F);
		when(applet.brightness(-4216690)).thenReturn(13F);
		
		when(applet.hue(-399164)).thenReturn(3F);
		when(applet.saturation(-399164)).thenReturn(9F);
		when(applet.brightness(-399164)).thenReturn(14F);*/
		
		HSBColor color = ColorHelper.hsbColorFromImage(applet, image, 100);
		
		// Check results.
		assertEquals(3F, color.h, 0);
		assertEquals(7F, color.s, 0);
		assertEquals(12F, color.b, 0);
	}
	
	@Test public void testProcessImageForHue() {
		
		ColorHelper.processImageForHue(applet, image, 10, 10, true);
		verify(applet).colorMode(PApplet.HSB, 9);
	}
	
	@Test public void testApplyColorFilter() {
		
		// alpha, red, green, blue
		//stub(applet.alpha(Mockito.eq(px1))).toReturn(0F);
		when(applet.alpha(px1)).thenReturn(0F);
		
		ColorHelper.applyColorFilter(applet, image, 10, 10, 10, 100);
		verify(applet).colorMode(PApplet.RGB, 100);
	}
}
