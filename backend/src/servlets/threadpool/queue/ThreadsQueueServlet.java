package servlets.threadpool.queue;

import api.Routes;
import config.Configuration;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import json.JsonParser;
import json.Keys;
import json.SingletonObjectMapper;
import modules.Constants;
import other.ResponseDTO;

import java.io.IOException;
import java.util.Objects;

@WebServlet(Routes.THREADPOOL_QUEUE)
public class ThreadsQueueServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(Constants.JSON_CONTENT_TYPE);
        ResponseDTO responseDTO = Configuration.api.getQueueManagementDetails();

        if (responseDTO.getStatus() != Constants.API_RESPONSE_OK) {
            resp.setStatus(responseDTO.getStatus());

            if (!Objects.isNull(responseDTO.getErrorDescription()))
                resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, responseDTO.getErrorDescription().getCause()));
        }

        else {
            String queueMgmtData = SingletonObjectMapper.objectMapper.readValue(responseDTO.getData(), String.class);
            resp.getWriter().write(JsonParser.toJson(Keys.VALID_RESPONSE_KEY, queueMgmtData));
        }

    }
}
