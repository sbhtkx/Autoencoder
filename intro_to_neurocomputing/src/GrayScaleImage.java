import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

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
	
	public GrayScaleImage(int[] grayValues, int height, int width, int maxColorVal) {
		this.height = height;
		this.width = width;
		this.maxColorVal = maxColorVal;
		for (int i = 0; i < grayValues.length; i++) {
			this.grayValues[i/width][i%width] = grayValues[i];
		}
	}
	
	public int[] asArray() {
		int[] ans = new int[grayValues.length * grayValues[0].length];
		
		for (int i = 0; i < grayValues.length; i++) {
			for (int j = 0; j < grayValues[i].length; j++) {
				ans[i+j] = grayValues[i][j];
			}
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
		for(int row=0;row<rowdimension;row++){
			for(int column=0;column<columndimension;column++){
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
	
}