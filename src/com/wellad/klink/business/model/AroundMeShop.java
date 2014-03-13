package com.wellad.klink.business.model;

public class AroundMeShop {
	public AroundMeShop(String shoplocationame, String shopname,
			String shopgpsx, String shopgpsy, String areaname) {
		super();
		this.shoplocationame = shoplocationame;
		this.shopname = shopname;
		this.shopgpsx = shopgpsx;
		this.shopgpsy = shopgpsy;
		this.areaname = areaname;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public String getShopgpsx() {
		return shopgpsx;
	}

	public void setShopgpsx(String shopgpsx) {
		this.shopgpsx = shopgpsx;
	}

	public String getShopgpsy() {
		return shopgpsy;
	}

	public void setShopgpsy(String shopgpsy) {
		this.shopgpsy = shopgpsy;
	}

	public String getShoplocationame() {
		return shoplocationame;
	}

	public void setShoplocationame(String shoplocationame) {
		this.shoplocationame = shoplocationame;
	}

	public String getShopnearestid() {
		return shopnearestid;
	}

	public void setShopnearestid(String shopnearestid) {
		this.shopnearestid = shopnearestid;
	}

	public String getAreaname() {
		return areaname;
	}

	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}

	private String shoplocationame;
	private String shopname;
	private String shopgpsx;
	private String shopgpsy;
	private String shopnearestid;
	private String areaname;

}
