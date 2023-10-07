package servlets.simulation.get;

import api.Routes;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(Routes.SIMULATION_ENV_PROPS)
public class GetEnvPropsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setContentType(ApiConstants.JSON_CONTENT_TYPE);
//        Map<String, Object> requestBody = JsonParser.getRequestBodyMap(req.getReader());
//        String simulationUuid = requestBody.get(Keys.UUID_KEY).toString();
//
//        if (!TypesUtils.isNullOrEmpty(simulationUuid)) {
//            ResponseDTO responseDTO = Configuration.api.getEnvironmentProperties(simulationUuid);
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
