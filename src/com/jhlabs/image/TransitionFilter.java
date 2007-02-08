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
import java.lang.reflect.*;
import java.beans.*;
import java.util.*;

/**
 * A filter which uses another filter to perform a transition.
 * e.g. to create a blur transition, you could write: new TransitionFilter( new BoxBlurFilter(), "radius", 0, 100 );
 */
public class TransitionFilter extends AbstractBufferedImageOp {
	
	private float transition = 0;
	private BufferedImage destination;
    protected BufferedImageOp filter;
    private String property;
    private Method method;
    protected float minValue, maxValue;

	private TransitionFilter() {
	}

	public TransitionFilter( BufferedImageOp filter, String property, float minValue, float maxValue ) {
		this.filter = filter;
		this.property = property;
		this.minValue = minValue;
		this.maxValue = maxValue;
		try {
			BeanInfo info = Introspector.getBeanInfo( filter.getClass() );
            PropertyDescriptor[] pds = info.getPropertyDescriptors();
            for ( int i = 0; i < pds.length; i++ ) {
                PropertyDescriptor pd = pds[i];
                if ( property.equals( pd.getName() ) ) {
                    method = pd.getWriteMethod();
                    break;
                }
            }
            if ( method == null )
                throw new IllegalArgumentException( "No such property in object: "+property );
		}
		catch (IntrospectionException e) {
            throw new IllegalArgumentException( e.toString() );
		}
	}

	/**
	 * Set the transition of the image in the range 0..1.
	 * *arg transition The transition
	 */
	public void setTransition( float transition ) {
		this.transition = transition;
	}
	
	public float getTransition() {
		return transition;
	}
	
	public void setDestination( BufferedImage destination ) {
		this.destination = destination;
	}
	
	public BufferedImage getDestination() {
		return destination;
	}
	
/*
	public void setFilter( BufferedImageOp filter ) {
		this.filter = filter;
	}
	
	public int getFilter() {
		return filter;
	}
*/
	
	public void prepareFilter( float transition ) {
        try {
            method.invoke( filter, new Object[] { new Float( transition ) } );
        }
        catch ( Exception e ) {
            throw new IllegalArgumentException("Error setting value for property: "+property);
        }
	}
	
    public BufferedImage filter( BufferedImage src, BufferedImage dst ) {
        if ( dst == null )
            dst = createCompatibleDestImage( src, null );
		if ( destination == null )
			return dst;

		float itransition = 1-transition;

		Graphics2D g = dst.createGraphics();
		if ( transition != 1 ) {
            float t = minValue + transition * ( maxValue-minValue );
			prepareFilter( t );
            g.drawImage( src, filter, 0, 0 );
		}
		if ( transition != 0 ) {
            g.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, transition ) );
            float t = minValue + itransition * ( maxValue-minValue );
			prepareFilter( t );
            g.drawImage( destination, filter, 0, 0 );
		}
		g.dispose();

        return dst;
    }

	public String toString() {
		return "Transitions/Transition...";
	}
}
