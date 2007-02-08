/*
Copyright 2006 Jerry Huxtable

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.jhlabs.image;

import java.awt.*;
import java.awt.image.*;
import com.jhlabs.math.*;

/**
 * A filter which distorts an image by rippling it in the X or Y directions.
 * The amplitude and wavelength of rippling can be specified as well as whether 
 * pixels going off the edges are wrapped or not.
 */
public class RippleFilter extends TransformFilter {
	
	public final static int SINE = 0;
	public final static int SAWTOOTH = 1;
	public final static int TRIANGLE = 2;
	public final static int NOISE = 3;

	public float xAmplitude, yAmplitude;
	public float xWavelength, yWavelength;
	private int waveType;

	/**
	 * Construct a RIppleFIlter
	 */
	public RippleFilter() {
		xAmplitude = 5.0f;
		yAmplitude = 0.0f;
		xWavelength = yWavelength = 16.0f;
	}

	/**
	 * Set the amplitude of ripple in the X direction.
	 * @param xAmplitude the amplitude (in pixels).
	 */
	public void setXAmplitude(float xAmplitude) {
		this.xAmplitude = xAmplitude;
	}

	/**
	 * Get the amplitude of ripple in the X direction.
	 * @return the amplitude (in pixels).
	 */
	public float getXAmplitude() {
		return xAmplitude;
	}

	/**
	 * Set the wavelength of ripple in the X direction.
	 * @param xWavelength the wavelength (in pixels).
	 */
	public void setXWavelength(float xWavelength) {
		this.xWavelength = xWavelength;
	}

	/**
	 * Get the wavelength of ripple in the X direction.
	 * @return the wavelength (in pixels).
	 */
	public float getXWavelength() {
		return xWavelength;
	}

	/**
	 * Set the amplitude of ripple in the Y direction.
	 * @param yAmplitude the amplitude (in pixels).
	 */
	public void setYAmplitude(float yAmplitude) {
		this.yAmplitude = yAmplitude;
	}

	/**
	 * Get the amplitude of ripple in the Y direction.
	 * @return the amplitude (in pixels).
	 */
	public float getYAmplitude() {
		return yAmplitude;
	}

	/**
	 * Set the wavelength of ripple in the Y direction.
	 * @param yWavelength the wavelength (in pixels).
	 */
	public void setYWavelength(float yWavelength) {
		this.yWavelength = yWavelength;
	}

	/**
	 * Get the wavelength of ripple in the Y direction.
	 * @return the wavelength (in pixels).
	 */
	public float getYWavelength() {
		return yWavelength;
	}


	public void setWaveType(int waveType) {
		this.waveType = waveType;
	}

	public int getWaveType() {
		return waveType;
	}

	protected void transformSpace(Rectangle r) {
		if (edgeAction == ZERO) {
			r.x -= (int)xAmplitude;
			r.width += (int)(2*xAmplitude);
			r.y -= (int)yAmplitude;
			r.height += (int)(2*yAmplitude);
		}
	}

	protected void transformInverse(int x, int y, float[] out) {
		float nx = (float)y / xWavelength;
		float ny = (float)x / yWavelength;
		float fx, fy;
		switch (waveType) {
		case SINE:
		default:
			fx = (float)Math.sin(nx);
			fy = (float)Math.sin(ny);
			break;
		case SAWTOOTH:
			fx = ImageMath.mod(nx, 1);
			fy = ImageMath.mod(ny, 1);
			break;
		case TRIANGLE:
			fx = ImageMath.triangle(nx);
			fy = ImageMath.triangle(ny);
			break;
		case NOISE:
			fx = Noise.noise1(nx);
			fy = Noise.noise1(ny);
			break;
		}
		out[0] = x + xAmplitude * fx;
		out[1] = y + yAmplitude * fy;
	}

	public String toString() {
		return "Distort/Ripple...";
	}

}
