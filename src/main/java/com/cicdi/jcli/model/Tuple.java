package com.cicdi.jcli.model;

import lombok.Data;

/**
 * @author haypo
 * @date 2021/3/1
 */
@Data
public class Tuple<A, B> {
    private A a;
    private B b;

    private Tuple(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public static <A, B> Tuple<A, B> create(A a, B b) {
        return new Tuple<>(a, b);
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }
}
