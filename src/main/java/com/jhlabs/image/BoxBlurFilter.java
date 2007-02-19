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
import java.awt.geom.*;

/**
 * A filter which performs a box blur on an image. The horizontal and vertical blurs can be specified separately
 * and a number of iterations can be given which allows an approximation to Gaussian blur.
 */
public class BoxBlurFilter extends AbstractBufferedImageOp {

	private int hRadius;
	private int vRadius;
	private int iterations = 1;
	
    /**
     * Construct a default BoxBlurFilter.
     */
    public BoxBlurFilter() {
	}
	
    /**
     * Construct a BoxBlurFilter.
     * @param hRadius the horizontal radius of blur
     * @param vRadius the vertical radius of blur
     * @param iterations the number of time to iterate the blur
     */
    public BoxBlurFilter( int hRadius, int vRadius, int iterations ) {
		this.hRadius = hRadius;
		this.vRadius = vRadius;
		this.iterations = iterations;
	}
	
	public BufferedImage filter( BufferedImage src, BufferedImage dst ) {
        int width = src.getWidth();
        int height = src.getHeight();

        if ( dst == null )
            dst = createCompatibleDestImage( src, null );

        int[] inPixels = new int[width*height];
        int[] outPixels = new int[width*height];
        getRGB( src, 0, 0, width, height, inPixels );

        for (int i = 0; i < iterations; i++ ) {
            blur( inPixels, outPixels, width, height, hRadius );
            blur( outPixels, inPixels, height, width, vRadius );
        }

        setRGB( dst, 0, 0, width, height, inPixels );
        return dst;
    }

    /**
     * Blur and transpose a block of ARGB pixels.
     * @param in the input pixels
     * @param out the output pixels
     * @param width the width of the pixel array
     * @param height the height of the pixel array
     * @param radius the radius of blur
     */
    public static void blur( int[] in, int[] out, int width, int height, int radius ) {
        int widthMinus1 = width-1;
        int tableSize = 2*radius+1;
        int divide[] = new int[256*tableSize];

        for ( int i = 0; i < 256*tableSize; i++ )
            divide[i] = i/tableSize;

        int inIndex = 0;
        
        for ( int y = 0; y < height; y++ ) {
            int outIndex = y;
            int ta = 0, tr = 0, tg = 0, tb = 0;

            for ( int i = -radius; i <= radius; i++ ) {
                int rgb = in[inIndex + ImageMath.clamp(i, 0, width-1)];
                ta += (rgb >> 24) & 0xff;
                tr += (rgb >> 16) & 0xff;
                tg += (rgb >> 8) & 0xff;
                tb += rgb & 0xff;
            }

            for ( int x = 0; x < width; x++ ) {
                out[ outIndex ] = (divide[ta] << 24) | (divide[tr] << 16) | (divide[tg] << 8) | divide[tb];

                int i1 = x+radius+1;
                if ( i1 > widthMinus1 )
                    i1 = widthMinus1;
                int i2 = x-radius;
                if ( i2 < 0 )
                    i2 = 0;
                int rgb1 = in[inIndex+i1];
                int rgb2 = in[inIndex+i2];
                
                ta += ((rgb1 >> 24) & 0xff)-((rgb2 >> 24) & 0xff);
                tr += ((rgb1 & 0xff0000)-(rgb2 & 0xff0000)) >> 16;
                tg += ((rgb1 & 0xff00)-(rgb2 & 0xff00)) >> 8;
                tb += (rgb1 & 0xff)-(rgb2 & 0xff);
                outIndex += height;
            }
            inIndex += width;
        }
    }
        
	/**
	 * Set the horizontal size of the blur.
	 * @param hRadius the radius of the blur in the horizontal direction
     * @min-value 0
     * @see #getHRadius
	 */
	public void setHRadius(int hRadius) {
		this.hRadius = hRadius;
	}
	
	/**
	 * Get the horizontal size of the blur.
	 * @return the radius of the blur in the horizontal direction
     * @see #setHRadius
	 */
	public int getHRadius() {
		return hRadius;
	}
	
	/**
	 * Set the vertical size of the blur.
	 * @param vRadius the radius of the blur in the vertical direction
     * @min-value 0
     * @see #getVRadius
	 */
	public void setVRadius(int vRadius) {
		this.vRadius = vRadius;
	}
	
	/**
	 * Get the vertical size of the blur.
	 * @return the radius of the blur in the vertical direction
     * @see #setVRadius
	 */
	public int getVRadius() {
		return vRadius;
	}
	
	/**
	 * Set both the horizontal and vertical sizes of the blur.
	 * @param radius the radius of the blur in both directions
     * @min-value 0
     * @see #getRadius
	 */
	public void setRadius(int radius) {
		this.hRadius = this.vRadius = radius;
	}
	
	/**
	 * Get the size of the blur.
	 * @return the radius of the blur in the horizontal direction
     * @see #setRadius
	 */
	public int getRadius() {
		return hRadius;
	}
	
	/**
	 * Set the number of iterations the blur is performed.
	 * @param iterations the number of iterations
     * @min-value 0
     * @see #getIterations
	 */
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}
	
	/**
	 * Get the number of iterations the blur is performed.
	 * @return the number of iterations
     * @see #setIterations
	 */
	public int getIterations() {
		return iterations;
	}
	
	public String toString() {
		return "Blur/Box Blur...";
	}
}
