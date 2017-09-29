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
	private long uuid;
	private String data;
}
