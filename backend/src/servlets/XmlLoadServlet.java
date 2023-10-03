package servlets;

import config.Configuration;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import json.JsonParser;
import modules.Constants;
import other.ResponseDTO;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@MultipartConfig
@WebServlet("/xml/load")
public class XmlLoadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(Configuration.JSON_CONTENT_TYPE);

        try {
            Part filePart = request.getPart("fileField");
            InputStream fileContent = filePart.getInputStream();
            ResponseDTO responseDTO = Configuration.api.loadXml(fileContent);

            if (responseDTO.getStatus() == Constants.API_RESPONSE_OK) {
                response.setStatus(Constants.API_RESPONSE_OK);
                response.getWriter().write(JsonParser.toJson("data", "OK"));
            }

            else {
                response.setStatus(Constants.API_RESPONSE_BAD_REQUEST);

                if (!Objects.isNull(responseDTO.getErrorDescription()))
                    response.getWriter().write(JsonParser.toJson("detail", responseDTO.getErrorDescription().getCause()));
            }
        }

        catch (IOException e) {
            response.setStatus(Constants.API_RESPONSE_SERVER_ERROR);
        } catch (Exception se) {
            response.setStatus(Constants.API_RESPONSE_BAD_REQUEST);
        }
    }
}
