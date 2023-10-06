package servlets.simulation.setters.termination;

import api.Routes;
import config.Configuration;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import json.JsonParser;
import json.Keys;
import json.JsonMapper;
import modules.Constants;
import other.ResponseDTO;
import other.TerminationDTO;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@WebServlet(Routes.SET_TERMINATION)
public class SetTerminationServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(Constants.JSON_CONTENT_TYPE);

        Map<String, Object> requestBodyMap = JsonParser.getRequestBodyMap(req.getReader());
        String simulationUuid = requestBodyMap.get(Keys.UUID_KEY).toString();
        TerminationDTO termination = JsonMapper.objectMapper.readValue(
                requestBodyMap.get(Keys.TERMINATION_KEY).toString(),
                TerminationDTO.class
        );

        ResponseDTO responseDTO = Configuration.api.setTermination(simulationUuid, termination);
        resp.setStatus(responseDTO.getStatus());

        if (!Objects.isNull(responseDTO.getErrorDescription()))
            resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, responseDTO.getErrorDescription().getCause()));
    }
}
