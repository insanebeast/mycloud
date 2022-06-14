package com.pueeo.im.reactor.multiThread;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * 反应器
 * 负责响应IO事件，并且分发到Handler处理器
 */
class Reactor implements Runnable {
    //每条线程负责一个选择器的查询
    final Selector selector;

    public Reactor(Selector selector) {
        this.selector = selector;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                //单位为毫秒
                selector.select(1000);
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                if (null == selectedKeys || selectedKeys.size() == 0) {
                    continue;
                }
                Iterator<SelectionKey> it = selectedKeys.iterator();
                while (it.hasNext()) {
                    //Reactor负责dispatch收到的事件
                    SelectionKey sk = it.next();
                    dispatch(sk);
                }
                selectedKeys.clear();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void dispatch(SelectionKey sk) {
        Runnable handler = (Runnable) sk.attachment();
        //调用之前attach绑定到选择键的handler处理器对象
        if (handler != null) {
            handler.run();
        }
    }
}