package com.imonkeyz.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Jesse on 2017/9/26.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QRCodeData {
	private String uuid;
	private String data;
	private int counter;

	public QRCodeData(String uuid, String data) {
		this.uuid = uuid;
		this.data = data;
	}

	public QRCodeData(String data, int counter) {
		this.data = data;
		this.counter = counter;
	}
}
