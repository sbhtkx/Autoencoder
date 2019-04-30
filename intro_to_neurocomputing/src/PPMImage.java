import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

public class PPMImage {

	String magicNumber; // A "magic number" for identifying the file type
	int width;  // Width of the image
	int height;  // Height of the image
	int maxColorVal;  // Maximum color value
	int[][][] rgb;  // matrix that contains the RGB value for each pixel 
	int[][] grayScale;  // matrix that contains the gray value for each pixel
	
	Stack<Integer> i_stack_train, j_stack_train;  // to remember which indexes we already used in our training if we want
	int trainSetSize = 100, testSetSize = 50;
	private Stack<Integer> i_stack_test;
	private Stack<Integer> j_stack_test;

	public PPMImage(String fileName) throws IOException{

		File file = new File(fileName);     	  
		BufferedReader br = new BufferedReader(new FileReader(file));

		magicNumber = br.readLine();

		String[] dimensions = br.readLine().split("\\s+");
		width = Integer.parseInt(dimensions[0]);
		height = Integer.parseInt(dimensions[1]);

		maxColorVal = Integer.parseInt(br.readLine());

		rgb = new int[width][height][3];
		grayScale = new int[width][height];

		String st; 
		int i = 0, row = 0, column = 0;
		while ((st = br.readLine()) != null && i < height * width *3) {
			String[] valuesArr = st.trim().split("\\s+");
			List<Integer> values = new ArrayList<>();
			for (String v : valuesArr) {
				int x = Integer.parseInt(v);
				values.add(x);
			}
			for (int j = 0; j < values.size(); j++) {
				rgb[row][column][i%3] = values.get(j);
				i++;
				if(i % 3 == 0) {
					column++;
				}
				if(column >= width) {
					row++;
					column = 0;
				}
			}
		} 
		br.close();
		for (i = 0; i < rgb.length; i++) {
			for (int j = 0; j < rgb[0].length; j++) {
				grayScale[i][j]  = (int)( (rgb[i][j][0] * 0.299) + (rgb[i][j][1] * 0.587) + (rgb[i][j][2] * 0.114));
			}
			
		}
	}
	
	public GrayScaleImage getSubImageGrey(int i, int j, int width, int height){
		if (i + height >= this.height) {
			i = this.height - height;
		}
		if (j + width >= this.width) {
			System.out.println("j1: " + j);
			j = this.width - width;
			System.out.println("width: " + width);
			System.out.println("j2: " + j);
		}
		
		int[][] ans = new int[height][width];
		for (int k = i, l = 0; k < i + height; k++, l++) {
			ans[l] = Arrays.copyOfRange(grayScale[k], j, j + width);
		}
		return new GrayScaleImage(ans, maxColorVal);
	}
	
	public double[] getNextRandomWindowTrain(int width, int height) {
		if(i_stack_train == null || j_stack_train == null || i_stack_train.isEmpty() || j_stack_train.isEmpty()) {
			resetRandom(trainSetSize, testSetSize, width, height);
		}
		return getSubImageGrey(i_stack_train.pop(), j_stack_train.pop() , width, height).asArray();
	}
	
	public double[] getNextRandomWindowTest(int width, int height) {
		if(i_stack_train == null || j_stack_train == null || i_stack_train.isEmpty() || j_stack_train.isEmpty()) {
			resetRandom(trainSetSize, testSetSize, width, height);
		}
		return getSubImageGrey(i_stack_test.pop(), j_stack_test.pop() , width, height).asArray();
	}

	private void resetRandom(int trainSetSize, int testSetSize, int width, int height) {
		int[] i_arr = new int[this.height - height];
		int[] j_arr = new int[this.width - width];
		
		for (int i = 0; i < i_arr.length; i++) {
			i_arr[i] = i;
		}
		for (int j = 0; j < j_arr.length; j++) {
			j_arr[j] = j;
		}
		shuffleArray(i_arr);
		shuffleArray(j_arr);
		
		i_stack_train = new Stack<>();
		i_stack_test = new Stack<>();
		for (int i = 0; i < i_arr.length && i < trainSetSize + testSetSize; i++) {
			if(i < trainSetSize)
				i_stack_train.push(i_arr[i]);
			else
				i_stack_test.push(i_arr[i]);
		}
		j_stack_train = new Stack<>();
		j_stack_test = new Stack<>();
		for (int j = 0; j < j_arr.length && j < trainSetSize + testSetSize; j++) {
			if(j < trainSetSize)
				j_stack_train.push(j_arr[j]);
			else
				j_stack_test.push(j_arr[j]);
		}
		
	}
	
	static void shuffleArray(int[] ar){
	    Random rnd = ThreadLocalRandom.current();
	    for (int i = ar.length - 1; i > 0; i--)
	    {
	      int index = rnd.nextInt(i + 1);
	      // Simple swap
	      int a = ar[index];
	      ar[index] = ar[i];
	      ar[i] = a;
	    }
	  }
	
}

