
public class Net {
	Node[] firstLayer, secondLayer, thirdLayer;
	public Net(Net other){
		firstLayer = new Node[other.firstLayer.length];
		secondLayer = new Node[other.secondLayer.length];
		thirdLayer = new Node[other.thirdLayer.length];
		for (int i = 0; i < firstLayer.length; i++) {
			firstLayer[i]=new Node(other.firstLayer[i], null);
		}
		for (int i = 0; i < secondLayer.length; i++) {
			secondLayer[i]=new Node(other.secondLayer[i], firstLayer);
		}
		for (int i = 0; i < thirdLayer.length; i++) {
			thirdLayer[i]=new Node(other.thirdLayer[i], secondLayer);
		}
	}
	public Net(int num) {
		firstLayer = new Node[num];
		secondLayer = new Node[num / 2 + 1];
		thirdLayer = new Node[num];
		for (int i = 0; i < num; i++) {
			firstLayer[i] = new Node();
			if (i < num / 2 + 1)
				secondLayer[i] = new Node(firstLayer);
			thirdLayer[i] = new Node(secondLayer);
		}
	}

	public double[] train(double[] input) {
		if (input.length != firstLayer.length) {
			try {
				throw new Exception("wrong input length");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		double[] output = test(input);
		
		for (int i = 0; i < input.length; i++) {
					thirdLayer[i].calculateErrorAndUpdate(input[i] - thirdLayer[i].output);
		}

		for (int i = 0; i < input.length / 2 + 1; i++) {
			secondLayer[i].calculateErrorAndUpdate(0);
		}
		return output;

	}
	public double[] test(double[] input){
		for (int i = 0; i < input.length; i++) {
			firstLayer[i].input(input[i]);
		}

		for (int i = 0; i < input.length / 2 + 1; i++) {
			secondLayer[i].calculateOutput();
		}

		for (int i = 0; i < input.length; i++) {
			thirdLayer[i].calculateOutput();
		}
		double[] ans = new double[input.length];
		for (int i = 0; i < ans.length; i++) {
			ans[i]=(thirdLayer[i].output);
		}
		return ans;
	}
}
