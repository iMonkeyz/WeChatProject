package com.imonkeyz.demo.entity;

public class PanelInfoData {
	private String title;
	private String content;

	public PanelInfoData() {
	}

	public PanelInfoData(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFormattedConent() {
		StringBuilder sb = new StringBuilder();
		for (String line : this.content.split("\n")) {
			sb.append("<div>").append("".equals(line) ? "&nbsp;" : line).append("</div>");
		}
		return sb.toString();
	}

	public boolean isValid() {
		return true;
	}

	@Override
	public String toString() {
		return "PanelInfoData{" +
				"title='" + title + '\'' +
				", content='" + content + '\'' +
				'}';
	}
}
