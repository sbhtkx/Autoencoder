
public class Net {
	Node[] firstLayer, secondLayer, thirdLayer;

	public Net(int num) {
		firstLayer = new Node[num];
		secondLayer = new Node[num / 2 + 1];
		thirdLayer = new Node[num];
		for (int i = 0; i < num; i++) {
			firstLayer[i] = new Node();
			secondLayer[i] = new Node(firstLayer);
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
		
		for (int i = 0; i < input.length; i++) {
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
		
		for (int i = 0; i < input.length; i++) {
			secondLayer[i].calculateErrorAndUpdate(0);
		}

	}
}
