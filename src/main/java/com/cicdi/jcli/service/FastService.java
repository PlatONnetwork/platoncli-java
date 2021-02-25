package com.cicdi.jcli.service;

import com.alaya.protocol.ObjectMapperFactory;
import com.alaya.protocol.Web3jService;
import com.alaya.protocol.core.Request;
import com.alaya.protocol.core.Response;
import com.alaya.protocol.core.methods.response.PlatonSendTransaction;
import com.alaya.protocol.websocket.events.Notification;
import com.alaya.utils.Async;
import com.fasterxml.jackson.databind.ObjectMapper;
import rx.Observable;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

/**
 * @author haypo
 * @date 2020/12/1
 */
public abstract class FastService implements Web3jService {

    protected final ObjectMapper objectMapper;

    public FastService(boolean includeRawResponses) {
        objectMapper = ObjectMapperFactory.getObjectMapper(includeRawResponses);
    }

    /**
     * 无需回应的io操作
     *
     * @param payload 数据载荷
     * @throws IOException io异常
     */
    protected abstract void performIOWithOutResp(String payload) throws IOException;

    /**
     * io操作
     *
     * @param payload 数据载荷
     * @return io流
     * @throws IOException io异常
     */
    protected abstract InputStream performIO(String payload) throws IOException;

    public <T extends Response<String>> void fastSend(Request<String, PlatonSendTransaction> request) throws IOException {
        String payload = objectMapper.writeValueAsString(request);
        performIOWithOutResp(payload);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public <T extends Response> T send(Request request, Class<T> responseType) throws IOException {
        String payload = objectMapper.writeValueAsString(request);
        try (InputStream result = performIO(payload)) {
            if (result != null) {
                return objectMapper.readValue(result, responseType);
            } else {
                return null;
            }
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public <T extends Response> CompletableFuture<T> sendAsync(
            Request jsonRpc20Request, Class<T> responseType) {
        return Async.run(() -> send(jsonRpc20Request, responseType));
    }

    @Override
    public <T extends Notification<?>> Observable<T> subscribe(
            Request request,
            String unsubscribeMethod,
            Class<T> responseType) {
        throw new UnsupportedOperationException(
                String.format(
                        "Service %s does not support subscriptions",
                        this.getClass().getSimpleName()));
    }
}
