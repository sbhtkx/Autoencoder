import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class GrayScaleImage{
	int[][] grayValues;
	int height, width, maxColorVal = 256;

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

	public GrayScaleImage(double[] grayValuesArrNormalized, int width) {
		int[] grayValuesArr = new int[grayValuesArrNormalized.length];
		for (int i = 0; i < grayValuesArrNormalized.length; i ++) {
			int v = normalizedToGrayScale(grayValuesArrNormalized[i]);
			grayValuesArr[i] = v;
		}
		grayValues = new int[grayValuesArr.length/width][width];
		int max = 0;
		for (int i = 0; i < grayValuesArr.length; i++) {
			if(max < grayValuesArr[i]) {
				max = grayValuesArr[i];
			}
			grayValues[i/width][i%width] = grayValuesArr[i];
		}
		//		maxColorVal = max;
		this.width = width;
		this.height = grayValuesArr.length / width;
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

	/**
	 * 
	 * @return the matrix flattened to array and each number is normalaized
	 */
	public double[] asArray() {
		double ans[] = new double[grayValues.length *grayValues[0].length];
		for(int i = 0; i < grayValues.length; i++) {
			for(int j = 0; j < grayValues[i].length; j++) {
				int number = grayValues[i][j];
				ans[i*grayValues[i].length+j] = normalize(number);
			}
		}
		return ans;
	}

	public static double normalize(int number) {		
		return (double)number / 256;
	}

	private int normalizedToGrayScale(double n) {
		return (int) (n * 256);
	}


	public void append(GrayScaleImage other, int width) {
		// find an empty cell (that contains '-1')
		int startI = -1, startJ = -1;
		boolean flag = true;
		for (int i = 0; i < grayValues.length && flag; i++) {
			for (int j = 0; j < grayValues[i].length && flag; j++) {
				if (grayValues[i][j] == -1) {
					startI = i;
					startJ = j;
					if(startJ + other.width <= this.width && startI + other.height <= this.height) {
						flag = false;
					}
					else {
					}
				}
			}
		}
		// case 1: there is enough empty space
		if(startI != -1 && startJ + other.width <= this.width && startI + other.height <= this.height) {
			for (int i = 0; i < other.grayValues.length; i++) {
				for (int j = 0; j < other.grayValues[i].length; j++) {
					grayValues[startI + i][startJ + j] = other.grayValues[i][j];
				}
			}
		}
		// case 2: need to expand the matrix horizontally
		else if (this.width + other.width <= width) {
			int[][] newGrayValues = new int[this.height][width];
			// marking all the not yet updated cells with '-1'
			for (int i = 0; i < newGrayValues.length; i++) {
				for (int j = 0; j < newGrayValues[i].length; j++) {
					newGrayValues[i][j] = -1;
				}
			}
			// copy to the new matrix
			for (int i = 0; i < grayValues.length; i++) {
				for (int j = 0; j < grayValues[i].length; j++) {
					newGrayValues[i][j] = grayValues[i][j];
				}
			}
			// add the new pixels
			for (int i = 0; i < other.grayValues.length; i++) {
				for (int j = 0; j < other.grayValues[i].length; j++) {
					newGrayValues[i][this.width+j] = other.grayValues[i][j];
				}
			}
			grayValues = newGrayValues;
			this.width = grayValues[0].length;
		}
		// case 3: need to expand the matrix vertically
		else {
			int[][] newGrayValues = new int[this.height + other.height][width];
			// marking all the not yet updated cells with '-1'
			for (int i = 0; i < newGrayValues.length; i++) {
				for (int j = 0; j < newGrayValues[i].length; j++) {
					newGrayValues[i][j] = -1;
				}
			}
			// copy to the new matrix
			for (int i = 0; i < grayValues.length; i++) {
				for (int j = 0; j < grayValues[i].length; j++) {
					newGrayValues[i][j] = grayValues[i][j];
				}
			}
			// add the new pixels
			for (int i = 0; i < other.grayValues.length; i++) {
				for (int j = 0; j < other.grayValues[i].length; j++) {
					newGrayValues[this.height+i][j] = other.grayValues[i][j];
				}
			}
			grayValues = newGrayValues;
			this.height = grayValues.length;
		}









		// case 2: find a cell with '-1' and add there the image
		//		else {

		if(startI != -1 && startI + other.width < this.width && startJ + other.height < -99999) {
			for (int i = 0; i < other.grayValues.length; i++) {
				for (int j = 0; j < other.grayValues[i].length; j++) {
					grayValues[startI + i][startJ + j] = other.grayValues[i][j];
				}
			}
		}
		else {

		}
	}



	//	}

}
