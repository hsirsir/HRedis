package cn.clo.data;

public class Znode {
	private String score;
	private String member;
	/**
	 * @return the score
	 */
	public String getScore() {
		return score;
	}
	public Znode(String score, String member) {
		super();
		this.score = score;
		this.member = member;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(String score) {
		this.score = score;
	}
	/**
	 * @return the member
	 */
	public String getMember() {
		return member;
	}
	/**
	 * @param member the member to set
	 */
	public void setMember(String member) {
		this.member = member;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "[score="+score+",data="+member+"]";
	}
	
	
	
}
