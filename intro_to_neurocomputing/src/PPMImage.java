import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class PPMImage {
	
	String magicNumber; // A "magic number" for identifying the file type
    int width; // Width of the image
    int height; // Height of the image
    int maxColorVal; // Maximum color value
//    char *threeChan; // A series of rows and columns (raster) that stores important binary image data
    
    
    public static String read(String fileName) throws IOException{
    	File file = new File(fileName); 
    	  
    	BufferedReader br = new BufferedReader(new FileReader(file)); 
    	  
    	String st; 
    	int k = 0;
    	while ((st = br.readLine()) != null) {
    		if(k<=4) {
    			System.out.println(st);
    			k++;
    		}
    	} 
    	br.close();
    	return null;	
    }
    

	public static void main(String[] args) {
		try{
			read("C:\\Users\\sbhtk\\git_clones\\ppm-converter\\lena_gray_p3.ppm");
		}catch (Exception e) {
			// TODO: handle exception
		}

	}

}
