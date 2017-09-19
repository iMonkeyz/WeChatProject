package com.imonkeyz.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PanelInfoData {
	private String title;
	private String content;

	public String getFormattedConent() {
		StringBuilder sb = new StringBuilder();
		for (String line : this.content.split("\n")) {
			sb.append("<div>").append("".equals(line) ? "&nbsp;" : line).append("</div>");
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return "PanelInfoData{" +
				"title='" + title + '\'' +
				", content='" + content + '\'' +
				'}';
	}
}
