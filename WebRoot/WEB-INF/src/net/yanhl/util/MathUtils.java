package net.yanhl.util;

/**
 * 数学工具类
 *
 * @author HenryYan
 *
 */
public class MathUtils {
	
	/**
	 * 四舍五入Float值
	 * @param f		 需要四舍五入的数字
	 * @param bit	小数点位数
	 * @return
	 */
	public static float round(float f, int bit) {
		float p = (float) pow(bit);
		return (Math.round(f * p) / p);
	}

	/**
	 * 四舍五入Double值
	 * @param f		 需要四舍五入的数字
	 * @param bit	小数点位数
	 * @return
	 */
	public static double round(double d, int bit) {
		double p = pow(bit);
		return (Math.round(d * p) / p);
	}

	private static long pow(int bit) {
		return Math.round(Math.pow(10.0D, bit));
	}
	
	public static void main(String[] args) {
		Double d1 = 1.6d;
		int reate = 30;
		double round = round(d1, 2);
		System.out.println(round * reate / 100);
	}
	
}
