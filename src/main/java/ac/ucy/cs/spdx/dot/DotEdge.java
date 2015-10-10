package ac.ucy.cs.spdx.dot;

public class DotEdge {
	private String from;
	private String to;
	private boolean isTransitive;
	
	/**
	 * The constructor of {@link DotEdge} objects
	 * @param String
	 * @param String
	 * @param boolean
	 * 
	 */
	public DotEdge(String from,String to,boolean isTransitive){
		this.setFrom(from);
		this.setTo(to);
		this.setTransitive(isTransitive);
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the isTransitive
	 */
	public boolean isTransitive() {
		return isTransitive;
	}

	/**
	 * @param isTransitive the isTransitive to set
	 */
	public void setTransitive(boolean isTransitive) {
		this.isTransitive = isTransitive;
	}
	
	/**
	 * toString() implementation for {@link DotEdge}
	 * @return String
	 */
	@Override
	public String toString(){
		return this.from+" -> "+this.to;
	}
}
