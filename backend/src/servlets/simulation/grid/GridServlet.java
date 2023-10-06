package servlets.simulation.grid;

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

@WebServlet(Routes.GRID)
public class GridServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(ApiConstants.JSON_CONTENT_TYPE);
        Map<String, String[]> params = req.getParameterMap();
        String simulationUuid = params.get(Keys.UUID_KEY)[0];

        if (Objects.isNull(simulationUuid) || simulationUuid.isEmpty()) {
            resp.setStatus(ApiConstants.API_RESPONSE_BAD_REQUEST);
            resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, "No uuid key found in params"));
        }

        ResponseDTO responseDTO = Configuration.api.getGrid(simulationUuid);
        resp.setStatus(responseDTO.getStatus());

        if (!Objects.isNull(responseDTO.getErrorDescription()))
            resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, responseDTO.getErrorDescription().getCause()));

        else
            resp.getWriter().write(responseDTO.getData());
    }
}
