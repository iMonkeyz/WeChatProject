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
	private List<String> qrs;

	public String getFormattedIntro () {
		StringBuilder sb = new StringBuilder();
		for (String line : this.intro.split("\n")) {
			sb.append("<div>").append("".equals(line) ? "&nbsp;" : line).append("</div>");
		}
		return sb.toString();
	}
}
