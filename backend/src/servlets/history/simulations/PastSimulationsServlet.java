package servlets.history.simulations;

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

import java.io.IOException;

@WebServlet(Routes.SIMULATIONS_HISTORY)
public class PastSimulationsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(Constants.JSON_CONTENT_TYPE);

        ResponseDTO responseDTO = Configuration.api.getPastSimulations();
        resp.setStatus(responseDTO.getStatus());

        if (resp.getStatus() == Constants.API_RESPONSE_OK)
            resp.getWriter().write(responseDTO.getData());

        else
            resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, responseDTO.getErrorDescription().getCause()));
    }
}
