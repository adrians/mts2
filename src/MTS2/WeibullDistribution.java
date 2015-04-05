package MTS2;

public class WeibullDistribution implements Distribution {

	private int lambda = 10;
	private int lengthNumber = 4;
	private int param = 10;
	
	public WeibullDistribution() {}
	
	public WeibullDistribution(int lambda, int param, int lengthNumber) throws Exception {
		this.lambda = lambda;
		
		if (param == 0) {
			throw new Exception("Division by zero");
		}
		
		this.param = param;
		
		if (lengthNumber == 0) {
			throw new Exception("Length of number can be 0");
		}
		
		this.lengthNumber = lengthNumber;
	}
	
	@Override
	public long getJobLength() {
		double formula = Math.pow((-Math.log(Math.random())), 1 / (double)this.param) / this.lambda;
		
		return (long) (formula * Math.pow(10, this.lengthNumber) + 1);
	}
}
