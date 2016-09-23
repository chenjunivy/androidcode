package domain;

import java.util.ArrayList;

/**
 * ��Ϣ��װ��
 * @author SystemIvy
 *
 */
public class NewsMenu {
	public int retcode;
	public ArrayList<Integer> extend;
	public ArrayList<NewsMenuData> data;
	
	//������˵�
	public class NewsMenuData{
		public int id;
		public String title;
		public int type;
		public ArrayList<NewsTabData> children;
		
		@Override
		public String toString() {
			return "NewsMenuData [title=" + title + ", type=" + type
					+ ", children=" + children + "]";
		}
	}
	
	//������ѡ��
	public class NewsTabData{
		public int id;
		public String title;
		public int type;
		public String url;
		
		@Override
		public String toString() {
			return "NewsTabData [title=" + title + ", type=" + type + ", url="
					+ url + "]";
		}
	}

	@Override
	public String toString() {
		return "NewsMenu [data=" + data + "]";
	}
	
}
