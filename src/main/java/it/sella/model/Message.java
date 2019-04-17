package it.sella.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {
	@SerializedName("mid")
	@Expose
	private String mid;
	@SerializedName("seq")
	@Expose
	private Integer seq;
	@SerializedName("text")
	@Expose
	private String text;
	/*
	  {
	"object": "page",
	"entry": [{
		"id": "437062153490261",
		"time": 1555469630374,
		"messaging": [{
			"sender": {
				"id": "2068406659891626"
			},
			"recipient": {
				"id": "437062153490261"
			},
			"timestamp": 1555469630015,
			"message": {
				"mid": "rponMsA3EvnoJ5tpgkUNM-JuxLiYm92OU63KEmX1ZCCL6isF9RuZdEeECvC8Y704O3Idzc3aM4GsboWwOZF0iw",
				"seq": 20743,
				"text": "hi"
			}
		}]
	}]
}
	 */
	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Message [mid=");
		builder.append(mid);
		builder.append(", seq=");
		builder.append(seq);
		builder.append(", text=");
		builder.append(text);
		builder.append("]");
		return builder.toString();
	}
	

}