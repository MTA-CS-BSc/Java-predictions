package servlets.allocations;

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
import other.TerminationDTO;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@WebServlet(Routes.ALLOCATIONS_HISTORY)
public class AllocationRequestsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(Constants.JSON_CONTENT_TYPE);

        ResponseDTO responseDTO = Configuration.api.getAllocationRequests();
        resp.setStatus(responseDTO.getStatus());

        if (resp.getStatus() == Constants.API_RESPONSE_OK)
            resp.getWriter().write(responseDTO.getData());

        else if (!Objects.isNull(responseDTO.getErrorDescription()))
            resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, responseDTO.getErrorDescription().getCause()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(Constants.JSON_CONTENT_TYPE);

        Map<String, Object> requestBodyMap = JsonParser.getRequestBodyMap(req.getReader());

        String initialWorldName = requestBodyMap.get(Keys.INITIAL_WORLD_NAME_KEY).toString();
        String createdUser = requestBodyMap.get(Keys.CREATED_USER_KEY).toString();
        int requestedExecutions = Integer.parseInt(requestBodyMap.get(Keys.REQUESTED_EXECUTIONS_KEY).toString());

        // Catch is for postman check
        try {
            TerminationDTO termination = JsonParser.objectMapper.readValue(
                    requestBodyMap.get(Keys.TERMINATION_KEY).toString(),
                    TerminationDTO.class
            );

            ResponseDTO responseDTO = Configuration.api.createAllocationRequest(initialWorldName,
                    requestedExecutions, createdUser, termination);

            resp.setStatus(responseDTO.getStatus());

            if (!Objects.isNull(responseDTO.getErrorDescription()))
                resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, responseDTO.getErrorDescription().getCause()));
        }

        catch (Exception e) {
            TerminationDTO termination = JsonParser.objectMapper.readValue(
                    JsonParser.objectMapper.writeValueAsString(requestBodyMap.get(Keys.TERMINATION_KEY)),
                    TerminationDTO.class
            );

            ResponseDTO responseDTO = Configuration.api.createAllocationRequest(initialWorldName,
                    requestedExecutions, createdUser, termination);

            resp.setStatus(responseDTO.getStatus());

            if (!Objects.isNull(responseDTO.getErrorDescription()))
                resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, responseDTO.getErrorDescription().getCause()));
        }
    }
}
