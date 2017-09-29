package com.imonkeyz.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Jesse on 2017/9/27.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokenData {
	private String uuid;
	private long tsm;
}
