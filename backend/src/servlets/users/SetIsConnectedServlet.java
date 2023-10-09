package servlets.users;

import api.ApiConstants;
import api.Routes;
import config.Configuration;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import json.JsonParser;
import json.Keys;
import other.ResponseDTO;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@WebServlet(Routes.SET_IS_CONNECTED)
public class SetIsConnectedServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> requestBodyMap = JsonParser.getRequestBodyMap(req.getReader());
        resp.setContentType(ApiConstants.JSON_CONTENT_TYPE);

        String username = requestBodyMap.get(Keys.USERNAME_KEY).toString();
        boolean isConnected = JsonParser.objectMapper.readValue(
                requestBodyMap.get(Keys.IS_CONNECTED_KEY).toString(),
                Boolean.class
        );

        ResponseDTO responseDTO = Configuration.api.setUserConnected(username, isConnected);
        resp.setStatus(responseDTO.getStatus());

        if (!Objects.isNull(responseDTO.getErrorDescription()))
            resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, responseDTO.getErrorDescription().getCause()));
    }
}
