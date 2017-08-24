package me.xeno.unengtrainer.test.myrx;

/**
 * Created by xeno on 2017/8/24.
 * 拥有两个泛型，T为传入的参数类型，R为返回的类型
 * 这个接口实际上具备了一个方法的功能：传入一种T类型参数，经过fun()处理后，返回R类型的参数，
 * 只是用一个接口将其包装了起来。因为JAVA不允许把方法当做变量使用
 */

public interface Function<T,R> {

    R fun(T t);

}
