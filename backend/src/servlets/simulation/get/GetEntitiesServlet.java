package servlets.simulation.get;

import api.Routes;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(Routes.SIMULATION_ENTITIES)
public class GetEntitiesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setContentType(ApiConstants.JSON_CONTENT_TYPE);
//        Map<String, Object> requestBody = JsonParser.getRequestBodyMap(req.getReader());
//        String simulationUuid = requestBody.get(Keys.UUID_KEY).toString();
//        boolean isInitial = JsonParser.objectMapper.readValue(
//                requestBody.get(Keys.IS_INITIAL_KEY).toString(),
//                Boolean.class
//        );
//
//        if (!TypesUtils.isNullOrEmpty(simulationUuid)) {
//            ResponseDTO responseDTO = Configuration.api.getEntities(simulationUuid, isInitial);
//            resp.setStatus(responseDTO.getStatus());
//
//            if (!Objects.isNull(responseDTO.getErrorDescription()))
//                resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, responseDTO.getErrorDescription().getCause()));
//
//            else
//                resp.getWriter().write(responseDTO.getData());
//        }
    }
}
