package servlets.threadpool;

import api.ApiConstants;
import api.Routes;
import config.Configuration;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import json.JsonParser;
import json.Keys;
import other.ResponseDTO;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@WebServlet(Routes.THREADPOOL)
public class ThreadsAmountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(ApiConstants.JSON_CONTENT_TYPE);

        try {
            ResponseDTO responseDTO = Configuration.api.getThreadsAmount();
            resp.setStatus(responseDTO.getStatus());

            if (!Objects.isNull(responseDTO.getErrorDescription()))
                resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, responseDTO.getErrorDescription().getCause()));

            else {
                int threadsAmount = JsonParser.objectMapper.readValue(responseDTO.getData(), Integer.class);
                resp.getWriter().write(threadsAmount);
            }
        } catch (Exception e) {
            resp.setStatus(ApiConstants.API_RESPONSE_SERVER_ERROR);
            resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, e.getMessage()));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(ApiConstants.JSON_CONTENT_TYPE);
        Map<String, Object> requestBody = JsonParser.getRequestBodyMap(req.getReader());

        if (!Objects.isNull(requestBody.get(Keys.THREADS_AMOUNT_KEY))) {
            int amount = Integer.parseInt(requestBody.get(Keys.THREADS_AMOUNT_KEY).toString());

            if (amount <= 0) {
                resp.setStatus(ApiConstants.API_RESPONSE_BAD_REQUEST);
                resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, "Amount should be positive"));
            }

            else {
                ResponseDTO responseDTO = Configuration.api.setThreadsAmount(amount);
                resp.setStatus(responseDTO.getStatus());

                if (!Objects.isNull(responseDTO.getErrorDescription()))
                    resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, responseDTO.getErrorDescription().getCause()));
            }
        }

        else {
            resp.setStatus(ApiConstants.API_RESPONSE_SERVER_ERROR);
            resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, "Unknown"));
        }
    }
}
