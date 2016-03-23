package cn.clo.data;

public class Hnode {
	private String field;
	private String val;
	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}
	public Hnode(String field, String val) {
		super();
		this.field = field;
		this.val = val;
	}
	/**
	 * @param field the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}
	/**
	 * @return the val
	 */
	public String getVal() {
		return val;
	}
	/**
	 * @param val the val to set
	 */
	public void setVal(String val) {
		this.val = val;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "[field="+field+",val="+val+"]";
	}
}
