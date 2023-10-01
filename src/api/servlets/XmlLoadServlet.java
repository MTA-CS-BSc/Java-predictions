package api.servlets;

import api.Configuration;
import helpers.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;

@WebServlet(name = "XmlLoadServlet", urlPatterns = { "/xml/load" })
public class XmlLoadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("fileField");
        InputStream fileContent = filePart.getInputStream();

        try {
            response.setContentType("text/plain");

            Configuration.api.loadXml(fileContent);

            response.setStatus(Constants.API_RESPONSE_OK);
            response.getWriter().write("OK");
        } catch (JAXBException e) {
            response.setStatus(Constants.API_RESPONSE_SERVER_ERROR);
            response.getWriter().write("Failed to parse file");
        }
    }
}
