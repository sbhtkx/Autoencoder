
public class Net {
	Node[] firstLayer, secondLayer, thirdLayer;

	public Net(int num) {
		firstLayer = new Node[num];
		secondLayer = new Node[num / 2 + 1];
		thirdLayer = new Node[num];
		for (int i = 0; i < num; i++) {
			firstLayer[i] = new Node();
			if(i < num / 2 + 1) {
				secondLayer[i] = new Node(firstLayer);
			}
			thirdLayer[i] = new Node(secondLayer);
		}
	}

	public void train(int[] input) {
		if (input.length != firstLayer.length){
			try {
				throw new Exception("wrong input length");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < input.length; i++) {
			firstLayer[i].input(input[i]);
		}

		for (int i = 0; i < input.length / 2 + 1; i++) {
			secondLayer[i].calculateOutput();
		}

		for (int i = 0; i < input.length; i++) {
			thirdLayer[i].calculateOutput();
		}

		for (int i = 0; i < input.length; i++) {
			if(thirdLayer[i].output > 0.5){
				if(input[i] == 0)
					thirdLayer[i].calculateErrorAndUpdate(0.5 - thirdLayer[i].output);
			}else{
				if(input[i] == 1)
					thirdLayer[i].calculateErrorAndUpdate(0.5 - thirdLayer[i].output);
			}

		}

		for (int i = 0; i < input.length / 2 + 1; i++) {
			secondLayer[i].calculateErrorAndUpdate(0);
		}

	}
	
	public static void main(String[] args) {
		try {
		PPMImage p = new PPMImage("images\\lena_gray_p3.ppm");
		Net network = new Net(256);
		for (int i = 0; i < p.height * p.width * 0.5; i++) {
			network.train(p.getNextRandomWindow(16, 16));
		}
		
		System.out.println("fin");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
