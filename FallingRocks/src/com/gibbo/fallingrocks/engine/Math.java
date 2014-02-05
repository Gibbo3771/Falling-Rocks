package com.gibbo.fallingrocks.engine;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

/**
 * Math class used for basic math, such as conversions
 * 
 * @author Stephen Gibson
 * 
 */
public abstract class Math {

	public static final int WIDTH = 80;
	public static final int HEIGHT = 45;

	/**
	 * Convert a second to nanoseconds
	 * 
	 * @param toConvert
	 * @return
	 */
	public static double secondToNano(double toConvert) {
		return toConvert * 1000000000;
	}

	/**
	 * Round a number to the nearest decimal place
	 */
	public static double round(double value, int decimalPlaces) {
		BigDecimal bd = new BigDecimal(value).setScale(decimalPlaces,
				RoundingMode.HALF_UP);
		return value = bd.doubleValue();
	}

	/**
	 * Takes an array of floats and returns the largest value
	 * 
	 * @param values
	 * @return
	 */
	public static float max(float[] values) {
		float max = -1;
		for (int i = 0; i < values.length; i++) {
			if (max < values[i]) {
				max = values[i];
				continue;
			}
		}

		return max;
	}

	/**
	 * Takes an array of floats and returns the smallest value
	 * 
	 * @param values
	 * @return
	 */
	public static float min(float[] values) {
		float min = 100000;
		for (int i = 0; i < values.length; i++) {
			if (min > values[i]) {
				min = values[i];
				continue;
			}
		}

		return min;
	}

	public static float pixelToDP(float dp) {
		if (Gdx.graphics.getDensity() < 1)
			return dp + dp * Gdx.graphics.getDensity();
		return dp * Gdx.graphics.getDensity();
	}

	/**
	 * Get the angle between 2 vectors
	 * 
	 * @param vec1
	 * @param vec2
	 * @return
	 */
	public static float getAngleAtoB(float vec1x, float vec2x, float vec1y,
			float vec2y) {
		return MathUtils.radiansToDegrees
				* MathUtils.atan2(vec1x - vec2x, vec1y - vec2y);
	}

}
