import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class GrayScaleImage{
	int[][] grayValues;
	int height, width, maxColorVal;
	
	public GrayScaleImage(int [][] grayValues, int maxColorVal) {
		this.maxColorVal = maxColorVal;
		height = grayValues.length;
		width = grayValues[0].length;
		this.grayValues = new int[grayValues.length][grayValues[0].length];
		for (int i = 0; i < grayValues.length; i++) {
			for (int j = 0; j < grayValues[i].length; j++) {
				this.grayValues[i][j] = grayValues[i][j];
			}
		}
	}
	
	public GrayScaleImage(int[] grayValuesBits, int width) {
		int[] grayValuesArr = new int[grayValuesBits.length/8];
		for (int i = 0; i < grayValuesBits.length; i += 8) {
			int v = bitsArrayToInt(Arrays.copyOfRange(grayValuesBits, i, i + 8));
			grayValuesArr[i/8] = v;
		}
		grayValues = new int[grayValuesArr.length/width][width];
		int max = 0;
		for (int i = 0; i < grayValuesArr.length; i++) {
			if(max < grayValuesArr[i]) {
				max = grayValuesArr[i];
			}
			grayValues[i/width][i%width] = grayValuesArr[i];
		}
		maxColorVal = max;
		this.width = width;
		this.height = grayValuesArr.length / 16;
	}
	
	private int bitsArrayToInt(int[] arr) {
		int ans = 0;
		for (int i = 0; i < arr.length; i++) {
			ans += Math.pow(2, i);
		}
		return ans;
	}
	
	public void writeGrayScale(String filename) throws IOException{
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
		// write header
		int rowdimension = height;
		int columndimension = width;
		writer.write("P3");
		writer.newLine();
		writer.write(width+" "+height);
		writer.newLine();
		writer.write(Integer.toString(maxColorVal));
		writer.newLine();
		System.out.println("rowdimension: " + rowdimension);
		System.out.println("columndimension: " +columndimension);
		for(int row=0;row<rowdimension;row++){
			for(int column=0;column<columndimension;column++){
//				System.out.println("row: " + row + ", column: " + column);
				writer.write(grayValues[row][column]+" ");
				writer.write(grayValues[row][column]+" ");
				writer.write(grayValues[row][column]+"");
				if(column < columndimension - 1)writer.write(" ");
			}
			writer.newLine();
		}
		writer.flush();
		writer.close();
	}

	/**
	 * 
	 * @return the matrix flattened to array and each number is expanded to eight 1-0 bits
	 */
	public double[] asArray() {
		System.out.println();
		double ans[] = new double[grayValues.length *grayValues[0].length*8];
	    for(int i = 0; i < grayValues.length; i++) {
	        for(int j = 0; j < grayValues[i].length; j++) {
	        	int number = grayValues[i][j];
	        	ans[i*grayValues[i].length+j] = normalize(number);
	        }
	    }
		return ans;
	}

	public static double normalize(int number) {
		// TODO Auto-generated method stub
		
		return number / 256;
	}
	
}
