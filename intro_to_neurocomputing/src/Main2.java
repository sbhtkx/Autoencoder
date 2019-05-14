import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Main2 {
	static final int size = 16;
	static final int SetSize = 1000 ;
	static final int printImageIteration = SetSize * 10;




	public static void main(String[] args) {

		try {
			BufferedWriter graphPointsWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("images\\graph.csv")));
			PPMImage train = new PPMImage("images\\lena_gray_p3.ppm", SetSize);
			PPMImage test = new PPMImage("images\\lena_gray_p3.ppm", SetSize);
			Net network = new Net(size * size);
			System.out.println("start training.");
			Net best = new Net(network);
			double bestNum= Integer.MAX_VALUE;check(best, test);
			int count =0;
			double chck = check(network, train);
			System.out.println("test set: "+ bestNum+", train set: "+ chck);
			graphPointsWriter.write("iterations,test set error,train set error\n");
			for (int i = 0;; i++) { // delete condition  i < 50000 
				if((i+1) % 3000 == 0) {
					System.out.println("round " + (i+1));
					double current = check(network, test);
					chck = check(network, train);
					System.out.println("test set: "+ current+", train set: "+ chck);
					graphPointsWriter.write((i+1) + "," + current + "," + chck + "\n");
					if(current < bestNum){
						count = 0;
						best = new Net(network);
						bestNum = current;
					}
					else{
						count ++;
						if (count > 20)
							break;
					}
				}
				double[] arr = train.getNextRandomWindowTrain(size, size);
				network.train(arr);

			}
			System.out.println("finish training.");
			graphPointsWriter.close();;
			// write the image to file
			GrayScaleImage g = null;
			for (int i = 0; i < 512; i += size) {
				for (int j = 0; j < 512; j += size) {
					train.getSubImageGrey(i, j, size, size).writeGrayScale("images\\real_input.ppm");
					double[] arr = train.getSubImageGrey(i, j, size, size).asArray();
					double[] output = best.test(arr);
					GrayScaleImage g2 = new GrayScaleImage(output, size);
					if (g == null) {
						g = g2;
					}
					else {
						g.append(g2, 512);
					}
				}
			}

			g.writeGrayScale("images\\output.ppm");

			System.out.println("finish1");

			String[] imagesNames = {"mcfaddin_1", "trip3", "sage_1_p3"};
			for (String imageName : imagesNames) {
				feedImage(best, "images\\" + imageName + ".ppm");
			}
			System.out.println("finish all.");

		}catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void  feedImage(Net best, String imagePath) throws IOException {
		PPMImage ppmImage = new PPMImage(imagePath, 0);
		String grayPath = imagePath.substring(0, imagePath.indexOf(".ppm")) + "_gray.ppm";
		String outputPath = imagePath.substring(0, imagePath.indexOf(".ppm")) + "_output.ppm";
		ppmImage.getSubImageGrey(0, 0, ppmImage.width, ppmImage.height).writeGrayScale(grayPath);
		GrayScaleImage grayImage1 = null;
		for (int i = 0; i < ppmImage.height; i += size) {
			for (int j = 0; j < ppmImage.width; j += size) {
				double[] arr = ppmImage.getSubImageGrey(i, j, size, size).asArray();
				double[] output = best.test(arr);
				GrayScaleImage grayImage2 = new GrayScaleImage(output, size);
				if (grayImage1 == null) {
					grayImage1 = grayImage2;
				}
				else {
					grayImage1.append(grayImage2, ppmImage.width);
				}
			}
		}

		grayImage1.writeGrayScale(outputPath);
		
	}

	private static double check(Net net, PPMImage test) {
		double sum=0;
		for (int i = 0; i < SetSize; i++) {
			double[] arr = test.getNextRandomWindowTrain(size, size);
			double[] ans = net.test(arr);
			for (int j = 0; j < ans.length; j++) {
				sum+=Math.abs(arr[j]-ans[j]);
			}
		}
		return sum;
	}

}