package com.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

@Component
public final class RestTemplateWrapper  implements IRestTempalteWrapper{

    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateWrapper.class);

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    
   // private Class<T> clazz = null;
    
    public  static final RestTemplateWrapper INSTANCE = new RestTemplateWrapper(RestTemplateHelper.getInstance());
    
    private RestTemplateWrapper(RestTemplateHelper restTemplate) {
        this.restTemplate = RestTemplateHelper.getInstance().getRestTemplate();
        this.objectMapper = RestTemplateHelper.getInstance().getObjectMapper();
        //this.clazz = t;
    }
    
    

    /**
     * @param clazz
     * @param url
     * @param params
     * @return
     */
    public <T> T getForEntity(Class<T> clazz,String url, Map<String,String> params) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, params);
            return converter(clazz, response);
        } catch (HttpClientErrorException exception) {
            httpClientErrorExceptionHandler(url, exception);
        }
        return null;
    }
    
    /**
     * @param clazz
     * @param url
     * @param uriVariables
     * @return
     */
    public <T> T getForEntity(Class<T> clazz, String url, Object... uriVariables) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, uriVariables);
            return converter(clazz, response);
        } catch (HttpClientErrorException exception) {
            httpClientErrorExceptionHandler(url, exception);
        }
        return null;
    }
    
    /**
     * @param clazz
     * @param url
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
	public <T> T getForObject(Class<T> clazz, String url, Map<String,String> params) {
        try {
            return (T) restTemplate.getForObject(url, String.class, params);
        } catch (HttpClientErrorException exception) {
            httpClientErrorExceptionHandler(url, exception);
        }
        return null;
    }
    
    /**
     * @param clazz
     * @param url
     * @param uriVariables
     * @return
     */
    @SuppressWarnings("unchecked")
   	public <T> T getForObject(Class<T> clazz, String url, Object... uriVariables) {
           try {
               return (T) restTemplate.getForObject(url, String.class, uriVariables);
           } catch (HttpClientErrorException exception) {
               httpClientErrorExceptionHandler(url, exception);
           }
           return null;
       }

    /**
     * @param clazz
     * @param url
     * @param uriVariables
     * @return
     */
    public <T> List<T> getForList(Class<T> clazz, String url, Object... uriVariables) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, uriVariables);
            CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
            return readValue(response, collectionType);
        } catch (HttpClientErrorException exception) {
            httpClientErrorExceptionHandler(url, exception);
        }
        return null;
    }

    /**
     * @param clazz
     * @param url
     * @param body
     * @param uriVariables
     * @return
     */
    public <T, R> T postForEntity(Class<T> clazz, String url, R body, Object... uriVariables) {
        HttpEntity<R> request = new HttpEntity<>(body);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class, uriVariables);
        JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
        return readValue(response, javaType);
    }
    
    /**
     * @param clazz
     * @param url
     * @param body
     * @param params
     * @return
     */
    public <T, R> T postForEntity(Class<T> clazz, String url, R body, Map<String,String> params) {
        HttpEntity<R> request = new HttpEntity<>(body);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class, params);
        JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
        return readValue(response, javaType);
    }
    
    /**
     * @param clazz
     * @param url
     * @param body
     * @param uriVariables
     * @return
     */
    public <T, R> T putForEntity(Class<T> clazz, String url, R body, Object... uriVariables) {
        HttpEntity<R> request = new HttpEntity<>(body);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class, uriVariables);
        JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
        return readValue(response, javaType);
    }

    /**
     * @param url
     * @param uriVariables
     */
    public void delete(String url, Object... uriVariables) {
        try {
            restTemplate.delete(url, uriVariables);
        } catch (RestClientException exception) {
            LOGGER.error(exception.getMessage());
        }
    }

    /**
     * @param response
     * @param javaType
     * @return
     */
    private <T> T readValue(ResponseEntity<String> response, JavaType javaType) {
        T result = null;
        if (response.getStatusCode() == HttpStatus.OK ||
                response.getStatusCode() == HttpStatus.CREATED) {
            try {
                result = objectMapper.readValue(response.getBody(), javaType);
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        } else {
            LOGGER.error("No data found {}", response.getStatusCode());
        }
        return result;
    }

    /**
	 * @param url
	 * @param exception
	 */
	private void httpClientErrorExceptionHandler(String url, HttpClientErrorException exception) {
		
		if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
		    LOGGER.error("No data found {}", url);
		} else {
		    LOGGER.error("rest client exception", exception.getMessage());
		}
	}
	
	/**
	 * @param clazz
	 * @param response
	 * @return
	 */
	private <T> T converter(Class<T> clazz, ResponseEntity<String> response) {
		LOGGER.info("response {}", response);
		JavaType javaType = null;
			javaType = objectMapper.getTypeFactory().constructType(clazz);
		return readValue(response, javaType);
	}
	
	public static boolean isError(HttpStatus status) {
		HttpStatus.Series series = status.series();
		return !status.equals(HttpStatus.NOT_FOUND) && (HttpStatus.Series.SERVER_ERROR.equals(series) || HttpStatus.Series.CLIENT_ERROR
				.equals(series));
	}
	
	//NOTE: in future we can try to use RestError... 
	
	public static class RestError {

		private HttpStatus status;
		private Integer code;
		private String message;
		private String developerMessage;
		private String moreInfoUrl;

		public RestError() { }

		public RestError(HttpStatus status, Integer code, String message, String developerMessage,
				String moreInfoUrl) {
			if (status == null){
				throw new NullPointerException("HttpStatus argument cannot be null.");
			}
			this.status = status;
			this.code = code;
			this.message = message;
			this.developerMessage = developerMessage;
			this.moreInfoUrl = moreInfoUrl;
		}

		public HttpStatus getStatus() {
			return status;
		}

		public Integer getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}

		public String getDeveloperMessage() {
			return developerMessage;
		}

		public String getMoreInfoUrl() {
			return moreInfoUrl;
		}

	}
}
