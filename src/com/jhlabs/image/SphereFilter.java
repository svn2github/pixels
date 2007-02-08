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
import java.awt.geom.*;
import java.awt.image.*;

public class SphereFilter extends TransformFilter {

	private float a = 0;
	private float b = 0;
	private float a2 = 0;
	private float b2 = 0;
	private float centreX = 0.5f;
	private float centreY = 0.5f;
	private float refractionIndex = 1.5f;

	private float icentreX;
	private float icentreY;

	public SphereFilter() {
		setEdgeAction( CLAMP );
		setRadius( 100.0f );
	}

	public void setRefractionIndex(float refractionIndex) {
		this.refractionIndex = refractionIndex;
	}

	public float getRefractionIndex() {
		return refractionIndex;
	}

	public void setRadius(float r) {
		this.a = r;
		this.b = r;
	}

	public float getRadius() {
		return a;
	}

	public void setCentreX( float centreX ) {
		this.centreX = centreX;
	}

	public float getCentreX() {
		return centreX;
	}
	
	public void setCentreY( float centreY ) {
		this.centreY = centreY;
	}

	public float getCentreY() {
		return centreY;
	}
	
	public void setCentre( Point2D centre ) {
		this.centreX = (float)centre.getX();
		this.centreY = (float)centre.getY();
	}

	public Point2D getCentre() {
		return new Point2D.Float( centreX, centreY );
	}
	
    public BufferedImage filter( BufferedImage src, BufferedImage dst ) {
		int width = src.getWidth();
		int height = src.getHeight();
		icentreX = width * centreX;
		icentreY = height * centreY;
		if (a == 0)
			a = width/2;
		if (b == 0)
			b = height/2;
		a2 = a*a;
		b2 = b*b;
		return super.filter( src, dst );
	}
	
	protected void transformInverse(int x, int y, float[] out) {
		float dx = x-icentreX;
		float dy = y-icentreY;
		float x2 = dx*dx;
		float y2 = dy*dy;
		if (y2 >= (b2 - (b2*x2)/a2)) {
			out[0] = x;
			out[1] = y;
		} else {
			float rRefraction = 1.0f / refractionIndex;

			float z = (float)Math.sqrt((1.0f - x2/a2 - y2/b2) * (a*b));
			float z2 = z*z;

			float xAngle = (float)Math.acos(dx / Math.sqrt(x2+z2));
			float angle1 = ImageMath.HALF_PI - xAngle;
			float angle2 = (float)Math.asin(Math.sin(angle1)*rRefraction);
			angle2 = ImageMath.HALF_PI - xAngle - angle2;
			out[0] = x - (float)Math.tan(angle2)*z;

			float yAngle = (float)Math.acos(dy / Math.sqrt(y2+z2));
			angle1 = ImageMath.HALF_PI - yAngle;
			angle2 = (float)Math.asin(Math.sin(angle1)*rRefraction);
			angle2 = ImageMath.HALF_PI - yAngle - angle2;
			out[1] = y - (float)Math.tan(angle2)*z;
		}
	}

	public String toString() {
		return "Distort/Sphere...";
	}

}
