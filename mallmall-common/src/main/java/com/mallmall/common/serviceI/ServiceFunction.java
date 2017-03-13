package com.mallmall.common.serviceI;

/**
 * 
 * 
 * @author Ja0ck5
 *
 * @param <E> 输入参数
 * @param <T> 输出参数
 */
public interface ServiceFunction<E,T> {
	
	public T callback(E e);
}
