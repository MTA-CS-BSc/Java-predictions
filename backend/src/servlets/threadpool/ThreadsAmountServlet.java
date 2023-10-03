package servlets.threadpool;

import api.Routes;
import com.fasterxml.jackson.databind.JsonNode;
import config.Configuration;
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

@WebServlet(Routes.THREADPOOL)
public class ThreadsAmountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(Constants.JSON_CONTENT_TYPE);
        try {
            int threadsAmount = SingletonObjectMapper.objectMapper.readValue(Configuration.api.getThreadsAmount().getData(), Integer.class);
            resp.setStatus(Constants.API_RESPONSE_OK);
            resp.getWriter().write(JsonParser.toJson(Keys.VALID_RESPONSE_KEY, threadsAmount));
        } catch (Exception e) {
            resp.setStatus(Constants.API_RESPONSE_SERVER_ERROR);
            resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, e.getMessage()));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestBody = JsonParser.getJsonFromReader(req.getReader());
        JsonNode amountNode = SingletonObjectMapper.objectMapper.readTree(requestBody).get(Keys.THREADS_AMOUNT_KEY);

        if (!Objects.isNull(amountNode)) {
            if (amountNode.asInt() <= 0) {
                resp.setStatus(Constants.API_RESPONSE_BAD_REQUEST);
                resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, "Amount should be positive"));
            }

            else {
                ResponseDTO responseDTO = Configuration.api.setThreadsAmount(amountNode.asInt());

                if (responseDTO.getStatus() == Constants.API_RESPONSE_OK)
                    resp.setStatus(Constants.API_RESPONSE_OK);

                else {
                    resp.setStatus(responseDTO.getStatus());

                    if (!Objects.isNull(responseDTO.getErrorDescription()))
                        resp.getWriter().write(JsonParser.toJson(Keys.INVALID_RESPONSE_KEY, responseDTO.getErrorDescription().getCause()));
                }
            }
        }
    }
}
