package com.imonkeyz.demo.entity;

import java.util.List;

public class GroupInfoData {
	private Long id;
	private String name;
	private String datetime;
	private String intro;
	private String banner;
	private String avatar;
	private List<PanelInfoData> infos;

	public GroupInfoData() {
	}

	public GroupInfoData(Long id, String name, String datetime, String intro, String banner, String avatar, List<PanelInfoData> infos) {
		this.id = id;
		this.name = name;
		this.datetime = datetime;
		this.intro = intro;
		this.banner = banner;
		this.avatar = avatar;
		this.infos = infos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public List<PanelInfoData> getInfos() {
		return infos;
	}

	public void setInfos(List<PanelInfoData> infos) {
		this.infos = infos;
	}

	public String getFormattedIntro () {
		StringBuilder sb = new StringBuilder();
		for (String line : this.intro.split("\n")) {
			sb.append("<div>").append("".equals(line) ? "&nbsp;" : line).append("</div>");
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return "GroupInfoData{" +
				"id=" + id +
				", name='" + name + '\'' +
				", datetime='" + datetime + '\'' +
				", intro='" + intro + '\'' +
				", banner='" + banner + '\'' +
				", avatar='" + avatar + '\'' +
				", infos=" + infos +
				'}';
	}
}
