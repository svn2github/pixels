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

/**
 * A Filter to draw grids and check patterns.
 */
public class CheckFilter extends PointFilter {

	private int xScale = 8;
	private int yScale = 8;
	private int foreground = 0xffffffff;
	private int background = 0xff000000;
	private int fuzziness = 0;
	private float angle = 0.0f;
	private int operation;
	private float m00 = 1.0f;
	private float m01 = 0.0f;
	private float m10 = 0.0f;
	private float m11 = 1.0f;

	public CheckFilter() {
	}

	public void setForeground(int foreground) {
		this.foreground = foreground;
	}

	public int getForeground() {
		return foreground;
	}

	public void setBackground(int background) {
		this.background = background;
	}

	public int getBackground() {
		return background;
	}

	public void setXScale(int xScale) {
		this.xScale = xScale;
	}

	public int getXScale() {
		return xScale;
	}

	public void setYScale(int yScale) {
		this.yScale = yScale;
	}

	public int getYScale() {
		return yScale;
	}

	public void setFuzziness(int fuzziness) {
		this.fuzziness = fuzziness;
	}

	public int getFuzziness() {
		return fuzziness;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}
	
	public int getOperation() {
		return operation;
	}
	
	/**
     * Specifies the angle of the texture.
     * @param angle the angle of the texture.
     * @angle
     * @see #getAngle
     */
	public void setAngle(float angle) {
		this.angle = angle;
		float cos = (float)Math.cos(angle);
		float sin = (float)Math.sin(angle);
		m00 = cos;
		m01 = sin;
		m10 = -sin;
		m11 = cos;
	}

	/**
     * Returns the angle of the texture.
     * @return the angle of the texture.
     * @see #setAngle
     */
	public float getAngle() {
		return angle;
	}

	public int filterRGB(int x, int y, int rgb) {
		float nx = (m00*x + m01*y) / xScale;
		float ny = (m10*x + m11*y) / yScale;
		float f = ((int)(nx+100000) % 2 != (int)(ny+100000) % 2) ? 1.0f : 0.0f;
		if (fuzziness != 0) {
			float fuzz = (fuzziness/100.0f);
			float fx = ImageMath.smoothPulse(0, fuzz, 1-fuzz, 1, ImageMath.mod(nx, 1));
			float fy = ImageMath.smoothPulse(0, fuzz, 1-fuzz, 1, ImageMath.mod(ny, 1));
			f *= fx*fy;
		}
		return ImageMath.mixColors(f, foreground, background);
	}

	public String toString() {
		return "Texture/Checkerboard...";
	}
}

