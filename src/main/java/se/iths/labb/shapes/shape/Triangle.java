package se.iths.labb.shapes.shape;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Triangle {
}
/*
package com.paintcnlabb.labb3;

        import javafx.scene.canvas.GraphicsContext;
        import javafx.scene.paint.Color;

public class Triangle extends Shape {

    double[] xcoords = new double[3];
    double[] ycoords = new double[3];

    public Triangle(Color currentColor, double size, double x, double y) {
        super(currentColor, size, x, y);
    }


    @Override
    public void draw(GraphicsContext context) {

        double sizeSquarded = getSize() * getSize();
        double halfSizeSquared = (getSize()/2)*(getSize()/2);
        double hight = Math.sqrt(sizeSquarded-halfSizeSquared);


        xcoords[0] = getX();
        ycoords[0] = getY()-hight*0.67;
        xcoords[1] = getX()+(getSize()/2);
        ycoords[1] = getY()+hight*0.33;
        xcoords[2] = getX()-(getSize()/2);
        ycoords[2] = getY()+hight*0.33;

        context.setFill(getColor());
        context.fillPolygon(xcoords,ycoords,3);


    }

    double sign (Double x1, Double y1,Double x2, Double y2, Double x3, Double y3 ) {
        return (x1 - x3) * (y2 - y3) - (x2 - x3) * (y1 - y3);
    }



    @Override
    public boolean isInsideArea(double x, double y) {

        var distanceX = x - xcoords[2];
        var distanceY = y - ycoords[2];
        var distanceX21 = xcoords[2]-xcoords[1];
        var distanceY12 = ycoords[1]-ycoords[2];
        var D = distanceY12*(xcoords[0]-xcoords[2]) + distanceX21*(ycoords[0]-ycoords[2]);
        var s = distanceY12*distanceX + distanceX21*distanceY;
        var t = ((ycoords[2] - ycoords[0]) * distanceX) + ((xcoords[0] - xcoords[2]) * distanceY);

        if (D<0)
            return s<=0 && t<=0 && s+t>=D;
        return s>=0 && t>=0 && s+t<=D;





        */
/*

        var x1 = xcoords[0];
        var x2 = xcoords[1];
        var x3 = xcoords[2];
        var y1 = xcoords[0];
        var y2 = xcoords[1];
        var y3 = xcoords[2];


        var areaOrig = Math.abs((x2-x1)*(y3-y1) - (x3-x1)*(y2-y1)) ;

        // get the area of 3 triangles made between the point
        // and the corners of the triangle
        var area1 =   Math.abs((x1-x)*(y2-y) - (x2-x)*(y1-y)) ;
        var area2 =   Math.abs((x2-x)*(y3-y) - (x3-x)*(y2-y)) ;
        var area3 =   Math.abs((x3-x)*(y1-y) - (x1-x)*(y3-y)) ;

        // if the sum of the three areas equals the original,
        // we're inside the triangle!

        return area1 + area2 + area3 == areaOrig;

         *//*



    }

}
*/
