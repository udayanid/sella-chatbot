package demo;

import com.google.gson.Gson;

import it.sella.JsonUtil;
import it.sella.model.im.ImResponse;

public class TestDemo {
	public static void main(String[] args) {
		String jsonResponse="{ \"status\":\"EXCEPTION\", \"errors\":[ { \"messageCode\":\"IM_CHAT_ID_NOT_FOUND\", \"messageParams\":[  ], \"messageFEFields\":\"*\" } ], \"requests\":null, \"codes\":null, \"contextChange\":null, \"concepts\":null, \"favorites\":null, \"chatid\":null, \"chaturl\":null, \"result\":null, \"cause\":null, \"licenses\":null, \"transcript\":null, \"overTime\":null, \"errorMessageCode\":\"IM_CHAT_ID_NOT_FOUND\", \"results\":[{\"sender\":\"BOT\",\"message\":\"Hai bisogno di informazioni sul suo conto corrente, su un bonifico, su un F24 o vuoi informazioni sulle carte?\", \"answer\":null, \"intentName\":null, \"intentCode\":null, \"action\":null, \"operatorSkill\":null, \"link\":null,\"intentArea\":null, \"url\":null}] }";
		ImResponse response = JsonUtil.getInstance().fromJson(jsonResponse, ImResponse.class);
		System.out.println(response);
		
		Gson gson =new Gson();
		String str= gson.toJson(response);
		System.out.println(str);
	}
}
