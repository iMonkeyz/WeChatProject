package com.imonkeyz.demo.message.response;

/**
 * 音乐消息
 * Created by Shinelon on 2017/4/9.
 */
public class MusicMessage extends BaseMessage {
	// 音乐
	private Music Music;

	public Music getMusic() {
		return Music;
	}

	public void setMusic(Music music) {
		Music = music;
	}
}
