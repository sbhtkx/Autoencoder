import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PPMImage {

	String magicNumber; // A "magic number" for identifying the file type
	int width; // Width of the image
	int height; // Height of the image
	int maxColorVal; // Maximum color value
	int[][][] rgb;
	int[][] grayScale;
	//    char *threeChan; // A series of rows and columns (raster) that stores important binary image data


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
			i = height - i;
		}
		if (j + width >= this.width) {
			j = j - width;
		}
		
		int[][] ans = new int[height][width];
		for (int k = i, l = 0; k < i + height; k++, l++) {
//			System.out.println("k: " + k, );
			ans[l] = Arrays.copyOfRange(grayScale[k], j, j + width);
		}
		return new GrayScaleImage(ans, maxColorVal);
	}



	public static void main(String[] args) {
		try{
			PPMImage p = new PPMImage("C:\\Users\\sbhtk\\git_clones\\ppm-converter\\lena_gray_p3.ppm");
			System.out.println(p.magicNumber);
			System.out.println(p.height);
			System.out.println(p.width);
			System.out.println(p.maxColorVal);
			GrayScaleImage window = p.getSubImageGrey(0, 0, 400, 400);
			window.writeGrayScale("C:\\Users\\sbhtk\\git_clones\\ppm-converter\\window.ppm");
			System.out.println("fin");
		}catch (Exception e) {
//			System.out.println("error...");
			e.printStackTrace();
		}

	}
	
	
	

}

