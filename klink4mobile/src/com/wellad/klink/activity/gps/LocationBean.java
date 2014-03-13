package com.wellad.klink.activity.gps;

public class LocationBean {
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGpsx() {
		return gpsx;
	}

	public void setGpsx(String gpsx) {
		this.gpsx = gpsx;
	}

	public String getGpsy() {
		return gpsy;
	}

	public void setGpsy(String gpsy) {
		this.gpsy = gpsy;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getBannerurl() {
		return bannerurl;
	}

	public void setBannerurl(String bannerurl) {
		this.bannerurl = bannerurl;
	}

	public String getBannerlocalurl() {
		return bannerlocalurl;
	}

	public void setBannerlocalurl(String bannerlocalurl) {
		this.bannerlocalurl = bannerlocalurl;
	}


	private String name;
	private String gpsx;
	private String gpsy;
	private String imageurl;
	private String desc;
	private String distance;
	private String bannerurl;
	private String bannerlocalurl;
	
	public LocationBean(){
		
	}
	

}
