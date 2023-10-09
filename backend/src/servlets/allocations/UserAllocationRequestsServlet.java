package servlets.allocations;

import api.ApiConstants;
import api.Routes;
import config.Configuration;
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

@WebServlet(Routes.ALLOCATIONS_HISTORY_FOR_USER)
public class UserAllocationRequestsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(ApiConstants.JSON_CONTENT_TYPE);
        Map<String, String[]> params = req.getParameterMap();
        String username = params.get(Keys.USERNAME_KEY)[0];

        ResponseDTO responseDTO = Configuration.api.getAllocationRequestsForUser(username);
        resp.setStatus(responseDTO.getStatus());

        if (!Objects.isNull(responseDTO.getErrorDescription()))
            resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, responseDTO.getErrorDescription().getCause()));

        else
            resp.getWriter().write(responseDTO.getData());
    }
}
