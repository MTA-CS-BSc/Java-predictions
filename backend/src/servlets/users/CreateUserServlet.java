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

@WebServlet(Routes.CREATE_USER)
public class CreateUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(ApiConstants.JSON_CONTENT_TYPE);

        Map<String, Object> requestBodyMap = JsonParser.getRequestBodyMap(req.getReader());

        String username = requestBodyMap.get(Keys.USERNAME_KEY).toString();
        boolean isConnected = JsonParser.objectMapper.readValue(
                requestBodyMap.get(Keys.IS_CONNECTED_KEY).toString(),
                Boolean.class
        );

        ResponseDTO responseDTO = Configuration.api.createUser(username, isConnected);
        resp.setStatus(responseDTO.getStatus());

        if (!Objects.isNull(responseDTO.getErrorDescription()))
            resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, responseDTO.getErrorDescription().getCause()));
    }
}
