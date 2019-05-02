
public class Main2 {
	static final int size = 16;
	static final int SetSize = 1000 ;
	static final int printImageIteration = SetSize * 10;
	
	public static void main(String[] args) {
		
		try {
			PPMImage train = new PPMImage("images\\lena_gray_p3.ppm", SetSize);
			PPMImage test = new PPMImage("images\\lena_gray_p3.ppm", SetSize);
			Net network = new Net(size * size);
			System.out.println("start training.");
			Net best = new Net(network);
			double bestNum= Integer.MAX_VALUE;check(best, test);
			int count =0;
			System.out.println("test set: "+ bestNum+", train set: "+ check(network, train));
			for (int i = 0;; i++) {
				if((i+1) % 3000 == 0) {
					System.out.println("round " + (i+1));
					double current = check(network, test);
					System.out.println("test set: "+ current+", train set: "+ check(network, train));
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
				
				// write the image to file
//				if(i % printImageIteration == 0) {
//					GrayScaleImage g = null;
//					for (int k = 0; k < 512; k += size) {
//						for (int j = 0; j < 512; j += size) {
//							train.getSubImageGrey(k, j, size, size).writeGrayScale("images\\real_input.ppm");
//							double[] arr2 = train.getSubImageGrey(k, j, size, size).asArray();
//							double[] output = network.test(arr2);
//							GrayScaleImage g2 = new GrayScaleImage(output, size);
//							if (g == null) {
//								g = g2;
//							}
//							else {
//								g.append(g2, 512);
//							}
//						}
//					}
//
//					g.writeGrayScale("images\\output" + (i/printImageIteration) +".ppm");
//
//				}
			}
			System.out.println("finish training.");
			
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

			System.out.println("finish");
		}catch (Exception e) {
			e.printStackTrace();
		}

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