package MTS2;

public final class ExponentialDistribution implements Distribution {

	private int lambda = 10;
	private int lengthNumber = 4;
	
	public ExponentialDistribution() {}
	
	public ExponentialDistribution(int lambda, int lengthNumber) throws Exception {
		this.lambda = lambda;
		
		if (lengthNumber == 0) {
			throw new Exception("Length of number can be 0");
		}
		
		this.lengthNumber = lengthNumber;
	}
	
	@Override
	public long getJobLength() {
		double formula = (-Math.log(Math.random())) / this.lambda;

		return (long) (formula * Math.pow(10, this.lengthNumber) + 1);
	}
}
