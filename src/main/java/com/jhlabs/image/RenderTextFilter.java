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

/**
 * A filter which renders text onto an image.
 */
public class RenderTextFilter extends AbstractBufferedImageOp {

	private String text;
	private Font font;
    private Paint paint;
	private Composite composite;
    private AffineTransform transform;
	
	public RenderTextFilter() {
	}
	
	public RenderTextFilter( String text, Font font, Paint paint, Composite composite, AffineTransform transform ) {
		this.text = text;
		this.font = font;
		this.composite = composite;
		this.paint = paint;
		this.transform = transform;
	}
	
	public void setComposite( String text ) {
		this.text = text;
	}
    
    public String getText() {
        return text;
    }
	
	public void setComposite( Composite composite ) {
		this.composite = composite;
	}
    
    public Composite getComposite() {
        return composite;
    }
	
	public BufferedImage filter( BufferedImage src, BufferedImage dst ) {
        if ( dst == null )
            dst = createCompatibleDestImage( src, null );

		Graphics2D g = dst.createGraphics();
        g.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
        if ( font != null )
            g.setFont( font );
        if ( transform != null )
            g.setTransform( transform );
        if ( composite != null )
            g.setComposite( composite );
        if ( paint != null )
            g.setPaint( paint );
        if ( text != null )
            g.drawString( text, 10, 100 );
        g.dispose();
		return dst;
	}
}
