package com.imonkeyz.demo.entity;

import jp.sourceforge.qrcode.data.QRCodeImage;

import java.awt.image.BufferedImage;

/**
 * Created by Jesse on 2017/9/18.
 */
public class TwoDimensionCodeImage implements QRCodeImage {

	private BufferedImage bufImg;

	public TwoDimensionCodeImage(BufferedImage bufImg) {
		super();
		this.bufImg = bufImg;
	}

	public int getHeight() {
		return bufImg.getHeight();
	}

	public int getPixel(int x, int y) {
		return bufImg.getRGB(x, y);
	}

	public int getWidth() {
		return bufImg.getWidth();
	}

}
