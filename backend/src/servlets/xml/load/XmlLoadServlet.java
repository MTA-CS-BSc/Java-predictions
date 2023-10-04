package servlets.xml.load;

import config.Configuration;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import json.JsonParser;
import json.Keys;
import modules.Constants;
import other.ResponseDTO;

import java.io.InputStream;
import java.util.Objects;

@MultipartConfig
@WebServlet("/xml/load")
public class XmlLoadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType(Constants.JSON_CONTENT_TYPE);

        try {
            Part filePart = req.getPart(Keys.XML_UPLOAD_KEY);
            InputStream fileContent = filePart.getInputStream();
            ResponseDTO responseDTO = Configuration.api.loadXml(fileContent);
            resp.setStatus(responseDTO.getStatus());

            if (!Objects.isNull(responseDTO.getErrorDescription()))
                resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, responseDTO.getErrorDescription().getCause()));
        } catch (Exception se) {
            resp.setStatus(Constants.API_RESPONSE_BAD_REQUEST);
        }
    }
}
