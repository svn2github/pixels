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

import java.awt.image.*;
import com.jhlabs.math.*;

public class TextureFilter extends PointFilter {

	private float scale = 32;
	private float stretch = 1.0f;
	private float angle = 0.0f;
	public float amount = 1.0f;
	public float turbulence = 1.0f;
	public float gain = 0.5f;
	public float bias = 0.5f;
	public int operation;
	private float m00 = 1.0f;
	private float m01 = 0.0f;
	private float m10 = 0.0f;
	private float m11 = 1.0f;
	private Colormap colormap = new Gradient();
	private Function2D function = new Noise();

	public TextureFilter() {
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public float getAmount() {
		return amount;
	}

	public void setFunction(Function2D function) {
		this.function = function;
	}

	public Function2D getFunction() {
		return function;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}
	
	public int getOperation() {
		return operation;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getScale() {
		return scale;
	}

	public void setStretch(float stretch) {
		this.stretch = stretch;
	}

	public float getStretch() {
		return stretch;
	}

	public void setAngle(float angle) {
		this.angle = angle;
		float cos = (float)Math.cos(angle);
		float sin = (float)Math.sin(angle);
		m00 = cos;
		m01 = sin;
		m10 = -sin;
		m11 = cos;
	}

	public float getAngle() {
		return angle;
	}

	public void setTurbulence(float turbulence) {
		this.turbulence = turbulence;
	}

	public float getTurbulence() {
		return turbulence;
	}

	public void setColormap(Colormap colormap) {
		this.colormap = colormap;
	}
	
	public Colormap getColormap() {
		return colormap;
	}
	
	public int filterRGB(int x, int y, int rgb) {
		float nx = m00*x + m01*y;
		float ny = m10*x + m11*y;
		nx /= scale;
		ny /= scale * stretch;
		float f = turbulence == 1.0 ? Noise.noise2(nx, ny) : Noise.turbulence2(nx, ny, turbulence);
		f = (f * 0.5f) + 0.5f;
		f = ImageMath.gain(f, gain);
		f = ImageMath.bias(f, bias);
		f *= amount;
		int a = rgb & 0xff000000;
		int v;
		if (colormap != null)
			v = colormap.getColor(f);
		else {
			v = PixelUtils.clamp((int)(f*255));
			int r = v << 16;
			int g = v << 8;
			int b = v;
			v = a|r|g|b;
		}
		if (operation != PixelUtils.REPLACE)
			v = PixelUtils.combinePixels(rgb, v, operation);
		return v;
	}

	public String toString() {
		return "Texture/Noise...";
	}
	
}
