package servlets.simulation.setters.byStep;

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
import other.ResponseDTO;
import types.ByStep;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@WebServlet(Routes.SET_BY_STEP)
public class SetByStepServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(Constants.JSON_CONTENT_TYPE);

        Map<String, Object> requestBodyMap = JsonParser.getRequestBodyMap(req.getReader());
        String simulationUuid = requestBodyMap.get(Keys.UUID_KEY).toString();
        ByStep value = SingletonObjectMapper.objectMapper.readValue(
                requestBodyMap.get(Keys.VALUE_KEY).toString(),
                ByStep.class
        );

        ResponseDTO responseDTO = Configuration.api.setByStep(simulationUuid, value);
        resp.setStatus(responseDTO.getStatus());

        if (!Objects.isNull(responseDTO.getErrorDescription()))
            resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, responseDTO.getErrorDescription().getCause()));
    }
}
