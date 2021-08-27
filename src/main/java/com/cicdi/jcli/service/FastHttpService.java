package com.cicdi.jcli.service;

import com.platon.protocol.exceptions.ClientConnectionException;
import okhttp3.*;
import okio.Buffer;
import okio.BufferedSource;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 另外实现的快速http服务
 *
 * @author haypo
 * @date 2020/12/1
 */
public class FastHttpService extends FastService {
    /**
     * 定义json类型
     */
    public static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");


    /**
     * 定义http客户端对象
     */
    static OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    private final String url;
    private final boolean includeRawResponse;
    private final OkHttpClient httpClient;
    private final HashMap<String, String> headers;

    public FastHttpService(String url, OkHttpClient httpClient, boolean includeRawResponses) {
        super(includeRawResponses);
        this.headers = new HashMap<>();
        this.url = url;
        this.httpClient = httpClient;
        this.includeRawResponse = includeRawResponses;
    }

    private FastHttpService(String url, OkHttpClient httpClient) {
        this(url, httpClient, false);
    }

    public FastHttpService(String url) {
        this(url, okHttpClient);
    }

    /**
     * 执行一次无需响应的request
     *
     * @param request http请求
     * @throws IOException http io异常
     */
    @Override
    protected void performIOWithOutResp(String request) throws IOException {
        RequestBody requestBody = RequestBody.create(JSON_MEDIA_TYPE, request);
        Headers headers = this.buildHeaders();
        Request httpRequest = (new Request.Builder()).url(this.url).headers(headers).post(requestBody).build();
        this.httpClient.newCall(httpRequest).execute();
    }

    /**
     * 执行一次request，并且返回response的io流
     *
     * @param request http请求
     * @return 返回io流
     * @throws IOException http io异常
     */
    @Override
    protected InputStream performIO(String request) throws IOException {
        RequestBody requestBody = RequestBody.create(JSON_MEDIA_TYPE, request);
        Headers headers = this.buildHeaders();
        Request httpRequest = (new Request.Builder()).url(this.url).headers(headers).post(requestBody).build();
        Response response = this.httpClient.newCall(httpRequest).execute();
        ResponseBody responseBody = response.body();
        if (response.isSuccessful()) {
            return responseBody != null ? this.buildInputStream(responseBody) : null;
        } else {
            int code = response.code();
            String text = responseBody == null ? "N/A" : responseBody.string();
            throw new ClientConnectionException("Invalid response received: " + code + "; " + text);
        }
    }

    private InputStream buildInputStream(ResponseBody responseBody) throws IOException {
        InputStream inputStream = responseBody.byteStream();
        if (this.includeRawResponse) {
            BufferedSource source = responseBody.source();
            source.request(9223372036854775807L);
            Buffer buffer = source.buffer();
            long size = buffer.size();
            if (size > 2147483647L) {
                throw new UnsupportedOperationException("Non-integer input buffer size specified: " + size);
            } else {
                int bufferSize = (int) size;
                BufferedInputStream bufferedinputStream = new BufferedInputStream(inputStream, bufferSize);
                bufferedinputStream.mark(inputStream.available());
                return bufferedinputStream;
            }
        } else {
            return inputStream;
        }
    }

    private Headers buildHeaders() {
        return Headers.of(this.headers);
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public void addHeaders(Map<String, String> headersToAdd) {
        this.headers.putAll(headersToAdd);
    }

    public HashMap<String, String> getHeaders() {
        return this.headers;
    }

    @Override
    public void close() {

    }
}
