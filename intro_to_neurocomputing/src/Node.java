
public class Node {
	final static double ALFA = 0.1;
	double output, error;
	Node[] input;
	double[] inputWeights;
	double threshold;

	/**
	 * constructor for the nodes of the first layers, the input nodes
	 * 
	 * @param output
	 *            is part of the input of the net
	 */
	public Node() {
		this.input = null;
		this.inputWeights = null;
	}

	public Node(Node[] input) {
		error = 0;
		this.input = input;
		inputWeights = new double[input.length];
		for (int i = 0; i < input.length; i++) {
			inputWeights[i] = Math.random();
		}
	}

	public void input(int in) {
		output = in;
	}

	public void calculateOutput() {
		double sum = 0;
		for (int i = 0; i < input.length; i++) {
			sum += input[i].output * inputWeights[i];
		}
		output = sigmoid(sum);
	}

	private double sigmoid(double sum) {
		return 1 / (1 + Math.pow(Math.E, -sum));
	}

	public void calculateErrorAndUpdate(double err) {
		error += err;
		double myDelta = err * output * (1 - output);
		for (int i = 0; i < inputWeights.length; i++) {
			input[i].error += myDelta*inputWeights[i];
			inputWeights[i] += ALFA*myDelta*input[i].output;
		}
		error = 0;
	}

}
