package com.yunmall.ymsdk.utility.thirdparty.sinawb;

import android.graphics.Bitmap;

public class SWBSendParam {
	
		
	public enum SWEType{TYPE_TEXT_OR_IMAGE,TYPE_WEB,TYPE_MUSIC,TYPE_VIDEO,TYPE_VOICE}
	
	private final String text;
	private final Bitmap bitmap;
	private final Bitmap thumbView;
	private final String title;
	private final String desc;
	private final String actionUrl;
	private final SWEType type;
	private final String pathUrl;
	
	public static class Builder{
		private String text;
		private Bitmap bitmap;
		private Bitmap thumbView;
		private String title;
		private String desc;
		private String actionUrl;
		private String pathUrl;
		private final SWEType type;
		
		public Builder(SWEType type){
			this.type = type;
		}
		
		public Builder text(String text){
			this.text = text;
			return this;
		}
		
		public Builder bitmap(Bitmap bitmap){
			this.bitmap = bitmap;
			return this;
		}
		
		public Builder title(String title){
			this.title = title;
			return this;
		}
		
		public Builder desc(String desc){
			this.desc = desc;
			return this;
		}
		
		public Builder actionUrl(String actionUrl){
			this.actionUrl = actionUrl;
			return this;
		}
		
		public Builder thumbView(Bitmap thumbView){
			this.thumbView = thumbView;
			return this;
		}
		
		public Builder pathUrl(String pathUrl){
			this.pathUrl = pathUrl;
			return this;
		}
		
		public SWBSendParam build(){
			return new SWBSendParam(this);
		}
		
	}
	
	
	private SWBSendParam(Builder builder){
		this.text = builder.text;
		this.bitmap = builder.bitmap;
		this.title = builder.title;
		this.desc = builder.desc;
		this.type = builder.type;
		this.actionUrl = builder.actionUrl;
		this.thumbView = builder.thumbView;
		this.pathUrl = builder.pathUrl;
	}


	public String getText() {
		return text;
	}


	public Bitmap getBitmap() {
		return bitmap;
	}


	public String getTitle() {
		return title;
	}


	public String getDesc() {
		return desc;
	}


	public String getActionUrl() {
		return actionUrl;
	}


	public SWEType getType() {
		return type;
	}


	public Bitmap getThumbView() {
		return thumbView;
	}


	public String getPathUrl() {
		return pathUrl;
	}
	
	
	
	

}
