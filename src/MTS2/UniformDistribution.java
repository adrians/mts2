package MTS2;
public final class UniformDistribution implements Distribution {

	private int range;
	private int min = 0;
	private int max = 20;
	
	public UniformDistribution() {}
	
	public UniformDistribution(int max, int min) throws Exception {
		if ((max == min) || (max == 0 && min == 0)) {
			throw new Exception("Limits are wrong");
		}
		
		this.max = max;
		this.min = min;
	}
	
	@Override
	public long getJobLength() {
		this.range = Math.abs(max - min);
		return (long)(Math.random() * range) + (min <= max ? min : max) + 1;
	}
}