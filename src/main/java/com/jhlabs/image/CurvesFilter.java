/*
** Copyright 2005 Huxtable.com. All rights reserved.
*/

package com.jhlabs.image;

import java.awt.*;
import java.awt.image.*;

public class CurvesFilter extends TransferFilter {

	private Curve[] curves = new Curve[1];
	
    public CurvesFilter() {
        curves = new Curve[3];
        curves[0] = new Curve();
        curves[1] = new Curve();
        curves[2] = new Curve();
    }
    
	protected void initialize() {
		initialized = true;
		if ( curves.length == 1 )
            rTable = gTable = bTable = curves[0].makeTable();
        else {
            rTable = curves[0].makeTable();
            gTable = curves[1].makeTable();
            bTable = curves[2].makeTable();
        }
	}

	public void setCurve( Curve curve ) {
        curves = new Curve[] { curve };
		initialized = false;
	}
	
	public void setCurves( Curve[] curves ) {
		if ( curves == null || (curves.length != 1 && curves.length != 3) )
            throw new IllegalArgumentException( "Curves must be length 1 or 3" );
        this.curves = curves;
		initialized = false;
	}
	
	public Curve[] getCurves() {
		return curves;
	}

	public String toString() {
		return "Colors/Curves...";
	}

}

