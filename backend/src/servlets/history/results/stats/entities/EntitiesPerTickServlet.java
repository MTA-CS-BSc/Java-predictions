package servlets.history.results.stats.entities;

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
import java.util.Map;
import java.util.Objects;

@WebServlet(Routes.ENTITIES_AMOUNTS_PER_TICK)
public class EntitiesPerTickServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(Constants.JSON_CONTENT_TYPE);
        Map<String, String[]> params = req.getParameterMap();
        String uuid = params.get(Keys.UUID_KEY)[0];

        if (Objects.isNull(uuid) || uuid.isEmpty()) {
            resp.setStatus(Constants.API_RESPONSE_BAD_REQUEST);
            resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, "No uuid key found in params"));
        }

        else {
            ResponseDTO responseDTO = Configuration.api.getEntitiesAmountsPerTick(uuid);
            resp.setStatus(responseDTO.getStatus());

            if (!Objects.isNull(responseDTO.getErrorDescription()))
                resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, responseDTO.getErrorDescription().getCause()));

            else
                resp.getWriter().write(responseDTO.getData());
        }

    }
}
