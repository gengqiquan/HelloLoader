/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sunshine.view.library.utils;

import java.math.BigDecimal;


// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbMathUtil.java 
 * 描述：数学处理类.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-01-17 下午11:52:13
 */
public class MathUtil {
  public static BigDecimal round(double number, int decimal){
    return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_HALF_UP);
  }
  
  public static String byte2HexStr(byte[] b, int length){
    String hs = "";
    String stmp = "";
    for (int n = 0; n < length; ++n) {
      stmp = Integer.toHexString(b[n] & 0xFF);
      if (stmp.length() == 1)
        hs = hs + "0" + stmp;
      else {
        hs = hs + stmp;
      }
      hs = hs + ",";
    }
    return hs.toUpperCase();
  } 
  
	public static char binaryToHex(int binary) {
		char ch = ' ';
		switch (binary){
		case 0:
			ch = '0';
			break;
		case 1:
			ch = '1';
			break;
		case 2:
			ch = '2';
			break;
		case 3:
			ch = '3';
			break;
		case 4:
			ch = '4';
			break;
		case 5:
			ch = '5';
			break;
		case 6:
			ch = '6';
			break;
		case 7:
			ch = '7';
			break;
		case 8:
			ch = '8';
			break;
		case 9:
			ch = '9';
			break;
		case 10:
			ch = 'a';
			break;
		case 11:
			ch = 'b';
			break;
		case 12:
			ch = 'c';
			break;
		case 13:
			ch = 'd';
			break;
		case 14:
			ch = 'e';
			break;
		case 15:
			ch = 'f';
			break;
		default:
			ch = ' ';
		}
		return ch;
	}
	
	
    public static int[][] arrayToMatrix(int[] m, int width, int height) {
        int[][] result = new int[height][width];  
        for (int i = 0; i < height; i++) {  
            for (int j = 0; j < width; j++) {  
                int p = j * height + i;  
                result[i][j] = m[p];  
            }  
        }  
        return result;  
    }  

    public static double[] matrixToArray(double[][] m) {
        int p = m.length * m[0].length;  
        double[] result = new double[p];  
        for (int i = 0; i < m.length; i++) {  
            for (int j = 0; j < m[i].length; j++) {  
                int q = j * m.length + i;  
                result[q] = m[i][j];  
            }  
        }  
        return result;  
    }  

    public static double[] intToDoubleArray(int[] input) {
        int length = input.length;  
        double[] output = new double[length];  
        for (int i = 0; i < length; i++){  
            output[i] = Double.valueOf(String.valueOf(input[i]));  
        }
        return output;  
    }  
    
    public static double[][] intToDoubleMatrix(int[][] input) {
        int height = input.length;  
        int width = input[0].length;  
        double[][] output = new double[height][width];  
        for (int i = 0; i < height; i++) {  
            // 列   
            for (int j = 0; j < width; j++) {  
                // 行   
                output[i][j] = Double.valueOf(String.valueOf(input[i][j]));  
            }  
        }  
        return output;  
    }  

    public static int average(int[] pixels) {
		float m = 0;
		for (int i = 0; i < pixels.length; ++i) {
			m += pixels[i];
		}
		m = m / pixels.length;
		return (int) m;
	}
    
    public static int average(double[] pixels) {
		float m = 0;
		for (int i = 0; i < pixels.length; ++i) {
			m += pixels[i];
		}
		m = m / pixels.length;
		return (int) m;
	}
    
    public boolean pointAtSLine(double x,double y,double x1,double y1,double x2,double y2){
        double result = ( x - x1 ) * ( y2 - y1 ) - ( y - y1 ) * ( x2 - x1 );
		return result == 0;
    }
    
    
    public static boolean pointAtELine(double x,double y,double x1,double y1,double x2,double y2){
    	double result = ( x - x1 ) * ( y2 - y1 ) - ( y - y1 ) * ( x2 - x1 );
    	if(result==0){
			return x >= Math.min(x1, x2) && x <= Math.max(x1, x2)
					&& y >= Math.min(y1, y2) && y <= Math.max(y1, y2);
    	}else{
    		return false;
    	}
    }
    
    public  static boolean LineAtLine(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4){
	    double k1 = ( y2-y1 )/(x2-x1);
	    double k2 = ( y4-y3 )/(x4-x3);
		if(k1==k2){
			//System.out.println("平行线");
			return false;
		}else{
		  double x = ((x1*y2-y1*x2)*(x3-x4)-(x3*y4-y3*x4)*(x1-x2))/((y2-y1)*(x3-x4)-(y4-y3)*(x1-x2));
		  double y = ( x1*y2-y1*x2 - x*(y2-y1) ) / (x1-x2);
		  //System.out.println("直线的交点("+x+","+y+")");
		  return true;
		}
	}
    
    public static boolean eLineAtELine(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4){
		    double k1 = ( y2-y1 )/(x2-x1);
		    double k2 = ( y4-y3 )/(x4-x3);
			if(k1==k2){
				//System.out.println("平行线");
				return false;
			}else{
			  double x = ((x1*y2-y1*x2)*(x3-x4)-(x3*y4-y3*x4)*(x1-x2))/((y2-y1)*(x3-x4)-(y4-y3)*(x1-x2));
			  double y = ( x1*y2-y1*x2 - x*(y2-y1) ) / (x1-x2);
			  //System.out.println("直线的交点("+x+","+y+")");
				//System.out.println("交点（"+x+","+y+"）在线段上");
//System.out.println("交点（"+x+","+y+"）不在线段上");
				return x >= Math.min(x1, x2) && x <= Math.max(x1, x2)
						&& y >= Math.min(y1, y2) && y <= Math.max(y1, y2)
						&& x >= Math.min(x3, x4) && x <= Math.max(x3, x4)
						&& y >= Math.min(y3, y4) && y <= Math.max(y3, y4);
	       }
	}
    public static boolean eLineAtLine(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4){
		    double k1 = ( y2-y1 )/(x2-x1);
		    double k2 = ( y4-y3 )/(x4-x3);
			if(k1==k2){
				//System.out.println("平行线");
				return false;
			}else{
			  double x = ((x1*y2-y1*x2)*(x3-x4)-(x3*y4-y3*x4)*(x1-x2))/((y2-y1)*(x3-x4)-(y4-y3)*(x1-x2));
			  double y = ( x1*y2-y1*x2 - x*(y2-y1) ) / (x1-x2);
			  //System.out.println("交点("+x+","+y+")");
				//System.out.println("交点（"+x+","+y+"）在线段上");
//System.out.println("交点（"+x+","+y+"）不在线段上");
				return x >= Math.min(x1, x2) && x <= Math.max(x1, x2)
						&& y >= Math.min(y1, y2) && y <= Math.max(y1, y2);
		}
	}
    public static boolean pointAtRect(double x,double y,double x1,double y1,double x2,double y2){
		//System.out.println("点（"+x+","+y+"）在矩形内上");
//System.out.println("点（"+x+","+y+"）不在矩形内上");
		return x >= Math.min(x1, x2) && x <= Math.max(x1, x2) && y >= Math.min(y1, y2) && y <= Math.max(y1, y2);
	}
    
    public static boolean rectAtRect(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4){
		//System.out.println("矩形在矩形内");
//System.out.println("矩形不在矩形内");
		return x1 >= Math.min(x3, x4) && x1 <= Math.max(x3, x4)
				&& y1 >= Math.min(y3, y4) && y1 <= Math.max(y3, y4)
				&& x2 >= Math.min(x3, x4) && x2 <= Math.max(x3, x4)
				&& y2 >= Math.min(y3, y4) && y2 <= Math.max(y3, y4);
	}
    public static boolean circleAtRect(double x,double y,double r,double x1,double y1,double x2,double y2){
		//圆心在矩形内   
		if(x >= Math.min(x1, x2) && x <= Math.max(x1,x2) 
						  && y >= Math.min(y1, y2) && y <= Math.max(y1,y2)){
		//圆心到4条边的距离		  
        double l1= Math.abs(x-x1);
		double l2= Math.abs(y-y2);
		double l3= Math.abs(x-x2);
		double l4= Math.abs(y-y2);
			//System.out.println("圆在矩形内");
//System.out.println("圆不在矩形内");
			return r <= l1 && r <= l2 && r <= l3 && r <= l4;
    	 
       }else{
    	     //System.out.println("圆不在矩形内");
    	    return false;
	   }
	}

    public static double getDistance(double x1,double y1,double x2,double y2) {
    	double x = x1 - x2;  
    	double y = y1 - y2;  
        return Math.sqrt(x * x + y * y);  
    }  
    
	public static boolean isRectCollision(float x1, float y1, float w1,
			float h1, float x2, float y2, float w2, float h2) {
		if (x2 > x1 && x2 > x1 + w1) {
			return false;
		} else if (x2 < x1 && x2 < x1 - w2) {
			return false;
		} else if (y2 > y1 && y2 > y1 + h1) {
			return false;
		} else return !(y2 < y1 && y2 < y1 - h2);
	}
 
}
