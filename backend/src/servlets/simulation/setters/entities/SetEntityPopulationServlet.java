package servlets.simulation.setters.entities;

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

@WebServlet(Routes.SET_ENTITY_INIT_POPULATION)
public class SetEntityPopulationServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(ApiConstants.JSON_CONTENT_TYPE);

        Map<String, Object> requestBodyMap = JsonParser.getRequestBodyMap(req.getReader());
        String simulationUuid = requestBodyMap.get(Keys.UUID_KEY).toString();
        int value = JsonParser.objectMapper.readValue(requestBodyMap.get(Keys.VALUE_KEY).toString(), Integer.class);
        String entityName = requestBodyMap.get(Keys.ENTITY_NAME_KEY).toString();

        ResponseDTO responseDTO = Configuration.api.setEntityInitialPopulation(simulationUuid, entityName, value);
        resp.setStatus(responseDTO.getStatus());

        if (!Objects.isNull(responseDTO.getErrorDescription()))
            resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, responseDTO.getErrorDescription().getCause()));
    }
}
