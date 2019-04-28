import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PPMImage {

	String magicNumber; // A "magic number" for identifying the file type
	int width;  // Width of the image
	int height;  // Height of the image
	int maxColorVal;  // Maximum color value
	int[][][] rgb;  // matrix that contains the RGB value for each pixel 
	int[][] grayScale;  // matrix that contains the gray value for each pixel
	
	boolean [] i_stock, j_stock;  // to remember which indexes we already used in our training if we want
	int is_left, js_left;

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
//			System.out.println("k: " + k, );
			ans[l] = Arrays.copyOfRange(grayScale[k], j, j + width);
		}
		return new GrayScaleImage(ans, maxColorVal);
	}
	
	public GrayScaleImage getRandomWindow(int width, int height) {
		if(is_left <= 0 || js_left <= 0) {
			i_stock = new boolean [this.height];
			j_stock = new boolean [this.width];
			is_left = this.height;
			js_left = this.width;
		}

		Random rand = new Random();
		int i, j;
		do {
			i = rand.nextInt(this.height);
		} while (i_stock[i] == false);
		
		do {
		j = rand.nextInt(this.width);
		} while (j_stock[j] == false);
		
		i_stock[i] = true;
		j_stock[j] = true;
		is_left--;
		js_left--;
				
		return getSubImageGrey(i, j , width, height);
	}



	public static void main(String[] args) {
		boolean[] bbb = new boolean[10];
		System.out.println(Arrays.toString(bbb));
		try{
			PPMImage p = new PPMImage("images\\lena_gray_p3.ppm");
			System.out.println(p.magicNumber);
			System.out.println(p.height);
			System.out.println(p.width);
			System.out.println(p.maxColorVal);
			GrayScaleImage window = p.getSubImageGrey(400, 400, 200, 200);
			window.writeGrayScale("images\\window.ppm");
			System.out.println("fin");
		}catch (Exception e) {
//			System.out.println("error...");
			e.printStackTrace();
		}

	}
	
	
	

}

