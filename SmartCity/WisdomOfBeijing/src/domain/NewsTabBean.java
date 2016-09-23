package domain;

import java.util.ArrayList;

public class NewsTabBean {
	
	public NewsTab data;
	
	public class NewsTab{
		public String more;
		public ArrayList<NewsData> news;
		public ArrayList<TopNews> topnews;
	}
	
	//新闻列表
	public class NewsData{
		public int id;
		public String listimage;
		public String pubdate;
		public String title;
		public String type;
		public String url;
		@Override
		public String toString() {
			return "NewsData [id=" + id + ", listimage=" + listimage
					+ ", pubdate=" + pubdate + ", title=" + title + ", type="
					+ type + ", url=" + url + "]";
		}
		
	}

	//新闻头条
	public class TopNews{
		public int id;
		public String pubdate;
		public String title;
		public String topimage;
		public String type;
		public String url;
		@Override
		public String toString() {
			return "TopNews [id=" + id + ", pubdate=" + pubdate + ", title="
					+ title + ", topimage=" + topimage + ", type=" + type
					+ ", url=" + url + "]";
		}
		
		
	}

	@Override
	public String toString() {
		return "NewsTabBean [data=" + data + "]";
	}
	
	
}
