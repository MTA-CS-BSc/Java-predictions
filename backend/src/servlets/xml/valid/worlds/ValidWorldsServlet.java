package servlets.xml.valid.worlds;

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
import java.util.Objects;

@WebServlet(Routes.XML_VALID_WORLDS)
public class ValidWorldsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(Constants.JSON_CONTENT_TYPE);

        ResponseDTO responseDTO = Configuration.api.getAllValidWorldsNames();
        resp.setStatus(responseDTO.getStatus());

        if (!Objects.isNull(responseDTO.getErrorDescription()))
            resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, responseDTO.getErrorDescription().getCause()));

        else
            resp.getWriter().write(responseDTO.getData());
    }
}
