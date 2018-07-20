package com.bolsadeideas.springboot.app.auth.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		SessionFlashMapManager flashMapManager = new SessionFlashMapManager();

		FlashMap flashMap = new FlashMap();

		flashMap.put("error", "ERROR: " + exception.getMessage());

		flashMapManager.saveOutputFlashMap(flashMap, request, response);

		super.onAuthenticationFailure(request, response, exception);
	}

}
