package com.yunmall.ymsdk.utility.thirdparty.wx;

import android.animation.TimeAnimator.TimeListener;
import android.graphics.Bitmap;
@SuppressWarnings("unused")
public class WXSendParam {
	
	
	public final static int TYPE_TEXT = 1;
	public final static int TYPE_IMAGE = 2;
	public final static int TYPE_WEB = 3;
	
	private final String title;
	private final String description;
	private final Bitmap bitmap;
	private final String imageUrl;
	private final String imagePath;
	private final String webUrl;
	private final int type;
	private final boolean isTimeLine;
	
	
	public static class Builder{
		private String title;
		private final String description;
		private Bitmap bitmap;
		private String imageUrl;
		private String imagePath;
		private String webUrl;
		private final int type;
		private final boolean isTimeLine;
		
		
		public Builder(int type,String description,boolean isTimeLine){
			this.type = type;
			this.description = description;
			this.isTimeLine = isTimeLine;
		}
		
		public Builder title(String title){
			this.title = title;
			return this;
		}
		
		public Builder bitmap(Bitmap bitmap){
			this.bitmap = bitmap;
			return this;
		}
		
		public Builder imageUrl(String imageUrl){
			this.imageUrl = imageUrl;
			return this;
		}
		
		public Builder webUrl(String webUrl){
			this.webUrl = webUrl;
			return this;
		}
		
		
		public WXSendParam build(){
			return new WXSendParam(this);
		}
		
	}
	
	
	private WXSendParam(Builder builder){
		this.title = builder.title;
		this.description = builder.description;
		this.imagePath = builder.imagePath;
		this.imageUrl = builder.imageUrl;
		this.type = builder.type;
		this.webUrl = builder.webUrl;
		this.bitmap = builder.bitmap;
		this.isTimeLine = builder.isTimeLine;
	}


	public String getTitle() {
		return title;
	}


	public String getDescription() {
		return description;
	}


	public Bitmap getBitmap() {
		return bitmap;
	}


	public String getImageUrl() {
		return imageUrl;
	}


	public String getImagePath() {
		return imagePath;
	}


	public String getWebUrl() {
		return webUrl;
	}


	public int getType() {
		return type;
	}


	public boolean isTimeLine() {
		return isTimeLine;
	}
	
	
	
	

}
