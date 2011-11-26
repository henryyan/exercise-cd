package net.yanhl.venue.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 场馆图片工具
 *
 * @author HenryYan
 *
 */
public class VenuePictureUtil {
	
	/**
	 *	场馆图片保存位置--相对于应用根目录的目录位置
	 */
	public static final String VENUE_PICTURE_PATH = "/venue/pictures/";

	/**
	 * 场馆图片缩略图大小
	 * @return	每组大小：[宽, 高]
	 */
	public static List<Integer[]> getThumbnailsSizes() {
		List<Integer[]> sizes = new ArrayList<Integer[]>();
		
		// 用于管理员系统的场馆列表
		sizes.add(new Integer[] {60, 60});
		
		// 图片列表
		sizes.add(new Integer[] {120, 120});
		
		return sizes;
	}
	
}
