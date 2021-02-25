package com.cicdi.jcli.submodule;

/**
 * @author haypo
 * @date 2021/1/27
 */
@FunctionalInterface
public interface FunctionUtil<U> {
    /**
     * 生成U对象
     *
     * @return U对象
     * @throws Exception 可能抛出一些异常，将统一在Main方法里处理
     */
    U genU() throws Exception;
}
