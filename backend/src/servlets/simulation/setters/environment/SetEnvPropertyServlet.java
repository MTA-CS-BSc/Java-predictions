package servlets.simulation.setters.environment;

import api.Routes;
import config.Configuration;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import json.JsonParser;
import json.Keys;
import json.SingletonObjectMapper;
import modules.Constants;
import other.PropertyDTO;
import other.ResponseDTO;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@WebServlet(Routes.SET_ENV_PROP_VALUE)
public class SetEnvPropertyServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(Constants.JSON_CONTENT_TYPE);

        Map<String, Object> requestBodyMap = JsonParser.getRequestBodyMap(req.getReader());
        String simulationUuid = requestBodyMap.get(Keys.UUID_KEY).toString();
        String value = requestBodyMap.get(Keys.VALUE_KEY).toString();
        PropertyDTO propertyDTO = SingletonObjectMapper.objectMapper.readValue(
                requestBodyMap.get(Keys.PROPERTY_DTO_KEY).toString(),
                PropertyDTO.class
        );

        ResponseDTO responseDTO = Configuration.api.setEnvironmentVariable(simulationUuid, propertyDTO, value);
        resp.setStatus(responseDTO.getStatus());

        if (!Objects.isNull(responseDTO.getErrorDescription()))
            resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, responseDTO.getErrorDescription().getCause()));
    }
}
