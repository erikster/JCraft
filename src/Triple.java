/**
 * Tuple of three ints, used to track chunks under the ChunkManager
 *
 */
public class Triple {
	public int one;
	public int two;
	public int thr;
	
	/**
	 * Constructor, takes three ints
	 *
	 */
	public Triple(int one, int two, int thr) {
		this.one = one;
		this.two = two;
		this.thr = thr;
	}
	
	/**
	 * Returns true if all three int values match, false if other object 
	 * is not a Triple or one/two vlaues don't match
	 *
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof Triple)
			return this.one == ((Triple) other).one && this.two == ((Triple) other).two && this.thr == ((Triple) other).thr;
		else
			return false;
	}
	
	/**
	 * Returns String representation of the triple
	 *
	 */
	@Override
	public String toString() {
		return one + ", " + two + ", " + thr;
	}
}