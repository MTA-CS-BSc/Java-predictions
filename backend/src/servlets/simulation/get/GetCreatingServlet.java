package servlets.simulation.get;

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
import types.TypesUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@WebServlet(Routes.CREATING_SIMULATION)
public class GetCreatingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(ApiConstants.JSON_CONTENT_TYPE);
        Map<String, String[]> requestBody = req.getParameterMap();
        String simulationUuid = requestBody.get(Keys.UUID_KEY)[0];

        if (!TypesUtils.isNullOrEmpty(simulationUuid)) {
            ResponseDTO responseDTO = Configuration.api.getCreatingSimulation(simulationUuid);
            resp.setStatus(responseDTO.getStatus());

            if (!Objects.isNull(responseDTO.getErrorDescription()))
                resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, responseDTO.getErrorDescription().getCause()));

            else
                resp.getWriter().write(responseDTO.getData());
        }
    }
}
