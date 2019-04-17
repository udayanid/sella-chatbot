package it.sella.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Messaging {

	@SerializedName("sender")
	@Expose
	private Sender sender;
	@SerializedName("recipient")
	@Expose
	private Recipient recipient;	
	@SerializedName("message")
	@Expose
	private Message message;
	@SerializedName("postback")
	@Expose
	private Postback postback;
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
	

	public Postback getPostback() {
		return postback;
	}

	public void setPostback(Postback postback) {
		this.postback = postback;
	}

	public Sender getSender() {
		return sender;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Messaging [sender=");
		builder.append(sender);
		builder.append(", recipient=");
		builder.append(recipient);
		builder.append(", message=");
		builder.append(message);
		builder.append(", postback=");
		builder.append(postback);
		builder.append("]");
		return builder.toString();
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}

	public Recipient getRecipient() {
		return recipient;
	}

	public void setRecipient(Recipient recipient) {
		this.recipient = recipient;
	}

}
