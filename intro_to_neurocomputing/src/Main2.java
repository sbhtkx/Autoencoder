
public class Main2 {
	static final int size = 32;
	static final int trainSetSize = 512 * 512 / 32;
	static final int numberOfIterations = trainSetSize * 1000;
	static final int printImageIteration = trainSetSize * 10;
	
	public static void main(String[] args) {
		
		try {
			PPMImage p = new PPMImage("images\\lena_gray_p3.ppm", trainSetSize);
			Net network = new Net(size * size);
			System.out.println("start training.");
			for (int i = 0; i < numberOfIterations; i++) {
				if((i+1) % 10000 == 0) {
					System.out.println("round " + (i+1));
				}
				double[] arr = p.getNextRandomWindowTrain(size, size);
				network.train(arr);
				
				// write the image to file
				if(i % printImageIteration == 0) {
					GrayScaleImage g = null;
					for (int k = 0; k < 512; k += size) {
						for (int j = 0; j < 512; j += size) {
							p.getSubImageGrey(k, j, size, size).writeGrayScale("images\\real_input.ppm");
							double[] arr2 = p.getSubImageGrey(k, j, size, size).asArray();
							double[] output = network.test(arr2);
							GrayScaleImage g2 = new GrayScaleImage(output, size);
							if (g == null) {
								g = g2;
							}
							else {
								g.append(g2, 512);
							}
						}
					}

					g.writeGrayScale("images\\output" + (i/printImageIteration) +".ppm");
					System.out.println("write to file: output" + (i/printImageIteration) +".ppm");
				}
			}
			System.out.println("finish training.");
			
			// write the image to file
			GrayScaleImage g = null;
			for (int i = 0; i < 512; i += size) {
				for (int j = 0; j < 512; j += size) {
					p.getSubImageGrey(i, j, size, size).writeGrayScale("images\\real_input.ppm");
					double[] arr = p.getSubImageGrey(i, j, size, size).asArray();
					double[] output = network.test(arr);
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

			System.out.println("finish");
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

}
