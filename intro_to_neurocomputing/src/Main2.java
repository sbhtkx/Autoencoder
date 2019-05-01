
public class Main2 {
	static final int size = 32;
	public static void main(String[] args) {
		
		try {
			PPMImage p = new PPMImage("images\\lena_gray_p3.ppm");
			Net network = new Net(size * size);
			System.out.println("start training.");
			for (int i = 0; i < 50000 * 100; i++) {
				if((i+1)%10000==0) {
					System.out.println("round " + (i+1));
				}
				double[] arr = p.getNextRandomWindowTrain(size, size);
				network.train(arr);
				
				if(i % 1000000 == 0) {
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
								System.out.println("g.height: " + g.height + ", g.width: " + g.width);
							}
						}
					}

					g.writeGrayScale("images\\output" + (i/100000) +".ppm");

				}
			}
			System.out.println("finish training.");
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
						System.out.println("g.height: " + g.height + ", g.width: " + g.width);
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
