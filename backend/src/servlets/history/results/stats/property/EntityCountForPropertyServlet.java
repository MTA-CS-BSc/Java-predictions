package servlets.history.results.stats.property;

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

@WebServlet(Routes.ENTITITY_COUNT_FOR_PROP)
public class EntityCountForPropertyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(ApiConstants.JSON_CONTENT_TYPE);
        Map<String, String[]> params = req.getParameterMap();

        String propertyName = params.get(Keys.PROPERTY_NAME_KEY)[0];
        String entityName = params.get(Keys.ENTITY_NAME_KEY)[0];
        String uuid = params.get(Keys.UUID_KEY)[0];

        if (TypesUtils.isNullOrEmpty(entityName) || TypesUtils.isNullOrEmpty(propertyName) || TypesUtils.isNullOrEmpty(uuid)) {
            resp.setStatus(ApiConstants.API_RESPONSE_BAD_REQUEST);
            resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, "One or more keys are missing"));
        }

        ResponseDTO responseDTO = Configuration.api.getEntitiesCountForProp(uuid, entityName, propertyName);
        resp.setStatus(responseDTO.getStatus());

        if (!Objects.isNull(responseDTO.getErrorDescription()))
            resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, responseDTO.getErrorDescription().getCause()));

        else
            resp.getWriter().write(responseDTO.getData());
    }
}
