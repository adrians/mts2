package MTS2;

public class PoissonDistribution implements Distribution {

	private int lambda = 10;
	
	public PoissonDistribution() {}

	public PoissonDistribution(int lambda) throws Exception {
		if (lambda == 0) {
			throw new Exception("Lambda can not be zero");
		}
		
		this.lambda = lambda;
	}

	@Override
	public long getJobLength() {
		long sum = 0;
		long length = -1;

		while (sum < this.lambda) {
			length++;
			sum -= Math.log(Math.random());
		}
		
		return length + 1;
	}
}
