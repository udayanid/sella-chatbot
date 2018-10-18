package it.sella;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.sella.model.UserDetail;

public class QnaResponse {
	private static final Logger logger = LoggerFactory.getLogger(QnaResponse.class);

	public static String getJsonResponse(String senderId, String requestText, UserDetail userDetail) {
		String jsonResponse = "";
		
		switch (getActualKeyword(requestText)) {
		case "hi":
		case "hai":
		case "hello":
		case "ciao":
		case "caio":
			jsonResponse = String.format(
					"{ \"recipient\": { \"id\": \"recipientId\" }, \"message\": { \"text\": \"%s\" } }",
					"Ciao " + userDetail.getFirstName() + " " + userDetail.getLastName());
			break;
		case "bye":
		case "":
			jsonResponse = String.format(
					"{ \"recipient\": { \"id\": \"recipientId\" }, \"message\": { \"text\": \"%s\" } }",
					"Grazie " + userDetail.getFirstName() + " " + userDetail.getLastName()+", Speriamo di aver risposto alle tue domamde.  Buona giornata ");
		break;
		case "carte di debito":
		case "debito":
			jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"generic\", \"elements\":[ { \"title\":\"CARTE DI DEBITO\", \"image_url\":\"https://chatbot-hook.herokuapp.com/img/cirrus_maestro_basic.jpg\", \"subtitle\":\"Ricevila a casa o ritirala in succursale\\n Saldo del c/c in tempo reale\\n Max sicurezza con Chip & PIN \\n Paghi e prelevi ovunque con il circuito Maestro \\n Accedi a promozioni dedicate \\n Scopri un mondo di servizi\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/carte/cirrus-maestro-basic.jsp\", \"title\":\"For More Details\" }, { \"type\":\"web_url\", \"url\":\"https://www.sella.it/AuthenticationDelegatedServlet?delegated_service=219&SECODE=ONBOARDING_CL&SEACTION=LOGIN&SEPARAMS=parameterCode=CIRRUSBASIC\", \"title\":\"On Boarding\" }, { \"type\":\"postback\", \"title\":\"Any Other Query\", \"payload\":\"call our representative\" } ] } ] } } } }";
			break;
		case "credito":
		case "credit":
		case "visa classic":
		case "mastercard classic":
		case "carte di credito":		
			jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"generic\", \"elements\":[ { \"title\":\"VISA CLASSIC\", \"image_url\":\"https://chatbot-hook.herokuapp.com/img/cirrus_maestro_basic.jpg\", \"subtitle\":\"Con Visa Classic puoi fare acquisti e prelievi in tutto il mondo, con la garanzia della massima sicurezza grazie alla tecnologia Chip & PIN e al servizio Verified by Visa.\\n Canone primo anno 31 €\\n Gratis oltre i 7000 € di spese/anno\\n Sicura con Chip & PIN e Verified by Visa\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/carte/visa-classic.jsp\", \"title\":\"For More Details\" }, { \"type\":\"web_url\", \"url\":\"https://www.sella.it/AuthenticationDelegatedServlet?delegated_service=219&SECODE=ONBOARDING_CL&SEACTION=LOGIN&SEPARAMS=parameterCode=VISA_CLASSIC_BSE\", \"title\":\"On Boarding\" }, { \"type\":\"postback\", \"title\":\"Any Other Query\", \"payload\":\"call our representative\" } ] }, { \"title\":\"MASTERCARD CLASSIC\", \"image_url\":\"https://chatbot-hook.herokuapp.com/img/cirrus_maestro_basic.jpg\", \"subtitle\":\"Mastercard Classic è la carta di credito che prevede, se utilizzata a saldo, un addebito unico mensile del tuo speso. E' adatta per la gestione della tua liquidità e ti permette di effettuare pagamenti, anche tramite la funzionalità contatless\\n Canone primo anno 31 € \\n Gratis oltre i 7000 € di spese/anno \\n Sicura con Chip & PIN e Mastercard SecureCode\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/carte/mastercard-classic.jsp\", \"title\":\"For More Details\" }, { \"type\":\"web_url\", \"url\":\"https://www.sella.it/AuthenticationDelegatedServlet?delegated_service=219&SECODE=ONBOARDING_CL&SEACTION=LOGIN&SEPARAMS=parameterCode=MASTERCARD_CLASSIC\", \"title\":\"On Boarding\" }, { \"type\":\"postback\", \"title\":\"Any Other Query\", \"payload\":\"call our representative\" } ] } ] } } } }";
			break;
		case "hype plus":
		case "hype":
		case "plus":
		case "hypeplus":
			jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"generic\", \"elements\":[ { \"title\":\"HYPE PLUS\", \"image_url\":\"https://chatbot-hook.herokuapp.com/img/hype_bse.gif\", \"subtitle\":\"HYPE Plus è semplice e smart: controlla tutto ciò che serve dal tuo smartphone\\n Puoi ricaricare la tua carta fino a 50.000€ all'anno\\n Prelievi gratuiti da tutti gli ATM in Italia e nel mondo\\n Visualizzi all'istante i movimenti della tua carta\\n La metti in pausa quando vuoi, direttamente dall'app\\n Hai il tuo IBAN attivo in pochi minuti\\n Puoi pagare con il tuo smartphone\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/carte/hype.jsp\", \"title\":\"For More Details\" }, { \"type\":\"web_url\", \"url\":\"https://www.hype.it/signup/request/hype/contact-info\", \"title\":\"On Boarding\" }, { \"type\":\"postback\", \"title\":\"Any Other Query\", \"payload\":\"call our representative\" } ] }  ] } } } }";
			break;
		case "all carte details":
			jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"generic\", \"elements\":[ { \"title\":\"CARTE DI DEBITO\", \"image_url\":\"https://chatbot-hook.herokuapp.com/img/cirrus_maestro_basic.jpg\", \"subtitle\":\"CARTE\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"CARTE DI DEBITO\", \"payload\":\"CARTE DI DEBITO\" } ] }, { \"title\":\"CARTE DI CREDITO\", \"image_url\":\"https://chatbot-hook.herokuapp.com/img/cirrus_maestro_basic.jpg\", \"subtitle\":\"CARTE\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"CARTE DI CREDITO\", \"payload\":\"CARTE DI CREDITO\" } ] }, { \"title\":\"HYPE PLUS\", \"image_url\":\"https://chatbot-hook.herokuapp.com/img/hype_bse.gif\", \"subtitle\":\"CARTE\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"HYPE PLUS\", \"payload\":\"HYPE PLUS\" } ] } ] } } } }";
			break;
		case "conti":
		case "canti":
		case "cante":
		case "list conti":
			jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"generic\", \"elements\":[ { \"title\":\"CONTI\", \"image_url\":\"https://chatbot-hook.herokuapp.com/img/conti_correnti_bse.gif\", \"subtitle\":\"WEBSELLA\", \"default_action\":{ \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/websella.jsp\", \"webview_height_ratio\":\"compact\" }, \"buttons\":[{ \"type\":\"postback\", \"title\":\"Websella\", \"payload\":\"websella\" } ] } ] } } } }";
			break;
		case "websella":
		case "websela":
			jsonResponse = " { \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"generic\", \"elements\":[ { \"title\":\"WEBSELLA\", \"image_url\":\"https://chatbot-hook.herokuapp.com/img/conti_correnti_bse.gif\", \"subtitle\":\"Canone mensile zero euro  0 Euro\\n Una carta di debito a zero euro\\n operatività 24h/24h \\n13h al giorno di assistenza\\n4 prelievi gratuiti al mese da tutti gli ATM UE\\n300 succursali\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/websella.jsp\", \"title\":\"For More Details\" }, { \"type\":\"web_url\", \"url\":\"https://www.sella.it/onboarding/fe/03268/contowebsella/#/start/CONTO_WEBSELLA\", \"title\":\"Onboarding\" }, { \"type\":\"postback\", \"title\":\"Any Other Query\", \"payload\":\"call our representative\" } ] } ] } } } }";
			break;
		case "cirrus maestro basic":
			jsonResponse = "{ \"recipient\": { \"id\": \"recipientId\" }, \"message\": { \"text\": \"You are applying cirrus maestro basic \" } }";
			break;
		case "hypeplus application":
			jsonResponse = "{ \"recipient\": { \"id\": \"recipientId\" }, \"message\": { \"text\": \"You are applying hypeplus application \" } }";
			break;
		case "first hand shake":
			jsonResponse =String.format("{ \"recipient\": { \"id\": \"recipientId\" }, \"message\": { \"text\": \"%s, Welcome to Sella bot, How can I help you? \" } }","Hi "+userDetail.getFirstName()+" "+userDetail.getLastName());
			break;
		case "list products":
		case "products":
		case "product":
		case "our products":
			jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"button\", \"text\":\"Our Products!!!\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"CONTI\", \"payload\":\"list conti\" }, { \"type\":\"postback\", \"title\":\"CARTE\", \"payload\":\"list carte\" }, { \"type\":\"postback\", \"title\":\"INVESTIMENTI E RISPARMIO\", \"payload\":\"list investimenti e risparmio\" } ] } } } }";
			break;
		case "list carte":
		case "carte":
		case "carti":
            jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"button\", \"text\":\"Our Carte!!!\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"CARTE DI DEBITO\", \"payload\":\"all carte details\" }, { \"type\":\"postback\", \"title\":\"CARTE DI CREDITO\", \"payload\":\"all carte details\" }, { \"type\":\"postback\", \"title\":\"HYPE PLUS\", \"payload\":\"all carte details\" }  ] } } } }";
			break;
		case "list investimenti e risparmio":
		case "investimenti":
		case "list investimenti":
		case "investments":
		case "investment":
		case "savings":
		case "saving":
			jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"button\", \"text\":\"Our investimenti!!!\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"CONSULENZA INVESTIMENTI\", \"payload\":\"all investimenti details\" }, { \"type\":\"postback\", \"title\":\"SELLA EVOLUTION GESTIONI PATRIMONIALI\", \"payload\":\"all investimenti details\" }, { \"type\":\"postback\", \"title\":\"SELLA MULTISOLUTION\", \"payload\":\"all investimenti details\" } ] } } } }";
			break;
		case "all investimenti details":
			jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"button\", \"text\":\"For more details please click this button!!! \", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/websella.jsp?fbclid=IwAR3yZoet66tQB-z43deD7yr3cZt8v-yhLiiXPLR0vcX3Kxb9ZZTPcYxN3NI\", \"title\":\"Visit Our Website\" } ] } } } }";
			break;
		case "call our representative":
			//jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"button\", \"text\":\"For further assistance,please talk to our representative\", \"buttons\":[ { \"type\":\"phone_number\", \"title\":\"Call Representative\", \"payload\":\"+02224410023\" } ] } } } }";
			jsonResponse = String.format(
					"{ \"recipient\": { \"id\": \"recipientId\" }, \"message\": { \"text\": \"%s\" } }",
					"Hi " + userDetail.getFirstName() + " " + userDetail.getLastName()+", puoi contattare l'assistenza clienti al numero 800.142.142 (per le chiamate da rete fissa nazionale) oppure al numero 0039.015.24.34.617 (per le chiamate dall'estero e da telefono cellulare) attivi dal luneda al venerda dalle ore 8.00 alle 21.00. Hai bisogno di altre informazioni?");
			break;		
		default:
			jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"button\", \"text\":\"Non sono riuscita a comprendere la richiesta, se lo desideri ti posso mettere in contatto con un nostro operatore della assitenza.\", \"buttons\":[ { \"type\":\"phone_number\", \"title\":\"Clicca qui per proseguire\", \"payload\":\"+02224410023\" } ] } } } }";
			break;
		}
		jsonResponse = jsonResponse.replace("recipientId", senderId);
		logger.info("jsonresponse>>>>{}", jsonResponse);
		return jsonResponse;
	}
	
	private static String getActualKeyword(String textSearch) {
		String searchString=textSearch;
		if( textSearch.contains("product")) {
			searchString="product";
		}else if(textSearch.contains("conti") || textSearch.contains("account")  ) {
			searchString="conti";
		}else if((textSearch.contains("carte") || textSearch.contains("card")) && !(textSearch.equals("carte di debito")||textSearch.equals("carte di credito") || textSearch.equals("hype plus") || textSearch.equals("all carte details"))) {
			searchString="carte";
		}else if(textSearch.contains("hype")) {
			searchString="hype";
		}else if(textSearch.contains("invest")) {
			searchString="invest";
		}else if(textSearch.contains("websella") || textSearch.contains("websela")) {
			searchString="websella";
		}else if(textSearch.contains("morning") || textSearch.contains("noon")|| textSearch.contains("evening")||textSearch.contains("ciao")||textSearch.contains("caio")||textSearch.contains("ciao")) {
			searchString="hi";
		}else if(textSearch.contains("thank") || textSearch.contains("bye")|| textSearch.contains("grazie")) {
			searchString="bye";
		}
		logger.info("The actual keyword is{}",searchString);
		return searchString;
	}
}

