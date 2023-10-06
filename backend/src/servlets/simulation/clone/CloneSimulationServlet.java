package servlets.simulation.clone;

import api.Routes;
import config.Configuration;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import json.JsonParser;
import json.Keys;
import modules.Constants;
import other.ResponseDTO;
import types.TypesUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@WebServlet(Routes.CLONE_SIMULATION)
public class CloneSimulationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(Constants.JSON_CONTENT_TYPE);
        Map<String, Object> requestBody = JsonParser.getRequestBodyMap(req.getReader());
        String requestUuid = requestBody.get(Keys.UUID_KEY).toString();

        if (!TypesUtils.isNullOrEmpty(requestUuid)) {
            ResponseDTO responseDTO = Configuration.api.cloneSimulation(requestUuid);
            resp.setStatus(responseDTO.getStatus());

            if (!Objects.isNull(responseDTO.getErrorDescription()))
                resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, responseDTO.getErrorDescription().getCause()));

            else
                resp.getWriter().write(responseDTO.getData());
        }
    }
}
