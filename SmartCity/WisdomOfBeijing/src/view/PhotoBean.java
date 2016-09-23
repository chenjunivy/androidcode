package view;

import java.util.ArrayList;

public class PhotoBean {
	public  PhotoData data;
	
	public class PhotoData{
		public ArrayList<PhotoNews> news;

		@Override
		public String toString() {
			return "PhotoData [news=" + news + "]";
		}
		
		
	}
	
	public class PhotoNews{
		public int id;
		public String listimage;
		public String title;
		@Override
		public String toString() {
			return "PhotoNews [id=" + id + ", listimage=" + listimage
					+ ", title=" + title + "]";
		}
		
		
	}

	@Override
	public String toString() {
		return "PhotoBean [data=" + data + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
	
}
