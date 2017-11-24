package com.imonkeyz.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GroupInfoData {
	private Long id;
	private String name;
	private String datetime;
	private String intro;
	private String banner;
	private String avatar;
	private List<PanelInfoData> infos;
	private List<QRCodeData> qrs;

	private String openId;

	public GroupInfoData(Long id, String name, String datetime, String banner) {
		this.id = id;
		this.name = name;
		this.datetime = datetime;
		this.banner = banner;
	}

	public GroupInfoData(Long id, String name, String datetime, String banner, List<PanelInfoData> infos) {
		this.id = id;
		this.name = name;
		this.datetime = datetime;
		this.banner = banner;
		this.infos = infos;
	}

	public GroupInfoData(Long id, String name, String datetime, String intro, String banner, String avatar, List<PanelInfoData> infos, List<QRCodeData> qrs) {
		this.id = id;
		this.name = name;
		this.datetime = datetime;
		this.intro = intro;
		this.banner = banner;
		this.avatar = avatar;
		this.infos = infos;
		this.qrs = qrs;
	}

	public String getFormattedIntro () {
		StringBuilder sb = new StringBuilder();
		for (String line : this.intro.split("\n")) {
			sb.append("<div>").append("".equals(line) ? "&nbsp;" : line).append("</div>");
		}
		return sb.toString();
	}
}
