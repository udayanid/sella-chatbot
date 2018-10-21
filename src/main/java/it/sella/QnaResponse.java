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
			jsonResponse = String.format(
					"{ \"recipient\": { \"id\": \"recipientId\" }, \"message\": { \"text\": \"%s\" } }",
					"Ciao " + userDetail.getFirstName() + " " + userDetail.getLastName());
			break;		
		case "view product":
			jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"button\", \"text\":\"Our Products!!!\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"CONTI\", \"payload\":\"list conti\" }, { \"type\":\"postback\", \"title\":\"CARTE\", \"payload\":\"list carte\" }, { \"type\":\"postback\", \"title\":\"View More\", \"payload\":\"view1\" } ] } } } }";
			break;
		case "view1":
			jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"button\", \"text\":\"Other Products!!!\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"INVESTIMENTI E RISPARMIO\", \"payload\":\"list investimenti e risparmio\" }, { \"type\":\"postback\", \"title\":\"FINANZIAMENTI\", \"payload\":\"list finanziamenti\" }, { \"type\":\"postback\", \"title\":\"View More\", \"payload\":\"view2\" } ] } } } }";
			break;
		case "view2":
			jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"button\", \"text\":\"Other Products !!!\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"PROTEZIONE\", \"payload\":\"list protezione\" }, { \"type\":\"postback\", \"title\":\"More\", \"payload\":\"view product\" } ] } } } }";
			break;
		case "list conti":
			jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"generic\", \"elements\":[ { \"title\":\"CONTI\", \"image_url\":\"https://sella-bot.herokuapp.com/img/conti_correnti_bse.gif\", \"subtitle\":\"WEBSELLA\", \"default_action\":{ \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/websella.jsp\", \"webview_height_ratio\":\"compact\" }, \"buttons\":[{ \"type\":\"postback\", \"title\":\"Websella\", \"payload\":\"websella\" } ] } ] } } } }";
			break;
		case "list carte":
		    jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"button\", \"text\":\"Our Carte!!!\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"CARTE DI DEBITO\", \"payload\":\"all carte details\" }, { \"type\":\"postback\", \"title\":\"CARTE DI CREDITO\", \"payload\":\"all carte details\" }, { \"type\":\"postback\", \"title\":\"HYPE PLUS\", \"payload\":\"all carte details\" }  ] } } } }";
		break;
		case "list investimenti":
			//jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"button\", \"text\":\"Our investimenti!!!\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"CONSULENZA INVESTIMENTI\", \"payload\":\"all investimenti details\" }, { \"type\":\"postback\", \"title\":\"SELLA EVOLUTION GESTIONI PATRIMONIALI\", \"payload\":\"all investimenti details\" }, { \"type\":\"postback\", \"title\":\"SELLA MULTISOLUTION\", \"payload\":\"all investimenti details\" } ] } } } }";
			jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"generic\", \"elements\":[ { \"title\":\"Consulenza Servizi investimento\", \"image_url\":\"https://sella-bot.herokuapp.com/img/consulenza_investimenti.png\", \"subtitle\":\"Investimenti\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"View\", \"payload\":\"consulenza\" } ] }, { \"title\":\"Sella Evolution Gestioni Patrimoniali\", \"image_url\":\"https://sella-bot.herokuapp.com/img/sella_evolution.png\", \"subtitle\":\"Investimenti\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"View\", \"payload\":\"evolution\" } ] }, { \"title\":\"Fondi d'investimento\", \"image_url\":\"https://sella-bot.herokuapp.com/img/fondi.png\", \"subtitle\":\"Investimenti\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"View\", \"payload\":\"fondi\" } ] }, { \"title\":\"Investimenti PIR\", \"image_url\":\"https://sella-bot.herokuapp.com/img/investimenti_pir.png\", \"subtitle\":\"Investimenti\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"View\", \"payload\":\"pir\" } ] } ] } } } }";
			break;
		case "websella":
			jsonResponse = " { \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"generic\", \"elements\":[ { \"title\":\"WEBSELLA\", \"image_url\":\"https://sella-bot.herokuapp.com/img/conti_correnti_bse.gif\", \"subtitle\":\"Canone mensile zero euro  0 Euro\\n Una carta di debito a zero euro\\n operatività 24h/24h \\n13h al giorno di assistenza\\n4 prelievi gratuiti al mese da tutti gli ATM UE\\n300 succursali\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/websella.jsp\", \"title\":\"For More Details\" }, { \"type\":\"web_url\", \"url\":\"https://www.sella.it/onboarding/fe/03268/contowebsella/#/start/CONTO_WEBSELLA\", \"title\":\"Onboarding\" }, { \"type\":\"postback\", \"title\":\"Any Other Query\", \"payload\":\"call our representative\" } ] } ] } } } }";
			break;	
		case "carte di debito":
			jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"generic\", \"elements\":[ { \"title\":\"CARTE DI DEBITO\", \"image_url\":\"https://sella-bot.herokuapp.com/img/cirrus_maestro_basic.jpg\", \"subtitle\":\"Ricevila a casa o ritirala in succursale\\n Saldo del c/c in tempo reale\\n Max sicurezza con Chip & PIN \\n Paghi e prelevi ovunque con il circuito Maestro \\n Accedi a promozioni dedicate \\n Scopri un mondo di servizi\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/carte/cirrus-maestro-basic.jsp\", \"title\":\"For More Details\" }, { \"type\":\"web_url\", \"url\":\"https://www.sella.it/AuthenticationDelegatedServlet?delegated_service=219&SECODE=ONBOARDING_CL&SEACTION=LOGIN&SEPARAMS=parameterCode=CIRRUSBASIC\", \"title\":\"On Boarding\" }, { \"type\":\"postback\", \"title\":\"Any Other Query\", \"payload\":\"call our representative\" } ] } ] } } } }";
			break;			
		case "carte di credito":		
			jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"generic\", \"elements\":[ { \"title\":\"VISA CLASSIC\", \"image_url\":\"https://sella-bot.herokuapp.com/img/visa_classic_bse.gif\", \"subtitle\":\"Con Visa Classic puoi fare acquisti e prelievi in tutto il mondo, con la garanzia della massima sicurezza grazie alla tecnologia Chip & PIN e al servizio Verified by Visa.\\\\n Canone primo anno 31 €\\\\n Gratis oltre i 7000 € di spese/anno\\\\n Sicura con Chip & PIN e Verified by Visa\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/carte/visa-classic.jsp\", \"title\":\"For More Details\" }, { \"type\":\"web_url\", \"url\":\"https://www.sella.it/AuthenticationDelegatedServlet?delegated_service=219&SECODE=ONBOARDING_CL&SEACTION=LOGIN&SEPARAMS=parameterCode=VISA_CLASSIC_BSE\", \"title\":\"On Boarding\" }, { \"type\":\"postback\", \"title\":\"Any Other Query\", \"payload\":\"call our representative\" } ] },{ \"title\":\"MASTERCARD CLASSIC\", \"image_url\":\"https://sella-bot.herokuapp.com/img/mastercard_classic_bse.gif\", \"subtitle\":\"Mastercard Classic è la carta di credito che prevede, se utilizzata a saldo, un addebito unico mensile del tuo speso. E' adatta per la gestione della tua liquidità e ti permette di effettuare pagamenti, anche tramite la funzionalità contatless\\\\n Canone primo anno 31 € \\\\n Gratis oltre i 7000 € di spese/anno \\\\n Sicura con Chip & PIN e Mastercard SecureCode\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/carte/mastercard-classic.jsp\", \"title\":\"For More Details\" }, { \"type\":\"web_url\", \"url\":\"https://www.sella.it/AuthenticationDelegatedServlet?delegated_service=219&SECODE=ONBOARDING_CL&SEACTION=LOGIN&SEPARAMS=parameterCode=MASTERCARD_CLASSIC\", \"title\":\"On Boarding\" }, { \"type\":\"postback\", \"title\":\"Any Other Query\", \"payload\":\"call our representative\" } ] } ] } } } }";
			break;
		case "visa classic":
			jsonResponse="{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"generic\", \"elements\":[ { \"title\":\"VISA CLASSIC\", \"image_url\":\"https://sella-bot.herokuapp.com/img/visa_classic_bse.gif\", \"subtitle\":\"Con Visa Classic puoi fare acquisti e prelievi in tutto il mondo, con la garanzia della massima sicurezza grazie alla tecnologia Chip & PIN e al servizio Verified by Visa.\\\\n Canone primo anno 31 €\\\\n Gratis oltre i 7000 € di spese/anno\\\\n Sicura con Chip & PIN e Verified by Visa\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/carte/visa-classic.jsp\", \"title\":\"For More Details\" }, { \"type\":\"web_url\", \"url\":\"https://www.sella.it/AuthenticationDelegatedServlet?delegated_service=219&SECODE=ONBOARDING_CL&SEACTION=LOGIN&SEPARAMS=parameterCode=VISA_CLASSIC_BSE\", \"title\":\"On Boarding\" }, { \"type\":\"postback\", \"title\":\"Any Other Query\", \"payload\":\"call our representative\" } ] } ] } } } }";
			break;
		case "mastercard classic":
			jsonResponse="{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"generic\", \"elements\":[ { \"title\":\"MASTERCARD CLASSIC\", \"image_url\":\"https://sella-bot.herokuapp.com/img/mastercard_classic_bse.gif\", \"subtitle\":\"Mastercard Classic è la carta di credito che prevede, se utilizzata a saldo, un addebito unico mensile del tuo speso. E' adatta per la gestione della tua liquidità e ti permette di effettuare pagamenti, anche tramite la funzionalità contatless\\\\n Canone primo anno 31 € \\\\n Gratis oltre i 7000 € di spese/anno \\\\n Sicura con Chip & PIN e Mastercard SecureCode\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/carte/mastercard-classic.jsp\", \"title\":\"For More Details\" }, { \"type\":\"web_url\", \"url\":\"https://www.sella.it/AuthenticationDelegatedServlet?delegated_service=219&SECODE=ONBOARDING_CL&SEACTION=LOGIN&SEPARAMS=parameterCode=MASTERCARD_CLASSIC\", \"title\":\"On Boarding\" }, { \"type\":\"postback\", \"title\":\"Any Other Query\", \"payload\":\"call our representative\" } ] } ] } } } }";
			break;
		case "hype plus":
			jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"generic\", \"elements\":[ { \"title\":\"HYPE PLUS\", \"image_url\":\"https://sella-bot.herokuapp.com/img/hype_bse.gif\", \"subtitle\":\"HYPE Plus è semplice e smart: controlla tutto ciò che serve dal tuo smartphone\\n Puoi ricaricare la tua carta fino a 50.000€ all'anno\\n Prelievi gratuiti da tutti gli ATM in Italia e nel mondo\\n Visualizzi all'istante i movimenti della tua carta\\n La metti in pausa quando vuoi, direttamente dall'app\\n Hai il tuo IBAN attivo in pochi minuti\\n Puoi pagare con il tuo smartphone\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/carte/hype.jsp\", \"title\":\"For More Details\" }, { \"type\":\"web_url\", \"url\":\"https://www.hype.it/signup/request/hype/contact-info\", \"title\":\"On Boarding\" }, { \"type\":\"postback\", \"title\":\"Any Other Query\", \"payload\":\"call our representative\" } ] }  ] } } } }";
			break;
		case "all carte details":
			jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"list\", \"elements\":[ { \"title\":\"CARTE DI DEBITO\", \"image_url\":\"https://sella-bot.herokuapp.com/img/cirrus_maestro_basic.jpg\", \"subtitle\":\"CARTE\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"View\", \"payload\":\"CARTE DI DEBITO\" } ] }, { \"title\":\"CARTE DI CREDITO\", \"image_url\":\"https://sella-bot.herokuapp.com/img/visa_classic_bse.gif\", \"subtitle\":\"VISA\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"View\", \"payload\":\"CARTE DI CREDITO\" } ] },{ \"title\":\"CARTE DI CREDITO\", \"image_url\":\"https://sella-bot.herokuapp.com/img/mastercard_classic_bse.gif\", \"subtitle\":\"MASTER\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"View\", \"payload\":\"CARTE DI CREDITO\" } ] }, { \"title\":\"HYPE PLUS\", \"image_url\":\"https://sella-bot.herokuapp.com/img/hype_bse.gif\", \"subtitle\":\"CARTE\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"View\", \"payload\":\"HYPE PLUS\" } ] } ] } } } }";
			break;		
		case "cirrus maestro basic":
			jsonResponse = "{ \"recipient\": { \"id\": \"recipientId\" }, \"message\": { \"text\": \"You are applying cirrus maestro basic \" } }";
			break;
		case "hypeplus application":
			jsonResponse = "{ \"recipient\": { \"id\": \"recipientId\" }, \"message\": { \"text\": \"You are applying hypeplus application \" } }";
			break;
		case "welcome msg":
			jsonResponse =String.format("{ \"recipient\": { \"id\": \"recipientId\" }, \"message\": { \"text\": \"%s, Welcome to Sella bot, How can I help you? \" } }","Hi "+userDetail.getFirstName()+" "+userDetail.getLastName());
			break;		
		case "consulenza":
			jsonResponse="{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"generic\", \"elements\":[ { \"title\":\"Consulenza investimenti\", \"image_url\":\"https://sella-bot.herokuapp.com/img/consulenza_investimenti.png\", \"subtitle\":\"Il miglior modo che conosciamo per fare banca è dedicarci a te, completamente\\nUn professionista a tua disposizione\\nDedizione nel curare i tuoi interessi\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/investimenti-e-risparmio/consulenza-servizi-investimento.jsp\", \"title\":\"For More Details\" }, { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/investimenti-e-risparmio/consulenza-servizi-investimento.jsp\", \"title\":\"On Boarding\" }, { \"type\":\"postback\", \"title\":\"Any Other Query\", \"payload\":\"call our representative\" } ] } ] } } } }";
		break;
		case "evolution":
			jsonResponse="{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"generic\", \"elements\":[ { \"title\":\"Sella Evolution Gestioni Patrimoniali\", \"image_url\":\"https://sella-bot.herokuapp.com/img/sella_evolution.png\", \"subtitle\":\"Per i tuoi risparmi scegli Sella Evolution,\\nil nuovo servizio pensato per raggiungere i tuoi obiettivi\\nAttraverso un questionario online definiamo il tuo profilo di investitore\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/investimenti-e-risparmio/gestione-patrimoniale.jsp\", \"title\":\"For More Details\" }, { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/investimenti-e-risparmio/gestione-patrimoniale.jsp\", \"title\":\"On Boarding\" }, { \"type\":\"postback\", \"title\":\"Any Other Query\", \"payload\":\"call our representative\" } ] } ] } } } }";
		break;
		case "fondi":
			jsonResponse="{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"generic\", \"elements\":[ { \"title\":\"Fondi d'investimento\", \"image_url\":\"https://sella-bot.herokuapp.com/img/fondi.png\", \"subtitle\":\"L’eccellenza dei fondi sul mercato e le società di gestione che hanno dimostrato nel tempo la migliore strategia\\nPiù di 1800 fondi\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/investimenti-e-risparmio/fondi-sicav-pac.jsp\", \"title\":\"For More Details\" }, { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/investimenti-e-risparmio/fondi-sicav-pac.jsp\", \"title\":\"On Boarding\" }, { \"type\":\"postback\", \"title\":\"Any Other Query\", \"payload\":\"call our representative\" } ] } ] } } } } ";
		break;
		case "pir":
			jsonResponse="{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"generic\", \"elements\":[ { \"title\":\"Investimenti PIR\", \"image_url\":\"https://sella-bot.herokuapp.com/img/investimenti_pir.png\", \"subtitle\":\"I Piani individuali di Risparmio\\nLimiti di investimento sui PIR\\nLa proposta di Banca Sella\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/investimenti-e-risparmio/pir.jsp\", \"title\":\"For More Details\" }, { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/investimenti-e-risparmio/pir.jsp\", \"title\":\"On Boarding\" }, { \"type\":\"postback\", \"title\":\"Any Other Query\", \"payload\":\"call our representative\" } ] } ] } } } }";
		break;
		case "list finanziamenti":
			jsonResponse="{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"generic\", \"elements\":[ { \"title\":\"Prestito Sella.it\", \"image_url\":\"https://sella-bot.herokuapp.com/img/prestito_sella_it.png\", \"subtitle\":\"Finanziamenti\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"View\", \"payload\":\"prestito sella.it\" } ] }, { \"title\":\"Mutui\", \"image_url\":\"https://sella-bot.herokuapp.com/img/mutui.png\", \"subtitle\":\"Finanziamenti\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"View\", \"payload\":\"mutui\" } ] }, { \"title\":\"Prestito d’onore\", \"image_url\":\"https://sella-bot.herokuapp.com/img/loan_student.png\", \"subtitle\":\"Finanziamenti\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"View\", \"payload\":\"prestito d'onore\" } ] }, { \"title\":\"Cessione del quinto\", \"image_url\":\"https://sella-bot.herokuapp.com/img/Cessione_del_quinto.png\", \"subtitle\":\"Finanziamenti\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"View\", \"payload\":\"cessione del quinto\" } ] }, { \"title\":\"Prestidea Ambiente\", \"image_url\":\"https://sella-bot.herokuapp.com/img/prestidea_ambiente.png\", \"subtitle\":\"Finanziamenti\", \"buttons\":[ { \"type\":\"postback\", \"title\":\"View\", \"payload\":\"prestidea ambiente\" } ] } ] } } } }";
		break;
		case "prestito sella.it":
			jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"button\", \"text\":\"For more details please click this button!!! \", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/websella.jsp?fbclid=IwAR3yZoet66tQB-z43deD7yr3cZt8v-yhLiiXPLR0vcX3Kxb9ZZTPcYxN3NI\", \"title\":\"Visit Our Website\" } ] } } } }";
		break;
		case "mutui":
			jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"button\", \"text\":\"For more details please click this button!!! \", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/websella.jsp?fbclid=IwAR3yZoet66tQB-z43deD7yr3cZt8v-yhLiiXPLR0vcX3Kxb9ZZTPcYxN3NI\", \"title\":\"Visit Our Website\" } ] } } } }";
		break;
		case "prestito d'onore":
			jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"button\", \"text\":\"For more details please click this button!!! \", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/websella.jsp?fbclid=IwAR3yZoet66tQB-z43deD7yr3cZt8v-yhLiiXPLR0vcX3Kxb9ZZTPcYxN3NI\", \"title\":\"Visit Our Website\" } ] } } } }";
		break;
		case "cessione del quinto":
			jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"button\", \"text\":\"For more details please click this button!!! \", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/websella.jsp?fbclid=IwAR3yZoet66tQB-z43deD7yr3cZt8v-yhLiiXPLR0vcX3Kxb9ZZTPcYxN3NI\", \"title\":\"Visit Our Website\" } ] } } } }";
		break;
		case "prestidea ambiente":
			jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"button\", \"text\":\"For more details please click this button!!! \", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it/banca-online/privati/conti-e-carte/websella.jsp?fbclid=IwAR3yZoet66tQB-z43deD7yr3cZt8v-yhLiiXPLR0vcX3Kxb9ZZTPcYxN3NI\", \"title\":\"Visit Our Website\" } ] } } } }";
		break;
		case "call our representative":
			jsonResponse = String.format(
					"{ \"recipient\": { \"id\": \"recipientId\" }, \"message\": { \"text\": \"%s\" } }",
					"Hi " + userDetail.getFirstName() + " " + userDetail.getLastName()+", puoi contattare l'assistenza clienti al numero 800.142.142 (per le chiamate da rete fissa nazionale) oppure al numero 0039.015.24.34.617 (per le chiamate dall'estero e da telefono cellulare) attivi dal luneda al venerda dalle ore 8.00 alle 21.00. Hai bisogno di altre informazioni?");
			break;
		case "bye":
		case "":
			jsonResponse = String.format(
					"{ \"recipient\": { \"id\": \"recipientId\" }, \"message\": { \"text\": \"%s\" } }",
					"Grazie " + userDetail.getFirstName() + " " + userDetail.getLastName()+", Speriamo di aver risposto alle tue domamde.  Buona giornata ");
		break;
		case "test1":			
			jsonResponse="{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\": { \"attachment\": { \"type\": \"template\", \"payload\": { \"template_type\": \"list\", \"top_element_style\": \"compact\", \"elements\": [ { \"title\": \"Classic T-Shirt Collection\", \"subtitle\": \"See all our colors\", \"image_url\": \"https://sella-bot.herokuapp.com/img/hype_bse.gif\", \"buttons\": [ { \"title\": \"View\", \"type\": \"web_url\", \"url\": \"https://www.sella.it/banca-online/privati/conti-e-carte/carte/cirrus-maestro-basic.jsp\", \"messenger_extensions\": true, \"webview_height_ratio\": \"tall\", \"fallback_url\": \"https://www.sella.it/banca-online/privati/index.jsp\" } ] }, { \"title\": \"Classic Wgute T-Shirt\", \"subtitle\": \"See all other colors\", \"default_action\": { \"type\": \"web_url\", \"url\": \"https://www.sella.it/banca-online/privati/index.jsp\", \"messenger_extensions\": false, \"webview_height_ratio\": \"tall\" } }, { \"title\": \"Classic Blue T-Shirt\", \"image_url\": \"https://sella-bot.herokuapp.com/img/hype_bse.gif\", \"subtitle\": \"100% Cotton, 200% Comfortable\", \"default_action\": { \"type\": \"web_url\", \"url\": \"https://www.sella.it/banca-online/privati/index.jsp\", \"messenger_extensions\": true, \"webview_height_ratio\": \"tall\", \"fallback_url\": \"https://www.sella.it/banca-online/privati/index.jsp\" }, \"buttons\": [ { \"title\": \"Shop Now\", \"type\": \"web_url\", \"url\": \"https://sella-bot.herokuapp.com/img/hype_bse.gif\", \"messenger_extensions\": true, \"webview_height_ratio\": \"tall\", \"fallback_url\": \"https://www.sella.it/banca-online/privati/index.jsp\" } ] }, { \"title\": \"Classic Blue T-Shirt\", \"image_url\": \"https://sella-bot.herokuapp.com/img/hype_bse.gif\", \"subtitle\": \"100% Cotton, 200% Comfortable\", \"default_action\": { \"type\": \"web_url\", \"url\": \"https://www.sella.it/banca-online/privati/index.jsp\", \"messenger_extensions\": true, \"webview_height_ratio\": \"tall\", \"fallback_url\": \"https://www.sella.it/banca-online/privati/index.jsp\" }, \"buttons\": [ { \"title\": \"Shop Now\", \"type\": \"web_url\", \"url\": \"https://sella-bot.herokuapp.com/img/hype_bse.gif\", \"messenger_extensions\": true, \"webview_height_ratio\": \"tall\", \"fallback_url\": \"https://www.sella.it/banca-online/privati/index.jsp\" } ] } ], \"buttons\": [ { \"title\": \"View More\", \"type\": \"postback\", \"payload\": \"payload\" } ] } } } }";
		break;
		default:
			jsonResponse = "{ \"recipient\":{ \"id\":\"recipientId\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"button\", \"text\":\"Non sono riuscita a comprendere la richiesta, se lo desideri ti posso mettere in contatto con un nostro operatore della assitenza.\", \"buttons\":[ { \"type\":\"phone_number\", \"title\":\"Clicca qui per proseguire\", \"payload\":\"+02224410023\" } ] } } } }";
			break; 
		}
		jsonResponse = jsonResponse.replace("recipientId", senderId);
		logger.info("jsonresponse>>>>{}", jsonResponse);
		logger.info("tsttstset");
		return jsonResponse;
	}
	
	private static String getActualKeyword(String textSearch) {
		String searchString=textSearch;
		if(textSearch.equals("first hand shake")) {
			searchString="welcome msg";
		}
		else if( textSearch.contains("product")) {
			searchString="view product";
		}else if(textSearch.contains("conti") || textSearch.contains("account") || textSearch.contains("canti")) {
			searchString="list conti";
		}else if((textSearch.contains("carte") || textSearch.contains("card")|| textSearch.contains("carti")) && !(textSearch.contains("debit")||textSearch.contains("credit") || textSearch.contains("all carte details") || textSearch.contains("master"))) {
			searchString="list carte";
		}else if(textSearch.contains("websella") || textSearch.contains("websela")) {
			searchString="websella";
		}else if(textSearch.contains("debit")) {
			searchString="carte di debito";
		}else if(textSearch.contains("credit")) {
			searchString="carte di credito";
		}else if(textSearch.contains("hype")) {
			searchString="hype plus";
		}else if(textSearch.contains("visa")) {
			searchString="visa classic";
		}else if(textSearch.contains("master")) {
			searchString="mastercard classic";
		}else if(textSearch.contains("invest")) {
			searchString="list investimenti";
		}else if(textSearch.contains("morning") || textSearch.contains("noon")|| textSearch.contains("evening")||textSearch.contains("ciao")||textSearch.contains("caio")||textSearch.contains("ciao") || textSearch.contains("hi")|| textSearch.contains("hai") || textSearch.contains("hello") || textSearch.contains("helo")) {
			searchString="hi";
		}else if(textSearch.contains("thank") || textSearch.contains("bye")|| textSearch.contains("grazie")) {
			searchString="bye";
		}
		else if(textSearch.contains("consulenza")) {
			searchString="consulenza";
		}
		else if(textSearch.contains("evolution")) {
			searchString="evolution";
		}
		else if(textSearch.contains("fondi")) {
			searchString="fondi";
		}
		else if(textSearch.contains("pir")) {
			searchString="pir";
		}
		logger.info("The actual keyword is{}",searchString);
		return searchString;
	}
}

