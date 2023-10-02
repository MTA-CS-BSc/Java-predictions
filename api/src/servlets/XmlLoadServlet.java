package servlets;

import config.Configuration;
import other.ResponseDTO;
import modules.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@WebServlet(name = "XmlLoadServlet", urlPatterns = "/xml/load")
public class XmlLoadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("fileField");
        InputStream fileContent = filePart.getInputStream();

        response.setContentType("text/plain");
        ResponseDTO responseDTO = null;

        try {
            responseDTO = Configuration.api.loadXml(fileContent);
            response.setStatus(Constants.API_RESPONSE_OK);
            response.getWriter().write("OK");
        } catch (Exception e) {
            response.setStatus(Constants.API_RESPONSE_SERVER_ERROR);

            if (!Objects.isNull(responseDTO) && !Objects.isNull(responseDTO.getErrorDescription()))
                response.getWriter().write(responseDTO.getErrorDescription().getCause());
        }
    }
}
