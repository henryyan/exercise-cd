package test;

/**
 * <p><b>Title：</b> </p>
 * <p><b>Description：</b></p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.2009
 */
public class JDK5Test<T extends Object> {

	private T x;

	public T getX() {
		return x;
	}

	public void setX(T x) {
		this.x = x;
	}
	
	public static void main(String[] args) {
		JDK5Test<String> gf = new JDK5Test<String>(); 
        gf.setX("Hello"); 
        
        JDK5Test<String> gf2 = gf;
        gf2.setX("World"); 
	}
	
	
}
