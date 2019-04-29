
public class Main {

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
